package application.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import application.com.P2P;
import application.com.mom.MOM;
import application.ui.constants.FXMLConstants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {
	
	// FXML Variables
	@FXML AnchorPane root;
	@FXML HBox mainHBox;
	
	// COM Variables
	private MOM mom;
	private P2P p2p;
	
	// FXML Loaders
	private FXMLLoader chatLoader;
	private FXMLLoader configLoader;
	
	// Controllers
	private ChatController chatController;
	private ConfigController configController;
	
	// Main Object
	private Main main;
    public void setMainApp(Main main) {
        this.main = main;
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Initialize Objects
		mom = new MOM();
		p2p = new P2P();
		
		Scene chatScene = null;
		Scene configScene = null;
		
		chatLoader = new FXMLLoader(getClass().getResource(FXMLConstants.FXML_CHAT_CONTROLLER));
		configLoader = new FXMLLoader(getClass().getResource(FXMLConstants.FXML_CONFIG_CONTROLLER));
		
		//Load Scenes
		try {
			chatScene = new Scene(chatLoader.load());
			configScene = new Scene(configLoader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Add nodes to MainController holders 
		mainHBox.getChildren().add(configScene.getRoot());
		mainHBox.getChildren().add(chatScene.getRoot());
		
		// Get Controller
		chatController = chatLoader.getController();
		configController = configLoader.getController();
		
		// Authentication
		authentication();
		
		// Load common objects from parent
		chatController.loadFromParent(mom, p2p);
		configController.loadFromParent(mom, p2p);
	}
	
	public void closeApplication() {
		p2p.disconnect();
	}

    private Boolean authentication() {
        Parent layout;
        Scene scene;
        Stage popupStage;
        FXMLLoader loader = new FXMLLoader();
        AuthController popupController = new AuthController(mom, p2p, chatController, configController);
        
        loader.setLocation(getClass().getResource(FXMLConstants.FXML_AUTH_CONTROLLER));
        loader.setController(popupController);
        
        try {
            layout = loader.load();
            scene = new Scene(layout);
            popupStage = new Stage();
            popupController.setStage(popupStage);
            if(this.main!=null) { popupStage.initOwner(this.main.getPrimaryStage()); }
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
		return true;
    }
}