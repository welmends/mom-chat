package application.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.com.P2P;
import application.com.P2PConstants;
import application.ui.constants.PopupAuthConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupAuthController extends Thread implements Initializable {

	// FXML Variables
    @FXML private TextField nicknameTF;
    @FXML private TextField ipaddressTF;
    @FXML private TextField portnumberTF;
    @FXML private Button connectBtn;
    
	// P2P (Socket or RMI)
	private P2P p2p;
	
	// Variables
    private Stage stage = null;
    private HashMap<String, String> credentials = new HashMap<String, String>();
    
    private ContactsController contacts;
    private ChatController chat;
    
    public PopupAuthController(P2P p2p, ContactsController contacts, ChatController chat) {
    	this.p2p = p2p;
    	this.contacts = contacts;
    	this.chat = chat;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	setConnectBtnPressedBehavior();
    }
    
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(PopupAuthConstants.THREAD_SLEEP_TIME_MILLIS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(p2p.has_connection()==true) {
				break;
			}
		}
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	closeStage();
		    }
		});
	}
	
    private void setConnectBtnPressedBehavior() {
        connectBtn.setOnAction((event)->{
        	disableComponents(true);
        	acquireCredentials();
        	//credentials.get(PopupAuthConstants.HASHCODE_NICKNAME);
        	String ip_address = credentials.get(PopupAuthConstants.HASHCODE_IPADDRESS);
        	Integer port_number = Integer.valueOf(credentials.get(PopupAuthConstants.HASHCODE_PORTNUMBER));
        	
        	// P2P Connection
        	p2p.setup(ip_address, port_number);
        	p2p.set_technology(P2PConstants.SOCKET);
    		if(p2p.connect()==true) {
    			if(p2p.is_client()) {
    				closeStage();
    			}else {
    				//*** alertLoginInformation();
	        		
    				// Wait for connection
    				p2p.thread_call();
    				
    				// Trigger for client connection
    				start();
    			}
    		}else {
    			//*** alertLoginError();
    	        Platform.exit();
    	        System.exit(0);
    		}
        });
    }
    
    private void disableComponents(Boolean b) {
    	connectBtn.setDisable(b);
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