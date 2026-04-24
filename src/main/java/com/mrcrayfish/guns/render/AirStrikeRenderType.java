package com.mrcrayfish.guns.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.guns.blockentity.AirStrikeBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public interface AirStrikeRenderType {

    void render(Level level, AirStrikeBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay);
    String id();
    CompoundTag save();
}
