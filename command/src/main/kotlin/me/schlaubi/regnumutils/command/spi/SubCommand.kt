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

import me.schlaubi.regnumutils.command.spi.permission.Permissions

/**
 * Representation of a sub-command.
 * @see AbstractCommand
 * @see Command
 */
abstract class SubCommand(
    displayName: String,
    permissions: Permissions,
    aliases: Array<String>,
    description: String,
    exampleUsage: String
) : AbstractCommand(displayName, permissions, aliases, description, exampleUsage) {

    /**
     * @see AbstractCommand
     */
    constructor(
        displayName: String,
        permissions: Permissions,
        alias: String,
        description: String,
        exampleUsage: String = ""
    ) : this(displayName, permissions, arrayOf(alias), description, exampleUsage)

    /**
     * The commands parent
     */
    lateinit var parent: Command
}