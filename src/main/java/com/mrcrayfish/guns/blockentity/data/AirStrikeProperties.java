package com.mrcrayfish.guns.blockentity.data;

import com.mrcrayfish.guns.init.ModEntities;
import com.mrcrayfish.guns.render.AirStrikeRenderData;
import com.mrcrayfish.guns.render.AirStrikeRenderType;
import com.mrcrayfish.guns.render.airstrike_renders.circular.AirStrikeCircularRenderData;
import com.mrcrayfish.guns.render.airstrike_renders.party.AirStrikeCircularPartyRenderData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class AirStrikeProperties {

    public int fuse = 70;
    public int explosionsDuration = 60;
    public int randomRadius = 20;
    public int countPerStrike = 3;
    public int explosionEveryXTick = 1;
    Supplier<EntityType<? extends Entity>> entityType;
    /* Visuals */
    public AirStrikeRenderData renderData;

    public AirStrikeProperties fuse(int fuse) {
        this.fuse = fuse;
        return this;
    }

    public AirStrikeProperties render(AirStrikeRenderData data) {
        this.renderData = data;
        return this;
    }

    public AirStrikeRenderType buildRender() {
        return renderData != null ? renderData.build() : null;
    }

    public EntityType<? extends Entity> getEntityType() {
        if (entityType == null) {
            return ModEntities.AIRSTRIKE_BOMB_ENTITY.get();
        }
        return entityType.get();
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

    public AirStrikeProperties entityType(Supplier<EntityType<? extends Entity>> entityType) {
        this.entityType = entityType;
        return this;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putInt("Fuse", fuse);
        tag.putInt("ExplosionsDuration", explosionsDuration);
        tag.putInt("RandomRadius", randomRadius);
        tag.putInt("CountPerStrike", countPerStrike);
        tag.putInt("ExplosionEveryXTick", explosionEveryXTick);

        if (entityType != null) {
            EntityType<?> type = entityType.get();
            if (type != null) {
                tag.putString("EntityType", EntityType.getKey(type).toString());
            }
        }

        if (renderData != null) {
            tag.putString("RenderType", renderData.id());
            tag.put("RenderData", renderData.save());
        }

        return tag;
    }

    public static AirStrikeProperties load(CompoundTag tag) {
        AirStrikeProperties props = new AirStrikeProperties();

        props.fuse = tag.getInt("Fuse");
        props.explosionsDuration = tag.getInt("ExplosionsDuration");
        props.randomRadius = tag.getInt("RandomRadius");
        props.countPerStrike = tag.getInt("CountPerStrike");
        props.explosionEveryXTick = tag.getInt("ExplosionEveryXTick");

        if (tag.contains("EntityType")) {
            EntityType.byString(tag.getString("EntityType")).ifPresent(type -> {
                props.entityType = () -> type;
            });
        }

        String type = tag.getString("RenderType");
        CompoundTag data = tag.getCompound("RenderData");

        props.renderData = switch (type) {
            case "circular" -> AirStrikeCircularRenderData.load(data);
            case "circular_party" -> AirStrikeCircularPartyRenderData.load(data);
            default -> null;
        };
        return props;
    }
}