package com.mrcrayfish.guns.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.guns.entity.AirStrikeBombEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class AirstrikeBombRender extends EntityRenderer<AirStrikeBombEntity> {


    private final BlockRenderDispatcher blockRender;

    public AirstrikeBombRender(EntityRendererProvider.Context context) {
        super(context);
        this.blockRender = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(AirStrikeBombEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();

        // Center the model
        poseStack.translate(0.0, 0.5, 0.0);

        // 🔄 Spin effect (looks much better in air)
        float rotation = (entity.tickCount + partialTicks) * 10F;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        // Slight scale to make it look like a bomb
        poseStack.scale(0.8F, 0.8F, 0.8F);

        // Render as TNT block (replace later with custom model)
        //noinspection deprecation
        blockRender.renderSingleBlock(
                Blocks.TNT.defaultBlockState(),
                poseStack,
                buffer,
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull AirStrikeBombEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
