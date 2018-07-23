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

import com.amazonaws.services.s3.AmazonS3;
import io.haskins.java.cloudtrailviewer.controller.components.StatusBarController;
import io.haskins.java.cloudtrailviewer.filter.CompositeFilter;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import io.haskins.java.cloudtrailviewer.service.utils.FileProcessingThread;
import io.haskins.java.cloudtrailviewer.utils.AwsService;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Service responsible for handling CloudTrail events.
 * <p>
 * Created by markhaskins on 04/01/2017.
 */
@Service
public class EventService extends DataService {

    public static final int FILE_TYPE_LOCAL = 1;
    public static final int FILE_TYPE_S3 = 2;

    private final static int BUFFER_SIZE = 32;

    private final static Logger LOGGER = Logger.getLogger("EventService");

    private final GeoService geoService;
    private final AccountService accountDao;
    private final StatusBarController statusBarController;
    private final AwsService awsService;

    private final List<AwsData> eventDb = new ArrayList<>();

    @Autowired
    public EventService(AccountService accountDao, GeoService geoService,
            StatusBarController statusBarController, AwsService awsService) {

        this.accountDao = accountDao;
        this.geoService = geoService;
        this.awsService = awsService;

        this.statusBarController = statusBarController;
        this.listeners.add(statusBarController);
    }

    public void loadFiles(List<String> filenames, final CompositeFilter filters, int file_type) {

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                AwsAccount activeAccount = null;
                AmazonS3 s3Client = null;

                if (file_type == FILE_TYPE_S3) {
                    activeAccount = awsService.getActiveAccount(accountDao);
                    s3Client = awsService.getS3Client(activeAccount);
                }

                int count = 0;

                for (String filename : filenames) {

                    count++;

                    String message = "Processing file " + count + " of " + filenames.size();
                    updateMessage(message);

                    FileProcessingThread worker = new FileProcessingThread(
                            filename, file_type, filters,
                            s3Client, activeAccount, geoService,
                            eventDb, listeners
                    );

                    executor.execute(worker);
                }

                executor.shutdown();

                while (!executor.isTerminated()) {
                }

                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();

                updateMessage("");
                for (DataServiceListener l : listeners) {
                    l.finishedLoading(false);
                }
            }

        };

        statusBarController.message.textProperty().bind(task.messageProperty());

        new Thread(task).start();
    }


    public void clearEvents() {

        eventDb.clear();

        for (DataServiceListener l : listeners) {
            l.clearEvents();
        }
    }

    public List<AwsData> getAllEvents() {
        return this.eventDb;
    }

    List getDataDb() {
        return getAllEvents();
    }

}
