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
import me.schlaubi.regnumutils.command.utils.put

/**
 * Normal command interface.
 * @property displayName name for command in help messages
 * @property permissions the command's [Permissions]
 * @property aliases the command's aliases
 * @property description the command's description
 * @property exampleUsage the command's example usage
 */
abstract class AbstractCommand(
    override val displayName: String,
    override val permissions: Permissions,
    override val aliases: Array<String>,
    override val description: String,
    override val exampleUsage: String = ""
) : Command {

    final override val subCommandAssociations = mutableMapOf<String, Command>()

    /**
     * Normal command interface.
     * @property displayName name for command in help messages
     * @property permissions the command's [Permissions]
     * @property alias the command's invoke
     * @property description the command's description
     * @property exampleUsage the command's example usage
     */
    constructor(
        displayName: String,
        permissions: Permissions,
        alias: String,
        description: String,
        exampleUsage: String = ""
    ) : this(displayName, permissions, arrayOf(alias), description, exampleUsage)

    override fun registerSubCommand(subCommand: SubCommand) {
        subCommand.parent = this
        subCommandAssociations.put(subCommand)
    }
}