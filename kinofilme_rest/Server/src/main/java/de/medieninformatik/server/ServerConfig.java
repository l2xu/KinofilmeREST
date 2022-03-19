package de.medieninformatik.server;
import jakarta.ws.rs.core.Application;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 *  @author Lukas Weihrauch, m29202
 *  @date 05.02.2022
 *  @version 1.0
 *
 * Die Klasse ServerConfig erbt von der Klasse Application von jakarta.
 * Sie ist für die Konfiguration des Server zuständig und nach dem Singelton Entwurfsmuster aufgebaut.
 */
public class ServerConfig extends Application {

    private Set<Object > singletons = new HashSet<>();
    private Set <Class <?>> classes = new HashSet <>();

    /**
     * zu den hashsets wird ein neues Objekt hinzugefuegt
     */
    public ServerConfig() throws SQLException {
        singletons.add(new ServerRest());
        classes.add( ServerRest.class);
    }

    /**
     * Setter Methode
     * @return classes
     */
    @Override
    public Set <Class <?>> getClasses () { return classes; }

    /**
     * Setter Methode
     * @return singletons
     */
    @Override
    public Set <Object > getSingletons () { return singletons ; }
}



