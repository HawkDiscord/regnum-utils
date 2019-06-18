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

package me.schlaubi.regnumutils.command.listener

import cc.hawkbot.regnum.client.event.EventSubscriber
import me.schlaubi.regnumutils.command.CommandClient
import me.schlaubi.regnumutils.common.event.JDAListenerAdapter
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent

sealed class CommandListenerBase(protected val commandClient: CommandClient) : JDAListenerAdapter() {

    @EventSubscriber
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) =
        commandClient.dispatchCommand(CommandClient.CommandEvent(event))
}

/**
 * A command listener which listenes for [GuildMessageReceivedEvent]s.
 * @see me.schlaubi.regnumutils.command.CommandClientBuilder.setCommandListener
 */
class MessageReceivedCommandListener(commandClient: CommandClient) : CommandListenerBase(commandClient)

/**
 * A command listener which listenes for [GuildMessageReceivedEvent]s and [GuildMessageUpdateEvent]s.
 * @see me.schlaubi.regnumutils.command.CommandClientBuilder.setCommandListener
 */
class MessageEditCommandListener(commandClient: CommandClient) : CommandListenerBase(commandClient) {
    @EventSubscriber
    override fun onGuildMessageUpdate(event: GuildMessageUpdateEvent) =
        commandClient.dispatchCommand(CommandClient.CommandEvent(event))
}