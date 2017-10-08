package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.model.flowlog.VpcFlowLog;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class VpcFlowLogService extends DataService {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");

    private static final String REGEX = "^(\\d) (\\d+) (eni-\\w+) (\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}) (\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (ACCEPT|REJECT) (OK|NODATA|SKIPDATA)$";
    private Pattern pattern = Pattern.compile(REGEX);

    private final List<VpcFlowLog> logsDb = new ArrayList<>();

    public void processRecords(List<String> records) {

        for (String record : records) {

            try (Scanner scanner = new Scanner(new File(record))) {

                while (scanner.hasNext()){

                    String line = scanner.nextLine();

                    Matcher matcher = pattern.matcher(line);

                    if (matcher.matches()) {
                        VpcFlowLog log = new VpcFlowLog();
                        log.populateFromRegex(matcher);

                        logsDb.add(log);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private InputStream loadEventFromLocalFile(final String file) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(file));
        return new ByteArrayInputStream(encoded);
    }

    public List<VpcFlowLog> getAllLogs() {
        return logsDb;
    }

    List getDataDb() {
        return getAllLogs();
    }
}
