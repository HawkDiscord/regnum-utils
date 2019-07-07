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

import me.schlaubi.regnumutils.command.internal.PermissionsImpl
import net.dv8tion.jda.api.Permission

@Suppress("unused")
interface Permissions {

    /**
     * Everyone can execute the command.
     */
    val public: Boolean

    /**
     * Only the bot owner can execute the command.
     */
    val ownerExclusive: Boolean

    /**
     * Only someone with [Permission.MANAGE_SERVER] or [Permission.ADMINISTRATOR] can execute that command.
     * @see DefaultPermissionHandler
     */
    val serverOwnerExclusive: Boolean

    /**
     * Some permission node.
     */
    val node: String

    /**
     * Only people with that [Permission] can execute the command.
     */
    val discordPermission: Permission?

    companion object {
        private val publicPermission =
            PermissionsImpl(true, false, false, "public", null)

        private val ownerPermission =
            PermissionsImpl(false, true, false, "botOwner", null)

        private val serverOwnerPermission =
            PermissionsImpl(false, false, true, "serverOwner", null)

        /**
         * Everyone can execute the command.
         */
        @JvmStatic
        @JvmName("publicPermissions")
        fun public(): Permissions = publicPermission

        /**
         * Only the bot owner can execute the command.
         */
        @JvmStatic
        fun botOwner(): Permissions = ownerPermission

        /**
         * Only someone with [Permission.MANAGE_SERVER] or [Permission.ADMINISTRATOR] can execute that command.
         * @see DefaultPermissionHandler
         */
        @JvmStatic
        fun serverOwner(): Permissions = serverOwnerPermission

        /**
         * Only people with that [permission] can execute the command.
         */
        @JvmStatic
        fun discord(permission: Permission): Permissions = PermissionsImpl(
            false,
            false,
            false,
            permission.getName(),
            permission
        )
    }

}