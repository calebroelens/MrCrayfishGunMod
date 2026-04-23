package com.mrcrayfish.guns.blockentity.data;

import com.mrcrayfish.guns.init.ModEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class AirStrikeProperties {

    public int fuse = 70;
    public int explosionsDuration = 60;
    public int randomRadius = 20;
    public int countPerStrike = 3;
    public int explosionEveryXTick = 1;
    EntityType<? extends Entity> entityType;

    public AirStrikeProperties fuse(int fuse) {
        this.fuse = fuse;
        return this;
    }

    public EntityType<? extends Entity> getEntityType() {
        if (entityType == null) {
            return ModEntities.AIRSTRIKE_BOMB_ENTITY.get();
        }
        return entityType;
    }

    public AirStrikeProperties explosionsDuration(int explosionsDuration) {
        this.explosionsDuration = explosionsDuration;
        return this;
    }

    public AirStrikeProperties randomRadius(int randomRadius) {
        this.randomRadius = randomRadius;
        return this;
    }

    public AirStrikeProperties countPerStrike(int countPerStrike) {
        this.countPerStrike = countPerStrike;
        return this;
    }

    public AirStrikeProperties explosionEveryXTick(int explosionEveryXTick) {
        this.explosionEveryXTick = explosionEveryXTick;
        return this;
    }

    public AirStrikeProperties entityType(EntityType<? extends Entity> entityType) {
        this.entityType = entityType;
        return this;
    }
}