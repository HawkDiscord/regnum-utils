/*
 * Regnum utilities - Some common utils for Discord bots
 *
 * Copyright (C) 2019 Michael Rittmeister
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

@file:Suppress("HasPlatformType", "unused")

package me.schlaubi.regnumutils.common.jda

import cc.hawkbot.regnum.client.core.discord.GameAnimator
import me.schlaubi.regnumutils.common.messaging.SafeMessage
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import java.util.concurrent.TimeUnit

/**
 * Util class containing some JDA utils.
 */
class JDAUtils {
    companion object {
        /**
         * Converts an JDA [Activity] and [OnlineStatus] into a [GameAnimator.Game].
         */
        @JvmStatic
        fun gameOf(activity: Activity, status: OnlineStatus): GameAnimator.Game {
            return GameAnimator.Game(
                activity.type.key,
                status.key,
                activity.name
            )
        }

        /**
         * Builds a plain text message containing the specified [content].
         * @see MessageBuilder
         */
        @JvmStatic
        fun message(content: CharSequence): Message {
            return MessageBuilder(content).build()
        }

        /**
         * Builds a plain text message containing the specified [embed].
         * @see MessageBuilder
         */
        @JvmStatic
        fun message(embed: MessageEmbed): Message {
            return MessageBuilder(embed).build()
        }

    }
}

/* All methods from SafeMessage.java wrapped into extensions */

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: CharSequence, deleteAfter: Long, unit: TimeUnit) =
    SafeMessage.sendMessage(this, content, deleteAfter, unit)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: CharSequence, errorHandler: () -> Unit, deleteAfter: Long, unit: TimeUnit) =
    SafeMessage.sendMessage(this, content, errorHandler, deleteAfter, unit)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: CharSequence, errorHandler: () -> Unit) =
    SafeMessage.sendMessage(this, content, errorHandler)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessageSafe(content: CharSequence) =
    SafeMessage.sendMessage(this, content)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: EmbedBuilder, deleteAfter: Long, unit: TimeUnit) =
    SafeMessage.sendMessage(this, content, deleteAfter, unit)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: EmbedBuilder, errorHandler: () -> Unit, deleteAfter: Long, unit: TimeUnit) =
    SafeMessage.sendMessage(this, content, errorHandler, deleteAfter, unit)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: EmbedBuilder, errorHandler: () -> Unit) =
    SafeMessage.sendMessage(this, content, errorHandler)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: EmbedBuilder) =
    SafeMessage.sendMessage(this, content)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: MessageEmbed, deleteAfter: Long, unit: TimeUnit) =
    SafeMessage.sendMessage(this, content, deleteAfter, unit)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: MessageEmbed, errorHandler: () -> Unit, deleteAfter: Long, unit: TimeUnit) =
    SafeMessage.sendMessage(this, content, errorHandler, deleteAfter, unit)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: MessageEmbed, errorHandler: () -> Unit) =
    SafeMessage.sendMessage(this, content, errorHandler)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessageSafe(content: MessageEmbed) =
    SafeMessage.sendMessage(this, content)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: MessageBuilder, deleteAfter: Long, unit: TimeUnit) =
    SafeMessage.sendMessage(this, content, deleteAfter, unit)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: MessageBuilder, errorHandler: () -> Unit, deleteAfter: Long, unit: TimeUnit) =
    SafeMessage.sendMessage(this, content, errorHandler, deleteAfter, unit)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: MessageBuilder, errorHandler: () -> Unit) =
    SafeMessage.sendMessage(this, content, errorHandler)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: MessageBuilder) =
    SafeMessage.sendMessage(this, content)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: Message, deleteAfter: Long, unit: TimeUnit) =
    SafeMessage.sendMessage(this, content, deleteAfter, unit)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: Message, errorHandler: () -> Unit, deleteAfter: Long, unit: TimeUnit) =
    SafeMessage.sendMessage(this, content, errorHandler, deleteAfter, unit)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessage(content: Message, errorHandler: () -> Unit) =
    SafeMessage.sendMessage(this, content, errorHandler)

/**
 * @see SafeMessage.sendMessage
 */
fun TextChannel.sendMessageSafe(content: Message) =
    SafeMessage.sendMessage(this, content)