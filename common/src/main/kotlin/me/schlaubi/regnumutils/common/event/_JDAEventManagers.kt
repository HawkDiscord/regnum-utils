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

import cc.hawkbot.regnum.client.core.ClientEventWaiter
import cc.hawkbot.regnum.client.event.EventManager
import cc.hawkbot.regnum.client.event.ListenerAdapter
import net.dv8tion.jda.api.hooks.IEventManager
import cc.hawkbot.regnum.client.event.impl.InterfacedEventManager
import cc.hawkbot.regnum.client.event.impl.AnnotatedEventManger
import cc.hawkbot.regnum.util.DefaultThreadFactory
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Adapter for [EventManager] to [IEventManager].
 * @param manager the [EventManager] that is supposed to be adapted
 */
sealed class JDAEventManagerAdapter(
    private val manager: EventManager
) : IEventManager {

    /**
     * An [ClientEventWaiter] based on this [IEventManager].
     */
    val eventWaiter by lazy { ClientEventWaiter(toEventManager()) }

    override fun handle(event: GenericEvent) = manager.fireEvent(event)

    override fun register(listener: Any) = manager.register(listener)

    override fun getRegisteredListeners() = manager.registeredListeners

    override fun unregister(listener: Any) = manager.unregister(listener)

    /**
     * Converts this [IEventManager] to an [EventManager].
     */
    fun toEventManager(): EventManager = manager

}

/**
 * Implementation of [IEventManager] based on [InterfacedEventManager].
 * @see IEventManager
 * @see InterfacedEventManager
 */
class InterfacedEventManager(executor: ExecutorService = Executors.newCachedThreadPool(DefaultThreadFactory("EventListeningPool"))) :
    JDAEventManagerAdapter(InterfacedEventManager(executor))

/**
 * Implementation of [IEventManager] based on [AnnotatedEventManger].
 * @see IEventManager
 * @see AnnotatedEventManger
 */
class AnnotatedEventManager(executor: ExecutorService = Executors.newCachedThreadPool(DefaultThreadFactory("EventListeningPool"))) :
    JDAEventManagerAdapter(AnnotatedEventManger(executor))

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
