package org.example;


import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Statement;

@Service

public class PgNotifyListenerService {

    private static final Logger log = LoggerFactory.getLogger(PgNotifyListenerService.class);
    private final DataSource dataSource;
    private final RestClient restClient;

    public PgNotifyListenerService(DataSource dataSource, RestClient restClient) {
        this.dataSource = dataSource;
        this.restClient = restClient;
    }

    public void createPgListener() {

        PGNotificationListener listener = new PGNotificationListener() {
            @Override
            public void notification(int processId, String channelName, String payload) {
                restClient.sendRefreshSignal();            }
        };

        try (PGConnection connection = (PGConnection) dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("LISTEN properties");
            }
            connection.addNotificationListener(listener);
            // it only works if the connection is open. Therefore, we do an endless loop here.
            while (true) {
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }
}
