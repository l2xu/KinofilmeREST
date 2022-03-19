package de.medieninformatik.client;

import de.medieninformatik.common.Movie;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Lukas Weihrauch, m29202
 * @date 05.02.2022
 * @version 1.0
 * Die ClientControllerNormal Klasse dient als Controller Klasse für das Normale Fenster.
 * Hier werden alle Eingabe wahrgenommen und die entsprechende Logik dafür aufgerufen. Hierfür wird meist eine Methode aus der ClientModel Klasse aufgerufen.
 */
public class ClientControllerNormal {

    final String BASE_URI = "http://localhost:8080/rest";
    ClientModel clientModel =  new ClientModel(BASE_URI);
    private int movieCount;


    /**
     * FXML initialize Methode wird beim Laden des Fensters aufgerufen.
     * Die Methode verändert das Aussehen der Listview.
     */
    @FXML
    public void initialize(){
        updateMovieList();
        movielist.setCellFactory(new Callback<ListView<Movie>, ListCell<Movie>>() {
            @Override
            public ListCell<Movie> call(ListView<Movie> param) {
                return new ListCell<Movie>(){

                    @Override
                    protected void updateItem(Movie m, boolean b){
                        super.updateItem(m,b);
                        if(m != null){
                            setText(m.getTitle() + "\n" + "[Genre: " + m.getGenre() +"; Regisseur: "+ m.getRegi() + "; Bewertung: " + m.getBewertung() +"/10; Länge: " + m.getLaeng() + " Minuten]");
                        }else {
                            setText(null);
                        }
                    }
                };
            }
        });
    }


    // FXML Objekte
    @FXML
    ListView<Movie> movielist;
    @FXML
    ChoiceBox<String> sortBy;
    @FXML
    ChoiceBox<String> filterBy;


    /**
     * Die Methode backToLogin ist für das zurückkehren auf den Startbildschirm verantwortlich.
     * @param actionEvent  Event welches realisiert wird, um die Aktion durchzuführen.
     * @throws IOException Exception die geworfen wird, wenn ein Fehler bei der Ein- bzw. Ausgabe auftritt.
     */
    public void backToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Die Methode updateMovieList ist für das updaten der Filmliste im Client zuständig.
     * Zusätzlich wird hier auch die Genreliste aktualisiert. Somit kann der Nutzer nur nach Genres Filern, welches auch in der Datenbank existieren.
     */
    public void updateMovieList() {
        movielist.getItems().clear();

        //Holt sich alle verfügbaren Filme
        ArrayList<Movie> genreList = clientModel.get("/api/v1/movies");

        //Update der Genre nach denen gefiltert werden kann
        String oldFilter = filterBy.getValue();
        filterBy.getItems().clear();
        filterBy.getItems().add(0,"kein Filter");
        for (Movie movie : genreList) {
            ObservableList<String> list = filterBy.getItems();
            if (!list.contains(movie.getGenre())) {
                filterBy.getItems().add(movie.getGenre());
            }
            if(list.contains(oldFilter)){
                filterBy.setValue(oldFilter);
            }else{
                filterBy.setValue("kein Filter");
            }
        }

        //Filtert und Sortiert die Filme und fügt diese dann zur movielist hinzu.
        ArrayList<Movie> filterList = clientModel.filterList(genreList, filterBy.getValue().toString());
        ArrayList<Movie> sortList = clientModel.sortList(filterList, sortBy.getValue().toString());
        for (Movie m: sortList) {
            movielist.getItems().add(m);
        }
    }

}
