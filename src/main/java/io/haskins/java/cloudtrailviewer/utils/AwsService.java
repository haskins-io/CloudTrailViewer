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

package io.haskins.java.cloudtrailviewer.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.service.AccountService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class that handles AWS functionality
 *
 * Created by markhaskins on 05/01/2017.
 */
@Service
public class AwsService {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");

    private final Map<String, String> serviceNamesToEndpoints = new HashMap<>();
    private final Map<String, String> serviceEndpointsToNames = new HashMap<>();

    private final Map<String, List<String>> serviceAPIs = new HashMap<>();

    public AwsService() {

        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStreamReader io = new InputStreamReader(classLoader.getResourceAsStream("service_apis/service_names.txt"));

        try( BufferedReader br = new BufferedReader(io) ) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] parts = line.split(":");

                serviceNamesToEndpoints.put(parts[1].trim(), parts[0].trim());
                serviceEndpointsToNames.put(parts[0].trim(), parts[1].trim());
            }

        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING, "Unable to load service APIs", ioe);
        }
    }

    public AwsAccount getActiveAccount(AccountService accountDao) {

        List<AwsAccount> accounts = accountDao.getAllAccounts(true);
        if (accounts.isEmpty()) {
            return null;
        }

        return accounts.get(0);
    }

    public AmazonS3 getS3Client(AwsAccount activeAccount) {

        AmazonS3 client = getS3ClientUsingProfile(activeAccount);
        if (client != null) {
            return client;
        }

        client = getS3ClientUsingKeys(activeAccount);
        if (client != null) {
            return client;
        }

        return null;
    }

    /**
     * Returns the friendly name of a service for example autoscaling.amazonaws.com
     * would return AutoScaling
     * @param name service name
     * @return String value of Friendly name
     */
    public String getFriendlyName(String name) {

        String friendlyName = serviceEndpointsToNames.get(name);
        if (friendlyName == null) {
            friendlyName = name;
        }

        return friendlyName;
    }

    public String getEndpointFromFriendlyName(String friendlyName) {

        String endpoint = serviceNamesToEndpoints.get(friendlyName);
        if (endpoint == null) {
            endpoint = friendlyName;
        }

        return endpoint;
    }

    /**
     * Returns the names of all available AWS Services
     * @return Collection of AWS Services
     */
    public List<String> getServices() {

        Set<String> keys = serviceNamesToEndpoints.keySet();
        List<String> list = new ArrayList<>(keys);
        Collections.sort(list);

        return list;
    }

    private AmazonS3 getS3ClientUsingKeys(AwsAccount activeAccount) {

        String key = activeAccount.getKey();
        String secret = activeAccount.getSecret();

        if ( (key != null && key.trim().length() > 10) &&
             (secret != null && secret.trim().length() > 10) )
        {
            AWSCredentials credentials = new BasicAWSCredentials(key, secret);
            return new AmazonS3Client(credentials);
        }

        return null;
    }

    private AmazonS3 getS3ClientUsingProfile(AwsAccount currentAccount) {

        String profile = currentAccount.getProfile();

        if (profile != null && profile.trim().length() > 1) {
            AWSCredentials credentials = new ProfileCredentialsProvider(currentAccount.getProfile()).getCredentials();
            return new AmazonS3Client(credentials);
        }

        return null;
    }

    public String getS3BucketForAccount(AwsAccount activeAccount) {
        return activeAccount.getBucket();
    }
}
