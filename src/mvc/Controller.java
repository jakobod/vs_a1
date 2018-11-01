package mvc;

import javafx.stage.Stage;

/**
 * only passes function calls from buttons in gui to the model
 */
public class Controller implements IController {
    private final IModel model;
    private final IView view;

    public Controller(IModel model, Stage primaryStage) {
        this.model = model;
        view = new View(model, this, primaryStage);
    }

    @Override
    public String getMessage() {
        return model.getMessage();
    }

    @Override
    public String getAllMessages() {
        return model.getAllMessages();
    }

    @Override
    public void sendMessage(String message) { model.sendMessage(message); }

    public void configureConnection(String hostname, int port){
        model.configureConnection(hostname, port);
    }
}