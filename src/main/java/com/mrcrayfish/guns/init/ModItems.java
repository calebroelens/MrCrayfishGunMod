package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.blockentity.data.AirStrikeProperties;
import com.mrcrayfish.guns.common.Attachments;
import com.mrcrayfish.guns.common.GunModifiers;
import com.mrcrayfish.guns.item.*;
import com.mrcrayfish.guns.item.attachment.impl.Barrel;
import com.mrcrayfish.guns.item.attachment.impl.Stock;
import com.mrcrayfish.guns.item.attachment.impl.UnderBarrel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems
{
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<GunItem> PISTOL = REGISTER.register("pistol", () -> new GunItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SHOTGUN = REGISTER.register("shotgun", () -> new GunItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> RIFLE = REGISTER.register("rifle", () -> new GunItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GRENADE_LAUNCHER = REGISTER.register("grenade_launcher", () -> new GunItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BAZOOKA = REGISTER.register("bazooka", () -> new GunItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MEME_BAZOOKA = REGISTER.register("meme_bazooka", () -> new GunItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MINI_GUN = REGISTER.register("mini_gun", () -> new GunItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<GunItem> ASSAULT_RIFLE = REGISTER.register("assault_rifle", () -> new GunItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MACHINE_PISTOL = REGISTER.register("machine_pistol", () -> new GunItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HEAVY_RIFLE = REGISTER.register("heavy_rifle", () -> new GunItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BASIC_BULLET = REGISTER.register("basic_bullet", () -> new AmmoItem(new Item.Properties()));
    public static final RegistryObject<Item> ADVANCED_AMMO = REGISTER.register("advanced_bullet", () -> new AmmoItem(new Item.Properties()));
    public static final RegistryObject<Item> SHELL = REGISTER.register("shell", () -> new AmmoItem(new Item.Properties()));
    public static final RegistryObject<Item> MISSILE = REGISTER.register("missile", () -> new AmmoItem(new Item.Properties()));
    public static final RegistryObject<Item> GRENADE = REGISTER.register("grenade", () -> new GrenadeItem(new Item.Properties(), 20 * 4));
    public static final RegistryObject<Item> STUN_GRENADE = REGISTER.register("stun_grenade", () -> new StunGrenadeItem(new Item.Properties(), 72000));

    /* Scope Attachments */
    public static final RegistryObject<Item> SHORT_SCOPE = REGISTER.register("short_scope", () -> new ScopeItem(Attachments.SHORT_SCOPE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MEDIUM_SCOPE = REGISTER.register("medium_scope", () -> new ScopeItem(Attachments.MEDIUM_SCOPE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> LONG_SCOPE = REGISTER.register("long_scope", () -> new ScopeItem(Attachments.LONG_SCOPE, new Item.Properties().stacksTo(1)));

    /* Barrel Attachments */
    public static final RegistryObject<Item> SILENCER = REGISTER.register("silencer", () -> new BarrelItem(Barrel.create(8.0F, GunModifiers.SILENCED, GunModifiers.REDUCED_DAMAGE), new Item.Properties().stacksTo(1)));

    /* Stock Attachments */
    public static final RegistryObject<Item> LIGHT_STOCK = REGISTER.register("light_stock", () -> new StockItem(Stock.create(GunModifiers.BETTER_CONTROL), new Item.Properties().stacksTo(1), false));
    public static final RegistryObject<Item> TACTICAL_STOCK = REGISTER.register("tactical_stock", () -> new StockItem(Stock.create(GunModifiers.STABILISED), new Item.Properties().stacksTo(1), false));
    public static final RegistryObject<Item> WEIGHTED_STOCK = REGISTER.register("weighted_stock", () -> new StockItem(Stock.create(GunModifiers.SUPER_STABILISED), new Item.Properties().stacksTo(1)));

    /* Under Barrel Attachments */
    public static final RegistryObject<Item> LIGHT_GRIP = REGISTER.register("light_grip", () -> new UnderBarrelItem(UnderBarrel.create(GunModifiers.LIGHT_RECOIL), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SPECIALISED_GRIP = REGISTER.register("specialised_grip", () -> new UnderBarrelItem(UnderBarrel.create(GunModifiers.REDUCED_RECOIL), new Item.Properties().stacksTo(1)));

    /* Special customs */
    public static final RegistryObject<Item> DUPLO_BASIC = REGISTER.register("duplo_basic", () -> new DuploItem(new Item.Properties()));

    public static final RegistryObject<Item> BRIDGE_EGG = REGISTER.register("bridge_egg", () -> new BridgeEggItem(new Item.Properties()));
    public static final RegistryObject<Item> BRIDGE_EGG_PLUS = REGISTER.register("bridge_egg_plus", () -> new BridgeEggItem(new Item.Properties(), 5));
    public static final RegistryObject<Item> BRIDGE_EGG_TSUNAMI = REGISTER.register("bridge_egg_tsunami", () -> new BridgeEggItem(new Item.Properties(), 15));

    public static final RegistryObject<Item> AIRSTRIKE = REGISTER.register(
            "airstrike",
            () -> new AirStrikeItem(
                    new Item.Properties().stacksTo(16),
                    new AirStrikeProperties().explosionEveryXTick(10)
            )
    );
    public static final RegistryObject<Item> AIRSTRIKE_ORIGINAL = REGISTER.register(
            "airstrike_original",
            () -> new AirStrikeItem(
                    new Item.Properties().stacksTo(1),
                    new AirStrikeProperties()
            )
    );

    public static final RegistryObject<Item> AIRSTRIKE_CLUSTER = REGISTER.register(
            "airstrike_cluster",
            () -> new AirStrikeItem(
                  new Item.Properties().stacksTo(16),
                  new AirStrikeProperties()
                          .explosionEveryXTick(5)
                          .randomRadius(60)
                          .countPerStrike(6)
            )
    );

    public static final RegistryObject<Item> LASAGNA = REGISTER.register("lasagna", () -> new LasagnaItem(new Item.Properties().food(
            CustomFoods.LASAGNA
    )));

    public static final RegistryObject<Item> GAMEPLAY_MAP = REGISTER.register("gameplay_map", () -> new GamePlayMapItem(
            new Item.Properties()
    ));

    public static final RegistryObject<Item> GAMEPLAY_EMPTY_MAP = REGISTER.register("gameplay_empty_map", () -> new GamePlayEmptyMapItem(
            new Item.Properties()
    ));

    public static final RegistryObject<Item> DOEI_SWORD = REGISTER.register("doei_sword",
            () -> new DoeiSword(CustomTiers.DOEI, 3, -2.4F,
                    new Item.Properties().stacksTo(1).food(
        CustomFoods.DOEI_SWORD
    )));

    public static class CustomFoods{
        public static final FoodProperties LASAGNA = new FoodProperties.Builder()
                .nutrition(10)
                .saturationMod(1)
                .alwaysEat()
                .meat()
                .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 600, 2), 1f)
                .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 2), 1f)
                .build();
        public static final FoodProperties DOEI_SWORD = new FoodProperties.Builder()
                .nutrition(10)
                .saturationMod(1)
                .alwaysEat()
                .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 600, 4), 1f)
                .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3000, 255), 1f)
                .build();
    }
}
