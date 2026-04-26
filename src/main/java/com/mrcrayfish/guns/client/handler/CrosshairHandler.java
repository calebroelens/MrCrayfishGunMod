package com.mrcrayfish.guns.client.handler;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.render.crosshair.Crosshair;
import com.mrcrayfish.guns.client.render.crosshair.TechCrosshair;
import com.mrcrayfish.guns.client.render.crosshair.TexturedCrosshair;
import com.mrcrayfish.guns.event.GunFireEvent;
import com.mrcrayfish.guns.event.GunProjectileHitEvent;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.message.S2CMessageProjectileHitEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.client.gui.GuiComponent.GUI_ICONS_LOCATION;

/**
 * Author: MrCrayfish
 */
public class CrosshairHandler
{
    private static CrosshairHandler instance;

    private boolean hasHit = false;
    private boolean isCriticalHit = false;
    private float hitTimer = 0;
    private Crosshair savedCrosshair = null;
    private Entity lastHitEntity = null;
    private float cumulativeDamage = 0;

    // Kill feed
    private int killTimer = 0;
    private String killedPlayerName = null;
    private ResourceLocation killedPlayerSkin = null;

    public static CrosshairHandler get()
    {
        if(instance == null)
        {
            instance = new CrosshairHandler();
        }
        return instance;
    }

    private final Map<ResourceLocation, Crosshair> idToCrosshair = new HashMap<>();
    private final List<Crosshair> registeredCrosshairs = new ArrayList<>();
    private Crosshair currentCrosshair = null;

