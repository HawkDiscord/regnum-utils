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

package me.schlaubi.regnumutils.common.cache

import net.dv8tion.jda.api.entities.Message

class MemoryMessageCache private constructor() : MessageCache {

    private val storage = mutableMapOf<Long, Message>()

    override val keys: MutableSet<Long>
        get() = storage.keys

    override val values: MutableCollection<Message>
        get() = storage.values

    override val size: Int
        get() = storage.size

    override fun contains(id: Long) = storage.containsKey(id)

    override fun isEmpty() = storage.isEmpty()

    override fun get(key: Long) = storage[key]

    override fun put(key: Long, value: Message) = storage.put(key, value)

    override fun remove(key: Long) = storage.remove(key)

    override fun clear() = storage.clear()

    companion object {
        /**
         * @see MessageCache.activate
         */
        internal fun create() = MemoryMessageCache()
    }
}