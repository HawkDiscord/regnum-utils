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

@SuppressWarnings({"WeakerAccess", "unused"})
public class AnnotatedEventManger extends JDAEventMangerAdapter {

    public AnnotatedEventManger() {
        this(Executors.newCachedThreadPool(new DefaultThreadFactory(("EventPool"))));
    }

    public AnnotatedEventManger(ExecutorService executorService) {
        super(new cc.hawkbot.regnum.client.event.impl.AnnotatedEventManger(executorService));
    }
}
