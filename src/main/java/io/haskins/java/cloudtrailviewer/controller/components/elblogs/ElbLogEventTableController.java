package io.haskins.java.cloudtrailviewer.controller.components.elblogs;

import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.elblog.ElbLog;
import io.haskins.java.cloudtrailviewer.service.ElbLogService;
import io.haskins.java.cloudtrailviewer.service.EventService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import io.haskins.java.cloudtrailviewer.service.listener.EventTableServiceListener;
import io.haskins.java.cloudtrailviewer.utils.LuceneUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ElbLogEventTableController implements EventTableServiceListener, DataServiceListener {

    @FXML
    private TableView tableView;

    private ObservableList<AwsData> filteredEvents = FXCollections.observableArrayList();

    private EventService eventService;

    @Autowired
    public ElbLogEventTableController(EventService eventService, EventTableService eventTableService) {

        this.eventService = eventService;

        eventTableService.addListener(this, "elblogs");
        eventService.registerAsListener(this);
    }

    @FXML
    public void initialize() {

        tableView.setItems(filteredEvents);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        FilteredList<AwsData> filteredData = new FilteredList<>(filteredEvents, p -> true);

        SortedList<AwsData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }

    /**
     * Updates the table with the provided events.
     * @param results a List of Events
     */
    public void setEvents(TopDocs results) {

        try {
            IndexSearcher searcher = LuceneUtils.createSearcher(ElbLogService.LUCENE_DIR);

            List<ElbLog> logs = new ArrayList<>();

            for (ScoreDoc sd : results.scoreDocs) {

                Document d = searcher.doc(sd.doc);
                logs.add(new ElbLog().withDocument(d));
            }

            filteredEvents.clear();
            filteredEvents.addAll(logs);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void newEvent(AwsData event) {

    }

    @Override
    public void newEvents(List<? extends AwsData> events) {

    }

    @Override
    public void loadingFile(int fileNum, int totalFiles) {

    }

    @Override
    public void finishedLoading(boolean reload) {

    }

    @Override
    public void clearEvents() {

    }

}
