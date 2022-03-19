package de.medieninformatik.client;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Lukas Weihrauch, m29202
 * @date 05.02.2022
 * @version 1.0
 *
 * Die Klasse Client erbt von der Klasse Application von JavaFX.
 * Client wird verwendet, um den Client mittels JavaFX zu starten
 */
public class Client  extends Application {

    /**
     * In der main Methode wird die launch funktion aufgerufen. Diese startet das Prgramm mittels JavaFX
     * @param args Programmargumente
     */
    public static void main(String[] args)  {
        System.out.println("CLIENT STATED...");
        launch(args);
    }

    /**
     * Die start Funktion gehört zu JavaFx und ist zuständig für das Starten der Application.
     * Sie nimmt eine Stage entgegen.
     * In diesem Falle wird der Aufbau der Scene durch eine FXML Datei realisiert. Diese wird mit dem FXMLLoader eingelesne.
     * @param primaryStage
     * @throws IOException Input-Output exception bei fehlerhafter Ein- bzw. Ausgabe
     */
    @Override
    public void start(Stage primaryStage) throws  IOException {

        //Laden des FXML Files
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
  }
}
