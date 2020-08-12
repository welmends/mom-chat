package application.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.com.P2P;
import application.com.P2PConstants;
import application.com.mom.MOM;
import application.ui.constants.PopupAuthConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupAuthController implements Initializable {

	// FXML Variables
    @FXML private TextField nicknameTF;
    @FXML private TextField ipaddressTF;
    @FXML private TextField portnumberTF;
    @FXML private Button enterButton;
    
	// COM Variables
    private MOM mom;
	private P2P p2p;
	
	// Variables
    private Stage stage = null;
    private HashMap<String, String> credentials = new HashMap<String, String>();
    
    private ContactsController contacts;
    private ChatController chat;
    
    public PopupAuthController(MOM mom, P2P p2p, ContactsController contacts, ChatController chat) {
    	this.mom = mom;
    	this.p2p = p2p;
    	this.contacts = contacts;
    	this.chat = chat;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	setEnterBtnPressedBehavior();
    }
	
    private void setEnterBtnPressedBehavior() {
    	enterButton.setOnAction((event)->{
        	disableComponents(true);
        	acquireCredentials();
        	String nickname     = credentials.get(PopupAuthConstants.HASHCODE_NICKNAME);
        	String ip_address   = credentials.get(PopupAuthConstants.HASHCODE_IPADDRESS);
        	Integer port_number = Integer.valueOf(credentials.get(PopupAuthConstants.HASHCODE_PORTNUMBER));
        	String mom_url      = "tcp://"+ip_address+":61616";
        	
        	// MOM Connection
        	mom.setup(mom_url, nickname);
        	
        	// P2P Connection
        	p2p.setup(ip_address, port_number);
        	p2p.set_technology(P2PConstants.RMI);
        	closeStage();
        });
    }
    
    private void disableComponents(Boolean b) {
    	enterButton.setDisable(b);
    	nicknameTF.setDisable(b);
    	ipaddressTF.setDisable(b);
    	portnumberTF.setDisable(b);
    }
    
    private void acquireCredentials() {
    	credentials.clear();
    	if(nicknameTF.getText().equals("")) {
    		credentials.put(PopupAuthConstants.HASHCODE_NICKNAME, PopupAuthConstants.DEFAULT_NICKNAME);
    	}else {
    		credentials.put(PopupAuthConstants.HASHCODE_NICKNAME, nicknameTF.getText());
    	}
    	if(ipaddressTF.getText().equals("")) {
    		credentials.put(PopupAuthConstants.HASHCODE_IPADDRESS, PopupAuthConstants.DEFAULT_IPADDRESS);
    	}else {
    		credentials.put(PopupAuthConstants.HASHCODE_IPADDRESS, ipaddressTF.getText());
    	}
    	if(portnumberTF.getText().equals("")) {
    		credentials.put(PopupAuthConstants.HASHCODE_PORTNUMBER, PopupAuthConstants.DEFAULT_PORTNUMBER);
    	}else {
    		credentials.put(PopupAuthConstants.HASHCODE_PORTNUMBER, portnumberTF.getText());
    	}
    	return;
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    private void closeStage() {
    	chat.start();
        if(stage!=null) {
            stage.close();
        }
    }

}