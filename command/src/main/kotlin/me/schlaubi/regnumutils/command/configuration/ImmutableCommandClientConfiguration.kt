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
import net.dv8tion.jda.api.entities.Message

/**
 * An immutable implementation of [CommandClientConfiguration]
 * @property sendTyping Whether the bot should send typing before executing the command or not.
 * @property acceptMentionPrefix Whether the bot should listen for commands prefixed by its mention or not.
 * @property messageBuilder A function that build a permission error message
 */
class ImmutableCommandClientConfiguration(
    override val sendTyping: Boolean,
    override val acceptMentionPrefix: Boolean,
    val messageBuilder: (Context) -> Message, override val ownerPermission: Boolean,
    override val defaultPrefix: String,
    override val alwaysDefaultPrefix: Boolean, override val owners: List<Long>
) : CommandClientConfiguration {

    override fun buildPermissionErrorMessage(context: Context) = messageBuilder(context)
}