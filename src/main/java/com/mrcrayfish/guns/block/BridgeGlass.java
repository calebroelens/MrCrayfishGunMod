package com.mrcrayfish.guns.block;

import com.mrcrayfish.guns.blockentity.BridgeGlassEntity;
import com.mrcrayfish.guns.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;


public class BridgeGlass extends GlassBlock implements EntityBlock {

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);

    public BridgeGlass(AbstractGlassBlock.Properties p_53640_) {
        super(p_53640_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == ModBlocks.BRIDGE_GLASS_E.get() && !world.isClientSide ? BridgeGlassEntity::tick : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlocks.BRIDGE_GLASS_E.get().create(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        boolean result = super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
        updateStage(state, level, pos);
        return result;
    }

    private void updateStage(BlockState state, Level level, BlockPos pos) {
        if(state.getValue(STAGE).equals(0)){
            level.setBlock(pos, state.setValue(STAGE, 1), 1);
        }
        if(state.getValue(STAGE).equals(1)){
            level.setBlock(pos, state.setValue(STAGE, 2), 1);
        }
        if(state.getValue(STAGE).equals(2)){
            level.setBlock(pos, state.setValue(STAGE, 3), 1);
        }
        if(state.getValue(STAGE) >= 3){
            level.removeBlock(pos, false);
        }
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        super.setPlacedBy(p_49847_, p_49848_, p_49849_, p_49850_, p_49851_);
    }
}
