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

package me.schlaubi.regnumutils.command.util

import me.schlaubi.regnumutils.command.CommandClient
import me.schlaubi.regnumutils.command.spi.Command
import net.dv8tion.jda.api.entities.ISnowflake

/**
 * Adds all the aliases pointing to the [command] into the map.
 */
fun MutableMap<String, Command>.put(command: Command) = command.aliases.forEach { put(it, command) }

/**
 * Checks whether the snowflakes id is in the [commandClient]'s bot owners list or not.
 */
fun ISnowflake.isBotOwner(commandClient: CommandClient) = idLong in commandClient.config.owners

/**
 * Extension wrapper.
 */
@Suppress("unused")
object Extensions {
    /**
     * @see ISnowflake.isBotOwner
     */
    @JvmStatic
    fun isBotOwner(snowflake: ISnowflake, commandClient: CommandClient) = snowflake.isBotOwner(commandClient)
}