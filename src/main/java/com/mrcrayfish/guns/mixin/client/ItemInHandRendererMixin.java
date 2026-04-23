//package com.mrcrayfish.guns.mixin.client;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Axis;
//import com.mrcrayfish.guns.init.ModItems;
//import net.minecraft.client.player.AbstractClientPlayer;
//import net.minecraft.client.renderer.ItemInHandRenderer;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.entity.ItemRenderer;
//import net.minecraft.nbt.Tag;
//import net.minecraft.util.Mth;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.HumanoidArm;
//import net.minecraft.world.item.*;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.Redirect;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(ItemInHandRenderer.class)
//public abstract class ItemInHandRendererMixin {
//
//    @SuppressWarnings({"ConstantConditions"})
//    @Redirect(method = "renderArmWithItem", at = @At(value = "INVOKE", target="Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
//    private boolean isInRenderArmWithItemMapInjections(ItemStack instance, Item p_150931_) {
//        if(instance != null){
//            if(instance.getItem() == ModItems.GAMEPLAY_MAP.get()){
//                // Attempt to render instead
//                ItemStack stack = new ItemStack(Items.FILLED_MAP);
//                // Get the map ID
//                Tag tag = instance.getOrCreateTag().get("map");
//                return tag != null;
//            }
//            return instance.getItem() == Items.FILLED_MAP;
//        }
//        return false;
//    }
//}
