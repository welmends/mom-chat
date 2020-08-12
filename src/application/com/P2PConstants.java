package application.com;

public class P2PConstants {
	
	// Technologies
	public static final String RMI = "RMI";
	
	// RMI P2P Type - example: 'rmi://127.0.0.1:9999/Bizingo'
	public static String BIZINGO_RMI_NAME = "Bizingo";
	public static String CHAT_RMI_SERVER_NAME = BIZINGO_RMI_NAME + "-Server";
	public static String CHAT_RMI_CLIENT_NAME = BIZINGO_RMI_NAME + "-Client";
	
	public static Integer THREAD_SLEEP_TIME_MILLIS = 1000;
}
