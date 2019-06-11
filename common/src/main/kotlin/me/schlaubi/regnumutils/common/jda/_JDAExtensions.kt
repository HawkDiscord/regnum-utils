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

val cc.hawkbot.regnum.client.core.discord.ShardManager.jda: ShardManager
    get() = (this as JDAShardManager).jda

fun GameAnimator.Game.toActivity() = Activity.of(Activity.ActivityType.fromKey(type), content)

val GameAnimator.Game.onlineStatus: OnlineStatus
    get() = OnlineStatus.fromKey(status)

fun JDA.applyGame(game: GameAnimator.Game) = presence.setPresence(game.onlineStatus, game.toActivity())

fun ShardManager.applyGame(game: GameAnimator.Game) = setPresence(game.onlineStatus, game.toActivity())

object JDAExtensions {

    @JvmStatic
    fun jda(shardManager: cc.hawkbot.regnum.client.core.discord.ShardManager) = shardManager.jda

    @JvmStatic
    fun gameToActivity(game: GameAnimator.Game) = game.toActivity()

    @JvmStatic
    fun gameToOnlineStatus(game: GameAnimator.Game) = game.onlineStatus

    @JvmStatic
    fun applyGame(jda: JDA, game: GameAnimator.Game) = jda.applyGame(game)

    @JvmStatic
    fun applyGame(jda: ShardManager, game: GameAnimator.Game) = jda.applyGame(game)
}