/*
 * Regnum - A Discord bot clustering system made for Hawk
 *
 * Copyright (C) 2019  Michael Rittmeister
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package me.schlaubi.regnumutils.command.util;

import me.schlaubi.regnumutils.command.CommandClient;
import me.schlaubi.regnumutils.command.spi.Command;
import me.schlaubi.regnumutils.command.spi.SubCommand;
import me.schlaubi.regnumutils.common.formatting.FormatUtil;
import me.schlaubi.regnumutils.common.messaging.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public class CommandFormatter {

    @NotNull
    public static EmbedBuilder formatCommand(
            @NotNull CommandClient commandClient,
            @NotNull Command command
    ) {
        return formatCommand(commandClient.getConfig().getDefaultPrefix(), command);
    }

    @NotNull
    public static EmbedBuilder formatCommand(
            @NotNull CommandClient commandClient,
            @NotNull Guild guild,
            @NotNull Command command
    ) {

        var customPrefix = commandClient.getInformationProvider().getPrefix(guild);
        var prefix = customPrefix == null ? commandClient.getConfig().getDefaultPrefix() : customPrefix;
        return formatCommand(prefix, command);
    }

    @NotNull
    @SuppressWarnings("WeakerAccess")
    public static EmbedBuilder formatCommand(
            @NotNull CharSequence prefix,
            @NotNull Command command
    ) {
        var usageBase = formatUsageBase(prefix, command);
        var embed = EmbedUtil.info(
                command.getName() + " - Help",
                command.getDescription()
        );

        embed.addField("Aliases", FormatUtil.formatSingleCodeBlockCollection(List.of(command.getAliases())), false);

        if (command instanceof SubCommand) {
            embed.addField("Type", "SubCommand", true);
            embed.addField("Parent", ((SubCommand) command).getParent().getName(), true);
        } else {
            embed.addField("Type", "Command", true);
        }
        embed.addField("Usage", usageBase + command.getUsage(), false);
        if (!command.getExampleUsage().isEmpty() && !command.getExampleUsage().isBlank()) {
            embed.addField("Example Usage", usageBase + command.getExampleUsage(), false);
        }

        if (command.hasSubCommands()) {
            var buf = new StringBuilder();
            command.getSubCommandAssociations()
                    .values()
                    .stream()
                    .distinct()
                    .forEach(subCommand -> {
                        buf
                                .append(System.lineSeparator())
                                .append(usageBase)
                                .append(' ')
                                .append(subCommand.getName())
                                .append(' ')
                                .append(subCommand.getUsage())
                                .append(" - ")
                                .append(subCommand.getDescription());
                    });
            embed.addField("Subcommands", buf.toString(), false);
        }
        return embed;
    }

    private static String formatUsageBase(CharSequence prefix, Command command) {
        final var buf = new StringBuilder(prefix);
        var currentCommand = command;
        while (currentCommand != null) {
            var index = prefix.length();
            String alias = currentCommand.getName();
            buf.insert(index, alias);
            if (currentCommand != command) {
                buf.insert(index + alias.length(), ' ');
            }
            currentCommand = currentCommand instanceof SubCommand ? ((SubCommand) currentCommand).getParent() : null;
        }
        return buf.toString();
    }
}
