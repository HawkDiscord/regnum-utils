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
 * Container for command client options.
 * @see MutableCommandClientConfiguration
 * @see ImmutableCommandClientConfiguration
 */
interface CommandClientConfiguration {

    /**
     * The default prefix.
     */
    val defaultPrefix: String

    /**
     * Whether the default prefix should always be valid or not.
     * Event if there is a custom prefix for that guild
     */
    val alwaysDefaultPrefix: Boolean

    /**
     * A list of the bot-owner's id's
     * @see ownerPermission
     */
    val owners: List<Long>

    /**
     * Whether the bot should send typing before executing the command or not.
     */
    val sendTyping: Boolean

    /**
     * Whether the bot should listen for commands prefixed by its mention or not.
     */
    val acceptMentionPrefix: Boolean

    /**
     * Whether the owner should be allowed to execute every command or not.
     * @see me.schlaubi.regnumutils.command.spi.permission.DefaultPermissionHandler
     */
    val ownerPermission: Boolean

    /**
     * A function that build an permission error for the [context].
     */
    fun buildPermissionErrorMessage(context: Context): Message


}