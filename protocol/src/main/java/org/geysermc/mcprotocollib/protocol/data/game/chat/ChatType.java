package org.geysermc.mcprotocollib.protocol.data.game.chat;

import org.cloudburstmc.nbt.NbtMap;

import java.util.List;

public record ChatType(ChatTypeDecoration chat, ChatTypeDecoration narration) {
    public record ChatTypeDecorationImpl(String translationKey,
                                         List<Parameter> parameters,
                                         NbtMap style) implements ChatTypeDecoration {
    }
}
