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
import me.schlaubi.regnumutils.common.messaging.SafeMessage
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import java.util.concurrent.TimeUnit

@Suppress("unused")
interface Context {

    val command: Command

    val args: Arguments

    val message: Message

    val commandClient: CommandClient

    val jda: JDA
        get() = message.jda

    val messageId: Long
        get() = message.idLong

    val channel: TextChannel
        get() = message.textChannel

    val author: User
        get() = message.author

    val member: Member
        get() = message.member!! //Because it's a guild message this can't be null

    val guild: Guild
        get() = message.guild

    val me: Member
        get() = guild.selfMember

    val selfUser: SelfUser
        get() = jda.selfUser

    fun respond(content: String) = SafeMessage.sendMessage(content, channel, this::notifyUserAboutPermissionError)

    fun respond(content: String, delay: Long, unit: TimeUnit = TimeUnit.SECONDS) =
        SafeMessage.sendMessage(content, channel, this::notifyUserAboutPermissionError, delay, unit)

    fun respond(embed: MessageEmbed) = SafeMessage.sendMessage(embed, channel, this::notifyUserAboutPermissionError)

    fun respond(embed: MessageEmbed, delay: Long, unit: TimeUnit = TimeUnit.SECONDS) =
        SafeMessage.sendMessage(embed, channel, this::notifyUserAboutPermissionError, delay, unit)

    fun respond(embedBuilder: EmbedBuilder) = SafeMessage.sendMessage(embedBuilder, channel, this::notifyUserAboutPermissionError)

    fun respond(embedBuilder: EmbedBuilder, delay: Long, unit: TimeUnit = TimeUnit.SECONDS) =
        SafeMessage.sendMessage(embedBuilder, channel, this::notifyUserAboutPermissionError, delay, unit)

    fun notifyUserAboutPermissionError() {
        val message = commandClient.config.buildPermissionErrorMessage(this)
        author.openPrivateChannel().queue {
            it.sendMessage(message).queue()
        }
    }

}