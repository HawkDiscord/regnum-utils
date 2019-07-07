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

package me.schlaubi.regnumutils.common.event;

import cc.hawkbot.regnum.client.core.ClientEventWaiter;
import cc.hawkbot.regnum.client.event.EventManager;
import cc.hawkbot.regnum.waiter.EventWaiter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.IEventManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

class JDAEventMangerAdapter implements IEventManager, EventManager {

    private final EventManager internalEventManager;

    JDAEventMangerAdapter(EventManager internalEventManager) {
        this.internalEventManager = internalEventManager;
    }

    @Override
    public void fireEvent(@NotNull Object event) {
        internalEventManager.fireEvent(event);
    }

    @Override
    public void register(@NotNull Object... listeners) {
        internalEventManager.register(listeners);
    }

    @Override
    public void register(@NotNull Collection<?> listeners) {
        internalEventManager.register(listeners);
    }

    @Override
    public void register(@Nonnull Object listener) {
        internalEventManager.register(listener);
    }

    @Override
    public void unregister(@Nonnull Object listener) {
        internalEventManager.unregister(listener);
    }

    @Override
    public void handle(@Nonnull GenericEvent event) {
        fireEvent(event);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public List getRegisteredListeners() {
        return internalEventManager.getRegisteredListeners();
    }

    /**
     * Converts this into an {@link EventManager}.
     *
     * @deprecated {@link JDAEventMangerAdapter} does now implement {@link EventManager}
     * @return the event manager
     */
    @Deprecated
    public EventManager toEventManger() {
        return this;
    }

    /**
     * Returns an {@link EventWaiter} for this EventManager.
     * @return the event waiter.
     */
    public EventWaiter getEventWaiter() {
        return new ClientEventWaiter(this);
    }
}
