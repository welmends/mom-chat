package application.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import application.com.P2P;
import application.com.P2PConstants;
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
	private HashMap<String, P2P> p2ps;
	
	// Controllers
	private ChatController chat;
	
	// Variables
	private List<String> contactsNicknames;
	private List<Button> contactsButtons;
	private List<Circle> contactsCircles;
	
	public void loadFromParent(MOM mom, HashMap<String, P2P> p2ps, ChatController chat) {
		this.mom = mom;
		this.p2ps = p2ps;
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
			String new_contact_nickname = add_tf.getText();//*** Verify if its valid
			p2ps.put(new_contact_nickname, new P2P());
			p2ps.get(new_contact_nickname).setup(mount_rmi_name(mom.nickname, new_contact_nickname), mom.ip, mom.port);
			p2ps.get(new_contact_nickname).set_technology(P2PConstants.RMI);
			
			HBox h = new HBox();
			VBox v = new VBox();
			Button b = new Button();
			Circle c = new Circle();
			
			c.setRadius(ConfigConstants.CICLE_STATUS_RADIUS);
			c.setStrokeWidth(ConfigConstants.CICLE_STATUS_STROKE);
			c.setStroke(ConfigConstants.COLOR_STROKE);
			c.setFill(ConfigConstants.COLOR_UNKNOWN);
			
			b.setText(new_contact_nickname);
			b.setPrefWidth(ConfigConstants.CONTACT_BUTTON_PREF_WIDTH);
			setContactBtnPressedBehavior(b);
			
			v.getChildren().add(c);
			v.setPadding(ConfigConstants.PADDING_CONTACT_CIRCLE_VBOX);
			
			h.setPadding(ConfigConstants.PADDING_CONTACT_HBOX);
			h.getChildren().addAll(b, v);
			
			contactsNicknames.add(new_contact_nickname);
			contactsButtons.add(b);
			contactsCircles.add(c);

	        contactsVBoxOnScroll.getChildren().addAll(h);
	        contactsVBoxOnScroll.applyCss();
	        contactsVBoxOnScroll.layout();
	        
	        add_tf.setText("");
        });
    }
	
	private void setContactBtnPressedBehavior(Button b) {
		b.setOnAction((event)->{
			// Disconnect
			if (mom.is__online() && p2ps.containsKey(mom.get_contact_nickname()) && p2ps.get(mom.get_contact_nickname()).is_connected()) {
				p2ps.get(mom.get_contact_nickname()).disconnect();
        	}
    		// Select contact
			for (int i=0; i<contactsButtons.size(); i++) {
				if(contactsButtons.get(i).equals(b)) {
					contactsCircles.get(i).setFill(ConfigConstants.COLOR_ONLINE);
					mom.set_contact_nickname(contactsNicknames.get(i));
					chat.chatLabel.setText(mom.get_contact_nickname());
					chat.clearChat();
					//***Loads chat history
					if (mom.is__online() && p2ps.containsKey(mom.get_contact_nickname()) && !p2ps.get(mom.get_contact_nickname()).is_connected()) {
						p2ps.get(mom.get_contact_nickname()).connect();
					}
	        		break;
				}
			}
        });
	}
	
	private void setPowerBtnPressedBehavior() {
		power_btn.setOnAction((event)->{
        	if (mom.is__online()) {
        		getOffline();
        	} else {
        		getOnline();
    			if (p2ps.containsKey(mom.get_contact_nickname()) && !p2ps.get(mom.get_contact_nickname()).is_connected()) {
    				p2ps.get(mom.get_contact_nickname()).connect();
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
	
	private void getOnline() {
		mom.set_online(true);
		on_circle.setFill(ConfigConstants.COLOR_ONLINE);
		off_circle.setFill(ConfigConstants.COLOR_UNKNOWN);
	}
	
	private void getOffline() {
		mom.set_online(false);
		on_circle.setFill(ConfigConstants.COLOR_UNKNOWN);
		off_circle.setFill(ConfigConstants.COLOR_OFFLINE);
		
        for (String key : p2ps.keySet()) {
            p2ps.get(key).disconnect();
        }
	}
	
	private String mount_rmi_name(String nick1, String nick2) {
		String rmi_name;
		Boolean order;
		int size;
		int value1, value2;
		
		if (nick1.length()<=nick2.length()) {
			order = true;
			size = nick1.length();
		}else {
			order = false;
			size = nick2.length();
		}
		
		for (int i=0; i<size; i++) {
			value1 = (int) nick1.charAt(i);
			value2 = (int) nick2.charAt(i);
			if(value1==value2) {
				continue;
			}
			else if (value1<value2) {
				order = true;
				break;
			}
			else {
				order = false;
				break;
			}
		}
		
		if (order) {
			rmi_name = nick1+nick2;
		} else {
			rmi_name = nick2+nick1;
		}
		return rmi_name;
	}
}