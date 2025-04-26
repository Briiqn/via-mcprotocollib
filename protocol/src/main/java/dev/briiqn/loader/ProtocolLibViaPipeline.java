package dev.briiqn.loader;

import com.viaversion.vialoader.netty.VLPipeline;
import com.viaversion.viaversion.api.connection.UserConnection;
import org.geysermc.mcprotocollib.network.NetworkConstants;

public class ProtocolLibViaPipeline extends VLPipeline {

    public ProtocolLibViaPipeline(UserConnection connection) {
        super(connection);
    }

    @Override
    protected String compressionCodecName() {
        return NetworkConstants.COMPRESSION_NAME;
    }

    @Override
    protected String packetCodecName() {
        return NetworkConstants.CODEC_NAME;
    }

    @Override
    protected String lengthCodecName() {
        return NetworkConstants.SIZER_NAME;
    }

}
