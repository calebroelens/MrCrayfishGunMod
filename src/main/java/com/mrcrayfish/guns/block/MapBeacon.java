package com.mrcrayfish.guns.block;
import com.mrcrayfish.guns.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class MapBeacon extends AbstractMapBeaconBannerBlock implements EntityBlock {
    public MapBeacon(Properties p_49421_) {
        super(DyeColor.BLACK, p_49421_);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlocks.MAP_BEACON_E.get().create(pos, state);
    }

    @Override
    public boolean isPossibleToRespawnInThis() {
        return false;
    }
}
