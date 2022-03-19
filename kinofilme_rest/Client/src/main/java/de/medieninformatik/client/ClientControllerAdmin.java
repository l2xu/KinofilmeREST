package de.medieninformatik.client;

import de.medieninformatik.common.Movie;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Lukas Weihrauch, m29202
 * @date 05.02.2022
 * @version 1.0
 *
 * Die ClientControllerAdmin Klasse dient als Controller Klasse für das Admin Fenster.
 * Hier werden alle Eingabe wahrgenommen und die entsprechende Logik dafür aufgerufen. Hierfür wird meist eine Methode aus der ClientModel Klasse aufgerufen.
 */
public class ClientControllerAdmin {


    final String BASE_URI = "http://localhost:8080/rest";
    ClientModel clientModel = new ClientModel(BASE_URI);

    // FXML Objekte
    @FXML
    ListView<Movie> movielist;
    @FXML
    ChoiceBox sortBy;
    @FXML
    ChoiceBox<String> filterBy;
    @FXML
    AnchorPane addMovieView;
    @FXML
    TextField addMovieTitel;
    @FXML
    TextField addMovieRegi;
    @FXML
    TextField addMovieBewertung;
    @FXML
    TextField addMovieGenre;
    @FXML
    TextField addMovieLaenge;
    @FXML
    AnchorPane editMovieView;
    @FXML
    TextField editMovieTitel;
    @FXML
    TextField editMovieRegi;
    @FXML
    TextField editMovieBewertung;
    @FXML
    TextField editMovieGenre;
    @FXML
    TextField editMovieLaenge;
    @FXML
    Button edit;
    @FXML
    Button delete;
    @FXML
    Button toLoginButton;

