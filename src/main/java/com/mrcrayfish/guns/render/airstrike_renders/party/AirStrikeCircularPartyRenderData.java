package com.mrcrayfish.guns.render.airstrike_renders.party;

import com.mrcrayfish.guns.render.AirStrikeRenderData;
import com.mrcrayfish.guns.render.AirStrikeRenderType;
import net.minecraft.nbt.CompoundTag;

public class AirStrikeCircularPartyRenderData implements AirStrikeRenderData {

    public float radius = 20F;

    public AirStrikeCircularPartyRenderData radius(float radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public String id() {
        return "circular_party";
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Radius", radius);
        return tag;
    }

    public static AirStrikeCircularPartyRenderData load(CompoundTag tag) {
        AirStrikeCircularPartyRenderData data = new AirStrikeCircularPartyRenderData();
        data.radius = tag.getFloat("Radius");
        return data;
    }

    @Override
    public AirStrikeRenderType build() {
        return new AirStrikeCircularPartyRender(radius);
    }
}
