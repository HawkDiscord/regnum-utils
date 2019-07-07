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

import cc.hawkbot.regnum.client.event.EventManager
import cc.hawkbot.regnum.client.event.EventSubscriber
import me.schlaubi.regnumutils.common.event.JDAListenerAdapter
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent
import net.dv8tion.jda.api.utils.MarkdownSanitizer
import java.time.OffsetDateTime
import java.util.*

/**
 * Cache for [messages][Message] in order to get the old content in `MESSAGE_UPDATE` and `MESSAGE_DELETE` events.
 * @see Message
 */
@Suppress("unused")
interface MessageCache : MutableMap<Long, Message> {

    override val entries: MutableSet<MutableMap.MutableEntry<Long, Message>>
        get() = throw UnsupportedOperationException("Message caches do not support entrie sets")

    /**
     * A [MutableSet] of all cached ids.
     */
    override val keys: MutableSet<Long>

    /**
     * A [MutableSet] of all cached messages
     */
    override val values: MutableCollection<Message>

    /**
     * @see MessageCache.contains
     */
    override fun containsKey(key: Long) = contains(key)

    override fun containsValue(value: Message) =
        throw UnsupportedOperationException("Message caches do not support to verify values")

    /**
     * Checks whether an id is cached or not.
     * @return if there is a message corresponding to that id or not
     */
    operator fun contains(id: Long): Boolean

    /**
     * Checks whether there is at least one message cached.
     * @return if the cache is empty or not
     */
    override fun isEmpty(): Boolean

    /**
     * Gets a message from the cache.
     * @param key the id of the message
     * @return the message or `null` if there was no message cached
     */
    override operator fun get(key: Long): Message?

    /**
     * Adds a message to the cache.
     * @param key the id of the message
     * @param message the message
     * @see MessageCache.cacheMessage for recommended usage
     */
    override fun put(key: Long, value: Message): Message?

    /**
     * Adds a message to the cache.
     * @param message the message
     */
    fun cacheMessage(message: Message) = put(
        message.idLong,
        CachedMessage(message)
    )

    /**
     * @see MessageCache.cacheMessage
     */
    operator fun plusAssign(message: Message) = cacheMessage(message).run { Unit }

    override fun putAll(from: Map<out Long, Message>) =
        throw UnsupportedOperationException("Message caches do not support the putAll function")

    /**
     * Removes a message by it's [id][key] from the cache.
     */
    override fun remove(key: Long): Message?

    /**
     * @see MessageCache.remove
     */
    operator fun minusAssign(id: Long) = remove(id).run { Unit }

    /**
     * Removes the [message] from the cache.
     */
    fun remove(message: Message) = remove(message.idLong)

    /**
     * @see MessageCache.remove
     */
    operator fun minusAssign(message: Message) = remove(message).run { Unit }

    /**
     * Clears the cache
     */
    override fun clear()

    private data class CachedMessage(
        private val edited: Boolean,
        private val pinned: Boolean,
        private val mentionsEveryone: Boolean,
        private val contentRaw: String,
        private val contentDisplay: String,
        private val tts: Boolean,
        private val _embeds: List<MessageEmbed>,
        private val _idLong: Long,
        private val _nonce: String?,
        private val editedTime: OffsetDateTime?
    ) : Message {

        constructor(message: Message) : this(
            message.isEdited,
            message.isPinned,
            message.mentionsEveryone(),
            message.contentRaw,
            message.contentDisplay,
            message.isTTS,
            message.embeds,
            message.idLong,
            message.nonce,
            message.timeEdited
        )

        private val _contentStripped by lazy { MarkdownSanitizer.sanitize(contentDisplay) }

        override fun isFromType(type: ChannelType) = type == ChannelType.TEXT

        override fun isEdited() = edited

        override fun getActivity() = unsupported()

        override fun isPinned() = pinned

        override fun mentionsEveryone() = mentionsEveryone

        override fun addReaction(emote: Emote) = unsupported()

        override fun addReaction(unicode: String) = unsupported()

        override fun clearReactions() = unsupported()

        override fun formatTo(formatter: Formatter?, flags: Int, width: Int, precision: Int) = unsupported()

        override fun getJumpUrl() = unsupported()

        override fun getContentRaw() = contentRaw

        override fun getContentStripped() = _contentStripped

        override fun getGuild() = unsupported()

        override fun isTTS() = tts

        override fun isMentioned(mentionable: IMentionable, vararg types: Message.MentionType?) = unsupported()

        override fun editMessageFormat(format: String, vararg args: Any?) = unsupported()

        override fun getMentionedChannels() = unsupported()

        override fun getMember() = unsupported()

        override fun getIdLong() = _idLong

        override fun getContentDisplay() = contentDisplay

        override fun getPrivateChannel() = unsupported()

        override fun getChannelType() = ChannelType.TEXT

        override fun getAttachments() = unsupported()

        override fun getMentionedRoles() = unsupported()

        override fun pin() = unsupported()

        override fun getMentionedMembers(guild: Guild) = unsupported()

        override fun getMentionedMembers() = unsupported()

        override fun unpin() = unsupported()

        override fun getCategory() = unsupported()

        override fun getInvites() = unsupported()

        override fun getMentionedUsers() = unsupported()

        override fun getEmotes() = unsupported()

        override fun getAuthor() = unsupported()

        override fun editMessage(newContent: CharSequence) = unsupported()

        override fun editMessage(newContent: MessageEmbed) = unsupported()

        override fun editMessage(newContent: Message) = unsupported()

        override fun delete() = unsupported()

        override fun getMentions(vararg types: Message.MentionType?) = unsupported()

        override fun isWebhookMessage() = unsupported()

        override fun getEmbeds() = _embeds

        override fun getType() = unsupported()

        override fun getChannel() = unsupported()

        override fun getJDA() = unsupported()

        override fun getReactions() = unsupported()

        override fun getTimeEdited() = editedTime

        override fun getTextChannel() = unsupported()

        override fun getNonce() = _nonce

        private fun unsupported(): Nothing = throw UnsupportedOperationException()

    }

    private class MessageWatcher(
        private val cache: MessageCache,
        private val eventManager: EventManager
    ) : JDAListenerAdapter() {

        @SubscribeEvent
        @EventSubscriber
        override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
            cache.cacheMessage(event.message)
        }

        @SubscribeEvent
        @EventSubscriber
        override fun onGuildMessageUpdate(event: GuildMessageUpdateEvent) {
            val previous = cache[event.messageIdLong]
            eventManager.fireEvent(
                me.schlaubi.regnumutils.common.events.GuildMessageUpdateEvent(
                    event.jda,
                    event.responseNumber,
                    event.messageIdLong,
                    event.channel,
                    previous,
                    event.message
                )
            )
            cache.replace(event.messageIdLong, event.message)
        }

        @SubscribeEvent
        @EventSubscriber
        override fun onGuildMessageDelete(event: GuildMessageDeleteEvent) {
            val previous = cache[event.messageIdLong]
            eventManager.fireEvent(
                me.schlaubi.regnumutils.common.events.GuildMessageDeleteEvent(
                    event.jda,
                    event.responseNumber,
                    event.messageIdLong,
                    event.channel,
                    previous
                )
            )
            cache.remove(event.messageIdLong)
        }
    }

    companion object {
        /**
         * Creates a new Message cache and registers it's listner to the specified [eventManager].
         * @return the new [MessageCache]
         */
        @JvmStatic
        fun activate(eventManager: EventManager): MessageCache {
            return with (MemoryMessageCache.create()) {
                eventManager.register(MessageWatcher(this, eventManager))
                this
            }
        }
    }
}
