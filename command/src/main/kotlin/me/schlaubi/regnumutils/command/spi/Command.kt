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
 * Command interface.
 */
@Suppress("unused")
interface Command {

    /**
     * The display name of the command in help messages.
     */
    val displayName: String

    /**
     * The [Permissions]s that are needed to execute that command.
     */
    val permissions: Permissions

    /**
     * All aliases of this command.
     */
    val aliases: Array<String>

    /**
     * The description of the command.
     */
    val description: String

    /**
     * An example of how to use the command.
     */
    val exampleUsage: String

    /**
     * The commands sub-commands.
     */
    val subCommandAssociations: Map<String, Command>

    fun hasSubCommands() = !subCommandAssociations.isEmpty()

    /**
     * The main method of the command.
     * @param args the arguments the user provided
     * @param context the context of the command execution
     */
    fun process(args: Arguments, context: Context)

    /**
     * Registers the [subCommand].
     */
    fun registerSubCommand(subCommand: SubCommand)

    /**
     * Registers the [subCommandAssociations].
     */
    fun registerSubCommands(vararg subCommands: SubCommand) = subCommands.forEach(this::registerSubCommand)

}