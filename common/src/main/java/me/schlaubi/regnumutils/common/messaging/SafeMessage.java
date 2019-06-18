/*
 * Regnum utilities - Some common utils for Discord bots
 *
 * Copyright (C) 2019 Michael Rittmeister
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

package me.schlaubi.regnumutils.common.messaging;

import me.schlaubi.regnumutils.common.formatting.FormatUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.utils.AttachmentOption;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Useful util for safe message sending.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class SafeMessage {

    /**
     * This runnable will get invoked when one of the following methods fails:
     *
     * @see this#sendMessage(TextChannel, Message)
     * @see this#sendMessage(TextChannel, MessageBuilder)
     * @see this#sendMessage(TextChannel, MessageEmbed)
     * @see this#sendMessage(TextChannel, MessageEmbed)
     * @see this#sendMessage(TextChannel, CharSequence)
     */
    @NotNull
    @SuppressWarnings("CanBeFinal")
    public static Runnable DEFAULT_ERROR_HANDLER = () -> {

    };

    /**
     * Sends an plain-text message safely in a {@link TextChannel} and deletes it after a specified amount of time.
     * @param channel the channel the message should get send int
     * @param content the content of the message
     * @param deleteAfter the time after the message should get deleted again
     * @param unit the unit of the {@code deleteAfter} parameter
     * @return a {@link CompletionStage} containing the sent message
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(
            @NotNull TextChannel channel,
            @NotNull CharSequence content,
            long deleteAfter,
            @NotNull TimeUnit unit
    ) {
        return delete(sendMessage(channel, content, DEFAULT_ERROR_HANDLER), deleteAfter, unit, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Sends an plain-text message safely in a {@link TextChannel} and deletes it after a specified amount of time.
     * @param channel the channel the message should get send int
     * @param content the content of the message
     * @param errorHandler a {@link Runnable} that should get executed when there is no permission to execute the message
     * @param deleteAfter the time after the message should get deleted again
     * @param unit the unit of the {@code deleteAfter} parameter
     * @return a {@link CompletionStage} containing the sent message
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(
            @NotNull TextChannel channel,
            @NotNull CharSequence content,
            @NotNull Runnable errorHandler,
            long deleteAfter,
            @NotNull TimeUnit unit
    ) {
        return delete(sendMessage(channel, content, errorHandler), deleteAfter, unit, errorHandler);
    }

    /**
     * Sends an plain-text message safely in a {@link TextChannel}.
     * @param channel the channel the message should get send int
     * @param content the content of the message
     * @return a {@link MessageAction} that sends the message
     */
    @NotNull
    public static MessageAction sendMessage(TextChannel channel, CharSequence content) {
        return sendMessage(channel, content, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Sends an embed message safely in a {@link TextChannel} and deletes it after a specified amount of time.
     * @param channel the channel the message should get send int
     * @param embedBuilder the builder of the embed
     * @param deleteAfter the time after the message should get deleted again
     * @param unit the unit of the {@code deleteAfter} parameter
     * @return a {@link CompletionStage} containing the sent message
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(
            @NotNull TextChannel channel,
            @NotNull EmbedBuilder embedBuilder,
            long deleteAfter,
            @NotNull TimeUnit unit
    ) {
        return delete(sendMessage(channel, embedBuilder, DEFAULT_ERROR_HANDLER), deleteAfter, unit, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Sends an embed message safely in a {@link TextChannel} and deletes it after a specified amount of time.
     * @param channel the channel the message should get send int
     * @param embedBuilder the builder of the embed
     * @param errorHandler a {@link Runnable} that should get executed when there is no permission to execute the message
     * @param deleteAfter the time after the message should get deleted again
     * @param unit the unit of the {@code deleteAfter} parameter
     * @return a {@link CompletionStage} containing the sent message
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(
            @NotNull TextChannel channel,
            @NotNull EmbedBuilder embedBuilder,
            @NotNull Runnable errorHandler,
            long deleteAfter,
            @NotNull TimeUnit unit
    ) {
        return delete(sendMessage(channel, embedBuilder, errorHandler), deleteAfter, unit, errorHandler);
    }

    /**
     * Sends an embed message safely in a {@link TextChannel}.
     * @param channel the channel the message should get send int
     * @param embedBuilder the builder of the embed
     * @return a {@link MessageAction} that sends the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull TextChannel channel, @NotNull EmbedBuilder embedBuilder) {
        return sendMessage(channel, embedBuilder.build(), DEFAULT_ERROR_HANDLER);
    }

    /**
     * Sends an embed message safely in a {@link TextChannel}.
     * @param channel the channel the message should get send int
     * @param errorHandler a {@link Runnable} that should get executed when there is no permission to execute the message
     * @return a {@link MessageAction} that sends the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull TextChannel channel, @NotNull EmbedBuilder message, @NotNull Runnable errorHandler) {
        return sendMessage(channel, message.build(), errorHandler);
    }

    /**
     * Sends an embed message safely in a {@link TextChannel} and deletes it after a specified amount of time.
     * @param channel the channel the message should get send int
     * @param embed the embed of the message
     * @param deleteAfter the time after the message should get delete
     * @param unit the unit of the {@code deleteAfter} parameter
     * @return a {@link CompletionStage} containing the sent message
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(
            @NotNull TextChannel channel,
            @NotNull MessageEmbed embed,
            long deleteAfter,
            @NotNull TimeUnit unit
    ) {
        return delete(sendMessage(channel, embed, DEFAULT_ERROR_HANDLER), deleteAfter, unit, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Sends an embed message safely in a {@link TextChannel} and deletes it after a specified amount of time.
     * @param channel the channel the message should get send int
     * @param embed the embed of the message
     * @param errorHandler a {@link Runnable} that should get executed when there is no permission to execute the message
     * @param deleteAfter the time after the message should get deleted again
     * @param unit the unit of the {@code deleteAfter} parameter
     * @return a {@link CompletionStage} containing the sent message
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(
            @NotNull TextChannel channel,
            @NotNull MessageEmbed embed,
            @NotNull Runnable errorHandler,
            long deleteAfter,
            @NotNull TimeUnit unit
    ) {
        return delete(sendMessage(channel, embed, errorHandler), deleteAfter, unit, errorHandler);
    }

    /**
     * Sends an embed message safely in a {@link TextChannel}.
     * @param channel the channel the message should get send int
     * @param embed the embed of the message
     * @return a {@link MessageAction} that sends the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull TextChannel channel, @NotNull MessageEmbed embed) {
        return sendMessage(channel, embed, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Sends a message safely in a {@link TextChannel} and deletes it after a specified amount of time.
     * @param channel the channel the message should get send int
     * @param message the message
     * @param deleteAfter the time after the message should get deleted again
     * @param unit the unit of the {@code deleteAfter} parameter
     * @return a {@link CompletionStage} containing the sent message
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(
            @NotNull TextChannel channel,
            @NotNull MessageBuilder message,
            long deleteAfter,
            @NotNull TimeUnit unit
    ) {
        return delete(sendMessage(channel, message, DEFAULT_ERROR_HANDLER), deleteAfter, unit, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Sends a message safely in a {@link TextChannel} and deletes it after a specified amount of time.
     * @param channel the channel the message should get send int
     * @param message the message
     * @param errorHandler a {@link Runnable} that should get executed when there is no permission to execute the message
     * @param deleteAfter the time after the message should get deleted again
     * @param unit the unit of the {@code deleteAfter} parameter
     * @return a {@link CompletionStage} containing the sent message
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(
            @NotNull TextChannel channel,
            @NotNull MessageBuilder message,
            @NotNull Runnable errorHandler,
            long deleteAfter,
            @NotNull TimeUnit unit
    ) {
        return delete(sendMessage(channel, message, errorHandler), deleteAfter, unit, errorHandler);
    }

    /**
     * Sends a message safely in a {@link TextChannel}.
     * @param channel the channel the message should get send int
     * @param message the message
     * @return a {@link MessageAction} that sends the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull TextChannel channel, @NotNull MessageBuilder message) {
        return sendMessage(channel, message, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Sends a message safely in a {@link TextChannel}.
     * @param channel the channel the message should get send int
     * @param message the builder of the message
     * @param errorHandler a {@link Runnable} that should get executed when there is no permission to execute the message
     * @return a {@link MessageAction} that sends the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull TextChannel channel, @NotNull MessageBuilder message, @NotNull Runnable errorHandler) {
        return sendMessage(channel, message.build(), errorHandler);
    }

    /**
     * Sends a message safely in a {@link TextChannel} and deletes it after a specified amount of time.
     * @param channel the channel the message should get send int
     * @param message the message
     * @param deleteAfter the time after the message should get deleted again
     * @param unit the unit of the {@code deleteAfter} parameter
     * @return a {@link CompletionStage} containing the sent message
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(
            @NotNull TextChannel channel,
            @NotNull Message message,
            long deleteAfter,
            @NotNull TimeUnit unit
    ) {
        return delete(sendMessage(channel, message, DEFAULT_ERROR_HANDLER), deleteAfter, unit, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Sends a message safely in a {@link TextChannel} and deletes it after a specified amount of time.
     * @param channel the channel the message should get send int
     * @param message the message
     * @param errorHandler a {@link Runnable} that should get executed when there is no permission to execute the message
     * @param deleteAfter the time after the message should get deleted again
     * @param unit the unit of the {@code deleteAfter} parameter
     * @return a {@link CompletionStage} containing the sent message
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(
            @NotNull TextChannel channel,
            @NotNull Message message,
            @NotNull Runnable errorHandler,
            long deleteAfter,
            @NotNull TimeUnit unit
    ) {
        return delete(sendMessage(channel, message, errorHandler), deleteAfter, unit, errorHandler);
    }

    /**
     * Sends a message safely in a {@link TextChannel}.
     * @param channel the channel the message should get send int
     * @param message the message
     * @return a {@link MessageAction} that sends the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull TextChannel channel, @NotNull Message message) {
        return sendMessage(channel, message, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Sends a message safely in a {@link TextChannel}.
     * @param channel the channel the message should get send int
     * @param message the message
     * @param errorHandler a {@link Runnable} that should get executed when there is no permission to execute the message
     * @return a {@link MessageAction} that sends the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull TextChannel channel, @NotNull Message message, @NotNull Runnable errorHandler) {
        if (isPermitted(channel)) {
            return channel.sendMessage(message);
        } else {
            errorHandler.run();
            return new EmptyMessageAction(channel.getJDA(), channel, channel.getGuild());
        }
    }

    /**
     * Sends an embed message safely in a {@link TextChannel}.
     * @param channel the channel the message should get send int
     * @param embed the embed
     * @param errorHandler a {@link Runnable} that should get executed when there is no permission to execute the message
     * @return a {@link MessageAction} that sends the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull TextChannel channel, @NotNull MessageEmbed embed, @NotNull Runnable errorHandler) {
        if (isEmbedPermitted(channel)) {
            return sendMessage(channel, EmbedUtil.message(embed), errorHandler);
        } else {
            return sendMessage(channel, FormatUtil.stringifyEmbed(embed), errorHandler);
        }
    }

    /**
     * Sends a plain-text message safely in a {@link TextChannel}.
     * @param channel the channel the message should get send int
     * @param content the content of the message
     * @param errorHandler a {@link Runnable} that should get executed when there is no permission to execute the message
     * @return a {@link MessageAction} that sends the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull TextChannel channel, @NotNull CharSequence content, @NotNull Runnable errorHandler) {
        return sendMessage(channel, EmbedUtil.message(content), errorHandler);
    }

    private static CompletionStage<Message> delete(MessageAction action, long deleteAfter, TimeUnit unit, Runnable errorHandler) {
        return action
                .submit()
                .whenComplete((message, exception) -> {
                    if (exception == null) {
                        message.delete().queueAfter(deleteAfter, unit);
                    } else {
                        errorHandler.run();
                    }
                });
    }

    private static boolean isEmbedPermitted(TextChannel channel) {
        return hasPermissions(channel, Permission.MESSAGE_EMBED_LINKS);
    }

    private static boolean isPermitted(TextChannel channel) {
        return hasPermissions(channel, Permission.MESSAGE_WRITE);
    }

    private static boolean hasPermissions(TextChannel channel, Permission... permission) {
        return channel.getGuild().getSelfMember().hasPermission(channel, permission);
    }

    private static class EmptyMessageAction implements MessageAction {

        private final JDA jda;
        private final MessageChannel channel;
        private final Guild guild;

        private EmptyMessageAction(JDA jda, MessageChannel channel, Guild guild) {
            this.jda = jda;
            this.channel = channel;
            this.guild = guild;
        }

        @Nonnull
        @Override
        public JDA getJDA() {
            return jda;
        }

        @Nonnull
        @Override
        public MessageAction setCheck(@Nullable BooleanSupplier checks) {
            return this;
        }

        @Override
        public void queue(@Nullable Consumer<? super Message> success, @Nullable Consumer<? super Throwable> failure) {

        }

        @Override
        public Message complete(boolean shouldQueue) {
            throw new UnsupportedOperationException("EmptyMessageAction does not support complete() calls");
        }

        @Nonnull
        @Override
        public CompletableFuture<Message> submit(boolean shouldQueue) {
            return CompletableFuture.failedFuture(new InsufficientPermissionException(guild, Permission.MESSAGE_WRITE));
        }

        @Nonnull
        @Override
        public MessageChannel getChannel() {
            return channel;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean isEdit() {
            return false;
        }

        @Nonnull
        @Override
        public MessageAction apply(@Nullable Message message) {
            return this;
        }

        @Nonnull
        @Override
        public MessageAction tts(boolean isTTS) {
            return this;
        }

        @Nonnull
        @Override
        public MessageAction reset() {
            return this;
        }

        @Nonnull
        @Override
        public MessageAction nonce(@Nullable String nonce) {
            return this;
        }

        @Nonnull
        @Override
        public MessageAction content(@Nullable String content) {
            return this;
        }

        @Nonnull
        @Override
        public MessageAction embed(@Nullable MessageEmbed embed) {
            return this;
        }

        @Nonnull
        @Override
        public MessageAction append(@Nullable CharSequence csq, int start, int end) {
            return this;
        }

        @Nonnull
        @Override
        public MessageAction append(char c) {
            return this;
        }

        @Nonnull
        @Override
        public MessageAction addFile(@Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options) {
            return this;
        }

        @Nonnull
        @Override
        public MessageAction addFile(@Nonnull File file, @Nonnull String name, @Nonnull AttachmentOption... options) {
            return this;
        }

        @Nonnull
        @Override
        public MessageAction clearFiles() {
            return this;
        }

        @Nonnull
        @Override
        public MessageAction clearFiles(@Nonnull BiConsumer<String, InputStream> finalizer) {
            return this;
        }

        @Nonnull
        @Override
        public MessageAction clearFiles(@Nonnull Consumer<InputStream> finalizer) {
            return this;
        }

        @Nonnull
        @Override
        public MessageAction override(boolean bool) {
            return this;
        }
    }
}
