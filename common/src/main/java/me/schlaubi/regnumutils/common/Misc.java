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

package me.schlaubi.regnumutils.common;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class Misc {

    @Nullable
    public static <T> T first(Collection<T> collection) {
        if (collection.isEmpty()) {
            return null;
        }
        if (collection instanceof List) {
            return ((List<T>) collection).get(0);
        } else {
            return collection.iterator().next();
        }
    }

    public static boolean isNumeric(String input) {
        return input
                .chars()
                .mapToObj(it -> (char) it)
                .allMatch(Character::isDigit);

    }
}
