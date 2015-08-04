/*
 * Copyright (C) 2015 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.cloudtrailviewer.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author mark
 */
public class ResultSetRow extends LinkedHashMap
{

    public ResultSetRow()
    {
        super();
    }


    public ResultSetRow(int initialCapacity)
    {
        super(initialCapacity);
    }


    public ResultSetRow(int initialCapacity, float loadFactor)
    {
        super(initialCapacity, loadFactor);
    }


    public ResultSetRow(Map m)
    {
        super(m);
    }

    /**
     * <p>Attempts to retrieve the value corresponding to <code>String</code> key passed in, but in the event of that 
     * key mapping to NULL, it will try retrieving any corresponding value for an uppercase version of the key.
     * </p>
     * <p>This method should be used when retrieving values from result sets, and allows the user of the API to treat 
     * Oracle systems like normal and useful RDBMSs.</p>
     *  
     * @param key name of the column for which we want data
     * @return value of that column for this row
     */
    public Object get(String key)
    {
        // If key doesn't exist, try it in uppercase
        if (super.get(key) == null)
        {
            return super.get(key.toUpperCase());
        }

        return super.get(key);
    }
}