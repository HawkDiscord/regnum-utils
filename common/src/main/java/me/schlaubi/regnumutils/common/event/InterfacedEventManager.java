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

import cc.hawkbot.regnum.util.DefaultThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Adapter for using {@link cc.hawkbot.regnum.client.event.impl.InterfacedEventManager} for jda.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class InterfacedEventManager extends JDAEventMangerAdapter {

    /**
     * Creates an InterfacedEventManager.
     * @see cc.hawkbot.regnum.client.event.impl.InterfacedEventManager
     */
    public InterfacedEventManager() {
        this(Executors.newCachedThreadPool(new DefaultThreadFactory(("EventPool"))));
    }

    /**
     * Creates an InterfacedEventManager.
     * @see cc.hawkbot.regnum.client.event.impl.InterfacedEventManager
     */
    public InterfacedEventManager(ExecutorService executor) {
        super(new cc.hawkbot.regnum.client.event.impl.InterfacedEventManager(executor));
    }
}
