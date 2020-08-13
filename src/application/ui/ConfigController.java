package application.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.com.P2P;
import application.com.mom.MOM;
import application.ui.constants.ConfigConstants;
import application.ui.constants.ImageConstants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
	
	// Controllers
	private ChatController chat;
	
	// Variables
	private List<String> contactsNicknames;
	private List<Button> contactsButtons;
	private List<Circle> contactsCircles;
	
	public void loadFromParent(MOM mom, P2P p2p, ChatController chat) {
		this.mom = mom;
		this.p2p = p2p;
		this.chat = chat;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Initialize Objects
		contactsNicknames = new ArrayList<String>();
		contactsButtons   = new ArrayList<Button>();
		contactsCircles   = new ArrayList<Circle>();
		
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
		on_circle.setFill(ConfigConstants.COLOR_UNKNOWN);
		off_circle.setFill(ConfigConstants.COLOR_OFFLINE);
		
		add_btn.setGraphic(ImageConstants.ADD_BTN_ICON);
		power_btn.setGraphic(ImageConstants.POWER_BTN_ICON);
	}
	
	private void setAddBtnPressedBehavior() {
		add_btn.setOnAction((event)->{
			String nickname = add_tf.getText();//*** Verify if its valid
			
			HBox h = new HBox();
			VBox v = new VBox();
			Button b = new Button();
			Circle c = new Circle();
			
			c.setRadius(ConfigConstants.CICLE_STATUS_RADIUS);
			c.setStrokeWidth(ConfigConstants.CICLE_STATUS_STROKE);
			c.setStroke(ConfigConstants.COLOR_STROKE);
			c.setFill(ConfigConstants.COLOR_UNKNOWN);
			
			b.setText(nickname);
			b.setPrefWidth(ConfigConstants.CONTACT_BUTTON_PREF_WIDTH);
			setContactBtnPressedBehavior(b);
			
			v.getChildren().add(c);
			v.setPadding(ConfigConstants.PADDING_CONTACT_CIRCLE_VBOX);
			
			h.setPadding(ConfigConstants.PADDING_CONTACT_HBOX);
			h.getChildren().addAll(b, v);
			
			contactsNicknames.add(nickname);
			contactsButtons.add(b);
			contactsCircles.add(c);

	        contactsVBoxOnScroll.getChildren().addAll(h);
	        contactsVBoxOnScroll.applyCss();
	        contactsVBoxOnScroll.layout();
        });
    }
	
	private void setContactBtnPressedBehavior(Button b) {
		b.setOnAction((event)->{
			for (int i=0; i<contactsButtons.size(); i++) {
				if(contactsButtons.get(i).equals(b)) {
					chat.chatLabel.setText(contactsButtons.get(i).getText());
					contactsCircles.get(i).setFill(ConfigConstants.COLOR_ONLINE);
				}
			}
        });
	}
	
	private void setPowerBtnPressedBehavior() {
		power_btn.setOnAction((event)->{
        	if (p2p.is_active()) {
        		if(p2p.disconnect()) {
            		on_circle.setFill(ConfigConstants.COLOR_UNKNOWN);
            		off_circle.setFill(ConfigConstants.COLOR_OFFLINE);
        		}
        	} else {
        		if(p2p.connect()) {
        			on_circle.setFill(ConfigConstants.COLOR_ONLINE);
            		off_circle.setFill(ConfigConstants.COLOR_UNKNOWN);
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