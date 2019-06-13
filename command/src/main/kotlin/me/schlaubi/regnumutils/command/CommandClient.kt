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

package me.schlaubi.regnumutils.command

import me.schlaubi.regnumutils.command.configuration.CommandClientConfiguration
import me.schlaubi.regnumutils.command.spi.Command
import me.schlaubi.regnumutils.command.spi.permission.PermissionHandler
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
import java.util.concurrent.ExecutorService

interface CommandClient {

    val executor: ExecutorService

    val config: CommandClientConfiguration

    val commandAssociations: Map<String, Command>

    val commands: List<Command>
        get() = commandAssociations.values.distinct()

    val permissionHandler: PermissionHandler

    fun registerCommand(command: Command)

    fun registerCommands(vararg commands: Command) = commands.forEach(this::registerCommand)

    fun registerCommands(commands: Collection<Command>) = commands.forEach(this::registerCommand)

    fun unregisterCommand(command: Command)

    fun unregisterAlias(alias: String)

    fun dispatchCommand(commandEvent: CommandEvent)

    class CommandEvent(
        api: JDA,
        responseNumber: Long,
        messageId: Long,
        channel: TextChannel,
        val message: Message
    ) :
        GenericGuildMessageEvent(api, responseNumber, messageId, channel) {
        constructor(event: GuildMessageReceivedEvent) : this(
            event.jda,
            event.responseNumber,
            event.messageIdLong,
            event.channel,
            event.message
        )

        constructor(event: GuildMessageUpdateEvent) : this(
            event.jda,
            event.responseNumber,
            event.messageIdLong,
            event.channel,
            event.message
        )
    }


}