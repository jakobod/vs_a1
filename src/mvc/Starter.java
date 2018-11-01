package mvc;

import javafx.application.Application;
import javafx.stage.Stage;
import rmi.MessageClient;

public class Starter extends Application{
    public static void start(String... args){
        Application.launch( args );
    }

    @Override
    public void start(Stage primaryStage) {
        final IModel model = new MessageClient();
        
        @SuppressWarnings("unused")
        final IController controller = new Controller( model, primaryStage );
    }
}
