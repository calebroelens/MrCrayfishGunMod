package com.mrcrayfish.guns.render.airstrike_renders.party;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrcrayfish.guns.blockentity.AirStrikeBlockEntity;
import com.mrcrayfish.guns.render.AirStrikeRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;

public class AirStrikeCircularPartyRender implements AirStrikeRenderType {

    public float visualRadius;

    public AirStrikeCircularPartyRender(float visualRadius) {
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
        float hue = (blockEntity.fuseTimer * 0.05f) % 1.0f;
        float[] rgb = hsvToRgb(hue, 1.0f, 1.0f);
        float r = rgb[0], g = rgb[1], b = rgb[2], a = 0.8F;
        int segments = 64;

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

    private float[] hsvToRgb(float h, float s, float v) {
        int i = (int)(h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);
        return switch (i % 6) {
            case 0 -> new float[]{v, t, p};
            case 1 -> new float[]{q, v, p};
            case 2 -> new float[]{p, v, t};
            case 3 -> new float[]{p, q, v};
            case 4 -> new float[]{t, p, v};
            default -> new float[]{v, p, q};
        };
    }

    @Override
    public String id() {
        return "circular_party";
    }

    @Override
    public CompoundTag save() {
        return null;
    }
}
