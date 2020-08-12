package application.ui;

import java.net.URL;
import java.util.ResourceBundle;

import application.com.P2P;
import application.com.mom.MOM;
import application.ui.constants.ChatConstants;
import application.ui.constants.ConfigConstants;
import application.ui.constants.ImageConstants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class ConfigController extends Thread implements Initializable  {
	
	// FXML Variables
	@FXML HBox mainHBox;
	@FXML Button add_btn;
	@FXML Button power_btn;
	@FXML Circle on_circle;
	@FXML Circle off_circle;
	@FXML TextField add_tf;
	@FXML ScrollPane contactsScrollPane;
	@FXML VBox contactsVBoxOnScroll;
	
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
		setAddBtnPressedBehavior();
		setPowerBtnPressedBehavior();
		setVBoxScrollsBehavior();
	}
	
	@Override
	public void run() {
		return;
	}
	
	private void setupComponents() {
		on_circle.setFill(javafx.scene.paint.Color.GRAY);
		off_circle.setFill(javafx.scene.paint.Color.RED);
		
		add_btn.setGraphic(ImageConstants.ADD_BTN_ICON);
		power_btn.setGraphic(ImageConstants.POWER_BTN_ICON);
	}
	
	private void setAddBtnPressedBehavior() {
		add_btn.setOnAction((event)->{
	        Label txt = new Label(add_tf.getText());
	        StackPane sp = new StackPane();
	        sp.getChildren().add(txt);
	        contactsVBoxOnScroll.getChildren().addAll(sp);
	        contactsVBoxOnScroll.applyCss();
	        contactsVBoxOnScroll.layout();
        });
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
	
	private void setVBoxScrollsBehavior() {
		contactsVBoxOnScroll.heightProperty().addListener(new ChangeListener<Number>() {

	        @Override
	        public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
	        	if(arg1.intValue()!=0) {
	        		contactsScrollPane.setVvalue(1.0);
	        	}
	        }
	        
		});
	}
}