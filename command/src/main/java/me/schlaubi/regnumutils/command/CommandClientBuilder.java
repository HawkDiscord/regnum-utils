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
import me.schlaubi.regnumutils.command.internal.CommandClientImpl;
import me.schlaubi.regnumutils.command.listener.CommandListenerBase;
import me.schlaubi.regnumutils.command.listener.MessageEditCommandListener;
import me.schlaubi.regnumutils.command.listener.MessageReceivedCommandListener;
import me.schlaubi.regnumutils.command.spi.Command;
import me.schlaubi.regnumutils.command.spi.Context;
import me.schlaubi.regnumutils.command.spi.InformationProvider;
import me.schlaubi.regnumutils.command.spi.permission.DefaultPermissionHandler;
import me.schlaubi.regnumutils.command.spi.permission.PermissionHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.sharding.ShardManager;
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

    private PermissionHandler permissionHandler = new DefaultPermissionHandler();

    private Class<? extends CommandListenerBase> commandListener = MessageReceivedCommandListener.class;

    private CommandClientConfiguration configuration = new MutableCommandClientConfiguration();

    private InformationProvider informationProvider;

    private final EventRegistry eventRegistry;

    private CommandClientBuilder(EventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    /**
     * Creates a {@link CommandClientBuilder}
     *
     * @param jda your {@link JDA} instance
     */
    public CommandClientBuilder(JDA jda) {
        this(jda::addEventListener);
    }

    /**
     * Creates a {@link CommandClientBuilder}
     *
     * @param shardManager your {@link ShardManager} instance
     */

    public CommandClientBuilder(ShardManager shardManager) {
        this(shardManager::addEventListener);
    }

    /**
     * Returns the Class of the currently set {@link CommandListenerBase}.
     *
     * @return the {@link Class}
     */
    @NotNull
    public Class<? extends CommandListenerBase> getCommandListener() {
        return commandListener;
    }

    /**
     * Sets the Class of the currently set {@link CommandListenerBase}.
     * Changing this from an {@link MessageReceivedCommandListener} to an {@link MessageEditCommandListener} will also parse {@code MESSAGE_UPDATE} events as commands
     *
     * @return the {@link CommandClientBuilder}
     * @throws IllegalArgumentException when the constructor of the command listener is not public
     * @see MessageReceivedCommandListener
     * @see MessageEditCommandListener
     */
    public CommandClientBuilder setCommandListener(@NotNull Class<? extends CommandListenerBase> commandListener) {
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
     * @return the {@link CommandClientBuilder}
     * @throws IllegalStateException if the stored {@link CommandClientConfiguration} is a {@link me.schlaubi.regnumutils.command.configuration.ImmutableCommandClientConfiguration}
     * @see CommandClientConfiguration#getSendTyping()
     */
    @NotNull
    public CommandClientBuilder sendTyping(boolean sendTyping) {
        checkMutability();
        ((MutableCommandClientConfiguration) configuration).setSendTyping(sendTyping);
        return this;
    }

    /**
     * Sets whether the bot should accept a mention as a command prefix or not.
     *
     * @param acceptMentionPrefix whether the bot should accept a mention as a command prefix or not
     * @return the {@link CommandClientBuilder}
     * @throws IllegalStateException if the stored {@link CommandClientConfiguration} is a {@link me.schlaubi.regnumutils.command.configuration.ImmutableCommandClientConfiguration}
     * @see CommandClientConfiguration#getAcceptMentionPrefix()
     */
    @NotNull
    public CommandClientBuilder acceptMentionPrefix(boolean acceptMentionPrefix) {
        checkMutability();
        ((MutableCommandClientConfiguration) configuration).setAcceptMentionPrefix(acceptMentionPrefix);
        return this;
    }

    /**
     * Sets whether the owner should be allowed to execute all commands or not.
     *
     * @param ownerPermission whether the owner should be allowed to execute all commands or not.
     * @return the {@link CommandClientBuilder}
     * @throws IllegalStateException if the stored {@link CommandClientConfiguration} is a {@link me.schlaubi.regnumutils.command.configuration.ImmutableCommandClientConfiguration}
     * @see CommandClientConfiguration#getAcceptMentionPrefix()
     */
    @NotNull
    public CommandClientBuilder enableOwnerPermissions(boolean ownerPermission) {
        checkMutability();
        ((MutableCommandClientConfiguration) configuration).setOwnerPermission(ownerPermission);
        return this;
    }

    /**
     * Sets the builder for the permissions message that will get send to a user when {@link Context}'s send methods fails.
     *
     * @param permissionErrorMessageBuilder the {@link Function}
     * @return the {@link CommandClientBuilder}
     * @throws IllegalStateException if the stored {@link CommandClientConfiguration} is a {@link me.schlaubi.regnumutils.command.configuration.ImmutableCommandClientConfiguration}
     * @see CommandClientConfiguration#buildPermissionErrorMessage(Context)
     */
    @NotNull
    public CommandClientBuilder setPermissionErrorMessageBuilder(Function<@NotNull Context, @NotNull Message> permissionErrorMessageBuilder) {
        checkMutability();
        ((MutableCommandClientConfiguration) configuration).setPermissionErrorMessageBuilder(permissionErrorMessageBuilder);
        return this;
    }

    /**
     * Sets the list of bot owners.
     *
     * @param owners a {@link List} containing all bot owner ids
     * @return the {@link CommandClientBuilder}
     * @throws IllegalStateException if the stored {@link CommandClientConfiguration} is a {@link me.schlaubi.regnumutils.command.configuration.ImmutableCommandClientConfiguration}
     * @see CommandClientConfiguration#getOwners()
     */
    @NotNull
    public CommandClientBuilder setOwners(List<Long> owners) {
        checkMutability();
        ((MutableCommandClientConfiguration) configuration).setOwners(owners);
        return this;
    }

    /**
     * Adds bot owners.
     *
     * @param botOwnerIds the ids of the bot owners
     * @return the {@link CommandClientBuilder}
     * @throws IllegalStateException if the stored {@link CommandClientConfiguration} is a {@link me.schlaubi.regnumutils.command.configuration.ImmutableCommandClientConfiguration}
     * @see CommandClientConfiguration#getOwners()
     * @see me.schlaubi.regnumutils.command.spi.permission.DefaultPermissionHandler
     */
    @NotNull
    public CommandClientBuilder addBotOwners(Long... botOwnerIds) {
        checkMutability();
        Collections.addAll(configuration.getOwners(), botOwnerIds);
        return this;
    }

    /**
     * Adds bot owners.
     *
     * @param botOwnerIds a {@link Collection} of the ids of the bot owners
     * @return the {@link CommandClientBuilder}
     * @throws IllegalStateException if the stored {@link CommandClientConfiguration} is a {@link me.schlaubi.regnumutils.command.configuration.ImmutableCommandClientConfiguration}
     * @see CommandClientConfiguration#getOwners()
     * @see me.schlaubi.regnumutils.command.spi.permission.DefaultPermissionHandler
     */
    @NotNull
    public CommandClientBuilder addBotOwners(Collection<Long> botOwnerIds) {
        checkMutability();
        configuration.getOwners().addAll(botOwnerIds);
        return this;
    }

    /**
     * Sets the default prefix.
     *
     * @param prefix the prefix
     * @return the {@link CommandClientBuilder}
     * @throws IllegalStateException if the stored {@link CommandClientConfiguration} is a {@link me.schlaubi.regnumutils.command.configuration.ImmutableCommandClientConfiguration}
     * @see CommandClientConfiguration#getDefaultPrefix()
     */
    @NotNull
    public CommandClientBuilder setPrefix(String prefix) {
        checkMutability();
        ((MutableCommandClientConfiguration) configuration).setDefaultPrefix(prefix);
        return this;
    }

    /**
     * Lets the bot send typing before executing a command.
     *
     * @param sendTyping Whether this should get enabled or not
     * @return the {@link CommandClientBuilder}
     * @throws IllegalStateException if the stored {@link CommandClientConfiguration} is a {@link me.schlaubi.regnumutils.command.configuration.ImmutableCommandClientConfiguration}
     * @see MessageChannel#sendTyping()
     */
    @NotNull
    public CommandClientBuilder enableTyping(boolean sendTyping) {
        checkMutability();
        ((MutableCommandClientConfiguration) configuration).setSendTyping(sendTyping);
        return this;
    }

    /**
     * Let the bot always accept the default prefix even if there is a custom one.
     *
     * @param alwaysDefaultPrefix whether this should get enabled or not
     * @return the {@link CommandClientBuilder}
     * @throws IllegalStateException if the stored {@link CommandClientConfiguration} is a {@link me.schlaubi.regnumutils.command.configuration.ImmutableCommandClientConfiguration}
     * @see CommandClientConfiguration#getAlwaysDefaultPrefix()
     */
    @NotNull
    public CommandClientBuilder enableDefaultPrefixOverride(boolean alwaysDefaultPrefix) {
        checkMutability();
        ((MutableCommandClientConfiguration) configuration).alwaysAcceptDefaultPrefix(alwaysDefaultPrefix);
        return this;
    }

    /**
     * Returns the information provider.
     *
     * @return the {@link InformationProvider}
     * @see InformationProvider for more information
     */
    public InformationProvider getInformationProvider() {
        return informationProvider;
    }

    /**
     * Sets the information provider.
     *
     * @param informationProvider the {@link InformationProvider}
     * @return the {@link CommandClientBuilder}
     * @see InformationProvider for more information
     */
    @NotNull
    public CommandClientBuilder setInformationProvider(InformationProvider informationProvider) {
        this.informationProvider = informationProvider;
        return this;
    }

    private void checkMutability() {
        Preconditions.checkState(configuration instanceof MutableCommandClientConfiguration,
                "CommandClientConfiguration must be mutable for helper methods!");
    }

    private CommandClient activate(CommandClient commandClient) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        var listener = commandListener.getConstructor(CommandClient.class).newInstance(commandClient);
        eventRegistry.addEventListeners(listener);
        return commandClient;
    }

    /**
     * Represents an registry for listeners.
     * Used to register the {@link CommandListenerBase}.
     */
    private interface EventRegistry {
        /**
         * Registers event listeners
         *
         * @param listeners an array of event listeners
         */
        void addEventListeners(Object... listeners);
    }

    /**
     * Builds the CommandClient.
     *
     * @return the build {@link CommandClient}
     */
    @NotNull
    public CommandClient build() {
        Preconditions.checkNotNull(informationProvider, "Information provider cannot be null!");
        try {
            return activate(new CommandClientImpl(
                    executor,
                    configuration instanceof MutableCommandClientConfiguration ?
                            ((MutableCommandClientConfiguration) configuration).toImmutableCommandClientConfiguration() : configuration,
                    commands,
                    permissionHandler,
                    informationProvider
            ));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException("Could not build listener!", e);
        }
    }
}
