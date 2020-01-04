package com.fibermc.essentialcommands.commands;

import com.fibermc.essentialcommands.Config;
import com.fibermc.essentialcommands.PlayerData;
import com.fibermc.essentialcommands.PlayerDataManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.MessageType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class HomeDeleteCommand implements Command<ServerCommandSource> {
    private PlayerDataManager dataManager;

    public HomeDeleteCommand(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int out = 1;
        //Store command sender
        ServerPlayerEntity senderPlayer = context.getSource().getPlayer();
        PlayerData senderPlayerData = dataManager.getOrCreate(senderPlayer);
        //Store home name
        String homeName = StringArgumentType.getString(context, "home_name");

        //Remove Home
        boolean wasSuccessful = senderPlayerData.removeHome(homeName);

        //inform command sender that the home has been removed
        if (wasSuccessful) {
            senderPlayer.sendChatMessage(
                    new LiteralText("Home ").formatted(Config.FORMATTING_DEFAULT)
                            .append(new LiteralText(homeName).formatted(Config.FORMATTING_ACCENT))
                            .append(new LiteralText(" has been deleted.").formatted(Config.FORMATTING_DEFAULT))
                    , MessageType.SYSTEM);
        } else {
            senderPlayer.sendChatMessage(
                    new LiteralText("Home ").formatted(Config.FORMATTING_ERROR)
                            .append(new LiteralText(homeName).formatted(Config.FORMATTING_ACCENT))
                            .append(new LiteralText(" could not be deleted.").formatted(Config.FORMATTING_ERROR))
                    , MessageType.SYSTEM);
            out = 0;
        }

        return out;
    }
}