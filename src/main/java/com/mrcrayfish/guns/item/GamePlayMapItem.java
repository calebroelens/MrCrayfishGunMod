package com.mrcrayfish.guns.item;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.mrcrayfish.guns.init.ModBlocks;
import com.mrcrayfish.guns.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class GamePlayMapItem extends MapItem {
    public GamePlayMapItem(Properties p_42847_) {
        super(p_42847_);
    }

    public static ItemStack create(Player gamePlayer, Level p_42887_, int p_42888_, int p_42889_, byte p_42890_, boolean p_42891_, boolean p_42892_) {
        ItemStack itemstack = new ItemStack(ModItems.GAMEPLAY_MAP.get());
        int mapId = createAndStoreSavedDataGamePlay(itemstack, p_42887_, p_42888_, p_42889_, p_42891_, p_42892_, p_42887_.dimension());
        // Manipulate the map data : Hack -> Cannot override protected method
        return itemstack;
    }

    private static int createAndStoreSavedDataGamePlay(ItemStack p_151112_, Level p_151113_, int p_151114_, int p_151115_, boolean p_151117_, boolean p_151118_, ResourceKey<Level> p_151119_) {
        int i = createNewSavedDataGamePlay(p_151113_, p_151114_, p_151115_, 3, p_151117_, p_151118_, p_151119_);
        storeMapDataGamePlay(p_151112_, i);
        return i;
    }

    private static int createNewSavedDataGamePlay(Level p_151121_, int p_151122_, int p_151123_, int p_151124_, boolean p_151125_, boolean p_151126_, ResourceKey<Level> p_151127_) {
        MapItemSavedData mapitemsaveddata = MapItemSavedData.createFresh((double)p_151122_, (double)p_151123_, (byte)p_151124_, p_151125_, p_151126_, p_151127_);
        int i = p_151121_.getFreeMapId();
        p_151121_.setMapData(makeKey(i), mapitemsaveddata);
        return i;
    }
    private static void storeMapDataGamePlay(ItemStack p_151109_, int p_151110_) {
        p_151109_.getOrCreateTag().putInt("map", p_151110_);
    }


    public InteractionResult useOn(UseOnContext p_42885_) {
        BlockState blockstate = p_42885_.getLevel().getBlockState(p_42885_.getClickedPos());
        if (blockstate.is(BlockTags.BANNERS) || blockstate.is(ModBlocks.MAP_BEACON.get())) {
            if (!p_42885_.getLevel().isClientSide) {
                MapItemSavedData mapitemsaveddata = getSavedData(p_42885_.getItemInHand(), p_42885_.getLevel());
                if (mapitemsaveddata != null && !mapitemsaveddata.toggleBanner(p_42885_.getLevel(), p_42885_.getClickedPos())) {
                    return InteractionResult.FAIL;
                }
            }

            return InteractionResult.sidedSuccess(p_42885_.getLevel().isClientSide);
        } else {
            return super.useOn(p_42885_);
        }
    }

    public void update(Level p_42894_, Entity p_42895_, MapItemSavedData p_42896_) {
        if (p_42894_.dimension() == p_42896_.dimension && p_42895_ instanceof Player) {
            int i = 1 << p_42896_.scale;
            int j = p_42896_.centerX;
            int k = p_42896_.centerZ;
            int l = Mth.floor(p_42895_.getX() - (double)j) / i + 64;
            int i1 = Mth.floor(p_42895_.getZ() - (double)k) / i + 64;
            int j1 = 128 / i;
            if (p_42894_.dimensionType().hasCeiling()) {
                j1 /= 2;
            }

            MapItemSavedData.HoldingPlayer mapitemsaveddata$holdingplayer = p_42896_.getHoldingPlayer((Player)p_42895_);
            ++mapitemsaveddata$holdingplayer.step;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
            boolean flag = false;

            for(int k1 = l - j1 + 1; k1 < l + j1; ++k1) {
                if ((k1 & 15) == (mapitemsaveddata$holdingplayer.step & 15) || flag) {
                    flag = false;
                    double d0 = 0.0D;

                    for(int l1 = i1 - j1 - 1; l1 < i1 + j1; ++l1) {
                        if (k1 >= 0 && l1 >= -1 && k1 < 128 && l1 < 128) {
                            int i2 = Mth.square(k1 - l) + Mth.square(l1 - i1);
                            boolean flag1 = i2 > (j1 - 2) * (j1 - 2);
                            int j2 = (j / i + k1 - 64) * i;
                            int k2 = (k / i + l1 - 64) * i;
                            Multiset<MaterialColor> multiset = LinkedHashMultiset.create();
                            LevelChunk levelchunk = p_42894_.getChunk(SectionPos.blockToSectionCoord(j2), SectionPos.blockToSectionCoord(k2));
                            if (!levelchunk.isEmpty()) {
                                int l2 = 0;
                                double d1 = 0.0D;
                                if (p_42894_.dimensionType().hasCeiling()) {
                                    int i3 = j2 + k2 * 231871;
                                    i3 = i3 * i3 * 31287121 + i3 * 11;
                                    if ((i3 >> 20 & 1) == 0) {
                                        multiset.add(Blocks.DIRT.defaultBlockState().getMapColor(p_42894_, BlockPos.ZERO), 10);
                                    } else {
                                        multiset.add(Blocks.STONE.defaultBlockState().getMapColor(p_42894_, BlockPos.ZERO), 100);
                                    }

                                    d1 = 100.0D;
                                } else {
                                    for(int i4 = 0; i4 < i; ++i4) {
                                        for(int j3 = 0; j3 < i; ++j3) {
                                            blockpos$mutableblockpos.set(j2 + i4, 0, k2 + j3);
                                            int k3 = levelchunk.getHeight(Heightmap.Types.WORLD_SURFACE, blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getZ()) + 1;
                                            BlockState blockstate;
                                            if (k3 <= p_42894_.getMinBuildHeight() + 1) {
                                                blockstate = Blocks.BEDROCK.defaultBlockState();
                                            } else {
                                                do {
                                                    --k3;
                                                    blockpos$mutableblockpos.setY(k3);
                                                    blockstate = levelchunk.getBlockState(blockpos$mutableblockpos);
                                                } while(blockstate.getMapColor(p_42894_, blockpos$mutableblockpos) == MaterialColor.NONE && k3 > p_42894_.getMinBuildHeight());

                                                if (k3 > p_42894_.getMinBuildHeight() && !blockstate.getFluidState().isEmpty()) {
                                                    int l3 = k3 - 1;
                                                    blockpos$mutableblockpos1.set(blockpos$mutableblockpos);

                                                    BlockState blockstate1;
                                                    do {
                                                        blockpos$mutableblockpos1.setY(l3--);
                                                        blockstate1 = levelchunk.getBlockState(blockpos$mutableblockpos1);
                                                        ++l2;
                                                    } while(l3 > p_42894_.getMinBuildHeight() && !blockstate1.getFluidState().isEmpty());

                                                    blockstate = this.getCorrectStateForFluidBlock(p_42894_, blockstate, blockpos$mutableblockpos);
                                                }
                                            }

                                            p_42896_.checkBanners(p_42894_, blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getZ());
                                            d1 += (double)k3 / (double)(i * i);
                                            multiset.add(blockstate.getMapColor(p_42894_, blockpos$mutableblockpos));
                                        }
                                    }
                                }

                                l2 /= i * i;
                                MaterialColor materialcolor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MaterialColor.NONE);
                                MaterialColor.Brightness materialcolor$brightness;
                                if (materialcolor == MaterialColor.WATER) {
                                    double d2 = (double)l2 * 0.1D + (double)(k1 + l1 & 1) * 0.2D;
                                    if (d2 < 0.5D) {
                                        materialcolor$brightness = MaterialColor.Brightness.HIGH;
                                    } else if (d2 > 0.9D) {
                                        materialcolor$brightness = MaterialColor.Brightness.LOW;
                                    } else {
                                        materialcolor$brightness = MaterialColor.Brightness.NORMAL;
                                    }
                                } else {
                                    double d3 = (d1 - d0) * 4.0D / (double)(i + 4) + ((double)(k1 + l1 & 1) - 0.5D) * 0.4D;
                                    if (d3 > 0.6D) {
                                        materialcolor$brightness = MaterialColor.Brightness.HIGH;
                                    } else if (d3 < -0.6D) {
                                        materialcolor$brightness = MaterialColor.Brightness.LOW;
                                    } else {
                                        materialcolor$brightness = MaterialColor.Brightness.NORMAL;
                                    }
                                }

                                d0 = d1;
                                if (l1 >= 0 && i2 < j1 * j1 && (!flag1 || (k1 + l1 & 1) != 0)) {
                                    flag |= p_42896_.updateColor(k1, l1, materialcolor.getPackedId(materialcolor$brightness));
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private BlockState getCorrectStateForFluidBlock(Level p_42901_, BlockState p_42902_, BlockPos p_42903_) {
        FluidState fluidstate = p_42902_.getFluidState();
        return !fluidstate.isEmpty() && !p_42902_.isFaceSturdy(p_42901_, p_42903_, Direction.UP) ? fluidstate.createLegacyBlock() : p_42902_;
    }

}
