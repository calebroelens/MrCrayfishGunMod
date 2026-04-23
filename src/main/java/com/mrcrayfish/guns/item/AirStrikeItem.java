package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.blockentity.AirStrikeBlockEntity;
import com.mrcrayfish.guns.blockentity.data.AirStrikeProperties;
import com.mrcrayfish.guns.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class AirStrikeItem extends Item {

    /* Airstrike: Summon a barrage of explosions at y above the player cursor */

    public AirStrikeProperties airStrikeProperties = new AirStrikeProperties();

    public AirStrikeItem(Properties properties) {
        super(properties);
    }

    public AirStrikeItem(Properties properties, AirStrikeProperties  airStrikeProperties) {
        super(properties);
        this.airStrikeProperties = airStrikeProperties;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        HitResult hit = player.pick(400D, 0.0F, false);

        if (!level.isClientSide && hit.getType() == HitResult.Type.BLOCK) {

            BlockPos pos = ((BlockHitResult) hit).getBlockPos();
            if(!level.getWorldBorder().isWithinBounds(pos)){
                return InteractionResultHolder.pass(stack);
            }
            // Walk upward from the hit block until we find an air block
            BlockPos targetPos = pos.above();
            while (!level.getBlockState(targetPos).isAir()) {
                targetPos = targetPos.above();

                // Safety cap to avoid infinite loop
                if (targetPos.getY() > level.getMaxBuildHeight()) {
                    return InteractionResultHolder.fail(stack);
                }
            }

            level.setBlock(targetPos, ModBlocks.AIRSTRIKE.get().defaultBlockState(), 3);
            AirStrikeBlockEntity airStrikeBlockEntity = (AirStrikeBlockEntity) level.getBlockEntity(targetPos);
            if(airStrikeBlockEntity != null){
                airStrikeBlockEntity.strikeProperties = airStrikeProperties;
                /* Send update */
                airStrikeBlockEntity.setChanged();
                BlockState blockState = level.getBlockState(targetPos);
                level.sendBlockUpdated(targetPos, blockState, blockState, 3);
            }
        }

        ItemStack itemstack = player.getItemInHand(hand);
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        player.getCooldowns().addCooldown(this, 100);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
