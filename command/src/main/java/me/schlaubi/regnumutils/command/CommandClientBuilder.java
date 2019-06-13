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

package me.schlaubi.regnumutils.command;

import cc.hawkbot.regnum.util.DefaultThreadFactory;
import com.google.common.base.Preconditions;
import me.schlaubi.regnumutils.command.configuration.CommandClientConfiguration;
import me.schlaubi.regnumutils.command.configuration.MutableCommandClientConfiguration;
import me.schlaubi.regnumutils.command.listener.MessageEditCommandListener;
import me.schlaubi.regnumutils.command.listener.MessageReceivedCommandListener;
import me.schlaubi.regnumutils.command.listener.SealedCommandListener;
import me.schlaubi.regnumutils.command.spi.Command;
import me.schlaubi.regnumutils.command.spi.Context;
import me.schlaubi.regnumutils.command.spi.permission.PermissionHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@SuppressWarnings("unused")
public class CommandClientBuilder {

    private ExecutorService executor = Executors.newCachedThreadPool(new DefaultThreadFactory("Command"));

    private List<Command> commands = new ArrayList<>();

    private PermissionHandler permissionHandler;

    private Class<? extends SealedCommandListener> commandListener = MessageReceivedCommandListener.class;

    private CommandClientConfiguration configuration = new MutableCommandClientConfiguration();

    /**
     * Returns the Class of the currently set {@link SealedCommandListener}.
     *
     * @return the {@link Class}
     */
    @NotNull
    public Class<? extends SealedCommandListener> getCommandListener() {
        return commandListener;
    }

    /**
     * Sets the Class of the currently set {@link SealedCommandListener}.
     * Changing this from an {@link MessageReceivedCommandListener} to an {@link MessageEditCommandListener} will also parse {@code MESSAGE_UPDATE} events as commands
     *
     * @return the {@link CommandClientBuilder}
     * @throws IllegalArgumentException when the constructor of the command listener is not public
     * @see MessageReceivedCommandListener
     * @see MessageEditCommandListener
     */
    public CommandClientBuilder setCommandListener(@NotNull Class<? extends SealedCommandListener> commandListener) {
        try {
            Preconditions.checkArgument(!Modifier.isPublic(commandListener.getConstructor(CommandClient.class).getModifiers()), "The constructor of the command listener must be public!");
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("A command listener needs a constructor with a command client argument!");
        }
        this.commandListener = commandListener;
        return this;
    }

    /**
     * Returns the executor used to execute {@link Command }s.
     *
     * @return the {@link ExecutorService}
     */
    @NotNull
    public ExecutorService getExecutor() {
        return executor;
    }

