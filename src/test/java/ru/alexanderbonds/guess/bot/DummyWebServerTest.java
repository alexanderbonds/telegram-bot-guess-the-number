package ru.alexanderbonds.guess.bot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;


class DummyWebServerTest {

    @Test
    // @SetEnvironmentVariable annotation will not work on Java 17
    @SetEnvironmentVariable(key = "PORT", value = "8887")
    @DisplayName("Server must start and reply with defined content")
    void start_noArguments_mustStartWebServerAndReturnWebPage() throws Exception {
        // Config
        final String expectedContent = "<html><body><p>Hello from Dummy Web Server</p></body></html>";
        final int port = Integer.parseInt(System.getenv("PORT"));
        final String url = "http://localhost:" + port;
        final DummyWebServer localWebServer = new DummyWebServer();

        // Call
        localWebServer.start();
        final InputStream in = new BufferedInputStream(new URL(url).openStream());
        final byte[] byteContent = in.readAllBytes();
        final String content = new String(byteContent, StandardCharsets.UTF_8);
        in.close();

        // Verify
        assertEquals(expectedContent, content);
    }
}
