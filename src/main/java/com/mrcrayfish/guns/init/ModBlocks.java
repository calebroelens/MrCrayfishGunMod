package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.block.*;
import com.mrcrayfish.guns.blockentity.AirStrikeBlockEntity;
import com.mrcrayfish.guns.blockentity.BridgeGlassEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class ModBlocks
{
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);

    public static final RegistryObject<Block> WORKBENCH = register("workbench", () -> new WorkbenchBlock(Block.Properties.of(Material.METAL).strength(1.5F)));

    // Starting bridge glass
    public static final RegistryObject<Block> BRIDGE_GLASS = register("bridge_glass", () -> new BridgeGlass(
            BlockBehaviour.Properties.copy(Blocks.GLASS).noParticlesOnBreak()
    ));

    public static final RegistryObject<Block> AIRSTRIKE = register("airstrike_block", () -> new AirStrikeBlock(Block.Properties.of(Material.METAL)
            .strength(100000000F)
            .explosionResistance(100000000F)
            .noCollission())
    );


    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Reference.MOD_ID);


    public static final RegistryObject<Block> MAP_BEACON = register("map_beacon", () -> new MapBeacon(
            BlockBehaviour.Properties.copy(Blocks.BEACON)
    ));

    public static final RegistryObject<BlockEntityType<MapBeaconEntity>> MAP_BEACON_E = TILE_ENTITIES.register("map_beacon",
            () -> BlockEntityType.Builder.of(MapBeaconEntity::new, ModBlocks.MAP_BEACON.get()).build(null));

    public static final RegistryObject<BlockEntityType<BridgeGlassEntity>> BRIDGE_GLASS_E = TILE_ENTITIES.register("bridge_glass",
            () -> BlockEntityType.Builder.of(BridgeGlassEntity::new, ModBlocks.BRIDGE_GLASS.get()).build(null));

    public static final RegistryObject<BlockEntityType<AirStrikeBlockEntity>> AIRSTRIKE_E = TILE_ENTITIES.register(
            "airstrike_block", () -> BlockEntityType.Builder.of(AirStrikeBlockEntity::new, ModBlocks.AIRSTRIKE.get()).build(null));


    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier)
    {
        return register(id, blockSupplier, block1 -> new BlockItem(block1, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, @Nullable Function<T, BlockItem> supplier)
    {
        RegistryObject<T> registryObject = REGISTER.register(id, blockSupplier);
        if(supplier != null)
        {
            ModItems.REGISTER.register(id, () -> supplier.apply(registryObject.get()));
        }
        return registryObject;
    }
}
