package com.mrcrayfish.guns.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractMapBeaconBannerBlock extends AbstractBannerBlock {
    protected AbstractMapBeaconBannerBlock(DyeColor p_48659_, Properties p_48660_) {
        super(p_48659_, p_48660_);
    }

    @Override
    public boolean isPossibleToRespawnInThis() {
        return false;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }
}
