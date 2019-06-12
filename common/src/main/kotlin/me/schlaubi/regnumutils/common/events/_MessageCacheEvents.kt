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

package me.schlaubi.regnumutils.common.events

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent

/**
 * Base class for events fired by the [me.schlaubi.regnumutils.common.cache.MessageCache].
 * @property oldMessage the cached version of the message
 */
@Suppress("unused")
sealed class GenericMessageCacheEvent(
    api: JDA,
    responseNumber: Long,
    messageId: Long,
    channel: TextChannel,
    val oldMessage: Message?
) :
    GenericGuildMessageEvent(api, responseNumber, messageId, channel)

/**
 * Event that represents a message edit on a guild.
 * @property message the new edited message
 * @see me.schlaubi.regnumutils.common.cache.MessageCache to activate this event
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class GuildMessageUpdateEvent(
    api: JDA,
    responseNumber: Long,
    messageId: Long,
    channel: TextChannel,
    oldMessage: Message?,
    val message: Message
) : GenericMessageCacheEvent(api, responseNumber, messageId, channel, oldMessage) {

    val author: User
        get() = message.author

    val member: Member?
        get() = guild.getMember(author)
}

/**
 * Event that represents a message deletion on a guild.
 * @see me.schlaubi.regnumutils.common.cache.MessageCache to activate this event
 */
@Suppress("unused")
class GuildMessageDeleteEvent(
    api: JDA,
    responseNumber: Long,
    messageId: Long,
    channel: TextChannel,
    oldMessage: Message?
) : GenericMessageCacheEvent(api, responseNumber, messageId, channel, oldMessage)