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
import me.schlaubi.regnumutils.command.spi.InformationProvider
import me.schlaubi.regnumutils.command.spi.permission.PermissionHandler
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
import java.util.concurrent.ExecutorService

/**
 * The command client.
 */
@Suppress("unused")
interface CommandClient {

    /**
     * The [executor][ExecutorService] to execute command.
     */
    val executor: ExecutorService

    /**
     * The configuration of the client.
     * @see CommandClientConfiguration
     */
    val config: CommandClientConfiguration

    /**
     * The registered aliases and their command.
     */
    val commandAssociations: Map<String, Command>

    /**
     * All registered commands.
     */
    val commands: List<Command>
        get() = commandAssociations.values.distinct()

    /**
     * The permission handler.
     * @see PermissionHandler
     */
    val permissionHandler: PermissionHandler

    /**
     * The information provider.
     * @see InformationProvider
     */
    val informationProvider: InformationProvider

    /**
     * Registers the [command].
     */
    fun registerCommand(command: Command)

    /**
     * Registers the [commands].
     */
    fun registerCommands(vararg commands: Command) = commands.forEach(this::registerCommand)

    /**
     * Registers the [commands].
     */
    fun registerCommands(commands: Collection<Command>) = commands.forEach(this::registerCommand)

    /**
     * Unregisters the [command].
     */
    fun unregisterCommand(command: Command)

    /**
     * Unregisters the [alias].
     */
    fun unregisterAlias(alias: String)

    /**
     * Dispatches a command from the [commandEvent].
     */
    fun dispatchCommand(commandEvent: CommandEvent)

    /**
     * An command event.
     * @param jda jda instance
     * @param responseNumber response number
     * @param messageId message id
     * @param channel channel
     * @param message message
     */
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