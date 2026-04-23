package com.mrcrayfish.guns.blockentity;

import com.mrcrayfish.guns.blockentity.data.AirStrikeProperties;
import com.mrcrayfish.guns.init.ModBlocks;
import com.mrcrayfish.guns.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AirStrikeBlockEntity extends BlockEntity {

    public int fuseTimer;
    public boolean fused = false;
    public int explosionTimer;

    public AirStrikeProperties strikeProperties = new AirStrikeProperties();

    public AirStrikeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.AIRSTRIKE_E.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        AirStrikeBlockEntity entity = (AirStrikeBlockEntity) be;
        if(entity.fuseTimer == 0){
            playAirstrikeSound(level, pos);
        }
        if (!entity.fused) {
            if (entity.fuseTimer >= entity.strikeProperties.fuse) {
                entity.fused = true;
            } else {
                entity.fuseTimer++;
            }
            return;
        }
        // Airstrike phase
        if (entity.explosionTimer >= entity.strikeProperties.explosionsDuration) {
            level.destroyBlock(pos, true);
            return;
        }
        if(entity.explosionTimer % entity.strikeProperties.explosionEveryXTick == 0){
            spawnAirstrikeBomb(entity, level, pos);
        }
        entity.explosionTimer++;
    }

    private static void playAirstrikeSound(Level level, BlockPos pos){
        for(Player player : level.players()){
            level.playSound(null, player.getOnPos(), ModSounds.AIRSTRIKE_BOMB_SIREN.get(), SoundSource.BLOCKS, 1F, 1F);
        }
    }

    private static void spawnAirstrikeBomb(AirStrikeBlockEntity e, Level level, BlockPos pos) {
        int count = e.strikeProperties.countPerStrike;
        for (int i = 0; i < count; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * e.strikeProperties.randomRadius;
            double offsetZ = (level.random.nextDouble() - 0.5) * e.strikeProperties.randomRadius;

            double x = pos.getX() + 0.5 + offsetX;
            double y = level.getMaxBuildHeight() - 10;
            double z = pos.getZ() + 0.5 + offsetZ;
            Entity bomb = e.strikeProperties.getEntityType().create(level);
            if(bomb == null) continue;
            bomb.setPos(x, y, z);
            level.addFreshEntity(bomb);
        }
    }
}
