package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.init.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;

public class AirStrikeBombEntity extends Entity {

    public float explosionStrength = 12;
    public float downwardsSpeed = -20;

    public AirStrikeBombEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = false;
    }

    public AirStrikeBombEntity(Level level, double x, double y, double z) {
        this(ModEntities.AIRSTRIKE_BOMB_ENTITY.get(), level);
        this.setPos(x, y, z);
        this.setDeltaMovement(0, downwardsSpeed, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide) return;

//        // Apply gravity
        this.setDeltaMovement(this.getDeltaMovement().add(0, -20, 0));
        this.move(MoverType.SELF, this.getDeltaMovement());

        // Check block collision
        if (this.onGround) {
            explode();
        }

        // Safety: explode if below world
        if (this.getY() <= level.getMinBuildHeight()) {
            explode();
        }
    }

    private void explode() {
        if (!level.isClientSide) {
            level.explode(
                    this, this.getX(), this.getY(), this.getZ(),
                    explosionStrength, Level.ExplosionInteraction.TNT
            );
            this.discard();
        }
    }

    // Required but minimal since we don't need persistence
    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}
}