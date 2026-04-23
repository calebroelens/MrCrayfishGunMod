package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.entity.BridgeEggProjectileEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BridgeEggItem extends Item {

    public int width = 3;

    public BridgeEggItem(Properties properties) {
        super(properties);
    }

    public BridgeEggItem(Properties properties, int width) {
        super(properties);
        this.width = width;
    }

    public InteractionResultHolder<ItemStack> use(Level p_43142_, Player p_43143_, InteractionHand p_43144_) {
        ItemStack itemstack = p_43143_.getItemInHand(p_43144_);
        p_43142_.playSound((Player)null, p_43143_.getX(), p_43143_.getY(), p_43143_.getZ(), SoundEvents.EGG_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (p_43142_.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!p_43142_.isClientSide) {
            BridgeEggProjectileEntity bridge_egg = new BridgeEggProjectileEntity(p_43142_, p_43143_, width);
            bridge_egg.setItem(itemstack);
            bridge_egg.shootFromRotation(p_43143_, p_43143_.getXRot(), p_43143_.getYRot(), 0.0F, 1.5F, 0.5F);
            p_43142_.addFreshEntity(bridge_egg);
        }

        p_43143_.awardStat(Stats.ITEM_USED.get(this));
        if (!p_43143_.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, p_43142_.isClientSide());
    }
}
