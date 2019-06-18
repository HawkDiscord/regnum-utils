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

import me.schlaubi.regnumutils.common.event.JDAListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Extension of the {@link JDAListenerAdapter} for command events.
 *
 * @see JDAListenerAdapter
 * @see me.schlaubi.regnumutils.common.event.InterfacedEventManager
 */
@SuppressWarnings({"WeakerAccess", "unused", "EmptyMethod"})
public class ListenerAdapter extends JDAListenerAdapter {

    @Override
    public void onEvent(@NotNull Object event) {
        if (event instanceof GenericCommandEvent) {
            this.onGenericCommandEvent((GenericCommandEvent) event);
            if (event instanceof CommandPermissionViolationEvent) {
                this.onCommandPermissionViolation((CommandPermissionViolationEvent) event);
            }
            if (event instanceof GenericCommandContextEvent) {
                this.onGenericCommandContextEvent((GenericCommandContextEvent) event);
                if (event instanceof CommandExecutedEvent) {
                    this.onCommandExecution((CommandExecutedEvent) event);
                }
                if (event instanceof CommandFailEvent) {
                    this.onCommandFail((CommandFailEvent) event);
                }
            }
        }
        super.onEvent(event);
    }

    public void onGenericCommandEvent(GenericCommandEvent event) {

    }

    public void onCommandPermissionViolation(CommandPermissionViolationEvent event) {

    }

    public void onGenericCommandContextEvent(GenericCommandContextEvent event) {

    }

    public void onCommandExecution(CommandExecutedEvent event) {

    }

    public void onCommandFail(CommandFailEvent event) {

    }

}
