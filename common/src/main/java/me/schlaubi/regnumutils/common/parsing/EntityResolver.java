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

package me.schlaubi.regnumutils.common.parsing;

import me.schlaubi.regnumutils.common.Misc;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Useful tool to parse a text-message for EntityResolvable.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class EntityResolver {

    /**
     * Resolves an {@link User} entity by a mention, it's id or it's name (case-sensitive).
     *
     * @param input e.g an user input that should get parsed for mentions
     * @param jda the {@link JDA} instance of you bot
     * @return the entity or {@code null} if there was no entity found for that input
     */
    @Nullable
    public static User resolveUser(
            @NotNull String input,
            @NotNull JDA jda
    ) {
        return resolveUser(input, jda, false);
    }

    /**
     * Resolves an {@link TextChannel} entity by a mention, it's id or it's name.
     *
     * @param input e.g an user input that should get parsed for mentions
     * @param jda the {@link JDA} instance of you bot
     * @param ignoreCase Whether the name should be resolved case-sensitive or not
     * @return the entity or {@code null} if there was no entity found for that input
     */
    @Nullable
    public static User resolveUser(
            @NotNull String input,
            @NotNull JDA jda,
            boolean ignoreCase
    ) {
        return resolveEntity(input, Message.MentionType.USER, jda::getUserById, jda::getUsersByName, ignoreCase);
    }


    /**
     * Resolves an {@link Role} entity by a mention, it's id or it's name (case-sensitive).
     *
     * @param input e.g an user input that should get parsed for mentions
     * @param guild the {@link Guild} the member is on
     * @return the entity or {@code null} if there was no entity found for that input
     */
    @Nullable
    public static Member resolveMember(
            @NotNull String input,
            @NotNull Guild guild
    ) {
        return resolveMember(input, guild, false);
    }

    /**
     * Resolves an {@link TextChannel} entity by a mention, it's id or it's name.
     *
     * @param input e.g an user input that should get parsed for mentions
     * @param guild the {@link Guild} the member is on
     * @param ignoreCase Whether the name should be resolved case-sensitive or not
     * @return the entity or {@code null} if there was no entity found for that input
     */
    @Nullable
    public static Member resolveMember(
            @NotNull String input,
            @NotNull Guild guild,
            boolean ignoreCase
    ) {
        return resolveEntity(input, Message.MentionType.USER, guild::getMemberById, guild::getMembersByName);
    }

    /**
     * Resolves an {@link TextChannel} entity by a mention, it's id or it's name (case-sensitive).
     *
     * @param input e.g an user input that should get parsed for mentions
     * @param guild the {@link Guild} the channel is on
     * @return the entity or {@code null} if there was no entity found for that input
     */
    @Nullable
    public static TextChannel resolveTextChannel(
            @NotNull String input,
            @NotNull Guild guild
    ) {
        return resolveTextChannel(input, guild, false);
    }

    /**
     * Resolves an {@link TextChannel} entity by a mention, it's id or it's name.
     *
     * @param input e.g an user input that should get parsed for mentions
     * @param guild the {@link Guild} the channel is on
     * @param ignoreCase Whether the name should be resolved case-sensitive or not
     * @return the entity or {@code null} if there was no entity found for that input
     */
    @Nullable
    public static TextChannel resolveTextChannel(
            @NotNull String input,
            @NotNull Guild guild,
            boolean ignoreCase
    ) {
        return resolveEntity(input, Message.MentionType.CHANNEL, guild::getTextChannelById, guild::getTextChannelsByName);
    }

    /**
     * Resolves an {@link Role} entity by a mention, it's id or it's name (case-sensitive).
     *
     * @param input e.g an user input that should get parsed for mentions
     * @param guild the {@link Guild} the role is on
     * @return the entity or {@code null} if there was no entity found for that input
     */
    @Nullable
    public static Role resolveRole(
            @NotNull String input,
            @NotNull Guild guild
    ) {
        return resolveRole(input, guild, false);
    }

    /**
     * Resolves an {@link Role} entity by a mention, it's id or it's name.
     *
     * @param input e.g an user input that should get parsed for mentions
     * @param guild the {@link Guild} the role is on
     * @param ignoreCase Whether the name should be resolved case-sensitive or not
     * @return the entity or {@code null} if there was no entity found for that input
     */
    @Nullable
    public static Role resolveRole(
            @NotNull String input,
            @NotNull Guild guild,
            boolean ignoreCase
    ) {
        return resolveEntity(input, Message.MentionType.ROLE, guild::getRoleById, guild::getRolesByName);
    }

    /**
     * Resolves an {@link IMentionable mentionable} entity by a mention, it's id or it's name (case-sensitive).
     *
     * @param input e.g an user input that should get parsed for mentions
     * @param type the {@link net.dv8tion.jda.api.entities.Message.MentionType} of the expected entity
     * @param idResolver a {@link Function} that can resolve that type of entity by its id
     * @param nameResolver a {@link Function} that can resolve that type of entity by its name
     * @param <T> the type of the entity
     * @return the entity or {@code null} if there was no entity found for that input
     */
    @Nullable
    public static <T extends IMentionable> T resolveEntity(
            @NotNull String input,
            @NotNull Message.MentionType type,
            @NotNull Function<@NotNull String, @Nullable T> idResolver,
            @NotNull BiFunction<@NotNull String, Boolean, @NotNull Collection<T>> nameResolver
    ) {
        return resolveEntity(input, type, idResolver, nameResolver, false);
    }

    /**
     * Resolves an {@link IMentionable mentionable} entity by a mention, it's id or it's name.
     *
     * @param input e.g an user input that should get parsed for mentions
     * @param type the {@link net.dv8tion.jda.api.entities.Message.MentionType} of the expected entity
     * @param idResolver a {@link Function} that can resolve that type of entity by its id
     * @param nameResolver a {@link Function} that can resolve that type of entity by its name
     * @param ignoreCase Whether the name should be resolved case-sensitive or not
     * @param <T> the type of the entity
     * @return the entity or {@code null} if there was no entity found for that input
     */
    @Nullable
    public static <T extends IMentionable> T resolveEntity(
            @NotNull String input,
            @NotNull Message.MentionType type,
            @NotNull Function<@NotNull String, @Nullable T> idResolver,
            @NotNull BiFunction<@NotNull String, Boolean, @NotNull Collection<T>> nameResolver,
            boolean ignoreCase
    ) {
        if (input.isBlank() || input.isEmpty()) {
            throw new IllegalArgumentException("Input string hast to contain something");
        }

        var matcher = type.getPattern().matcher(input);
        if (matcher.matches()) {
            return idResolver.apply(matcher.group(1));
        } else {
            if (Misc.isNumeric(input)) {
                idResolver.apply(input);
            }
            return Misc.first(nameResolver.apply(input, ignoreCase));
        }
    }
}
