package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.init.ModBlocks;
import com.mrcrayfish.guns.init.ModEntities;
import com.mrcrayfish.guns.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BridgeEggProjectileEntity extends ThrowableItemProjectile {

    public int width = 3;

    public BridgeEggProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public BridgeEggProjectileEntity(Level world) {
        super(ModEntities.BRIDGE_EGG.get(), world);
    }

    public BridgeEggProjectileEntity(Level world, LivingEntity shooter) {
        super(ModEntities.BRIDGE_EGG.get(), shooter, world);
    }

    public BridgeEggProjectileEntity(Level world, LivingEntity shooter, int width) {
        super(ModEntities.BRIDGE_EGG.get(), shooter, world);
        this.width = width;
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.BRIDGE_EGG.get();
    }

//    @Override
//    public void tick() {
//        Direction direction = this.getDirection();
//        BlockPos pos = this.blockPosition();
//        for(int x = -1; x < 2; x++){
//            for(int z = 0; z < width; z++){
//                BlockPos check = new BlockPos(pos.getX() + x, pos.getY()-2, pos.getZ() + z);
//                if(level.getBlockState(check).isAir() && !check.equals(pos)){
//                    level.setBlockAndUpdate(check, ModBlocks.BRIDGE_GLASS.get().defaultBlockState());
//                }
//            }
//        }
//        super.tick();
//    }

    @Override
    public void tick(){

        super.tick();
        if(this.level.isClientSide) return;

        Direction direction = this.getDirection();
        BlockPos center = this.blockPosition();
        center = center.below();
        center = center.below();
        center = center.relative(direction.getOpposite());

        Direction left = direction.getCounterClockWise();
        Direction right = direction.getClockWise();

        int width = this.width;
        int half = width / 2;

        List<BlockPos> blockPositions = Arrays.asList(center, center.relative(direction));
        for(BlockPos pos : blockPositions){
            placeEntityBlock(pos);
            for (int i = 1; i <= half; i++) {
                BlockPos leftPos = pos.relative(left, i);
                BlockPos rightPos = pos.relative(right, i);
                placeEntityBlock(leftPos);
                placeEntityBlock(rightPos);
            }
        }
    }

    private void placeEntityBlock(BlockPos pos) {
        if (this.level.getBlockState(pos).isAir()) {
            this.level.setBlockAndUpdate(pos, ModBlocks.BRIDGE_GLASS.get().defaultBlockState());
        }
    }
}
