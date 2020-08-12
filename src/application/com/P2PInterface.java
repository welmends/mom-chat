package application.com;

public interface P2PInterface {
	
	// Technology
	public void set_technology(final String technology_name);
	public String get_technology_name();
	
	// Connection
	public void setup(String ip, int port);
	public void setup(String ip, String local_ip, int port);
	public Boolean connect();
	public Boolean disconnect();
	
	// Thread
	public void thread_call();
	
	// Getters
	public String get_peer_type();
	public String get_ip_address();
	public Integer get_port_number();
	public Boolean is_server();
	public Boolean is_client();
	public Boolean is_active();
	public Boolean is_connected();
	
	// Chat Stack Full
	public Boolean chat_stack_full();
	
	// Chat Getter
	public String get_chat_msg();
	
	// Calls
	public void send_chat_msg_call(String msg);
}
