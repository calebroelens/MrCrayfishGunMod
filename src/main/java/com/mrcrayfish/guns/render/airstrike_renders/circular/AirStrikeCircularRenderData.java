package com.mrcrayfish.guns.render.airstrike_renders.circular;

import com.mrcrayfish.guns.render.AirStrikeRenderData;
import com.mrcrayfish.guns.render.AirStrikeRenderType;
import net.minecraft.nbt.CompoundTag;

public class AirStrikeCircularRenderData implements AirStrikeRenderData {

    public float red = 1F;
    public float green = 0.2F;
    public float blue = 0.2F;
    public float alpha = 0.8F;
    public float radius = 20F;

    public AirStrikeCircularRenderData color(float r, float g, float b, float a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
        return this;
    }

    public AirStrikeCircularRenderData radius(float radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public String id() {
        return "circular";
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Red", red);
        tag.putFloat("Green", green);
        tag.putFloat("Blue", blue);
        tag.putFloat("Alpha", alpha);
        tag.putFloat("Radius", radius);
        return tag;
    }

    public static AirStrikeCircularRenderData load(CompoundTag tag) {
        AirStrikeCircularRenderData data = new AirStrikeCircularRenderData();
        data.red = tag.getFloat("Red");
        data.green = tag.getFloat("Green");
        data.blue = tag.getFloat("Blue");
        data.alpha = tag.getFloat("Alpha");
        data.radius = tag.getFloat("Radius");
        return data;
    }

    @Override
    public AirStrikeRenderType build() {
        return new AirStrikeCircularRender(red, green, blue, alpha, radius);
    }
}
