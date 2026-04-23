package com.mrcrayfish.guns.block;

import com.mrcrayfish.guns.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MapBeaconEntity extends BannerBlockEntity {

    public MapBeaconEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

}
