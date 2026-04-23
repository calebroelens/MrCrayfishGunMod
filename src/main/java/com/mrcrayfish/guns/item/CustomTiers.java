package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.init.ModItems;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;

public class CustomTiers {

    public static final ForgeTier DOEI = new ForgeTier(
            4,
            69,
            1.5F,
            255F,
            32,
            Tags.Blocks.NEEDS_WOOD_TOOL,
            () -> Ingredient.of(ModItems.LASAGNA::get)
    );
}
