package dev.briiqn.loader;

import com.viaversion.vialoader.impl.viaversion.VLLoader;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.protocol.version.BaseVersionProvider;
import lombok.Getter;
import lombok.Setter;

public class ProtocolLibViaLoader extends VLLoader {
    @Getter
    @Setter
    private int protocol;

    public ProtocolLibVersionProvider versionProvider = new ProtocolLibVersionProvider();

    public ProtocolLibViaLoader(int protocol) {
        this.protocol = protocol;
        versionProvider.setVersion(protocol);
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
        versionProvider.setVersion(protocol);
    }

    @Override
    public void load() {
        super.load();
        versionProvider.setVersion(protocol);
        Via.getManager().getProviders().use(VersionProvider.class, versionProvider);
    }

    @Override
    public void unload() {
        super.unload();
    }
}
