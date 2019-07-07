import me.schlaubi.regnumutils.command.CommandClient
import me.schlaubi.regnumutils.command.CommandClientBuilder
import me.schlaubi.regnumutils.command.spi.*
import me.schlaubi.regnumutils.command.spi.permission.Permissions
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.internal.requests.EmptyRestAction
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import java.util.concurrent.CompletableFuture

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

/**
 * Tests the [CommandClient] with a demo command.
 */
@RunWith(MockitoJUnitRunner::class)
class CommandTest {

    private lateinit var client: CommandClient
    private val waiter = CompletableFuture<Void>()
    private lateinit var jda: JDA
    private lateinit var channel: TextChannel
    private lateinit var message: Message

    @Suppress("unused")
    @Rule
    @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    /**
     * Prepares the commandClient and mocks the objects
     */
    @Before
    fun before() {
        jda = Mockito.mock(JDA::class.java)
       val author = Mockito.mock(User::class.java)
        val selfMember = Mockito.mock(Member::class.java)
        Mockito.`when`(selfMember.asMention).thenReturn("<@378321678416239>")
        val guild = Mockito.mock(Guild::class.java)
        message = Mockito.mock(Message::class.java)
        Mockito.`when`(message.author).thenReturn(author)
        Mockito.`when`(message.guild).thenReturn(guild)
        Mockito.`when`(message.contentRaw).thenReturn("!test Hi I am cool")
        Mockito.`when`(guild.selfMember).thenReturn(selfMember)
        channel = Mockito.mock(TextChannel::class.java)
        Mockito.`when`(channel.sendTyping()).thenReturn(EmptyRestAction<Void>(jda))
        Mockito.`when`(message.channel).thenReturn(channel)
        client = CommandClientBuilder(jda)
            .registerCommands(TestCommand())
            .setPrefix("!")
            .setInformationProvider(object : InformationProvider {
                override fun isOwner(member: Member) = false

                override fun getPrefix(guild: Guild): String? = null

            })
            .build()
    }

    /**
     * Runs the command and waits for it.
     */
    @Test
    fun test() {
        val event = CommandClient.CommandEvent(
            jda,
            200,
            1,
            channel,
            message
        )
        client.dispatchCommand(event)
        waiter.join()
    }

    private inner class TestCommand : AbstractCommand(
        "Test",
        Permissions.public(),
        "test",
        "Cool command"
    ) {
        val subCommand = TestSubCommand()

        init {
            registerSubCommand(subCommand)
        }

        override fun process(args: Arguments, context: Context) {
            println(context.message.contentRaw)
            println(args)
            waiter.complete(null)
        }

        private inner class TestSubCommand : SubCommand(
            "Hi",
            Permissions.public(),
            "hi",
            "Cool sub command"
        ) {

            init {
                registerSubCommand(TestSubCommandSubCommand())
            }

            override fun process(args: Arguments, context: Context) {
                println(context.message.contentRaw)
                println(args)
                waiter.complete(null)
            }

            private inner class TestSubCommandSubCommand : SubCommand(
                "I",
                Permissions.public(),
                "i",
                "sub sub command"
            ) {

                override fun process(args: Arguments, context: Context) {
                    println(context.message.contentRaw)
                    println(args)
                    waiter.complete(null)
                }
            }
        }
    }

}