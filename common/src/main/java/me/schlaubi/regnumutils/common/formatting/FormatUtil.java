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

package me.schlaubi.regnumutils.common.formatting;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

/**
 * Some useful formatting helpers.
 */
public class FormatUtil {

    /**
     * Converts an {@link MessageEmbed} into plain text.
     *
     * @param embed The embed
     * @return The embed as plain text
     */
    @NotNull
    public static String stringifyEmbed(@NotNull MessageEmbed embed) {
        var builder = new StringBuilder();
        if (embed.getTitle() != null) {
            builder.append("**").append(embed.getTitle()).append("**").append("\n");
        }
        if (embed.getDescription() != null) {
            builder.append(embed.getDescription()).append("\n");
        }
        embed.getFields().forEach(it -> builder.append("**").append(it.getName()).append("**").append("\n")
                .append(it.getValue()).append("\n"));
        if (embed.getFooter() != null) {
            builder.append('_').append(embed.getFooter()).append('_');
        }
        var string = builder.toString();
        if (string.length() > 1024) {
            return "This message is to long to be sent as plain text please give me MESSAGE_EMBED_LINKS"
                    + " permission";
        }
        return string;
    }
}
