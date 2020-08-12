package application.ui;

import java.net.URL;
import java.util.ResourceBundle;

import application.com.P2P;
import application.com.mom.MOM;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class ContactsController implements Initializable  {
	
	// FXML Variables
	@FXML VBox mainVBox;
	@FXML Button power_btn;
	@FXML Circle on_circle;
	@FXML Circle off_circle;
	
	// COM Variables
	private MOM mom;
	private P2P p2p;
	
	// Variables
	private Boolean is_connected;
	
	public void loadFromParent(MOM mom, P2P p2p) {
		this.mom = mom;
		this.p2p = p2p;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.is_connected = true;
		on_circle.setFill(javafx.scene.paint.Color.GREEN);
		off_circle.setFill(javafx.scene.paint.Color.GRAY);
		setPowerBtnPressedBehavior();
	}
	
	private void setPowerBtnPressedBehavior() {
		power_btn.setOnAction((event)->{
        	if (is_connected) {
        		this.is_connected = false;
        		on_circle.setFill(javafx.scene.paint.Color.GRAY);
        		off_circle.setFill(javafx.scene.paint.Color.RED);
        		p2p.disconnect();
        	} else {
        		this.is_connected = true;
        		on_circle.setFill(javafx.scene.paint.Color.GREEN);
        		off_circle.setFill(javafx.scene.paint.Color.GRAY);
        		p2p.connect();
        	}
        });
    }
}