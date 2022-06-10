package com.fibermc.essentialcommands.commands;

import com.fibermc.essentialcommands.ECText;
import com.fibermc.essentialcommands.access.ServerPlayerEntityAccess;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jpcode.eccore.util.TextUtil;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class TeleportCancelCommand implements Command<ServerCommandSource> {

    public TeleportCancelCommand() {}

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        //Store command sender
        ServerPlayerEntity senderPlayer = context.getSource().getPlayer();

        var existingTeleportRequest = ((ServerPlayerEntityAccess) senderPlayer)
            .getEcPlayerData()
            .getSentTeleportRequest();

        if (existingTeleportRequest == null) {
            senderPlayer.sendMessage(
                ECText.getInstance().getText("cmd.tpcancel.error.no_exists"),
                MessageType.SYSTEM);
            return 0;
        }

        existingTeleportRequest.end();

        senderPlayer.sendMessage(
            TextUtil.concat(
                ECText.getInstance().getText("cmd.tpcancel.feedback.1"),
                existingTeleportRequest.getTargetPlayer().getDisplayName(),
                ECText.getInstance().getText("cmd.tpcancel.feedback.2")),
            MessageType.SYSTEM);

        return 1;
    }
}
