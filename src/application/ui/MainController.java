package application.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.com.P2P;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class MainController implements Initializable {
	
	// FXML Variables
	@FXML AnchorPane root;
	@FXML HBox mainHBox;
	@FXML AnchorPane loginAnchorPane;
	
	// P2P (Socket or RMI)
	P2P p2p;
	
	// FXML Loaders
	FXMLLoader contactsLoader;
	FXMLLoader chatLoader;
	FXMLLoader loginLoader;
	
	// Controllers
	ContactsController contactsController;
	ChatController chatController;
	LoginController loginController;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Initialize Objects
		p2p = new P2P();
		
		Scene contactsScene = null;
		Scene chatScene = null;
		Scene loginScene = null;
		
		contactsLoader = new FXMLLoader(getClass().getResource("/application/scenes/contacts_scene.fxml"));
		chatLoader = new FXMLLoader(getClass().getResource("/application/scenes/chat_scene.fxml"));
		loginLoader = new FXMLLoader(getClass().getResource("/application/scenes/login_scene.fxml"));
		
		//Load Scenes
		try {
			contactsScene = new Scene(contactsLoader.load());
			chatScene = new Scene(chatLoader.load());
			loginScene = new Scene(loginLoader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Add nodes to MainController holders 
		mainHBox.getChildren().add(contactsScene.getRoot());
		mainHBox.getChildren().add(chatScene.getRoot());
		loginAnchorPane.getChildren().add(loginScene.getRoot());
		
		// Get Controller
		contactsController = contactsLoader.getController();
		chatController = chatLoader.getController();
		loginController = loginLoader.getController();
		
		// Load common objects from parent
		loginController.loadFromParent(p2p, chatController, mainHBox, loginAnchorPane);
		contactsController.loadFromParent(p2p);
		chatController.loadFromParent(p2p);
	}
}