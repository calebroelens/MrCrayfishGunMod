package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.framework.api.network.message.PlayMessage;
import com.mrcrayfish.guns.common.network.ServerPlayHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2SMessageHitboxUsage extends PlayMessage<C2SMessageHitboxUsage> {

    private boolean enabled;

    public C2SMessageHitboxUsage() {}

    public C2SMessageHitboxUsage(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void encode(C2SMessageHitboxUsage message, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(message.enabled);
    }

    @Override
    public C2SMessageHitboxUsage decode(FriendlyByteBuf friendlyByteBuf) {
        return new C2SMessageHitboxUsage(friendlyByteBuf.readBoolean());
    }

    public void handle(C2SMessageHitboxUsage message, MessageContext ctx) {
        ctx.execute(() ->
        {
            ServerPlayer player = ctx.getPlayer();
            if(player != null)
            {
                ServerPlayHandler.handleHitboxChange(message, player);
            }
        });
        ctx.setHandled(true);
    }
    public boolean getHitboxState(){
        return this.enabled;
    }
}
