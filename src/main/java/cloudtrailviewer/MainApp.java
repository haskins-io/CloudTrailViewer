/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2014  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package cloudtrailviewer;

import cloudtrailviewer.events.EventLoader;
import cloudtrailviewer.components.FilterPane;
import cloudtrailviewer.components.MainPane;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import cloudtrailviewer.components.MenuPane;
import cloudtrailviewer.events.EventsDatabase;
import cloudtrailviewer.filters.Filters;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;


public class MainApp extends Application {

    private EventLoader eventLoader;
    
    private EventsDatabase eventsDatabase;
    
    @Override
    public void start(Stage stage) throws Exception {
                
        Scene scene = new Scene(new Group());
        stage.setTitle("CloudTrail Log Viewer");
        stage.setWidth(1280);
        stage.setHeight(1024);
        
        eventLoader = new EventLoader(stage);

        Filters filters = new Filters();
        eventsDatabase = new EventsDatabase(eventLoader, filters);
        
        PropertiesSingleton.getInstance();
        
        // build Interface
        buildInterface(stage, scene, filters);
        
        stage.setScene(scene);
        stage.show();
    }
    
    private void buildInterface(Stage stage, Scene scene, Filters filters) {
        
        // instead of passing the scene around as it's only being used for detecting
        // changes in the size, just do it here and 
        
        // --- Menu
        MenuPane menuPane = new MenuPane(stage, scene, eventLoader);
        MenuBar menuBar = menuPane.getPane();
        
        // --- Primary layout container
        BorderPane borderPane = new BorderPane();
        borderPane.setLayoutY(30);
        
        // -- Top
        FilterPane filterPane = new FilterPane(scene, eventsDatabase, filters);
        borderPane.setTop(filterPane.getPane());
        
        // --- Center
        MainPane mainPane = new MainPane(scene, eventsDatabase, filterPane);
        borderPane.setCenter(mainPane.getPane());
        
        ((Group) scene.getRoot()).getChildren().addAll(menuBar, borderPane);
    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
