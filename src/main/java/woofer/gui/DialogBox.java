package woofer.gui;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Represents one conversation message with a speaker label and message text.
 */
public class DialogBox extends HBox {
    /** Label containing the conversation message. */
    @FXML
    private Label dialog;

    /** Label identifying the speaker. */
    @FXML
    private Label avatar;

    /**
     * Creates a dialog box from its FXML layout.
     *
     * @param text message text.
     * @param speaker speaker label.
     */
    private DialogBox(String text, String speaker) {
        URL fxmlResource = DialogBox.class.getResource("/view/DialogBox.fxml");
        if (fxmlResource == null) {
            throw new IllegalStateException("Unable to find the dialog box resource.");
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load a dialog box.", exception);
        }

        dialog.setText(text);
        avatar.setText(speaker);
        getStyleClass().add("dialog-box");
        HBox.setHgrow(dialog, Priority.ALWAYS);
    }

    /**
     * Flips the dialog box so that Woofer's responses appear on the left.
     */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Creates a dialog box for a user message.
     *
     * @param text user message text.
     * @return a right-aligned user dialog box.
     */
    public static DialogBox getUserDialog(String text) {
        return new DialogBox(text, "You");
    }

    /**
     * Creates a dialog box for a Woofer response.
     *
     * @param text Woofer response text.
     * @return a left-aligned Woofer dialog box.
     */
    public static DialogBox getWooferDialog(String text) {
        DialogBox dialogBox = new DialogBox(text, "W");
        dialogBox.flip();
        return dialogBox;
    }
}
