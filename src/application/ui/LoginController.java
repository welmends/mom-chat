package application.ui;

import java.net.URL;
import java.util.ResourceBundle;

import application.com.P2P;
import application.com.P2PConstants;
import application.ui.constants.FontConstants;
import application.ui.constants.LoginConstants;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class LoginController extends Thread implements Initializable {
	
	// FXML Variables
	@FXML VBox loginVBox;
	@FXML Label ipLabel;
	@FXML TextField ipTextField;
	@FXML Label portLabel;
	@FXML TextField portTextField;
	@FXML Button connectButton;
	@FXML Label technologyLabel;
	@FXML ToggleButton socketTButton;
	@FXML ToggleButton rmiTButton;
	
	// P2P (Socket or RMI)
	P2P p2p;
	
	// Fading nodes
	AnchorPane node1;
	HBox node2;
	
	// Controllers
	ChatController chat;
	
	public void loadFromParent(P2P p2p, ChatController chat, HBox mainHBox, AnchorPane loginAnchorPane) {
		this.p2p = p2p;
		
		this.chat = chat;
		
		this.node1 = loginAnchorPane;
		this.node2 = mainHBox;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Setup components
		setupComponents();
		
		// Buttons Pressed Behavior
		setButtonsPressedBehavior();
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(LoginConstants.THREAD_SLEEP_TIME_MILLIS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(p2p.has_connection()==true) {
				break;
			}
		}
		// Transition to game
		startGame();
	}
	
	private void setupComponents() {
		ipLabel.setText(LoginConstants.TEXT_LABEL_IP);
		ipLabel.setFont(FontConstants.sixty14p);
		
		portLabel.setText(LoginConstants.TEXT_LABEL_PORT);
		portLabel.setFont(FontConstants.sixty14p);
		
		portLabel.setText(LoginConstants.TEXT_LABEL_PORT);
		portLabel.setFont(FontConstants.sixty14p);
		
		connectButton.setText(LoginConstants.TEXT_BUTTON_CONNECTION);
		connectButton.setFont(FontConstants.sixty14p);
		
		socketTButton.setText(LoginConstants.TEXT_TBUTTON_SOCKET);
		socketTButton.setFont(FontConstants.sixty14p);
		socketTButton.setSelected(true);
		
		rmiTButton.setText(LoginConstants.TEXT_TBUTTON_RMI);
		rmiTButton.setFont(FontConstants.sixty14p);
		rmiTButton.setSelected(false);
	}
	
	private void setButtonsPressedBehavior() {
		connectButton.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){

	        @Override
	        public void handle(MouseEvent event) {
	        	tryConnection();
	        }
		});
		
		connectButton.setOnKeyPressed(new EventHandler<KeyEvent>(){
			
	        @Override
	        public void handle(KeyEvent key){
	            if (key.getCode().equals(KeyCode.ENTER)){
	            	tryConnection();
	            }
	        }
	        
	    });
		
		socketTButton.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){

	        @Override
	        public void handle(MouseEvent event) {
	        	if(socketTButton.isSelected()) {
	        		rmiTButton.setSelected(true);
	        	}
	        	else {
	        		rmiTButton.setSelected(false);
	        	}
	        }
	        
		});
		
		rmiTButton.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){

	        @Override
	        public void handle(MouseEvent event) {
	        	if(rmiTButton.isSelected()) {
	        		socketTButton.setSelected(true);
	        	}
	        	else {
	        		socketTButton.setSelected(false);
	        	}
	        }
	        
		});
	}
	
	public void alertLoginInformation() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Notification");
		alert.setResizable(false);
		alert.setHeaderText("Aguardando novo jogador!");
		alert.showAndWait();
	}
	
	public void alertLoginError() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Notification");
		alert.setResizable(false);
		alert.setHeaderText("Conexão mal sucedida!");
		alert.showAndWait();
	}
	
	public void alertLoginWarning() {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Notification");
		alert.setResizable(false);
		alert.setHeaderText("Preencha os campos informando IP/PORTA!");
		alert.showAndWait();
	}
	
	private void tryConnection() {
		if(ipTextField.getText().length()>0 && portTextField.getText().length()>0) {
    		connectButton.setDisable(true);
    		ipTextField.setDisable(true);
    		portTextField.setDisable(true);
    		socketTButton.setDisable(true);
    		rmiTButton.setDisable(true);
    		
    		if(socketTButton.isSelected()) {
    			p2p.set_technology(P2PConstants.SOCKET);
    		}else {
    			p2p.set_technology(P2PConstants.RMI);
    		}
    		
    		p2p.setup(ipTextField.getText(), Integer.valueOf(portTextField.getText()));
    		
    		if(p2p.connect()==true) {
    			if(p2p.is_client()) {
    				// Transition to game
    				startGame();
    			}else {
    				alertLoginInformation();
	        		
    				// Wait for connection
    				p2p.thread_call();
    				
    				// Trigger for client connection
    				start();
    			}
    		}else {
    			alertLoginError();
    	        Platform.exit();
    	        System.exit(0);
    		}
    		
    	}else {
    		alertLoginWarning();
    	}
	}
	
	private void startGame() {
		FadeTransition fadeTransition1 = new FadeTransition(Duration.millis(LoginConstants.FADING_TIME_MILLIS), node1);
		FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(LoginConstants.FADING_TIME_MILLIS), node2);
		node2.setVisible(false);
		fadeTransition1.setFromValue(1);
		fadeTransition1.setToValue(0);
		fadeTransition1.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
			    Platform.runLater(new Runnable() {
			        @Override
			        public void run() {
			        }
			    });
			    node1.setVisible(false);
				node2.setVisible(true);
				node2.setOpacity(0);
				fadeTransition2.setFromValue(0);
				fadeTransition2.setToValue(1);
				fadeTransition2.play();
				fadeTransition2.setOnFinished(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						// Trigger to enable message receive
						chat.start();
					}
				});
			}
		});
		fadeTransition1.play();
	}

}