    /**
     * FXML initialize Methode wird beim Laden des Fensters aufgerufen.
     * Die Methode wird hier verwendet, um den Edit und Delete Button anfangs zu deaktivieren, solange noch kein Film ausgewählt wurde
     */
    @FXML
    public void initialize() {
        //Filme werden beim Ladend es Fensterns aktualisiert und angzeigt
        updateMovieList();

        //Deaktiviert den Edit und Delte Button wenn kein Film ausgewählt ist.
        edit.disableProperty()
                .bind(movielist.getSelectionModel().selectedItemProperty().isNull());
        delete.disableProperty()
                .bind(movielist.getSelectionModel().selectedItemProperty().isNull());

        //Verändert das Aussehen der Movielist
        movielist.setCellFactory(new Callback<ListView<Movie>, ListCell<Movie>>() {
            @Override
            public ListCell<Movie> call(ListView<Movie> param) {
                ListCell<Movie> cell = new ListCell<Movie>() {

                    @Override
                    protected void updateItem(Movie m, boolean b) {
                        super.updateItem(m, b);
                        if (m != null) {
                            setText(m.getTitle() + "\n" + "[Genre: " + m.getGenre() + "; Regisseur: " + m.getRegi() + "; Bewertung: " + m.getBewertung() + "/10; Länge: " + m.getLaeng() + " Minuten]");
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
    }


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

    /**
     * Die Methode holt sich einen einzelnen Film auf Grundlage des ausgewählten Films.
     * Dieser wird zum Bearbeiten verwendet.
     */
    public void getSingleMovie() {
        System.out.println(movielist.getSelectionModel().getSelectedItem());
        Movie movie = (Movie) movielist.getSelectionModel().getSelectedItem();
        clientModel.getSingleMovie("/api/v1/movies/" + movie.getId());
        System.out.println("SELECTED: " + movie.getId());
    }


    /**
     * Die Methode deleteMovie löscht den ausgewählten Film aus der Datenbank
     */
    public void deleteMovie() {
        Movie movie = (Movie) movielist.getSelectionModel().getSelectedItem();
        clientModel.delete("/api/v1/movies/" + movie.getId());
        updateMovieList();
    }

    /**
     * Die Methode openAddMovie ist für das öffnen des "addMovieView" zuständig.
     */
    public void openAddMovie() {
        addMovieView.setVisible(true);
    }

    /**
     * Die Methode openEditMovie ist für das öffnen des "openEditMovie" View zuständig.
     * Alle Elemente die zum Editieren eines Films notwendig sind werden hier aktiviert.
     */
    public void openEditMovie() {
        Movie movie = (Movie) movielist.getSelectionModel().getSelectedItem();
        editMovieView.setVisible(true);
        editMovieTitel.setText(movie.getTitle());
        editMovieRegi.setText(movie.getRegi());
        editMovieBewertung.setText(String.valueOf(movie.getBewertung()));
        editMovieGenre.setText(movie.getGenre());
        editMovieLaenge.setText(String.valueOf(movie.getLaeng()));
    }

    /**
     * Die editMovieToDB Methode ist für das Editieren eines Films und das speichern in der Datenbank zuständig.
     */
    public void editMovieToDB() {

        //Check ob alles eingegeben wurde
        if (editMovieTitel.getText().equals("")) {
            editMovieTitel.setStyle("-fx-border-color:red");
        } else {
            editMovieTitel.setStyle("-fx-border-color:green");
        }

        if (editMovieRegi.getText().equals("")) {
            editMovieRegi.setStyle("-fx-border-color:red");
        } else {
            editMovieRegi.setStyle("-fx-border-color:green");
        }

        if (editMovieBewertung.getText().equals("") || !editMovieBewertung.getText().matches("-?(0|[1-9]\\d*)")) {
            editMovieBewertung.setStyle("-fx-border-color:red");
        } else {
            editMovieBewertung.setStyle("-fx-border-color:green");
        }

        if (editMovieGenre.getText().equals("")) {
            editMovieGenre.setStyle("-fx-border-color:red");
        } else {
            editMovieGenre.setStyle("-fx-border-color:green");
        }
        if (editMovieLaenge.getText().equals("") || !editMovieLaenge.getText().matches("-?(0|[1-9]\\d*)")) {
            editMovieLaenge.setStyle("-fx-border-color:red");
        } else {
            editMovieLaenge.setStyle("-fx-border-color:green");
        }

        //Wenn etwas nicht eingegeben wurde => Fehler
        if (editMovieTitel.getText().equals("") ||
                editMovieRegi.getText().equals("") ||
                editMovieBewertung.getText().equals("") ||
                editMovieGenre.getText().equals("") ||
                editMovieLaenge.getText().equals("") ||
                !editMovieBewertung.getText().matches("-?(0|[1-9]\\d*)") ||
                !editMovieLaenge.getText().matches("-?(0|[1-9]\\d*)")
        ) {
            System.out.println("BITTE ALLES EINGEBEN");
            return;
        }


        String editTitle = editMovieTitel.getText();
        String editRegi = editMovieRegi.getText();
        String editBewertung = editMovieBewertung.getText();
        String editGenre = editMovieGenre.getText();
        String editLaenge = editMovieLaenge.getText();
        Movie movie = (Movie) movielist.getSelectionModel().getSelectedItem();

        //Übergabe der geänderten Werte an die update Methode aus der ClientModel Klasse.
        clientModel.update("/api/v1/movies/" + movie.getId(), new Movie(movie.getId(), editTitle, editRegi, Integer.parseInt(editBewertung), editGenre, Integer.parseInt(editLaenge)));

        //schließt den "Edit View" und zeigt wieder die Filmliste an.
        addMovieBack();
    }


    /**
     * Die addMovieToDB Methode ist für das hinzufügen eines neuen Film in die Datenbank zuständig
     */
    public void addMovieToDB() {

        //Check ob alles eingegeben wurde
        if (addMovieTitel.getText().equals("")) {
            addMovieTitel.setStyle("-fx-border-color:red");
        } else {
            addMovieTitel.setStyle("-fx-border-color:green");
        }

        if (addMovieRegi.getText().equals("")) {
            addMovieRegi.setStyle("-fx-border-color:red");
        } else {
            addMovieRegi.setStyle("-fx-border-color:green");
        }

        if (addMovieBewertung.getText().equals("") || !addMovieBewertung.getText().matches("-?(0|[1-9]\\d*)")) {
            addMovieBewertung.setStyle("-fx-border-color:red");
        } else {
            addMovieBewertung.setStyle("-fx-border-color:green");
        }

        if (addMovieGenre.getText().equals("")) {
            addMovieGenre.setStyle("-fx-border-color:red");
        } else {
            addMovieGenre.setStyle("-fx-border-color:green");
        }
        if (addMovieLaenge.getText().equals("") || !addMovieLaenge.getText().matches("-?(0|[1-9]\\d*)")) {
            addMovieLaenge.setStyle("-fx-border-color:red");
        } else {
            addMovieLaenge.setStyle("-fx-border-color:green");
        }

        //Wenn etwas nicht eingegeben wurde => Fehler
        if (addMovieTitel.getText().equals("") ||
                addMovieRegi.getText().equals("") ||
                addMovieBewertung.getText().equals("") ||
                addMovieGenre.getText().equals("") ||
                addMovieLaenge.getText().equals("") ||
                !addMovieBewertung.getText().matches("-?(0|[1-9]\\d*)") ||
                !addMovieLaenge.getText().matches("-?(0|[1-9]\\d*)")
        ) {
            System.out.println("BITTE ALLES EINGEBEN");
            return;
        }

        String newTitle = addMovieTitel.getText();
        String newRegi = addMovieRegi.getText();
        String newBewertung = addMovieBewertung.getText();
        String newGenre = addMovieGenre.getText();
        String newLaenge = addMovieLaenge.getText();

        //Übergabe der geänderten Werte an die add Methode aus der ClientModel Klasse.
        clientModel.add("/api/v1/movies", new Movie(0, newTitle, newRegi, Integer.parseInt(newBewertung), newGenre, Integer.parseInt(newLaenge)));

        //schließt den "Add View" und zeigt wieder die Filmliste an.
        addMovieBack();

    }

    /**
     * Die Methode für einen reset der Nutzereingabe durch.
     */
    private void resetUserInput() {
        addMovieTitel.setText("");
        addMovieRegi.setText("");
        addMovieBewertung.setText("");
        addMovieGenre.setText("");
        addMovieLaenge.setText("");
    }

    /**
     * Die Methode schließt die "EditView" und die "AddView", resettet die Nutzereingaben und aktualisiert die Filmliste und zeigt diese an.
     */
    public void addMovieBack() {
        addMovieView.setVisible(false);
        editMovieView.setVisible(false);
        resetUserInput();
        updateMovieList();
    }
}


