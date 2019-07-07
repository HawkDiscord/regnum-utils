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

package me.schlaubi.regnumutils.common.messaging;

import me.schlaubi.regnumutils.common.formatting.FormatUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
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
import java.util.function.Supplier;

/**
 * Util to send messages safely without risking {@link InsufficientPermissionException}s
 */
@SuppressWarnings({"unused", "WeakerAccess", "CanBeFinal"})
public class SafeMessage {

    /**
     * Error handler that get passed in be methods without an {@code errorHandler} parameter
     *
     * @see SafeMessage#sendMessage(String, TextChannel)
     * @see SafeMessage#sendMessage(EmbedBuilder, TextChannel)
     * @see SafeMessage#sendMessage(MessageEmbed, TextChannel)
     * @see SafeMessage#sendMessage(String, TextChannel, long, TimeUnit)
     * @see SafeMessage#sendMessage(EmbedBuilder, TextChannel, long, TimeUnit)
     * @see SafeMessage#sendMessage(MessageEmbed, TextChannel, long, TimeUnit)
     */
    @SuppressWarnings("CanBeFinal")
    @NotNull
    private static Runnable DEFAULT_ERROR_HANDLER = () -> {
    };

    /**
     * Sends an {@link EmbedBuilder} and converts it into an plain text message if needed and deletes it after a specified amount of time.
     *
     * @param embed   the {@link MessageEmbed}
     * @param channel the channel in which the message should get sent to
     * @param delay   the amount of time to wait until deleting the message
     * @param unit    the {@link TimeUnit} for the delay
     * @return a {@link CompletionStage} containing the {@link Message} and fails when the permission check fails
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(@NotNull EmbedBuilder embed, @NotNull TextChannel channel,
                                                       long delay, @NotNull TimeUnit unit) {
        return delete(sendMessage(embed, channel), delay, unit);
    }

    /**
     * Sends an {@link EmbedBuilder} and converts it into an plain text message if needed and deletes it after a specified amount of time.
     *
     * @param embed   the {@link MessageEmbed}
     * @param channel the channel in which the message should get sent to
     * @param onError a {@link Runnable} that gets executed when the permission check fails
     * @param delay   the amount of time to wait until deleting the message
     * @param unit    the {@link TimeUnit} for the delay
     * @return a {@link CompletionStage} containing the {@link Message} and fails when the permission check fails
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(@NotNull EmbedBuilder embed, @NotNull TextChannel channel,
                                                       @NotNull Runnable onError, long delay, @NotNull TimeUnit unit) {
        return delete(sendMessage(embed, channel, onError), delay, unit);
    }

    /**
     * Sends an {@link EmbedBuilder} and converts it into an plain text message if needed
     *
     * @param embed   the {@link MessageEmbed}
     * @param channel the channel in which the message should get sent to
     * @return a {@link MessageAction} to send the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull EmbedBuilder embed, @NotNull TextChannel channel) {
        return sendMessage(embed, channel, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Sends an {@link EmbedBuilder} and converts it into an plain text message if needed
     *
     * @param embed   the {@link MessageEmbed}
     * @param channel the channel in which the message should get sent to
     * @param onError a {@link Runnable} that gets executed when the permission check fails
     * @return a {@link MessageAction} to send the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull EmbedBuilder embed, @NotNull TextChannel channel,
                                            @NotNull Runnable onError) {
        return sendMessage(embed.build(), channel, onError);
    }

    /**
     * Sends an {@link MessageEmbed} and converts it into an plain text message if needed and deletes it after a specified amount of time.
     *
     * @param embed   the {@link MessageEmbed}
     * @param channel the channel in which the message should get sent to
     * @param delay   the amount of time to wait until deleting the message
     * @param unit    the {@link TimeUnit} for the delay
     * @return a {@link CompletionStage} containing the {@link Message} and fails when the permission check fails
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(@NotNull MessageEmbed embed, @NotNull TextChannel channel,
                                                       long delay, @NotNull TimeUnit unit) {
        return delete(sendMessage(embed, channel), delay, unit);
    }

    /**
     * Sends an {@link MessageEmbed} and converts it into an plain text message if needed and deletes it after a specified amount of time.
     *
     * @param embed   the {@link MessageEmbed}
     * @param channel the channel in which the message should get sent to
     * @param onError a {@link Runnable} that gets executed when the permission check fails
     * @param delay   the amount of time to wait until deleting the message
     * @param unit    the {@link TimeUnit} for the delay
     * @return a {@link CompletionStage} containing the {@link Message} and fails when the permission check fails
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(@NotNull MessageEmbed embed, @NotNull TextChannel channel,
                                                       @NotNull Runnable onError, long delay, @NotNull TimeUnit unit) {
        return delete(sendMessage(embed, channel, onError), delay, unit);
    }

    /**
     * Sends an {@link MessageEmbed} and converts it into an plain text message if needed
     *
     * @param embed   the {@link MessageEmbed}
     * @param channel the channel in which the message should get sent to
     * @return a {@link MessageAction} to send the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull MessageEmbed embed, @NotNull TextChannel channel) {
        return sendMessage(embed, channel, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Sends an {@link MessageEmbed} and converts it into an plain text message if needed
     *
     * @param embed   the {@link MessageEmbed}
     * @param channel the channel in which the message should get sent to
     * @param onError a {@link Runnable} that gets executed when the permission check fails
     * @return a {@link MessageAction} to send the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull MessageEmbed embed, @NotNull TextChannel channel,
                                            @NotNull Runnable onError) {
        return writeCheck(channel, onError, () -> {
            if (isEmbeddable(channel)) {
                return channel.sendMessage(embed);
            } else {
                return channel.sendMessage(FormatUtil.stringifyEmbed(embed));
            }
        });
    }

    /**
     * Sends a plain text message into a {@link TextChannel} and deletes it after a specified amount of time.
     *
     * @param content the content of the message
     * @param channel the channel in which the message should get sent to
     * @param delay   the amount of time to wait until deleting the message
     * @param unit    the {@link TimeUnit} for the delay
     * @return a {@link CompletionStage} containing the {@link Message} and fails when the permission check fails
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(@NotNull String content, @NotNull TextChannel channel,
                                                       long delay, @NotNull TimeUnit unit) {
        return delete(sendMessage(content, channel), delay, unit);
    }

    /**
     * Sends a plain text message into a {@link TextChannel} and deletes it after a specified amount of time.
     *
     * @param content the content of the message
     * @param channel the channel in which the message should get sent to
     * @param onError a {@link Runnable} that gets executed when the permission check fails
     * @param delay   the amount of time to wait until deleting the message
     * @param unit    the {@link TimeUnit} for the delay
     * @return a {@link CompletionStage} containing the {@link Message} and fails when the permission check fails
     */
    @NotNull
    public static CompletionStage<Message> sendMessage(@NotNull String content, @NotNull TextChannel channel,
                                                       @NotNull Runnable onError, long delay, @NotNull TimeUnit unit) {
        return delete(sendMessage(content, channel, onError), delay, unit);
    }

