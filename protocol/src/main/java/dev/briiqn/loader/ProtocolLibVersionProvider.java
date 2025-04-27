package dev.briiqn.loader;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocol.version.BaseVersionProvider;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;

public class ProtocolLibVersionProvider extends BaseVersionProvider {
    @Getter
    @Setter
    public int version;

    @Override
    public ProtocolVersion getClosestServerProtocol(UserConnection connection) throws Exception {
        System.out.println("Overriding protocol version to " + version);
        if (connection.isClientSide()) {
            return ProtocolVersion.getProtocol(version);
        }
        return super.getClosestServerProtocol(connection);
    }
}
