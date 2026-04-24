package com.mrcrayfish.guns.render.airstrike_renders.circular;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrcrayfish.guns.blockentity.AirStrikeBlockEntity;
import com.mrcrayfish.guns.render.AirStrikeRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;

public class AirStrikeCircularRender implements AirStrikeRenderType {

    public float red;
    public float green;
    public float blue;
    public float alpha;
    public float visualRadius;

    public AirStrikeCircularRender(float red, float green, float blue, float alpha, float visualRadius) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.visualRadius = visualRadius;
    }

    @Override
    public void render(Level level, AirStrikeBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);

        VertexConsumer builder = bufferSource.getBuffer(RenderType.lightning());
        Matrix4f matrix = poseStack.last().pose();

        float progress = (float) blockEntity.fuseTimer / blockEntity.strikeProperties.fuse;
        float radius = 1f + (visualRadius - 1) * progress;
        float height = 512f;
        float r = red, g = green, b = blue, a = alpha;
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
            builder.vertex(matrix, x1, level.getMinBuildHeight(),z1).color(r, g, b, a) .endVertex();
            builder.vertex(matrix, x0, level.getMinBuildHeight(),z0).color(r, g, b, a) .endVertex();
        }

        poseStack.popPose();
    }

    @Override
    public String id() {
        return "circular";
    }

    @Override
    public CompoundTag save() {
        return null;
    }
}