    private CrosshairHandler()
    {
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "better_default")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "circle")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "filled_circle"), false));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "square")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "round")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "arrow")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "dot")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "box")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "hit_marker")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "hit_marker_crit")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "line")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "t")));
        this.register(new TexturedCrosshair(new ResourceLocation(Reference.MOD_ID, "smiley")));
        this.register(new TechCrosshair());
    }

    /**
     * Registers a new crosshair. If the crosshair has already been registered, it will be ignored.
     */
    public void register(Crosshair crosshair)
    {
        if(!this.idToCrosshair.containsKey(crosshair.getLocation()))
        {
            this.idToCrosshair.put(crosshair.getLocation(), crosshair);
            this.registeredCrosshairs.add(crosshair);
        }
    }

    /**
     * Sets the crosshair using the given id. The crosshair with the associated id must be registered
     * or the default crosshair will be used.
     *
     * @param id the id of the crosshair
     */
    public void setCrosshair(ResourceLocation id)
    {
        this.currentCrosshair = this.idToCrosshair.getOrDefault(id, Crosshair.DEFAULT);
    }

    /**
     * Gets the current crosshair
     */
    @Nullable
    public Crosshair getCurrentCrosshair()
    {
        if(this.currentCrosshair == null && this.registeredCrosshairs.size() > 0)
        {
            ResourceLocation id = ResourceLocation.tryParse(Config.CLIENT.display.crosshair.get());
            this.currentCrosshair = id != null ? this.idToCrosshair.getOrDefault(id, Crosshair.DEFAULT) : Crosshair.DEFAULT;
        }
        return this.currentCrosshair;
    }

    /**
     * Gets a list of registered crosshairs. Please note that this list is immutable.
     */
    public List<Crosshair> getRegisteredCrosshairs()
    {
        return ImmutableList.copyOf(this.registeredCrosshairs);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGuiOverlayEvent.Pre event)
    {
        if(event.getOverlay() != VanillaGuiOverlay.CROSSHAIR.type())
            return;

        // Tick the hit timer here, not in a second event handler
        if (hitTimer > 0) {
            hitTimer -= Minecraft.getInstance().getDeltaFrameTime();
            if (hitTimer <= 0) {
                hitTimer = 0;
                hasHit = false;
                // Restore the saved crosshair instead of setting null
                currentCrosshair = savedCrosshair;
                savedCrosshair = null;
                cumulativeDamage = 0;
            }
            if(cumulativeDamage > 0){
                renderHearts(
                        event.getPoseStack(),
                        event.getWindow().getGuiScaledWidth() / 2,
                        event.getWindow().getGuiScaledHeight() - 64,
                        cumulativeDamage
                );
            }

        }

        // Render kill feed if active
        if(killTimer > 0 && killedPlayerName != null && killedPlayerSkin != null)
        {
            renderKillFeed(
                    event.getPoseStack(),
                    event.getWindow().getGuiScaledWidth(),
                    event.getWindow().getGuiScaledHeight()
            );
        }

        Crosshair crosshair = this.getCurrentCrosshair();
        if(AimingHandler.get().getNormalisedAdsProgress() > 0.5)
        {
            event.setCanceled(true);
            return;
        }

        if(crosshair == null || crosshair.isDefault())
        {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return;

        ItemStack heldItem = mc.player.getMainHandItem();
        if(!(heldItem.getItem() instanceof GunItem))
            return;

        event.setCanceled(true);

        if(!mc.options.getCameraType().isFirstPerson())
            return;

        if(mc.player.getUseItem().getItem() == Items.SHIELD)
            return;

        PoseStack stack = event.getPoseStack();
        stack.pushPose();
        int scaledWidth = event.getWindow().getGuiScaledWidth();
        int scaledHeight = event.getWindow().getGuiScaledHeight();
        crosshair.render(mc, stack, scaledWidth, scaledHeight, event.getPartialTick());
        stack.popPose();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        Crosshair crosshair = this.getCurrentCrosshair();
        if(crosshair == null || crosshair.isDefault())
            return;

        crosshair.tick();
    }

    @SubscribeEvent
    public void onGunFired(GunFireEvent.Post event)
    {
        Crosshair crosshair = this.getCurrentCrosshair();
        if(crosshair == null || crosshair.isDefault())
            return;

        crosshair.onGunFired();
    }


    public static void onGunHitEntityEvent(S2CMessageProjectileHitEntity message){
        Minecraft mc = Minecraft.getInstance();
        CrosshairHandler handler = CrosshairHandler.get();
        if (!handler.hasHit) {
            handler.savedCrosshair = handler.getCurrentCrosshair();
        }
        handler.hasHit = true;
        handler.hitTimer = 10f;
        if(handler.lastHitEntity == null || handler.lastHitEntity.getId() != message.getTargetEntityId()){
            handler.cumulativeDamage = message.getDamage();
        } else {
            handler.cumulativeDamage += message.getDamage();
        }
        if(mc.level != null){
            handler.lastHitEntity = mc.level.getEntity(message.getTargetEntityId());
        }
        if(message.isHeadshot() || message.isCritical()){
            handler.isCriticalHit = true;
            handler.currentCrosshair = CrosshairHandler.get().registeredCrosshairs.get(9);
        } else {
            handler.isCriticalHit = false;
            handler.currentCrosshair = CrosshairHandler.get().registeredCrosshairs.get(8);
        }

        // Kill feed
        // --- Kill feed: show killed player's head + name for 20 ticks ---
        if(message.isDead() && mc.level != null)
        {
            Entity target = mc.level.getEntity(message.getTargetEntityId());
            if(target instanceof AbstractClientPlayer killedPlayer)
            {
                handler.killedPlayerName = killedPlayer.getGameProfile().getName();
                handler.killedPlayerSkin = killedPlayer.getSkinTextureLocation();
                handler.killTimer = 20;
            }
            else if(target instanceof Player killedPlayer)
            {
                // Fallback for non-AbstractClientPlayer (shouldn't normally happen client-side)
                handler.killedPlayerName = killedPlayer.getGameProfile().getName();
                handler.killedPlayerSkin = new ResourceLocation("textures/entity/steve.png");
                handler.killTimer = 20;
            }
        }

    }

    private void renderKillFeed(PoseStack stack, int screenWidth, int screenHeight)
    {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        int headSize = 32;
        int padding = 8;
        int margin = 10;

        // Fade out over the last 5 ticks
        float alpha = killTimer <= 5 ? killTimer / 5f : 1f;
        int alphaBits = (int)(alpha * 255) << 24;

        int headX = screenWidth - headSize - margin;
        int headY = margin;

        // --- Draw player face ---
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, killedPlayerSkin);
        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        stack.pushPose();
        // The face is at UV (8, 8) size 8×8 on a 64×64 skin texture
        GuiComponent.blit(stack, headX, headY, headSize, headSize, 8, 8, 8, 8, 64, 64);
        // Optionally overlay the hat layer (UV 40, 8)
        GuiComponent.blit(stack, headX, headY, headSize, headSize, 40, 8, 8, 8, 64, 64);
        stack.popPose();

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();

        // --- Draw player name ---
        String name = killedPlayerName;
        int textWidth = font.width(name);
        int textX = headX - textWidth - padding;
        int textY = headY + (headSize - font.lineHeight) / 2; // vertically centered with head

        // Semi-transparent dark background behind name for readability
        int bgPad = 3;
        GuiComponent.fill(stack,
                textX - bgPad,
                textY - bgPad,
                headX - padding + bgPad,
                textY + font.lineHeight + bgPad,
                alphaBits & 0x00FFFFFF | ((int)(alpha * 120) << 24) // ~50% black
        );

        font.draw(stack, name, textX, textY, 0xFFFFFF | alphaBits);
    }

    private void renderHearts(PoseStack stack, int x, int y, float health) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        // Clamp to max 10 hearts
        float clampedHealth = Math.min(health, 10f);

        // Render health number text above the hearts
        String text = (health % 1 == 0)
                ? String.valueOf((int) health)
                : String.format("%.1f", health);

        int textWidth = font.width(text);
        font.draw(
                stack, text, x - textWidth / 2f, y - font.lineHeight - 2,
                !isCriticalHit ? 0xFFFFFF : 0xFFAA00
        );

        // Calculate total hearts to render (each heart = 1, half heart = 0.5)
        int fullHearts = (int) clampedHealth;
        boolean hasHalfHeart = (clampedHealth % 1) >= 0.5f;
        int totalHearts = fullHearts + (hasHalfHeart ? 1 : 0);

        int heartSize = 9;
        int spacing = 1;
        int totalWidth = totalHearts * (heartSize + spacing);
        int startX = x - totalWidth / 2; // centered around x

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation("textures/gui/icons.png"));
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableBlend();

        for (int i = 0; i < totalHearts; i++) {
            int heartX = startX + i * (heartSize + spacing);
            boolean isHalf = (i == fullHearts) && hasHalfHeart;
            // Half heart UV: (61, 0), Full heart UV: (52, 0)
            float u = isCriticalHit
                    ? (isHalf ? 152 : 160)  // golden
                    : (isHalf ? 61 : 52);   // normal
            GuiComponent.blit(stack, heartX, y, heartSize, heartSize, u, 0, 9, 9, 256, 256);
        }

        RenderSystem.disableBlend();
    }

    /* Updates the crosshair if the config is reloaded. */
    public static void onConfigReload(ModConfigEvent.Reloading event)
    {
        ModConfig config = event.getConfig();
        if(config.getType() == ModConfig.Type.CLIENT && config.getModId().equals(Reference.MOD_ID))
        {
            ResourceLocation id = ResourceLocation.tryParse(Config.CLIENT.display.crosshair.get());
            if(id != null)
            {
                CrosshairHandler.get().setCrosshair(id);
            }
        }
    }
}
