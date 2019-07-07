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

package me.schlaubi.regnumutils.command.spi

import me.schlaubi.regnumutils.command.CommandClient
import me.schlaubi.regnumutils.command.util.CommandFormatter
import me.schlaubi.regnumutils.common.messaging.SafeMessage
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import java.util.concurrent.TimeUnit

/**
 * The context of an command execution.
 */
@Suppress("unused")
interface Context {

    /**
     * The [Command] that was executed
     */
    val command: Command

    /**
     * The [Arguments] of the exexcution
     */
    val args: Arguments

    /**
     * The message which invoked the command
     */
    val message: Message

    /**
     * The [CommandClient] which has processed the command
     */
    val commandClient: CommandClient

    /**
     * The [JDA] instance.
     */
    val jda: JDA
        get() = message.jda

    /**
     * The id of [message]
     */
    val messageId: Long
        get() = message.idLong

    /**
     * The [TextChannel] of [message]
     */
    val channel: TextChannel
        get() = message.textChannel

    /**
     * The author of the [message].
     */
    val author: User
        get() = message.author

    /**
     * The member of the [author].
     */
    val member: Member
        get() = message.member!! //Because it's a guild message this can't be null

    /**
     * The guild of the [channel].
     */
    val guild: Guild
        get() = message.guild

    /**
     * The [self member][Member] of the bot.
     */
    val me: Member
        get() = guild.selfMember

    /**
     * the [SelfUser] of the botz
     */
    val selfUser: SelfUser
        get() = jda.selfUser

    /**
     * @see SafeMessage.sendMessage
     */
    fun respond(content: String) = SafeMessage.sendMessage(content, channel, this::notifyUserAboutPermissionError)

    /**
     * @see SafeMessage.sendMessage
     */
    fun respond(content: String, delay: Long, unit: TimeUnit = TimeUnit.SECONDS) =
        SafeMessage.sendMessage(content, channel, this::notifyUserAboutPermissionError, delay, unit)

    /**
     * @see SafeMessage.sendMessage
     */
    fun respond(embed: MessageEmbed) = SafeMessage.sendMessage(embed, channel, this::notifyUserAboutPermissionError)

    /**
     * @see SafeMessage.sendMessage
     */
    fun respond(embed: MessageEmbed, delay: Long, unit: TimeUnit = TimeUnit.SECONDS) =
        SafeMessage.sendMessage(embed, channel, this::notifyUserAboutPermissionError, delay, unit)

    /**
     * @see SafeMessage.sendMessage
     */
    fun respond(embedBuilder: EmbedBuilder) = SafeMessage.sendMessage(embedBuilder, channel, this::notifyUserAboutPermissionError)

    /**
     * @see SafeMessage.sendMessage
     */
    fun respond(embedBuilder: EmbedBuilder, delay: Long, unit: TimeUnit = TimeUnit.SECONDS) =
        SafeMessage.sendMessage(embedBuilder, channel, this::notifyUserAboutPermissionError, delay, unit)


    /**
     * Sends a usage message.
     * @see CommandFormatter.formatCommand
     * @see Context.respond
     * @return A [net.dv8tion.jda.api.requests.restaction.MessageAction]
     */
    fun sendUsage() = respond(CommandFormatter.formatCommand(commandClient, guild, command))

    /**
     * Sends the user an permission error via DM.
     * @see me.schlaubi.regnumutils.command.configuration.CommandClientConfiguration.buildPermissionErrorMessage
     */
    fun notifyUserAboutPermissionError() {
        val message = commandClient.config.buildPermissionErrorMessage(this)
        author.openPrivateChannel().queue {
            it.sendMessage(message).queue()
        }
    }

}