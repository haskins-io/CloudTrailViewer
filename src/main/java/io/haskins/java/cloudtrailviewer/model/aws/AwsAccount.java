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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

/**
 * Model class that holds information about an AWS account
 *
 * Created by markhaskins on 05/01/2017.
 */
public class AwsAccount implements Serializable {

    private static final long serialVersionUID = 1298354923304275550L;

    private final IntegerProperty id;
    private final StringProperty name;
    private StringProperty acctNumber;
    private StringProperty acctAlias;
    private final StringProperty bucket;
    private final StringProperty key;
    private final StringProperty secret;
    private StringProperty prefix;
    private StringProperty profile;

    public AwsAccount() {

        this(0, "","","","","","","");
    }

    public AwsAccount(int id, String name, String acctNum, String acctAlias, String bucket, String key, String secret, String prefix) {
        this(id, name, acctNum, acctAlias, bucket, key, secret, prefix,null);
    }

    public AwsAccount(int id, String name, String acctNum, String acctAlias, String bucket, String key, String secret, String prefix, String profile) {

        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.acctNumber = new SimpleStringProperty(acctNum);
        this.acctAlias = new SimpleStringProperty(acctAlias);
        this.bucket = new SimpleStringProperty(bucket);
        this.key = new SimpleStringProperty(key);
        this.secret = new SimpleStringProperty(secret);
        this.prefix = new SimpleStringProperty(prefix);
        this.profile = new SimpleStringProperty(profile);
    }

    public void setId(int id) {this.id.set(id);}
    public int getId() {
        return this.id.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public String getName() {
        return name.get();
    }

    public void setAcctNumber(String acctNumber) {
        this.acctNumber.set(acctNumber);
    }
    public String getAcctNumber() {
        return acctNumber.get();
    }

    public void setAcctAlias(String acctAlias)
    {
        this.acctAlias.set(acctAlias);
    }
    public String getAcctAlias() {
        return this.acctAlias.get();
    }

    public void setBucket(String bucket) {
        this.bucket.set(bucket);
    }
    public String getBucket() {
        return bucket.get();
    }

    public void setKey(String key) {
        this.key.set(key);
    }
    public String getKey() {
        return key.get();
    }

    public void setSecret(String secret) {
        this.secret.set(secret);
    }
    public String getSecret() {
        return secret.get();
    }

    public void setPrefix(String prefix) {
        this.prefix.set(prefix);
    }
    public String getPrefix() {
        return prefix.get();
    }

    public void setProfile(String profile) {
        this.profile.set(profile);
    }
    public String getProfile() {
        return this.profile.get();
    }
}
