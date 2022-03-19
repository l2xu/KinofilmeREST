package de.medieninformatik.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
 * Die ClientControllerLogin Klasse dient als Controller Klasse für das Login Fenster.
 *
 */
public class ClientControllerLogin {

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Die switchToNormal Methode ist für das Aufrufen und Starten des Fensters für den "normalen" User zuständig.
     * @param actionEvent Event welches realisiert wird, um die Aktion durchzuführen. Hier durch Mausklick.
     * @throws IOException Exception die geworfen wird, wenn ein Fehler bei der Ein- bzw. Ausgabe auftritt.
     */
    public void switchToNormal(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/normal.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Die switchToAdmin Methode ist für das Aufrufen und Starten des Fensters für den "admin" User zuständig
     * @param actionEvent Event welches realisiert wird, um die Aktion durchzuführen. Hier durch Mausklick.
     * @throws IOException Exception die geworfen wird, wenn ein Fehler bei der Ein- bzw. Ausgabe auftritt.
     */
    public void switchToAdmin(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/admin.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
