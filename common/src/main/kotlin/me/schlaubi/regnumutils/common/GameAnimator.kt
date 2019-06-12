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

package me.schlaubi.regnumutils.common

import cc.hawkbot.regnum.client.core.discord.GameAnimator
import me.schlaubi.regnumutils.common.builder.GameAnimatorBuilder
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Function

/**
 * Animates the bots presence according to the specified [interval] and [TimeUnit][unit].
 * @param scheduler the [Scheduler][ScheduledExecutorService] used to animate the games
 * @param applier A [function][Consumer] that applies the games to the JDA instance
 * @param initialDelay The delay between the [start] and the first [animate] call
 * @constructor Creates a new [GameAnimator]
 * @see GameAnimator.Game
 */
@Suppress("unused")
class GameAnimator internal constructor(
    private val scheduler: ScheduledExecutorService,
    private val games: List<GameAnimator.Game>,
    private val applier: BiConsumer<GameAnimator.Game, Function<String, String>>,
    private val interval: Long = 30,
    private val initialDelay: Long = 0,
    private val unit: TimeUnit = TimeUnit.SECONDS,
    private val transform: Function<String, String>
) {

    /**
     * Starts the animator.
     * @return a [ScheduledFuture] representing the task
     */
    fun start(): ScheduledFuture<*> = scheduler.scheduleAtFixedRate(this::animate, initialDelay, interval, unit)

    /**
     * Stops the game animator.
     * @return a [List] of remaining [tasks][Runnable]
     */
    fun stop(): List<Runnable> = scheduler.shutdownNow()

    private fun animate() = applier.accept(games[ThreadLocalRandom.current().nextInt(games.size - 1)], transform)

    companion object {
        /**
         * Creates a new [GameAnimatorBuilder].
         * @return the [GameAnimatorBuilder]
         */
        @JvmStatic
        fun builder() = GameAnimatorBuilder()
    }
}

