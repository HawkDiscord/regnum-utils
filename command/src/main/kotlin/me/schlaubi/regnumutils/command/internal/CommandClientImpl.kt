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

package me.schlaubi.regnumutils.command.internal

import me.schlaubi.regnumutils.command.CommandClient
import me.schlaubi.regnumutils.command.configuration.CommandClientConfiguration
import me.schlaubi.regnumutils.command.spi.Command
import me.schlaubi.regnumutils.command.spi.permission.PermissionHandler
import java.util.concurrent.ExecutorService

class CommandClientImpl(
    override val executor: ExecutorService,
    override val config: CommandClientConfiguration,
    override val commandAssociations: Map<String, Command>,
    override val permissionHandler: PermissionHandler
) : CommandClient {
    override fun registerCommand(command: Command) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unregisterCommand(command: Command) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unregisterAlias(alias: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dispatchCommand(commandEvent: CommandClient.CommandEvent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}