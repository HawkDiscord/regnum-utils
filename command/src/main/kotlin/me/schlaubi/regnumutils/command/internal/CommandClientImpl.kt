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

package me.schlaubi.regnumutils.command.internal

import me.schlaubi.regnumutils.command.CommandClient
import me.schlaubi.regnumutils.command.configuration.CommandClientConfiguration
import me.schlaubi.regnumutils.command.event.CommandFailEvent
import me.schlaubi.regnumutils.command.event.CommandPermissionViolationEvent
import me.schlaubi.regnumutils.command.spi.Command
import me.schlaubi.regnumutils.command.spi.InformationProvider
import me.schlaubi.regnumutils.command.spi.permission.PermissionHandler
import me.schlaubi.regnumutils.command.utils.put
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.GenericEvent
import java.util.concurrent.ExecutorService

internal class CommandClientImpl(
    override val executor: ExecutorService,
    override val config: CommandClientConfiguration,
    commands: List<Command>,
    override val permissionHandler: PermissionHandler,
    override val informationProvider: InformationProvider
    ) : CommandClient {

    private val _commandAssociations = mutableMapOf<String, Command>()

    override val commandAssociations: Map<String, Command>
        get() = _commandAssociations

    init {
        registerCommands(commands)
    }

    override fun registerCommand(command: Command) = _commandAssociations.put(command)

    override fun unregisterCommand(command: Command) =
        _commandAssociations.forEach { (alias, _) -> if (alias in command.aliases) _commandAssociations.remove(alias) }

    override fun unregisterAlias(alias: String) = _commandAssociations.remove(alias).run { Unit }

    override fun dispatchCommand(commandEvent: CommandClient.CommandEvent) {
        val message = commandEvent.message
        val author = message.author

        if (message.isWebhookMessage or author.isBot or author.isFake) {
            return
        }

        parseCommand(message)
    }

    private fun parseCommand(message: Message) {
        // Check for prefix
        val prefix = resolvePrefix(message) ?: return
        // Cut of prefix
        val input = message.contentRaw.substring(prefix.length)

        // Split arguments
        val rawArgs = input.split("\\s+".toRegex())

        // Just in case someone just sends the prefix
        if (rawArgs.isEmpty()) {
            return
        }

        // Find (sub)command
        val commandPair = resolveCommand(args = rawArgs) ?: return

        // Send typing
        if (config.sendTyping) {
            message.channel.sendTyping().queue()
        }

        val command = commandPair.first
        val args = commandPair.second

        val arguments = ArgumentsImpl(args)
        val context = ContextImpl(command, arguments, message, this)

        // Check permissions
        if (!permissionHandler.isCovered(context)) {
            val jda = message.jda
            fireEvent(jda, CommandPermissionViolationEvent(jda, 403, message.guild, command))
            return
        }

        // run command
        processCommand(command, arguments, context)
    }

    private fun processCommand(command: Command, arguments: ArgumentsImpl, context: ContextImpl) {
        executor.execute {
            try {
                command.process(arguments, context)
            } catch (everything: Throwable) {
                val jda = context.jda
                fireEvent(jda, CommandFailEvent(jda, 200, context.guild, context, everything))
            }
        }
    }

    private fun resolvePrefix(message: Message): String? {
        val guildPrefix = informationProvider.getPrefix(message.guild)
        val content = message.contentRaw
        val mention = message.guild.selfMember.asMention
        val defaultPrefix = config.defaultPrefix
        return when {
            ((guildPrefix == null) or config.alwaysDefaultPrefix) and content.startsWith(defaultPrefix) -> defaultPrefix
            (guildPrefix != null) && content.startsWith(guildPrefix) -> guildPrefix
            config.acceptMentionPrefix and content.startsWith(mention) -> mention
            else -> null
        }
    }

    private fun resolveCommand(
        command: Command? = null,
        associations: Map<String, Command> = commandAssociations,
        args: List<String>
    ): Pair<Command, List<String>>? {
        // Find the invoke
        val invoke = args[0].toLowerCase()
        // Search for a command
        val foundCommand = associations[invoke] ?: return command?.to(args)
        // Cut off invoke
        val newArgs = if (args.size > 1) args.subList(1, args.size) else emptyList()
        // Look for sub commands
        if (foundCommand.hasSubCommands() and newArgs.isNotEmpty())
            return resolveCommand(foundCommand, foundCommand.subCommandAssociations, newArgs)
        // Return command if there are no more sub commands
        return foundCommand to newArgs
    }

    private fun fireEvent(jda: JDA, event: GenericEvent) = jda.eventManager.handle(event)

}