package de.medieninformatik.server;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *  @author Lukas Weihrauch, m29202
 *  @date 05.02.2022
 *  @version 1.0
 *
 * Die Serverklasse ist für das Aufsetzen und Starten des Servers zuständig.
 *
 */
public class Server {
    /**
     * Startet den Server, wenn dieser noch nicht gestartet wurde
     * @param args Programmargumente
     * @throws URISyntaxException wird bei fehlern geworfen
     * @throws IOException wird bei Fehlern geworfen die auf die Ein- bzw. Ausgabe zurückzuführen sind.
     */
    public static void main(String [] args)
            throws URISyntaxException, IOException {
        Logger. getLogger ("org.glassfish").setLevel (Level.ALL);

        URI baseUri = new URI("http://0.0.0.0:8080/rest");
        ResourceConfig config = ResourceConfig . forApplicationClass (
               ServerConfig.class
        );
        HttpServer server =
                GrizzlyHttpServerFactory. createHttpServer (baseUri , config);

        // Optional: Einbindung statischer Webseiten:
        StaticHttpHandler handler = new StaticHttpHandler ("web");
        handler. setFileCacheEnabled (false);
        ServerConfiguration serverConfig =
                server. getServerConfiguration ();
        serverConfig . addHttpHandler (handler , "/");

        if(! server. isStarted ()) server.start ();
        System.out.println("http://localhost :8080/rest/");
        System.out.println("ENTER stoppt den de.medieninformatik.de.medieninformatik.server.Server.");
        System.in.read ();
        server.shutdownNow ();
    }
}


