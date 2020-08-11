package application.ui;

import java.net.URL;
import java.util.ResourceBundle;

import application.com.P2P;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class ContactsController implements Initializable  {
	
	// FXML Variables
	@FXML VBox mainVBox;
	
	// P2P (Socket or RMI)
	P2P p2p;
	
	public void loadFromParent(P2P p2p) {
		this.p2p = p2p;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}