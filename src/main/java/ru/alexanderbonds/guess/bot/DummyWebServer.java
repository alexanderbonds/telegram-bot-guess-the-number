package ru.alexanderbonds.guess.bot;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DummyWebServer {
    private static final Logger logger = LogManager.getLogger(DummyWebServer.class);
    private static final String HEROKU_SERVER_NAME = "https://salty-fjord-59326.herokuapp.com/";
    private static final String HTTP_OUTPUT = "<html><body><p>Hello from Dummy Web Server</p></body></html>";
    private static final String HTTP_OUTPUT_HEADERS =
            "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: ";
    private static final String HTTP_OUTPUT_END_OF_HEADERS = "\r\n\r\n";

    public void start() {
        // for eliminating Heroku Error R10 (no $PORT binding)
        final int port = Integer.parseInt(System.getenv("PORT"));

        ExecutorService errorR10Executor = Executors.newSingleThreadExecutor();
        errorR10Executor.execute(() -> {
            try {
                ServerSocket server = new ServerSocket(port);
                logger.log(Level.INFO, "Dummy Web Server started on port {}", port);
                while (true) {
                    Socket socket = server.accept();
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(
                                    new BufferedOutputStream(socket.getOutputStream()), StandardCharsets.UTF_8)
                    );
                    out.write(HTTP_OUTPUT_HEADERS + HTTP_OUTPUT.length() + HTTP_OUTPUT_END_OF_HEADERS + HTTP_OUTPUT);
                    out.flush();
                    out.close();
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Heroku free dynos get to sleep after 30 min of web inactivity
        ScheduledThreadPoolExecutor inactivityExecutor = new ScheduledThreadPoolExecutor(1);
        inactivityExecutor.scheduleAtFixedRate(
                () -> logger.log(Level.INFO, "NO SLEEPING HERE!! {}", getUrlContent(HEROKU_SERVER_NAME)),
                1,
                10,
                TimeUnit.MINUTES);
    }

    private String getUrlContent(String url) {
        String result = "<html><body>Empty</body></html>";
        try (InputStream in = new BufferedInputStream(new URL(url).openStream())) {
            byte[] content = in.readAllBytes();
            result = new String(content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.log(Level.ERROR, "Got an error while URL downloading: {}", e.getMessage());
        }
        return result;
    }
}
