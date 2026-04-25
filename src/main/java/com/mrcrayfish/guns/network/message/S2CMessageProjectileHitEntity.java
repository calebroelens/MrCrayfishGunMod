package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.framework.api.network.message.PlayMessage;
import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class S2CMessageProjectileHitEntity extends PlayMessage<S2CMessageProjectileHitEntity>
{
    private double x;
    private double y;
    private double z;
    private int type;
    private boolean player;
    private float damage;
    private boolean dead;

    public S2CMessageProjectileHitEntity() {}

    public S2CMessageProjectileHitEntity(double x, double y, double z, int type, boolean player,  float damage, boolean dead)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.player = player;
        this.damage = damage;
        this.dead = dead;
    }

    @Override
    public void encode(S2CMessageProjectileHitEntity message, FriendlyByteBuf buffer)
    {
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
        buffer.writeByte(message.type);
        buffer.writeBoolean(message.player);
        buffer.writeFloat(message.damage);
        buffer.writeBoolean(message.dead);
    }

    @Override
    public S2CMessageProjectileHitEntity decode(FriendlyByteBuf buffer)
    {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        byte type = buffer.readByte();
        boolean player = buffer.readBoolean();
        float damage = buffer.readFloat();
        boolean dead = buffer.readBoolean();
        return new S2CMessageProjectileHitEntity(x, y, z, type, player,  damage, dead);
    }

    @Override
    public void handle(S2CMessageProjectileHitEntity message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleProjectileHitEntity(message));
        context.setHandled(true);
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public double getZ()
    {
        return this.z;
    }

    public boolean isHeadshot()
    {
        return this.type == HitType.HEADSHOT;
    }

    public boolean isCritical()
    {
        return this.type == HitType.CRITICAL;
    }

    public boolean isPlayer()
    {
        return this.player;
    }

    public float getDamage()
    {
        return this.damage;
    }

    public boolean isDead()
    {
        return this.dead;
    }

    public static class HitType
    {
        public static final int NORMAL = 0;
        public static final int HEADSHOT = 1;
        public static final int CRITICAL = 2;
    }
}
