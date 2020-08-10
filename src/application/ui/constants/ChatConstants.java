package application.ui.constants;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;

public class ChatConstants {
	// Label
	public static String TEXT_LABEL_CHAT = "C H A T";
	
	// Components Style
	public static String STYLE_SCROLL_PANE_CHAT = "-fx-background-color:#d8e2eb; -fx-background-radius: 10 10 10 10; -fx-border-color: #7894ac; -fx-border-width: 3; -fx-border-radius: 10 10 10 10;";
	public static String STYLE_VBOX_CHAT = "-fx-background-color:#d8e2eb;";
	
	// Sleep
	public static Integer THREAD_SLEEP_TIME_MILLIS = 100;
	
	// Time Label
	public static String SPACE_FOR_LABEL_TIME = "          ";
	public static String LABEL_TIME_SIMPLE_DATE_FORMAT = "hh:mm a";
	public static Font LABEL_TIME_FONT = Font.font("System", FontPosture.ITALIC, 9);
	public static Insets PADDING_LABEL_TIME = new Insets(0,6,2,0);
	public static TextAlignment TEXT_ALIGNMENT_LABEL_TIME = TextAlignment.RIGHT;
	public static Pos ALIGNMENT_STACK_PANE_LABEL_TIME = Pos.BOTTOM_RIGHT;
	
	// Message Receive
	public static Color COLOR_LABEL_TEXT_RECEIVE = Color.BLACK;
	public static String STYLE_LABEL_TEXT_RECEIVE = "-fx-font-weight:bold; -fx-background-color: #ffffff; -fx-background-radius: 0 20 20 20;";
	public static Insets PADDING_LABEL_TEXT_RECEIVE = new Insets(10, 10, 10, 10);
	public static Pos ALIGNMENT_LABEL_TEXT_RECEIVE = Pos.CENTER;
	
	public static Insets PADDING_STACK_PANE_RECEIVE = new Insets(0, 0, 5, 0);
	public static Pos ALIGNMENT_STACK_PANE_RECEIVE = Pos.CENTER_LEFT;
	
	// Message Send
	public static Color COLOR_LABEL_TEXT_SEND = Color.BLACK;
	public static String STYLE_LABEL_TEXT_SEND = "-fx-font-weight:bold; -fx-background-color: #e2ffc9; -fx-background-radius: 20 0 20 20;";
	public static Insets PADDING_LABEL_TEXT_SEND = new Insets(10, 10, 10, 10);
	public static Pos ALIGNMENT_LABEL_TEXT_SEND = Pos.CENTER;
	
	public static Insets PADDING_STACK_PANE_SEND = new Insets(0, 0, 5, 0);
	public static Pos ALIGNMENT_STACK_PANE_SEND = Pos.CENTER_RIGHT;
	
}