    /**
     * Sets the executor used to execute {@link Command }s.
     *
     * @param executor the {@link ExecutorService}
     * @return the {@link CommandClientBuilder}
     */
    @NotNull
    public CommandClientBuilder setExecutor(@NotNull ExecutorService executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Returns all currently registered commands.
     *
     * @return a {@link List} of {@link Command}s
     */
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Sets all currently registered {@link Command}s.
     *
     * @param commands a {@link List} of {@link Command}s
     * @return the {@link CommandClientBuilder}
     * @see CommandClientBuilder#registerCommands(Command...)
     * @see CommandClientBuilder#registerCommands(Collection)
     */
    @NotNull
    public CommandClientBuilder setCommands(@NotNull List<Command> commands) {
        this.commands = commands;
        return this;
    }

    /**
     * Registers commands without overwriting already registered command.
     *
     * @param commands the {@link Command}s
     * @return the {@link CommandClientBuilder}
     */
    @SuppressWarnings("WeakerAccess")
    @NotNull
    public CommandClientBuilder registerCommands(Command... commands) {
        Collections.addAll(this.commands, commands);
        return this;
    }

    /**
     * Registers commands without overwriting already registered command.
     *
     * @param commands a {@link Collection} of {@link Command}s
     * @return the {@link CommandClientBuilder}
     */
    @SuppressWarnings("WeakerAccess")
    @NotNull
    public CommandClientBuilder registerCommands(Collection<Command> commands) {
        this.commands.addAll(commands);
        return this;
    }

    /**
     * Returns the currently set permission handler.
     *
     * @return the {@link PermissionHandler}
     */
    @NotNull
    public PermissionHandler getPermissionHandler() {
        return permissionHandler;
    }

    /**
     * Sets the permission handler to check if a user is allowed to execute a command.
     *
     * @param permissionHandler the {@link PermissionHandler}
     * @return the {@link CommandClientBuilder}
     * @see PermissionHandler
     */
    @NotNull
    public CommandClientBuilder setPermissionHandler(@NotNull PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
        return this;
    }

    /**
     * Returns the currently used CommandClientConfiguration.
     *
     * @return the {@link CommandClientConfiguration}.
     */
    @NotNull
    public CommandClientConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Sets the currently used CommandClientConfiguration.
     *
     * @param configuration the {@link CommandClientConfiguration}
     * @return the {@link CommandClientBuilder}
     */
    @NotNull
    public CommandClientBuilder setConfiguration(@NotNull CommandClientConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    /**
     * Sets whether the bot should send typing before it parses commands or not.
     *
     * @param sendTyping whether the bot should send typing before it parses commands or not.
     *  @see CommandClientConfiguration#getSendTyping()
     * @return the {@link CommandClientBuilder}
     * @throws IllegalStateException if the stored {@link CommandClientConfiguration} is a {@link me.schlaubi.regnumutils.command.configuration.ImmutableCommandClientConfiguration}
     */
    @NotNull
    public CommandClientBuilder sendTyping(boolean sendTyping) {
        checkMutablility();
        ((MutableCommandClientConfiguration) configuration).setSendTyping(sendTyping);
        return this;
    }

    /**
     * Sets whether the bot should accept a mention as a command prefix or not.
     *
     * @param acceptMentionPrefix whether the bot should accept a mention as a command prefix or not
     * @see CommandClientConfiguration#getAcceptMentionPrefix()
     * @return the {@link CommandClientBuilder}
     * @throws IllegalStateException if the stored {@link CommandClientConfiguration} is a {@link me.schlaubi.regnumutils.command.configuration.ImmutableCommandClientConfiguration}
     */
    @NotNull
    public CommandClientBuilder acceptMentionPrefix(boolean acceptMentionPrefix) {
        checkMutablility();
        ((MutableCommandClientConfiguration) configuration).setAcceptMentionPrefix(acceptMentionPrefix);
        return this;
    }

    /**
     * Sets the builder for the permissions message that will get send to a user when {@link Context}'s send methods fails.
     *
     * @param permissionErrorMessageBuilder the {@link Function}
     * @see CommandClientConfiguration#buildPermissionErrorMessage(Context)
     * @return the {@link CommandClientBuilder}
     * @throws IllegalStateException if the stored {@link CommandClientConfiguration} is a {@link me.schlaubi.regnumutils.command.configuration.ImmutableCommandClientConfiguration}
     */
    @NotNull
    public CommandClientBuilder setPermissionErrorMessageBuilder(Function<@NotNull Context, @NotNull Message> permissionErrorMessageBuilder) {
        checkMutablility();
        ((MutableCommandClientConfiguration) configuration).setPermissionErrorMessageBuilder(permissionErrorMessageBuilder);
        return this;
    }


    private void checkMutablility() {
        Preconditions.checkState(!(configuration instanceof MutableCommandClientConfiguration),
                "CommandClientConfiguration must be mutable for helper methods!");
    }

    private void activate(CommandClient commandClient, JDA jda) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        var listener = commandListener.getConstructor(CommandClient.class).newInstance(commandClient);
        jda.addEventListener(listener);
    }

    /**
     * Builds the CommandClient.
     *
     * @return the build {@link CommandClient}
     */
    @NotNull
    public CommandClient build() {
        throw new UnsupportedOperationException();
    }
}
