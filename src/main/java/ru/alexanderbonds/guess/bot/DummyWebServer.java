package ru.alexanderbonds.guess.bot;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DummyWebServer {
    private final String HEROKU_SERVER_NAME = "https://salty-fjord-59326.herokuapp.com/";
    private final String HTTP_OUTPUT = "<html><head><title>Example</title></head><body><p>Worked!!!</p></body></html>";
    private final String HTTP_OUTPUT_HEADERS = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: ";
    private final String HTTP_OUTPUT_END_OF_HEADERS = "\r\n\r\n";

    public void start() {
        // for eliminating Heroku Error R10 (no $PORT binding)
        final int port = Integer.parseInt(System.getenv("PORT"));

        ExecutorService errorR10Executor = Executors.newSingleThreadExecutor();
        errorR10Executor.execute(() -> {
            try {
                ServerSocket server = new ServerSocket(port);
                while (true) {
                    Socket socket = server.accept();
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(
                                    new BufferedOutputStream(socket.getOutputStream()), "UTF-8")
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
        StringBuilder builder = new StringBuilder();
        inactivityExecutor.scheduleAtFixedRate(
                () -> {
                    try {
                        InputStream in = new URL(HEROKU_SERVER_NAME).openStream();
                        int x;
                        while ( (x = in.read()) != -1) {
                            builder.append((char) x);
                        }
                        System.out.printf("NO SLEEPING HERE!! %s%n", builder);
                        builder.setLength(0);
                        in.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                1,
                10,
                TimeUnit.MINUTES);
    }
}
