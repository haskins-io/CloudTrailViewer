package io.haskins.java.cloudtrailviewer.controller.components.vpclogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.vpclog.VpcFlowLog;
import io.haskins.java.cloudtrailviewer.service.EventService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.service.VpcFlowLogService;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import io.haskins.java.cloudtrailviewer.service.listener.EventTableServiceListener;
import io.haskins.java.cloudtrailviewer.utils.LuceneUtils;
import javafx.beans.binding.Bindings;
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
public class VpcLogEventTableController implements EventTableServiceListener, DataServiceListener {

    @FXML private TableView tableView;

    @FXML private Label searchLabel;
    @FXML private TextField searchField;
    @FXML private Label resultCount;

    private ObservableList<AwsData> filteredEvents = FXCollections.observableArrayList();

    private EventService eventService;

    @Autowired
    public VpcLogEventTableController(EventService eventService, EventTableService eventTableService) {

        this.eventService = eventService;

        eventTableService.addListener(this, "vpclogs");
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

        searchLabel.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SEARCH));

        resultCount.textProperty().bind(Bindings.size(filteredData).asString());
    }

    /**
     * Updates the table with the provided events.
     * @param events a List of Events
     */
    public void setEvents(TopDocs results) {

        try {
            IndexSearcher searcher = LuceneUtils.createSearcher(VpcFlowLogService.LUCENE_DIR);

            List<VpcFlowLog> logs = new ArrayList<>();

            for (ScoreDoc sd : results.scoreDocs) {

                Document d = searcher.doc(sd.doc);
                logs.add(new VpcFlowLog().withDocument(d));
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
