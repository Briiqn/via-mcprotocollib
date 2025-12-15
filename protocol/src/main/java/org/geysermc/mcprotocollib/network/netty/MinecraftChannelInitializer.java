package org.geysermc.mcprotocollib.network.netty;

import com.viaversion.vialoader.ViaLoader;
import com.viaversion.vialoader.impl.platform.ViaBackwardsPlatformImpl;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import dev.briiqn.loader.ProtocolLibViaLoader;
import dev.briiqn.loader.ProtocolLibViaPipeline;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.geysermc.mcprotocollib.network.BuiltinFlags;
import org.geysermc.mcprotocollib.network.NetworkConstants;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;

import java.util.function.Function;

@RequiredArgsConstructor
public class MinecraftChannelInitializer<S extends Session & ChannelHandler> extends ChannelInitializer<Channel> {
    private final Function<Channel, S> sessionFactory;
    private final boolean client;
    private static final ProtocolLibViaLoader VIA_LOADER = new ProtocolLibViaLoader(774);
    private static boolean viaInitialized = false;

    @Override
    protected void initChannel(Channel ch) throws Exception {
        S session = createSession(ch);
        int protocolVersion = session.getPacketProtocol().getProtocolVersion();

        // update the protocol version in the loader object first
        // (without trying to access Via.getManager() yet)
        // still dont get why we cant just deinit and reinit via per channel but ok kennytv
        VIA_LOADER.setProtocol(protocolVersion);
        synchronized (MinecraftChannelInitializer.class) {
            if (!viaInitialized) {
                try {
                    ViaLoader.init(null, VIA_LOADER, null, null, ViaBackwardsPlatformImpl::new);
                    viaInitialized = true;
                } catch (Throwable throwable) {
                    System.err.println("Failed to initialize ViaLoader: " + throwable.getMessage());
                    throwable.printStackTrace();
                }
            } else if (Via.getManager() != null && Via.getManager().getProviders() != null) {
                Via.getManager().getProviders().use(VersionProvider.class, VIA_LOADER.versionProvider);
            }
        }

        addHandlers(session, ch);
    }

    protected S createSession(Channel ch) {
        return sessionFactory.apply(ch);
    }

    protected void addHandlers(S session, Channel ch) {
        MinecraftProtocol protocol = session.getPacketProtocol();
        ChannelPipeline pipeline = ch.pipeline();
        final UserConnectionImpl connection = new UserConnectionImpl(ch, true);
        new ProtocolPipelineImpl(connection);

        pipeline.addLast(NetworkConstants.READ_TIMEOUT_NAME,
            new ReadTimeoutHandler(session.getFlag(BuiltinFlags.READ_TIMEOUT, 30)));
        pipeline.addLast(NetworkConstants.WRITE_TIMEOUT_NAME,
            new WriteTimeoutHandler(session.getFlag(BuiltinFlags.WRITE_TIMEOUT, 0)));

        pipeline.addLast(NetworkConstants.ENCRYPTION_NAME, new PacketEncryptorCodec());
        pipeline.addLast(NetworkConstants.SIZER_NAME, new PacketSizerCodec(protocol.getPacketHeader()));
        pipeline.addLast(NetworkConstants.COMPRESSION_NAME, new PacketCompressionCodec());

        pipeline.addLast(NetworkConstants.FLOW_CONTROL_NAME, new AutoReadFlowControlHandler());
        pipeline.addLast(NetworkConstants.CODEC_NAME, new PacketCodec(session, client));
        pipeline.addLast(NetworkConstants.FLUSH_HANDLER_NAME, new FlushHandler());
        pipeline.addLast(NetworkConstants.MANAGER_NAME, session);
        pipeline.addLast(new ProtocolLibViaPipeline(connection));
    }
}
