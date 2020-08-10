package application.com;

public class P2PConstants {
	
	// Technology
	public static final String SOCKET = "SOCKET";
	public static final String RMI = "RMI";
	
	// Socket P2P Type
	public static String CHAT_CODEC = "#C$";
	public static String GAME_CODEC = "#G$";
	public static String SYS_CODEC = "#S$";
	
	// RMI P2P Type - example: 'rmi://127.0.0.1:9999/Bizingo'
	public static String BIZINGO_RMI_NAME = "Bizingo";
	public static String BIZINGO_RMI_SERVER_NAME = BIZINGO_RMI_NAME + "-Server";
	public static String BIZINGO_RMI_CLIENT_NAME = BIZINGO_RMI_NAME + "-Client";
	
	// Both Types
	public static String SYS_RESTART_REQUEST = "restart";
	public static String SYS_RESTART_RESPONSE_OK = "restart_ok";
	public static String SYS_RESTART_RESPONSE_FAIL = "restart_fail";
	
	public static Integer THREAD_SLEEP_TIME_MILLIS = 1000;
}
