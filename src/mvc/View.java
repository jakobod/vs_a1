package mvc;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View implements IView{
    private final Stage primaryStage;
    private final IController controller;
    private final IModel model;
    private final Scene scene;
    
    public View(IModel model, IController controller, Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = controller;
        this.model = model;
        final GridPane mainGridPane = new GridPane();

        /*server config input*/
        var serverConfigBox = new HBox();
        serverConfigBox.setPadding(new Insets(15, 12, 15, 12));
        serverConfigBox.setSpacing(10);
        Label serverLabel = new Label("host");
        final var hostNameInput = new TextField();
        hostNameInput.setPrefWidth(100);

        Label portLabel = new Label("port");
        final var portInput = new TextField();
        portInput.setPrefWidth(100);

        // send button!
        final Button configureServerButton = new Button("connect");
        configureServerButton.setOnAction( (event) -> {
            controller.configureConnection(hostNameInput.getText(), Integer.parseInt(portInput.getText()));
            hostNameInput.clear();
            portInput.clear();
        });
        serverConfigBox.getChildren().addAll(serverLabel, hostNameInput, portLabel, portInput, configureServerButton);



        /** input field! */
        VBox inputBox = new VBox();
        inputBox.setPadding(new Insets(15, 12, 15, 12));
        inputBox.setSpacing(10);
        Label inLabel = new Label("text input");
        final TextArea messageInputField = new TextArea();
        messageInputField.setPrefWidth(800);
        messageInputField.setPrefHeight(100);

        // send button!
        final Button sendMessageButton = new Button("send message");
        sendMessageButton.setOnAction( (event) -> {
            controller.sendMessage(messageInputField.getText());
            messageInputField.clear();
        });
        inputBox.getChildren().addAll(inLabel, messageInputField, sendMessageButton);

        /** output field! */
        VBox outputBox = new VBox();
        outputBox.setPadding(new Insets(15, 12, 15, 12));
        outputBox.setSpacing(10);
        Label outLabel = new Label("text output");
        final TextArea messageOutputField = new TextArea();
        messageOutputField.setPrefWidth(800);
        messageOutputField.setPrefHeight(400);
        messageOutputField.setEditable(false);

        // get button!
        final Button getMessageButton = new Button("get next message");
        getMessageButton.setOnAction((event) -> {
            String msg = controller.getMessage();
            if (msg != null) {
                messageOutputField.appendText(msg);
                messageOutputField.appendText("\n");
            }
        });

        final Button getAllMessageButton = new Button("get all messages");
        getAllMessageButton.setOnAction((event) -> {
            String msg = controller.getAllMessages();
            if (msg != null) {
                messageOutputField.appendText(msg);
                messageOutputField.appendText("\n");
            }
        });
        HBox buttons = new HBox();
        buttons.setPadding(new Insets(15, 12, 15, 12));
        buttons.setSpacing(10);
        buttons.getChildren().addAll(getMessageButton, getAllMessageButton);

        outputBox.getChildren().addAll(outLabel, messageOutputField, buttons);

        // add everything to the main grid
        mainGridPane.add(serverConfigBox, 0, 0);
        mainGridPane.add(outputBox, 0, 1);
        mainGridPane.add(inputBox, 0, 2);

        /* add GridPain to Scene */
        scene = new Scene(mainGridPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Message-Client");

        primaryStage.show();
    }
}
