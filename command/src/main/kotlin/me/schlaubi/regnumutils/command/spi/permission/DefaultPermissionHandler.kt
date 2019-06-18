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

package me.schlaubi.regnumutils.command.spi.permission

import me.schlaubi.regnumutils.command.spi.Context
import me.schlaubi.regnumutils.command.utils.isBotOwner
import net.dv8tion.jda.api.Permission

/**
 * Default implementation of [PermissionHandler]
 */
class DefaultPermissionHandler : PermissionHandler {

    override fun isCovered(context: Context): Boolean {
        val permissions = context.command.permissions

        // Public command
        if (permissions.public) {
            return true
        }

        // Owner permission
        val member = context.member
        val commandClient = context.commandClient
        if (commandClient.config.ownerPermission and member.isBotOwner(commandClient)) {
            return true
        }

        // Check bot owner
        if (permissions.ownerExclusive) {
            return member.isBotOwner(commandClient)
        }

        // Check server owner
        if (permissions.serverOwnerExclusive) {
            return context.member.hasPermission(Permission.MANAGE_SERVER)
        }

        // Check for discord permission
        val discordPermission = permissions.discordPermission
        if (discordPermission != null) {
            return member.hasPermission(context.channel, discordPermission)
        }
        return false // Just in case of a misconfiguration
    }
}