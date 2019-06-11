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

package me.schlaubi.regnumutils.common.builder;

import cc.hawkbot.regnum.util.DefaultThreadFactory;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import me.schlaubi.regnumutils.common.GameAnimator;
import me.schlaubi.regnumutils.common.jda.JDAExtensions;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Builder for {@link GameAnimator}.
 *
 * @see GameAnimator
 * @see cc.hawkbot.regnum.client.core.discord.GameAnimator.Game
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class GameAnimatorBuilder {


    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("GameAnimator"));
    private List<cc.hawkbot.regnum.client.core.discord.GameAnimator.Game> games = new ArrayList<>();
    private Consumer<cc.hawkbot.regnum.client.core.discord.GameAnimator.Game> applier;
    private long interval = 30;
    private long initialDelay = 0;
    private TimeUnit unit = TimeUnit.SECONDS;

    /**
     * Returns the currently selected scheduler.
     *
     * @return the {@link ScheduledExecutorService}
     */
    @NotNull
    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    /**
     * Sets the scheduler used to animate the games.
     *
     * @param scheduler the {@link ScheduledExecutorService}
     * @return the {@link GameAnimatorBuilder}
     */
    @NotNull
    public GameAnimatorBuilder setScheduler(@NotNull ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    /**
     * Returns a list containing all {@link cc.hawkbot.regnum.client.core.discord.GameAnimator.Game}s.
     *
     * @return a {@link List} of all currently registered {@link cc.hawkbot.regnum.client.core.discord.GameAnimator.Game}s
     */
    @NotNull
    public List<cc.hawkbot.regnum.client.core.discord.GameAnimator.Game> getGames() {
        return games;
    }

    /**
     * Sets the {@link cc.hawkbot.regnum.client.core.discord.GameAnimator.Game}s that will be animated.
     *
     * @param games a {@link List} of  {@link cc.hawkbot.regnum.client.core.discord.GameAnimator.Game}s
     * @return the {@link GameAnimatorBuilder}
     */
    @NotNull
    public GameAnimatorBuilder setGames(@NotNull List<cc.hawkbot.regnum.client.core.discord.GameAnimator.Game> games) {
        this.games = games;
        return this;
    }

    /**
     * Adds all specified {@link cc.hawkbot.regnum.client.core.discord.GameAnimator.Game}s.
     *
     * @param games the {@link cc.hawkbot.regnum.client.core.discord.GameAnimator.Game}s
     * @return the {@link GameAnimatorBuilder}
     */
    @NotNull
    public GameAnimatorBuilder addGames(cc.hawkbot.regnum.client.core.discord.GameAnimator.Game... games) {
        Collections.addAll(this.games, games);
        return this;
    }

    /**
     * Adds all specified {@link cc.hawkbot.regnum.client.core.discord.GameAnimator.Game}s.
     *
     * @param games a {@link Collection} of {@link cc.hawkbot.regnum.client.core.discord.GameAnimator.Game}s
     * @return the {@link GameAnimatorBuilder}
     */
    @NotNull
    public GameAnimatorBuilder addGames(Collection<cc.hawkbot.regnum.client.core.discord.GameAnimator.Game> games) {
        this.games.addAll(games);
        return this;
    }

    /**
     * Returns the currently set function with applies the games.
     *
     * @return the {@link Consumer}
     */
    @NotNull
    public Consumer<cc.hawkbot.regnum.client.core.discord.GameAnimator.Game> getApplier() {
        return applier;
    }

    /**
     * Sets the function that is use to apply the games
     *
     * @param applier a {@link Consumer} which can consume {@link cc.hawkbot.regnum.client.core.discord.GameAnimator.Game}s
     * @return the {@link GameAnimatorBuilder}
     */
    @NotNull
    public GameAnimatorBuilder setApplier(@NotNull Consumer<cc.hawkbot.regnum.client.core.discord.GameAnimator.Game> applier) {
        this.applier = applier;
        return this;
    }

    /**
     * Returns the interval in which the games are going to be changed.
     *
     * @return the interval
     */
    public long getInterval() {
        return interval;
    }

    /**
     * Sets the interval in which the games are going to be changed.
     *
     * @param interval the interval as a long
     * @return the {@link GameAnimatorBuilder}
     */
    @NotNull
    public GameAnimatorBuilder setInterval(long interval) {
        Preconditions.checkArgument(initialDelay > 0, "Initial delay must be greater than 0");
        this.interval = interval;
        return this;
    }

    /**
     * Sets the animation interval and its unit.
     *
     * @param interval the interval
     * @param unit     the {@link TimeUnit} or {@code null} if you don't want to change it
     * @return the {@link GameAnimatorBuilder}
     */
    @NotNull
    public GameAnimatorBuilder setInterval(long interval, @Nullable TimeUnit unit) {
        setInterval(interval);
        if (unit != null) {
            setTimeUnit(unit);
        }
        return this;
    }

    /**
     * Returns the amount of time till the first animation.
     *
     * @return the amount of time till the first animation
     */
    public long getInitialDelay() {
        return initialDelay;
    }

    /**
     * Sets the amount of time till the first animation
     *
     * @param initialDelay the amount of time as a long
     * @return the {@link GameAnimatorBuilder}
     */
    @NotNull
    public GameAnimatorBuilder setInitialDelay(long initialDelay) {
        Preconditions.checkArgument(initialDelay >= 0, "Initial delay must be greater or equal to 0");
        this.initialDelay = initialDelay;
        return this;
    }

    /**
     * Sets the animation interval and its unit.
     *
     * @param initialDelay the amount of time as a long
     * @param unit         the {@link TimeUnit} or {@code null} if you don't want to change it
     * @return the {@link GameAnimatorBuilder}
     */
    @NotNull
    public GameAnimatorBuilder setInitialDelay(long initialDelay, @Nullable TimeUnit unit) {
        setInitialDelay(initialDelay);
        if (unit != null) {
            setTimeUnit(unit);
        }
        return this;
    }


    /**
     * Returns the {@link TimeUnit} for the interval and the initial delay.
     *
     * @see GameAnimatorBuilder#setInterval(long)
     * @see GameAnimatorBuilder#setInitialDelay(long)
     */
    @NotNull
    public TimeUnit getTimeUnit() {
        return unit;
    }

    /**
     * Sets the {@link TimeUnit} for the interval and the initial delay.
     *
     * @param unit the {@link TimeUnit}
     * @return the {@link GameAnimatorBuilder}
     * @see GameAnimatorBuilder#setInterval(long)
     * @see GameAnimatorBuilder#setInitialDelay(long)
     */
    @NotNull
    public GameAnimatorBuilder setTimeUnit(@NotNull TimeUnit unit) {
        this.unit = unit;
        return this;
    }

    /**
     * Uses an {@link JDA} instance for the Applier.
     *
     * @param jda the {@link JDA} instance
     * @return the {@link GameAnimatorBuilder}
     * @see GameAnimatorBuilder#setApplier(Consumer)
     */
    @NotNull
    public GameAnimatorBuilder setJDA(JDA jda) {
        return setApplier((game) -> JDAExtensions.applyGame(jda, game));
    }

    /**
     * Uses an {@link ShardManager} instance for the Applier.
     *
     * @param shardManager the {@link ShardManager} instance
     * @return the {@link GameAnimatorBuilder}
     * @see GameAnimatorBuilder#setApplier(Consumer)
     */

    @NotNull
    public GameAnimatorBuilder setShardManager(ShardManager shardManager) {
        return setApplier((game) -> JDAExtensions.applyGame(shardManager, game));
    }

    /**
     * Builds the {@link GameAnimator}
     *
     * @return the built {@link GameAnimator}
     */
    @NotNull
    public GameAnimator build() {
        return new GameAnimator(
                scheduler,
                ImmutableList.copyOf(games),
                applier,
                interval,
                initialDelay,
                unit
        );
    }
}
