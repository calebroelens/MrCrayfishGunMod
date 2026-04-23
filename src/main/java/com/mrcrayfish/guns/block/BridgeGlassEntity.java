package com.mrcrayfish.guns.block;

import com.mrcrayfish.guns.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.mrcrayfish.guns.block.BridgeGlass.STAGE;

public class BridgeGlassEntity extends BlockEntity {

    int timer;
    int timer_stage = 100;

    public BridgeGlassEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.BRIDGE_GLASS_E.get(), pos, state);
    }
    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        BridgeGlassEntity tile = (BridgeGlassEntity) be;
        if(tile.timer >= tile.timer_stage){
            // Timer elapsed or object not in state
            tile.timer = 0;
            if(state.getValue(STAGE) == 3){
                level.destroyBlock(pos, false);
            }
            else {
                int next = state.getValue(STAGE) + 1;
                level.setBlockAndUpdate(pos, state.setValue(STAGE, next));
            }
        }
        tile.timer++;
    }
}
