package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.block.BridgeGlassEntity;
import com.mrcrayfish.guns.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class DuploItem extends Item {
    public DuploItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        // Iterate a square and place glass if air
        BlockHitResult ray = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        BlockPos blockPos = ray.getBlockPos();
        Boolean consume = false;
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        if(!level.getBlockState(blockPos).isAir()){
            // Generate positions
            for(int y = 1; y < 4; y++){
                for(int x = -1; x < 2; x++){
                    for(int z = -1; z < 2; z++){
                        BlockPos check = new BlockPos(blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);
                        if(level.getBlockState(check).isAir() && check != blockPos){
                            consume = true;
                            level.setBlockAndUpdate(check, ModBlocks.BRIDGE_GLASS.get().defaultBlockState());
                        }
                    }
                }

            }
        }
        if(consume && !player.getAbilities().instabuild && !itemInHand.isEmpty()){
            itemInHand.shrink(1);
        }
        return super.use(level, player, interactionHand);
    }
}
