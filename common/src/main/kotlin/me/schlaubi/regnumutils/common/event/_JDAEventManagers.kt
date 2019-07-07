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

@file:Suppress("unused")

package me.schlaubi.regnumutils.common.event

import cc.hawkbot.regnum.client.event.ListenerAdapter
import cc.hawkbot.regnum.waiter.EventWaiter
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener

/**
 * Port of [ListenerAdapter] to [EventListener].
 */
open class RegnumListenerAdapter : ListenerAdapter(), EventListener {
    @Suppress("RedundantOverride")
    override fun onEvent(event: GenericEvent) {
        super.onEvent(event)
    }
}


/**
 * Port of [net.dv8tion.jda.api.hooks.ListenerAdapter] to [cc.hawkbot.regnum.client.event.impl.EventListener].
 */
open class JDAListenerAdapter : net.dv8tion.jda.api.hooks.ListenerAdapter(), cc.hawkbot.regnum.client.event.impl.EventListener {
    override fun onEvent(event: Any) {
        if (event is GenericEvent) {
            super.onGenericEvent(event)
        }
    }
}
