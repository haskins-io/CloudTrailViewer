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
package com.haskins.cloudtrailviewer.model;

/**
 *
 * @author mark
 */
public class AwsAlias {
    
    private final String number;
    private final String alias;
    
    public AwsAlias(String account_number, String account_alias) {
        this.number = account_number;
        this.alias = account_alias;
    }
    
    public String getAccountNumber() {
        return this.number;
    }
    
    public String getAccountAlias() {
        return this.alias;
    }
}
