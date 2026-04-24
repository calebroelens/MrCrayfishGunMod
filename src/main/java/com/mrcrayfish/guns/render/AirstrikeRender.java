package com.mrcrayfish.guns.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrcrayfish.guns.blockentity.AirStrikeBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class AirstrikeRender implements BlockEntityRenderer<AirStrikeBlockEntity> {

    public AirstrikeRender(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(AirStrikeBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        if(level == null)return;
        /* Render the airstrike */
        blockEntity.strikeProperties.buildRender().render(level, blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    public boolean shouldRenderOffScreen(AirStrikeBlockEntity p_112306_) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 1024;
    }

    @Override
    public boolean shouldRender(AirStrikeBlockEntity p_173568_, Vec3 p_173569_) {
        return true;
    }
}
