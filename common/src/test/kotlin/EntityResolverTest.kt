import cc.hawkbot.regnum.util.logging.Logger
import me.schlaubi.regnumutils.common.parsing.EntityResolver
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule

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
 * Tests if the [EntityResolver] calls the methods of the specified [Guild] and [JDA] objects correctly.
 */
@RunWith(MockitoJUnitRunner::class)
class EntityResolverTest {

    private val log = Logger.getLogger()

    private lateinit var jda: JDA
    private lateinit var guild: Guild

    @Suppress("unused")
    val rule: MockitoRule = MockitoJUnit.rule()
        @Rule get() = field

    /**
     * Initializes the mocks.
     */
    @Before
    fun before() {
        jda = Mockito.mock(JDA::class.java)
        guild = Mockito.mock(Guild::class.java)
    }

    /**
     * Runs the test.
     */
    @Test
    fun test() {
        user()
        member()
        channel()
        role()
    }

    private fun user() {
        log.info("Parsing user")
        val userId = 416902379598774273
        EntityResolver.resolveUser("<@$userId>", jda)
        EntityResolver.resolveUser("$userId", jda)
        verifyId(jda).getUserById(Mockito.eq("$userId"))
        val userName = "Schlaubi"
        EntityResolver.resolveUser(userName, jda)
        verifyName(jda).getUsersByName(Mockito.eq(userName), Mockito.anyBoolean())
        log.info("User test: Passed!")
    }

    private fun member() {
        log.info("Parsing member")
        val memberId = 416902379598774273
        EntityResolver.resolveMember("<@$memberId>", guild)
        EntityResolver.resolveMember("$memberId", guild)
        verifyId(guild).getMemberById(Mockito.eq("$memberId"))
        val memberName = "Schlaubi"
        EntityResolver.resolveMember(memberName, guild)
        verifyName(guild).getMembersByName(Mockito.eq(memberName), Mockito.anyBoolean())
        log.info("Member test: Passed!")
    }

    private fun channel() {
        log.info("Parsing channel")
        val channelId = 416902379598774273
        EntityResolver.resolveTextChannel("<#$channelId>", guild)
        EntityResolver.resolveTextChannel("$channelId", guild)
        verifyId(guild).getTextChannelById(Mockito.eq("$channelId"))
        val channelName = "support"
        EntityResolver.resolveTextChannel(channelName, guild)
        verifyName(guild).getTextChannelsByName(Mockito.eq(channelName), Mockito.anyBoolean())
        log.info("Channel test: Passed!")
    }

    private fun role() {
        log.info("Parsing role")
        val roleId = 416902379598774273
        EntityResolver.resolveRole("<@&$roleId>", guild)
        EntityResolver.resolveRole("$roleId", guild)
        verifyId(guild).getRoleById(Mockito.eq("$roleId"))
        val roleName = "Admin"
        EntityResolver.resolveRole(roleName, guild)
        verifyName(guild).getRolesByName(Mockito.eq("Admin"), Mockito.anyBoolean())
        log.info("Role test: passed")
    }


    private fun <T> verifyId(mock: T): T {
        return Mockito.verify(mock, Mockito.times(2))
    }

    private fun <T> verifyName(mock: T): T {
        return Mockito.verify(mock)
    }

}