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

package io.haskins.java.cloudtrailviewer.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that handles AWS functionality
 *
 * Created by markhaskins on 05/01/2017.
 */
@Service
class AwsService {

    private final AccountDao accountDao;

    @Autowired
    public AwsService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public AwsAccount getActiveAccount() {

        List<AwsAccount> accounts = accountDao.getAllAccounts(true);
        if (accounts.isEmpty()) {
            return null;
        }

        return accounts.get(0);
    }

    public AmazonS3 getS3Client(AwsAccount activeAccount) {

        String key = activeAccount.getKey();
        String secret = activeAccount.getSecret();

        AWSCredentials credentials= new BasicAWSCredentials(key, secret);

        return new AmazonS3Client(credentials);
    }

    public String getS3BucketForAccount(AwsAccount activeAccount) {
        return activeAccount.getBucket();
    }
}
