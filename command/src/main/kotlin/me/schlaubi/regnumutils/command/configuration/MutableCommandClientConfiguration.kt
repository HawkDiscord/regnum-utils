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

package me.schlaubi.regnumutils.command.configuration

import me.schlaubi.regnumutils.command.spi.Context
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message
import java.util.function.Function

/**
 * A mutable implementation of the [CommandClientConfiguration].
 */
class MutableCommandClientConfiguration : CommandClientConfiguration {

    override val sendTyping: Boolean
        get() = _sendTyping
    override val acceptMentionPrefix: Boolean
        get() = _acceptMentionPrefix

    private var _sendTyping = true

    private var _acceptMentionPrefix = true

    /**
     * A function that build a permission error message
     */
    var messageBuilder: (Context) -> Message =
        { MessageBuilder("It looks like I am unable to write in that channel please try again in another channel!").build() }

    /**
     * Sets whether the bot should send typing before executing the command or not.
     * @param sendTyping the new value
     */
    fun setSendTyping(sendTyping: Boolean) {
        this._sendTyping = sendTyping;
    }

    /**
     * Sets whether the bot should listen for commands prefixed by its mention or not.
     * @param acceptMentionPrefix the new value
     */
    fun setAcceptMentionPrefix(acceptMentionPrefix: Boolean) {
        this._acceptMentionPrefix = acceptMentionPrefix
    }

    /**
     * @see MutableCommandClientConfiguration.messageBuilder
     */
    fun setPermissionErrorMessageBuilder(builder: Function<Context, Message>) {
        this.messageBuilder = { builder.apply(it) }
    }

    override fun buildPermissionErrorMessage(context: Context) = messageBuilder(context)

    /**
     * Converts this into an [ImmutableCommandClientConfiguration].
     */
    fun toImmutableCommandClientConfiguration() = ImmutableCommandClientConfiguration(
        sendTyping, acceptMentionPrefix, messageBuilder
    )
}