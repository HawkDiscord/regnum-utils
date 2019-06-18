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

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member

/**
 * Provider for some information.
 */
interface InformationProvider {

    /**
     * Whether the [member] is a bot owner or not.
     */
    fun isOwner(member: Member): Boolean

    /**
     * The custom prefix for the [guild] or `null` if there is no custom prefix.
     */
    fun getPrefix(guild: Guild): String?
}