package io.haskins.java.cloudtrailviewer.service;

import javafx.concurrent.Task;
import javafx.scene.control.Label;
import org.springframework.stereotype.Service;

/**
 * Created by markhaskins on 05/02/2017.
 */
@Service
public class MemoryCheckService {

    private final Runtime runtime = Runtime.getRuntime();

    private Task task;

    MemoryCheckService() {
        checkMemory();
    }

    public void addLabel(Label label) {
        label.textProperty().bind(task.messageProperty());
    }

    private void checkMemory() {

        task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                while(true) {

                    long total = runtime.totalMemory() / 1024 / 1024;
                    long free = runtime.freeMemory() / 1024 / 1024;
                    long max = runtime.maxMemory() / 1024 / 1024;
                    long used = total - free;

                    updateMessage(String.format("Memory : Used %sMb | Free %dMb | Max Available %dMb", used, free, max));

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException interrupted) {
                        if (isCancelled()) {
                            updateMessage("Cancelled");
                            break;
                        }
                    }

                }

                return null;
            }
        };


        new Thread(task).start();
    }
}
