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

package me.schlaubi.regnumutils.command.spi

/**
 * Representation of user provided input.
 */
@Suppress("unused")
interface Arguments : Iterable<String> {

    /**
     * The args stored in a [List].
     */
    val list: List<String>

    /**
     * The args stored in a [Array].
     */
    val array: Array<String>

    /**
     * Returns the argument at the specified [index].
     */
    operator fun get(index: Int) = list.get(index)

    /**
     * Returns all arguments [from] [to] as a [List].
     */
    fun list(from: Int, to: Int) = list.subList(from, to)

    /**
     * Returns all arguments from the beginning [to] as a [List].
     */
    fun list(to: Int) = list.subList(0, to)

    /**
     * Returns all arguments from the beginning [to] as an [Array].
     */
    fun array(from: Int, to: Int) = array.slice(from..to)

    /**
     * Returns all arguments [from] [to] as an [Array].
     */
    fun array(to: Int) = array.slice(0..to)

    /**
     * Creates a string from all the elements separated using [separator] and using the given [prefix] and [postfix] if supplied.
     *
     * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the first [limit]
     * elements will be appended, followed by the [truncated] string (which defaults to "...").
     *
     * @sample samples.collections.Collections.Transformations.joinToString
     */

    fun string(
        separator: CharSequence = ", ",
        prefix: CharSequence = "",
        postfix: CharSequence = "",
        limit: Int = -1,
        truncated: CharSequence = "..."
    ) = list.joinToString(separator, prefix, postfix, limit, truncated)

    /**
     * Uses [from] and [to] as its range.
     * Creates a string from all the elements separated using [separator] and using the given [prefix] and [postfix] if supplied.
     *
     * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the first [limit]
     * elements will be appended, followed by the [truncated] string (which defaults to "...").
     *
     * @sample samples.collections.Collections.Transformations.joinToString
     */

    fun string(
        from: Int,
        to: Int,
        separator: CharSequence = ", ",
        prefix: CharSequence = "",
        postfix: CharSequence = "",
        limit: Int = -1,
        truncated: CharSequence = "..."
    ) = list(from, to).joinToString(separator, prefix, postfix, limit, truncated)

    /**
     * Uses [from] and [to] as its range.
     * Creates a string from all the elements separated using [separator] and using the given [prefix] and [postfix] if supplied.
     *
     * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the first [limit]
     * elements will be appended, followed by the [truncated] string (which defaults to "...").
     *
     * @sample samples.collections.Collections.Transformations.joinToString
     */

    fun string(
        to: Int,
        separator: CharSequence = ", ",
        prefix: CharSequence = "",
        postfix: CharSequence = "",
        limit: Int = -1,
        truncated: CharSequence = "..."
    ) = list(to).joinToString(separator, prefix, postfix, limit, truncated)

    /**
     * Returns an [Iterator] for all the arguments.
     */
    override operator fun iterator() = list.iterator()

    /**
     * Returns an [Iterator] for all arguments in the [from] [to] range.
     */
    fun iterator(from: Int, to: Int) = list(from, to).iterator()

    /**
     * Returns an [Iterator] for all arguments in the 0 [to] range.
     */
    fun iterator(to: Int) = list(to).iterator()
}