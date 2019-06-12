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

package me.schlaubi.regnumutils.common.jda

import cc.hawkbot.regnum.client.core.discord.GameAnimator
import cc.hawkbot.regnum.client.core.discord.impl.JDAShardManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.sharding.ShardManager
import java.util.function.Function

/**
 * The [JDA] instance used by the [cc.hawkbot.regnum.client.core.discord.ShardManager].
 * @see JDAShardManager
 */
val cc.hawkbot.regnum.client.core.discord.ShardManager.jda: ShardManager
    get() = (this as JDAShardManager).jda // We know that its an JDAShardManager because this whole lib is for the JDA

/**
 * Converts a [GameAnimator.Game] to an [Activity] object which can be used for changing a [jda presence][net.dv8tion.jda.api.managers.Presence].
 * @param transform a function that converts the games content if there are placeholders init
 * @see Activity
 */
fun GameAnimator.Game.toActivity(transform: (String) -> String = { it }) =
    Activity.of(Activity.ActivityType.fromKey(type), transform(content))

/**
 * Converts a [GameAnimator.Game] to an [OnlineStatus] object which can be used for changing a [jda presence][net.dv8tion.jda.api.managers.Presence]
 * @see OnlineStatus
 */
val GameAnimator.Game.onlineStatus: OnlineStatus
    get() = OnlineStatus.fromKey(status)

/**
 * Applies a [GameAnimator.Game] to a a [jda presence][net.dv8tion.jda.api.managers.Presence]
 * @param transform a function that converts the games content if there are placeholders init
 * @see net.dv8tion.jda.api.managers.Presence
 */
fun JDA.applyGame(game: GameAnimator.Game, transform: (String) -> String = { it }) =
    presence.setPresence(game.onlineStatus, game.toActivity(transform))

/**
 * Applies a [GameAnimator.Game] to a a [jda presence][net.dv8tion.jda.api.managers.Presence]
 * @param transform a function that converts the games content if there are placeholders init
 * @see net.dv8tion.jda.api.managers.Presence
 */
fun ShardManager.applyGame(game: GameAnimator.Game, transform: (String) -> String = { it }) =
    setPresence(game.onlineStatus, game.toActivity(transform))

/**
 * Wrapper for extension functions.
 */
object JDAExtensions {

    /**
     * @see cc.hawkbot.regnum.client.core.discord.ShardManager.jda
     */
    @JvmStatic
    fun jda(shardManager: cc.hawkbot.regnum.client.core.discord.ShardManager) = shardManager.jda

    /**
     * @see GameAnimator.Game.toActivity
     */
    @JvmStatic
    fun gameToActivity(game: GameAnimator.Game) = game.toActivity()

    /**
     * @see GameAnimator.Game.onlineStatus
     */
    @JvmStatic
    fun gameToOnlineStatus(game: GameAnimator.Game) = game.onlineStatus

    /**
     * @see JDA.applyGame
     */
    @JvmStatic
    fun applyGame(jda: JDA, game: GameAnimator.Game, transform: Function<String, String> = Function { it }) =
        jda.applyGame(
            game
        ) { transform.apply(it) }

    /**
     * @see ShardManager.applyGame
     */
    @JvmStatic
    fun applyGame(
        jda: ShardManager, game: GameAnimator.Game, transform: Function<String, String> = Function { it }
    ) =
        jda.applyGame(game) { transform.apply(it) }
}