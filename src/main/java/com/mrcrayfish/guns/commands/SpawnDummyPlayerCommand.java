package com.mrcrayfish.guns.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mrcrayfish.guns.GunMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameType;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class SpawnDummyPlayerCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(
                Commands.literal("spawndummy")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            ServerLevel level = source.getLevel();

                            GameProfile profile = new GameProfile(UUID.randomUUID(), "DummyTarget");
                            FakePlayer dummy = FakePlayerFactory.get(level, profile);

                            double x = source.getPosition().x + 3;
                            double y = source.getPosition().y + 1;
                            double z = source.getPosition().z;
                            dummy.setPos(x, y, z);
                            dummy.setHealth(20f);

                            // Send player info to all clients BEFORE adding the entity
                            ClientboundPlayerInfoUpdatePacket infoPacket = new ClientboundPlayerInfoUpdatePacket(
                                    EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER),
                                    List.of(dummy)
                            );
                            level.getServer().getPlayerList().broadcastAll(infoPacket);

                            level.addFreshEntity(dummy);

                            GunMod.LOGGER.info("Spawning dummy at {}, {}, {}", x, y, z);
                            GunMod.LOGGER.info("Dummy UUID: {}", dummy.getUUID());

                            source.sendSuccess(
                                    Component.literal("Spawned dummy player at " + (int)x + ", " + (int)y + ", " + (int)z),
                                    false
                            );
                            return 1;
                        })
        );
    }
    public static void onRegisterCommands(RegisterCommandsEvent event)
    {
        register(event.getDispatcher());
    }
}
