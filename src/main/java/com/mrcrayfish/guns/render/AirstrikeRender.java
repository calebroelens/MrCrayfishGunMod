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

    public AirstrikeRender(BlockEntityRendererProvider.Context context) {
        // context not needed but must be present
    }

    @Override
    public void render(AirStrikeBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        Level level = blockEntity.getLevel();
        if(level == null)return;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);

        VertexConsumer builder = bufferSource.getBuffer(RenderType.lightning());
        Matrix4f matrix = poseStack.last().pose();

        float progress = (float) blockEntity.fuseTimer / blockEntity.strikeProperties.fuse;
        float radius = 1f + (blockEntity.strikeProperties.visualRadius - 1) * progress;
        float height = 512f;
        float r = 1.0f, g = 0.2f, b = 0.2f, a = 0.8f;
        int segments = 64; // Smoothness of the circle

        float pulse = (float) Math.sin(blockEntity.fuseTimer * 0.3f) * 0.3f + 0.7f;
        a *= pulse;

        // Draw cylindrical beam as triangle strip (side quads around the circle)
        for (int i = 0; i < segments; i++) {
            float angle0 = (float) (2 * Math.PI * i / segments);
            float angle1 = (float) (2 * Math.PI * (i + 1) / segments);

            float x0 = (float) Math.cos(angle0) * radius;
            float z0 = (float) Math.sin(angle0) * radius;
            float x1 = (float) Math.cos(angle1) * radius;
            float z1 = (float) Math.sin(angle1) * radius;

            // One quad per segment: bottom-left, bottom-right, top-right, top-left
            builder.vertex(matrix, x0, level.getMinBuildHeight(), z0).color(r, g, b, a) .endVertex();
            builder.vertex(matrix, x1, level.getMinBuildHeight(), z1).color(r, g, b, a) .endVertex();
            builder.vertex(matrix, x1, height, z1).color(r, g, b, 0f).endVertex();
            builder.vertex(matrix, x0, height, z0).color(r, g, b, 0f).endVertex();

            builder.vertex(matrix, x0, height, z0).color(r, g, b, 0f).endVertex();
            builder.vertex(matrix, x1, height, z1).color(r, g, b, 0f).endVertex();
            builder.vertex(matrix, x1, level.getMinBuildHeight(),      z1).color(r, g, b, a) .endVertex();
            builder.vertex(matrix, x0, level.getMinBuildHeight(),      z0).color(r, g, b, a) .endVertex();
        }

        poseStack.popPose();
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
