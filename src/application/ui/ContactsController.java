package application.ui;

import java.net.URL;
import java.util.ResourceBundle;

import application.com.P2P;
import javafx.fxml.Initializable;

public class ContactsController implements Initializable  {
	
	// P2P (Socket or RMI)
	P2P p2p;
	
	public void loadFromParent(P2P p2p) {
		this.p2p = p2p;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
}