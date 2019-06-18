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

import me.schlaubi.regnumutils.command.spi.Context;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.Nonnull;

/**
 * Event that is fired whenever a {@link me.schlaubi.regnumutils.command.spi.Command} was executed successfully.
 */
@SuppressWarnings("unused")
public class CommandExecutedEvent extends GenericCommandContextEvent {

    /**
     * Constructs an {@link CommandExecutedEvent}.
     * @param api the {@link JDA} instance
     * @param responseNumber the response number
     * @param guild the {@link Guild} on which the command failed
     * @param context the {@link Context} of the command
     */
    public CommandExecutedEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, @Nonnull Context context) {
        super(api, responseNumber, guild, context);
    }

}
