package de.medieninformatik.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.medieninformatik.common.Movie;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Lukas Weihrauch, m29202
 * @date 05.02.2022
 * @version 1.0
 *
 * Die ClientModel Klasse beinhaltet die Logik für die Client Application.
 */
public class ClientModel {

        private final Client client;
        private final String baseURI;


        /**
         * Konstruktor
         * @param uri String
         */
        public ClientModel(String uri) {
            this.baseURI = uri;
            this.client = ClientBuilder.newClient();
        }

    /**
     * Die Methode filterList ist für das Anwenden der Filter auf die Ausgabe zuständig. Wenn ein Nutzer eine Filterfunktion auswählt, wird sie hier bearbeitet.
     * @param movieList Liste aller Filme
     * @param filter Azusgewählter Filter des Nutzers
     * @return die gefilterte Liste
     */
        public ArrayList<Movie> filterList(ArrayList<Movie> movieList, String filter){

            if(filter.equals("kein Filter"))
                return movieList;

            ArrayList<Movie> filterdMovieList = new ArrayList<>();
            for (Movie m: movieList) {
                if(m.getGenre().equals(filter))
                    filterdMovieList.add(m);
            }
            return filterdMovieList;
        }

    /**
     * Die Methode sortList ist für das Anwenden der Sortiereung auf die Ausgabe zuständig. Wenn ein Nutzer eine gewünschte Sortierung wählt, wird diese hier bearbeitet.
     * @param movieList Liste aller Filme
     * @param sort Ausgewählte Sortierung
     * @return Liste mit den sortieren Filmen
     */
        public ArrayList<Movie> sortList(ArrayList<Movie> movieList, String sort){
            Collections.sort(movieList, new Comparator<Movie>() {
                @Override
                public int compare(Movie o1, Movie o2) {
                    switch (sort){
                        case "Titel":
                            return  o1.getTitle().compareTo(o2.getTitle());
                        case "Regisseur":
                            return  o1.getRegi().compareTo(o2.getRegi());
                        case "Bewertung":
                            return Integer.compare(o1.getBewertung(), o2.getBewertung());
                        case "Genre":
                            return  o1.getGenre().compareTo(o2.getGenre());
                        case "Laenge":
                            return Integer.compare(o1.getLaeng(), o2.getLaeng());

                        default:
                            return 0;
                    }
                }
            });
            for (Movie m: movieList) {
                System.out.println(m.getTitle());
            }
            return movieList;
        }

        /**
         * Die Methode get sendet eine Anfrage an den Server und holt sich alle Filme, als JSON formatiert, und gibt diese als Liste zurück.
         *
         * @param uri adresse des Server, wo alle Filme zurückgegeben werden.
         */
        public ArrayList<Movie> get(String uri) {
            ArrayList<Movie> allMovies = new ArrayList<>();
            WebTarget target = getTarget("GET", uri);
            Response response = target.request().accept(MediaType.APPLICATION_JSON).get();

            //Wenn der Response Status OK, also die Anfrage korrekt ist.
            if (status(response) == 200) {
                Gson gson = new Gson();
                Type userListType = new TypeToken<ArrayList<Movie>>(){}.getType();
                allMovies = gson.fromJson(response.readEntity(String.class), userListType);
            }else{
                System.out.println("Something went wrong: " + status(response));
            }
                return allMovies;
        }


    /**
     * Die Methode getSingleMovie sendet eine Anfrage an den Server und holt sich einen einzelnen, als JSON formatierten, Film.
     * @param uri adresse des Server, wo der Filme zurückgegeben werden.
     */
    public void getSingleMovie(String uri){
            WebTarget target = getTarget("GET", uri);
            Response response = target.request().accept(MediaType.APPLICATION_JSON).get();

        //Wenn der Response Status OK, also die Anfrage korrekt ist.
        if(status(response) == 200){
                Gson gson = new Gson();
                Type movieType = new TypeToken<Movie>(){}.getType();
                Movie movie = gson.fromJson(response.readEntity(String.class), movieType);

                //Der Einzelne Film wird in der Konsole geprinted.
                System.out.println(movie.getTitle());
            }else{
                System.out.println("Something went wrong: " + status(response));
            }
        }


    /**
     * Die Methde add verwendet eine POST Anfrage an den Server, um einen Film hinzuzufügen.
     * @param uri adresse des Server, wo der Filme eingetragen werden soll.
     * @param movie Film der hinzugefügt werden soll.
     */
    public void add(String uri , Movie movie) {
        WebTarget target = getTarget ("POST",uri);
        String newMovie = new Gson().toJson(movie);
        Entity <String > entity = Entity.entity(newMovie , MediaType.APPLICATION_JSON );
        Response response = target.request().post(entity);

        //Wenn der Response Status CREATED, also der Film korrekt eingefügt wurde.
        if (status(response ) == 201) {
            System.out.println(response);
        }else{
            System.out.println("Something went wrong: " + status(response));
        }
    }


    /**
     * Die Methode delete löscht verwendet eine DELETE Anfrage an den Server, um einen Film zu entfernen.
     * @param uri adresse des Server, wo der Filme gelöscht werden soll.
     */
    public void delete(String uri){
            WebTarget target = getTarget("DELETE", uri);
            Response response = target.request().accept(MediaType.APPLICATION_JSON).delete();

        //Wenn der Response Status OK, also die Anfrage korrekt ist.
        if(status(response) == 200){
                System.out.println(response);
            }else{
                System.out.println("Something went wrong: " + status(response));
            }
        }


    /**
     * Die Methode update verwendet eine PUT Anfrage an den Server, um die Daten eines Films zu aktualisieren.
     * @param uri adresse des Server, wo der Filme geändert werden soll.
     * @param movie Veränderter Film
     */
    public void update(String uri, Movie movie){
            WebTarget target = getTarget ("PUT", uri);
            String newMovie = new Gson().toJson(movie);
//            String newMovie = movie.getTitle() + ": " + movie.getRegi()+ ": " +movie.getBewertung() + ": " + movie.getGenre()+ ": " + movie.getLaeng();
            Entity <String > entity = Entity.entity(newMovie , MediaType.APPLICATION_JSON );
            Response response = target.request ().put(entity);
            status(response);

        }

        /**
         * getter Methode für das Target
         * @param crud CREATE, READ, UPDATE, DELETE
         * @param uri adresse des Servers
         * @return Servertarget als baseURI variable
         */
        private WebTarget getTarget(String crud, String uri) {
            System.out.printf("%n--- %s %s%s%n", crud, baseURI, uri);
            return client.target(baseURI + uri);
        }

        /**
         *
         * @param response Rueckmeldung
         * @return den status der rueckmeldung
         */
        private int status(Response response) {
            int code = response.getStatus();
            String reason = response.getStatusInfo().getReasonPhrase(); System.out.printf("Status: %d %s%n", code, reason);
            return code;
        }


    /**
     * Die Methode getMovieList wird aufgerufen, wenn ein Nutzer sich die Filme ausgeben lassen möchte.
     * Die Methode ruft intern die Methoden: get, filterList und sortList auf.
     *
     * @param filter Filterkriterium des Nutzers
     * @param sort Sortierungskriterium des Nutzers
     * @return gefilterte und sortierte Liste aller Filme
     */
        public ArrayList<Movie> getMovieList(String filter, String sort){
            ArrayList<Movie> g = get("/api/v1/movies");
            ArrayList<Movie> f = filterList(g, filter);
            ArrayList<Movie> s = sortList(f, sort);

            return s;
        }
    }

