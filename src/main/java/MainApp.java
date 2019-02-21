import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private MainFormController mainFormController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_form.fxml"));
        Parent root = loader.load();

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Program closed.");
            Platform.exit();
            System.exit(0);
        });

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Eco Server");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        mainFormController = loader.getController();
    }
}
