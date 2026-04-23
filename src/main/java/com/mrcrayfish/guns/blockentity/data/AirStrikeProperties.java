package com.mrcrayfish.guns.blockentity.data;

import com.mrcrayfish.guns.init.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class AirStrikeProperties {

    public int fuse = 70;
    public int explosionsDuration = 60;
    public int randomRadius = 20;
    public int visualRadius = 20;
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

    public AirStrikeProperties visualRadius(int visualRadius) {
        this.visualRadius = visualRadius;
        return this;
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

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putInt("Fuse", fuse);
        tag.putInt("ExplosionsDuration", explosionsDuration);
        tag.putInt("RandomRadius", randomRadius);
        tag.putInt("VisualRadius", visualRadius);
        tag.putInt("CountPerStrike", countPerStrike);
        tag.putInt("ExplosionEveryXTick", explosionEveryXTick);

        if (entityType != null) {
            tag.putString("EntityType", EntityType.getKey(entityType).toString());
        }

        return tag;
    }

    public static AirStrikeProperties load(CompoundTag tag) {
        AirStrikeProperties props = new AirStrikeProperties();

        props.fuse = tag.getInt("Fuse");
        props.explosionsDuration = tag.getInt("ExplosionsDuration");
        props.randomRadius = tag.getInt("RandomRadius");
        props.visualRadius = tag.getInt("VisualRadius");
        props.countPerStrike = tag.getInt("CountPerStrike");
        props.explosionEveryXTick = tag.getInt("ExplosionEveryXTick");

        if (tag.contains("EntityType")) {
            EntityType.byString(tag.getString("EntityType")).ifPresent(type -> {
                props.entityType = type;
            });
        }

        return props;
    }
}