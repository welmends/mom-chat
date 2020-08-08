package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;

public class Main extends Application {
	public static void main(String[] args) {  
		launch(args);
	}
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("scenes/main_scene.fxml"));
		//primaryStage.getIcons().add(ImageConstants.MAIN_APPLICATION_ICON);
		primaryStage.setTitle("MOM Chat");
		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(root, 720, 720));
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override public void handle(WindowEvent t) {
		        Platform.exit();
		        System.exit(0);
		    }
		});
	}
}