    /**
     * Sends a plain text message into a {@link TextChannel}.
     *
     * @param content the content of the message
     * @param channel the channel in which the message should get sent to
     * @return a {@link MessageAction} to send the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull String content, @NotNull TextChannel channel) {
        return sendMessage(content, channel, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Sends a plain text message into a {@link TextChannel}.
     *
     * @param content the content of the message
     * @param channel the channel in which the message should get sent to
     * @param onError a {@link Runnable} that gets executed when the permission check fails
     * @return a {@link MessageAction} to send the message
     */
    @NotNull
    public static MessageAction sendMessage(@NotNull String content, @NotNull TextChannel channel,
                                            @NotNull Runnable onError) {
        return writeCheck(channel, onError, () -> channel.sendMessage(content));
    }

    private static MessageAction writeCheck(GuildChannel channel, Runnable onError, Supplier<MessageAction> action) {
        if (isWritable(channel)) {
            return action.get();
        } else {
            onError.run();
            return new EmptyMessageAction(channel.getGuild());
        }
    }

    private static CompletionStage<Message> delete(MessageAction sendAction, long delay, TimeUnit unit) {
        if (sendAction instanceof EmptyMessageAction) {
            return CompletableFuture.failedStage(new InsufficientPermissionException(((EmptyMessageAction) sendAction).getGuild(), Permission.MESSAGE_WRITE));
        }
        return sendAction.submit().thenApply(message -> {
            message.delete().queueAfter(delay, unit);
            return message;
        });
    }

    private static boolean isWritable(GuildChannel channel) {
        return hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_READ);
    }

    private static boolean isEmbeddable(GuildChannel channel) {
        return hasPermission(channel, Permission.MESSAGE_EMBED_LINKS);
    }

    private static boolean hasPermission(GuildChannel channel, Permission... permission) {
        return channel.getGuild().getSelfMember().hasPermission(channel, permission);
    }

    @SuppressWarnings("all")
    private static class EmptyMessageAction implements MessageAction {

        private final Guild guild;

        public EmptyMessageAction(Guild guild) {
            this.guild = guild;
        }

        public Guild getGuild() {
            return guild;
        }

        @Nonnull
        @Override
        public JDA getJDA() {
            return null;
        }

        @Nonnull
        @Override
        public MessageAction setCheck(@Nullable BooleanSupplier checks) {
            return null;
        }

        @Override
        public void queue(@Nullable Consumer<? super Message> success, @Nullable Consumer<? super Throwable> failure) {

        }

        @Override
        public Message complete(boolean shouldQueue) throws RateLimitedException {
            return null;
        }

        @Nonnull
        @Override
        public CompletableFuture<Message> submit(boolean shouldQueue) {
            return null;
        }

        @Nonnull
        @Override
        public MessageChannel getChannel() {
            return null;
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
            return null;
        }

        @Nonnull
        @Override
        public MessageAction tts(boolean isTTS) {
            return null;
        }

        @Nonnull
        @Override
        public MessageAction reset() {
            return null;
        }

        @Nonnull
        @Override
        public MessageAction nonce(@Nullable String nonce) {
            return null;
        }

        @Nonnull
        @Override
        public MessageAction content(@Nullable String content) {
            return null;
        }

        @Nonnull
        @Override
        public MessageAction embed(@Nullable MessageEmbed embed) {
            return null;
        }

        @Nonnull
        @Override
        public MessageAction append(@Nullable CharSequence csq, int start, int end) {
            return null;
        }

        @Nonnull
        @Override
        public MessageAction append(char c) {
            return null;
        }

        @Nonnull
        @Override
        public MessageAction addFile(@Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options) {
            return null;
        }

        @Nonnull
        @Override
        public MessageAction addFile(@Nonnull File file, @Nonnull String name, @Nonnull AttachmentOption... options) {
            return null;
        }

        @Nonnull
        @Override
        public MessageAction clearFiles() {
            return null;
        }

        @Nonnull
        @Override
        public MessageAction clearFiles(@Nonnull BiConsumer<String, InputStream> finalizer) {
            return null;
        }

        @Nonnull
        @Override
        public MessageAction clearFiles(@Nonnull Consumer<InputStream> finalizer) {
            return null;
        }

        @Nonnull
        @Override
        public MessageAction override(boolean bool) {
            return null;
        }
    }
}
