package com.mrcrayfish.guns.render;

import net.minecraft.nbt.CompoundTag;

public interface AirStrikeRenderData {
    String id();
    CompoundTag save();
    AirStrikeRenderType build();
}
