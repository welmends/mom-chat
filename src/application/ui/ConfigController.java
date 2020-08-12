package application.ui;

import java.net.URL;
import java.util.ResourceBundle;

import application.com.P2P;
import application.com.mom.MOM;
import application.ui.constants.ImageConstants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class ConfigController extends Thread implements Initializable  {
	
	// FXML Variables
	@FXML VBox mainVBox;
	@FXML Button power_btn;
	@FXML Circle on_circle;
	@FXML Circle off_circle;
	
	// COM Variables
	private MOM mom;
	private P2P p2p;
	
	public void loadFromParent(MOM mom, P2P p2p) {
		this.mom = mom;
		this.p2p = p2p;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setupComponents();
		setPowerBtnPressedBehavior();
	}
	
	@Override
	public void run() {
		return;
	}
	
	private void setupComponents() {
		on_circle.setFill(javafx.scene.paint.Color.GRAY);
		off_circle.setFill(javafx.scene.paint.Color.RED);
		
		power_btn.setGraphic(ImageConstants.POWER_BTN_ICON);
	}
	
	private void setPowerBtnPressedBehavior() {
		power_btn.setOnAction((event)->{
        	if (p2p.is_active()) {
        		if(p2p.disconnect()) {
            		on_circle.setFill(javafx.scene.paint.Color.GRAY);
            		off_circle.setFill(javafx.scene.paint.Color.RED);
        		}
        	} else {
        		if(p2p.connect()) {
        			on_circle.setFill(javafx.scene.paint.Color.GREEN);
            		off_circle.setFill(javafx.scene.paint.Color.GRAY);
        		}
        	}
        });
    }
}