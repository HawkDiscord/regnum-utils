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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Some tools.
 */
public class Misc {

    /**
     * Returns the first element of a {@link Collection}.
     * @param collection the collection
     * @param <T> the type of the elements
     * @return the element or {@code null} if the collection is empty
     */
    @Nullable
    public static <T> T first(@NotNull Collection<T> collection) {
        if (collection.isEmpty()) {
            return null;
        }
        if (collection instanceof List) {
            return ((List<T>) collection).get(0);
        } else {
            return collection.iterator().next();
        }
    }

    /**
     * Checks whether a String is numeric or not.
     * @param input the string to check
     * @return whether a String is numeric or not
     */
    public static boolean isNumeric(@NotNull String input) {
        return input
                .chars()
                .mapToObj(it -> (char) it)
                .allMatch(Character::isDigit);

    }
}
