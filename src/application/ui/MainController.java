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
	MOM mom;
	P2P p2p;
	
	// FXML Loaders
	FXMLLoader contactsLoader;
	FXMLLoader chatLoader;
	
	// Controllers
	ContactsController contactsController;
	ChatController chatController;
	
	private Main main;
    public void setMainApp(Main main) {
        this.main = main;
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Initialize Objects
		mom = new MOM();
		p2p = new P2P();
		
		Scene contactsScene = null;
		Scene chatScene = null;
		
		contactsLoader = new FXMLLoader(getClass().getResource(FXMLConstants.FXML_CONTACTS_CONTROLLER));
		chatLoader = new FXMLLoader(getClass().getResource(FXMLConstants.FXML_CHAT_CONTROLLER));
		
		//Load Scenes
		try {
			contactsScene = new Scene(contactsLoader.load());
			chatScene = new Scene(chatLoader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Add nodes to MainController holders 
		mainHBox.getChildren().add(contactsScene.getRoot());
		mainHBox.getChildren().add(chatScene.getRoot());
		
		// Get Controller
		contactsController = contactsLoader.getController();
		chatController = chatLoader.getController();
		
		// Authentication
		authentication();
		
		// Load common objects from parent
		contactsController.loadFromParent(mom, p2p);
		chatController.loadFromParent(mom, p2p);
	}


    private Boolean authentication() {
        Parent layout;
        Scene scene;
        Stage popupStage;
        FXMLLoader loader = new FXMLLoader();
        PopupAuthController popupController = new PopupAuthController(mom, p2p, contactsController, chatController);
        
        loader.setLocation(getClass().getResource(FXMLConstants.FXML_POPUP_AUTH_CONTROLLER));
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