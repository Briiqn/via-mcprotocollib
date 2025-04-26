package dev.briiqn.loader;

import com.viaversion.vialoader.impl.viaversion.VLLoader;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.protocol.version.BaseVersionProvider;

public class ProtocolLibViaLoader extends VLLoader {
    private int protocol =770;
    public ProtocolLibViaLoader() {
    }
    public ProtocolLibViaLoader(int protocol) {
        System.out.println("Using protocol " + protocol);
        this.protocol = protocol;
    }

    @Override
    public void load() {
        super.load();

        Via.getManager().getProviders().use(VersionProvider.class, new BaseVersionProvider() {
            @Override
            public ProtocolVersion getClosestServerProtocol(UserConnection connection) {
                return ProtocolVersion.getProtocol(protocol);
            }
        });
    }
}
