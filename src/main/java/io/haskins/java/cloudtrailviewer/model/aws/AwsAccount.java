/*
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2017  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package io.haskins.java.cloudtrailviewer.model.aws;

import java.io.Serializable;

/**
 * Model class that holds information about an AWS account
 *
 * Created by markhaskins on 05/01/2017.
 */
public class AwsAccount implements Serializable {

    private static final long serialVersionUID = 1298354923304275550L;

    private final int id;
    private final String name;
    private String acctNumber;
    private final String bucket;
    private final String key;
    private final String secret;
    private String prefix;

    public AwsAccount(int id, String name, String acctNum, String bucket, String key, String secret, String prefix) {

        this.id = id;
        this.name = name;
        this.acctNumber = acctNum;
        this.bucket = bucket;
        this.key = key;
        this.secret = secret;
        this.prefix = prefix;
    }

    public int getId() {
        return this.id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the acctNumber
     */
    public String getAcctNumber() {
        return acctNumber;
    }

    /**
     * @param acctNumber the acctNumber to set
     */
    public void setAcctNumber(String acctNumber) {
        this.acctNumber = acctNumber;
    }

    /**
     * @return the bucket
     */
    public String getBucket() {
        return bucket;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}