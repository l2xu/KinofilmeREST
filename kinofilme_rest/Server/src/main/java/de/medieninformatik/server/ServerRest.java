package de.medieninformatik.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.medieninformatik.common.Movie;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 *  @author Lukas Weihrauch, m29202
 *  @date 05.02.2022
 *  @version 1.0
 *
 * Die Klasse ServerRest beinhaltet die REST Methoden des Servers.
 * Hier wird die Anfrage vom Server an die Datenbank gestellt.
 */
@Path("api/v1/movies") // Teil des Resource -Pfades
public class ServerRest {

    /**
     * Die connect Methode stellt eine Verbindung zur Datenbank her.
     * @return Connectstatement
     * @throws SQLException Exception die auf Grundlage von SQL geworfen werden kann
     */
    private Statement connect() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/prog3", "root", null);
        Statement stmt = conn.createStatement();
        return stmt;
    }



    /**
     * Holt alle Filme und gibt diese zurueck.
     * @return String mit Filmen als JSON formatiert.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMovies() throws SQLException {
        ArrayList myArrayList = new ArrayList();
        Statement stmt = connect();
        ResultSet rs = stmt.executeQuery("SELECT * FROM movies");
        while (rs.next()) {
            Movie movie = new Movie(rs.getInt("ID"), rs.getString("Titel"), rs.getString("Regisseure"), rs.getInt("Bewertung"), rs.getString("Genre"), rs.getInt("Laenge"));
            myArrayList.add(movie);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Alle Filme vom Server: "+gson.toJson(myArrayList));
        return Response.ok(gson.toJson(myArrayList)).build();
    }


    /**
     * Die Methode holt den Film mit der entsprechenden ID aus der Datenbank
     * @param id Filmid
     * @return Film als JSON String formatiert
     * @throws SQLException Exception die auf Grundlage von SQL geworfen werden kann
     */
    @GET
    @Path("{id}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getMovieById(@PathParam("id") int id) throws SQLException {
        Statement stmt = connect();
        ResultSet rs = stmt.executeQuery("SELECT * FROM movies where ID = " + id );
        while (rs.next()){
            Movie movie = new Movie(rs.getInt("ID"), rs.getString("Titel"), rs.getString("Regisseure"), rs.getInt("Bewertung"), rs.getString("Genre"), rs.getInt("Laenge"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println("EINZEL REQUEST: " + gson.toJson(movie));
            return Response.ok(gson.toJson(movie)).build();

        }
        return Response.ok().build();
    }


    /**
     * Die Methode fügt einen neuen Film in die Datenbank ein
     * @param newMovie neuer Film der eingefüt werden soll
     * @return Rückmeldung, ob Film erfolgreich eingefügt wurde
     * @throws SQLException Exception die auf Grundlage von SQL geworfen werden kann
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMovie (String newMovie) throws SQLException {
        System.out.println(newMovie);
        Statement stmt = connect();
        Movie movie = new Gson().fromJson(newMovie,Movie.class);
        String sql = "INSERT INTO movies (Titel, Regisseure, Bewertung, Genre, Laenge) VALUES ('" + movie.getTitle() +"','" +movie.getRegi() + "'," + movie.getBewertung() + ",'" + movie.getGenre()+"'," + movie.getLaeng() + ")";
        stmt.executeQuery(sql);
        return Response.ok().build();
    }


    /**
     * Die Methode aktualisiert einen vorhandenen Film mit neuen Daten
     * @param id Id des Films der aktualisiert werden soll
     * @param newMovie Aktualisierte Daten des Films
     * @return aktualisierter Film
     * @throws SQLException Exception die auf Grundlage von SQL geworfen werden kann
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovie(@PathParam("id") int id, String newMovie) throws SQLException {
        Movie movie = new Gson().fromJson(newMovie, Movie.class);
        System.out.println("Ubertrage wird:" + newMovie);
        String sql = "UPDATE movies SET Titel = '" + movie.getTitle() + "', Regisseure = '" + movie.getRegi() + "', Bewertung = '" + movie.getBewertung() + "', Genre = '" + movie.getGenre() + "', Laenge = '" + movie.getLaeng() + "' WHERE ID =" + id;
        System.out.println(sql);
        Statement stmt = connect();
        stmt.executeQuery(sql);

        return Response.ok().build();
    }


    /**
     * Die Methode entfernt einen Film aus der Datenbank
     * @param id ID des Film der entfernt werden soll
     * @return Status, ob der Film erfolgreich entfernt wurde
     * @throws SQLException Exception die auf Grundlage von SQL geworfen werden kann
     */
    @DELETE
    @Path("{id}")
    public Response deleteMovie ( @PathParam("id") int id) throws SQLException {
        Statement stmt = connect();
        ResultSet rs = stmt.executeQuery("DELETE FROM movies where ID = " + id);
        System.out.println("I WANT TO DELETE");
        return Response.noContent().status(Response.Status.OK).build();
    }



}



