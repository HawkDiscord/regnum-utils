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

package me.schlaubi.regnumutils.command.event;

import me.schlaubi.regnumutils.command.spi.Command;
import me.schlaubi.regnumutils.command.spi.permission.Permissions;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.Nonnull;

/**
 * Event that is getting fired whenever someone tries to execute a command without having the needed permissions.
 */
@SuppressWarnings("unused")
public class CommandPermissionViolationEvent extends GenericCommandEvent {

    /**
     * Constructs an {@link CommandPermissionViolationEvent}.
     *
     * @param api            the {@link JDA} instance
     * @param responseNumber the response number
     * @param guild          the {@link Guild} on which the command failed
     * @param command        the {@link Command} which has thrown the permission error
     */
    public CommandPermissionViolationEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, @Nonnull Command command) {
        super(api, responseNumber, guild, command);
    }

    /**
     * Returns the missing permissions.
     * @return the {@link Permissions}
     */
    @Nonnull
    public Permissions getPermissions() {
        return getCommand().getPermissions();
    }
}
