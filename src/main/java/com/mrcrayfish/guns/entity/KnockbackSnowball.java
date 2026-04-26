package com.mrcrayfish.guns.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class KnockbackSnowball extends Snowball {
    public KnockbackSnowball(EntityType<? extends Snowball> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }

    public KnockbackSnowball(Level p_37399_, LivingEntity p_37400_) {
        super(p_37399_, p_37400_);
    }

    public KnockbackSnowball(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(p_37394_, p_37395_, p_37396_, p_37397_);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);

        Entity target = result.getEntity();

        if (target instanceof Player player) {
            player.hurt(damageSources().thrown(this, this.getOwner()), 0.5F);

            // Apply knockback
            Vec3 motion = this.getDeltaMovement().normalize();
            double strength = 10F; // tweak this

            player.push(
                    motion.x * strength,
                    1F, // small upward bump
                    motion.z * strength
            );

            /* Also apply a small explosion */
            if (!this.level.isClientSide) {
                this.level.explode(
                        this,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        1.5F,
                        Level.ExplosionInteraction.NONE // no terrain damage
                );
            }
        }
    }
}
