package application.com.socket;

import java.net.*;
import java.util.concurrent.Semaphore;

import application.com.P2PInterface;
import application.com.P2PConstants;

import java.io.*;

public class SocketP2P implements P2PInterface, Runnable {
	
	private Semaphore mutex;
	
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    
    private DataInputStream input_stream  = null;
    private DataOutputStream output_stream = null;

    private String message_input= "";
    private String message_output= "";
    
    private int thread_action;
    
    private Boolean is_connected;
    private String peer_type;
    private String ip;
    private int port;
    
    public SocketP2P() {
    	mutex = new Semaphore(1);
    	
    	this.thread_action = 1;
    	
    	this.is_connected = false;
    	this.peer_type = "";
		this.ip = "";
		this.port = -1;
    }

    // P2P Interface Implementation - Technology
	@Override
	public void set_technology(final String technology_name) {
		return;
	}
	
 	@Override
 	public String get_technology_name() {
 		return P2PConstants.SOCKET;
 	}
    
 	// P2P Interface Implementation - Connection
 	@Override
    public void setup(String ip, int port) {
        this.ip   = ip;
        this.port = port;
    }
 	
	@Override
	public void setup(String ip, String local_ip, int port) {
		this.ip = ip;
		this.port = port;
	}
 	
 	@Override
    public Boolean connect(){
        try {
        	this.peer_type = "server";
        	
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ip));
            
            return true;
        } catch(Exception e_server){
        	System.out.println(e_server);
        	try {
        		this.peer_type = "client";
        		
        		socket = new Socket(InetAddress.getByName(ip), port);
        		
        		input_stream = new DataInputStream(socket.getInputStream());
        		output_stream = new DataOutputStream(socket.getOutputStream());
        		
        		thread_action=2;// Flag receive behavior
        		
        		is_connected = true;
        		
        		new Thread(this).start();
        		
        		return true;
        	} catch(Exception e_client) {
        		System.out.println(e_client);
        		return false;
        	}
        }
    }
    
 	@Override
    public Boolean disconnect(){
    	try {
			socket.close();
			
			if(is_server()) {
				serverSocket.close();
			}
			
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
    }
 	
    public Boolean wait_connection() {
    	try {
            socket = serverSocket.accept();
            serverSocket.close();
            
            input_stream = new DataInputStream(socket.getInputStream());
            output_stream = new DataOutputStream(socket.getOutputStream());
            
            thread_action = 2;// Flag receive behavior
            
            is_connected = true;
            
            return true;
    	} catch(Exception e) {
    		System.out.println(e);
    		return false;
    	}

    }
    
 	// P2P Interface Implementation - Thread
 	@Override
 	public void thread_call() {
 		new Thread(this).start();
 	}

 	@Override
    public void run(){
    	if(thread_action==1) {
    		wait_connection();
    	}
    	if(thread_action==2) {
    		try {
                while(true){
                    String message_received = input_stream.readUTF();
                    mutex.acquire();
                    message_input = message_received;
                    mutex.release();
                }
            } catch(Exception e) {
                System.out.println(e);
                is_connected = false;
            }
    	}
    }
    
    // P2P Interface Implementation - Getters
    @Override
    public String get_peer_type() {
    	return this.peer_type;
    }
    
    @Override
    public String get_ip_address() {
    	return this.ip;
    }
    
    @Override
    public Integer get_port_number() {
    	return this.port;
    }
    
    @Override
    public Boolean is_server() {
    	if(this.peer_type.equals("server")) {
    		return true;
    	}
    	return false;
    }
    
    @Override
    public Boolean is_client() {
    	if(this.peer_type.equals("client")) {
    		return true;
    	}
    	return false;
    }
    
    @Override
    public Boolean has_connection() {
    	return is_connected;
    }
    
    // P2P Interface Implementation - Bizingo Stack Full
    @Override
    public Boolean chat_stack_full() {
    	String message_received = "";
		try {
			mutex.acquire();
			message_received = message_input;
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}

    	if(message_received.length()>0) {
    		if(P2PConstants.CHAT_CODEC.equals(message_received.substring(0,P2PConstants.CHAT_CODEC.length()))) {
    			return true;
    		}else {
    			return false;
    		}
    	}else {
    		return false;
    	}
    }
    
    @Override
    public Boolean game_stack_full() {
    	String message_received = "";
		try {
			mutex.acquire();
			message_received = message_input;
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}

    	if(message_received.length()>0) {
    		if(P2PConstants.GAME_CODEC.equals(message_received.substring(0,P2PConstants.GAME_CODEC.length()))) {
    			return true;
    		}else {
    			return false;
    		}
    	}else {
    		return false;
    	}
    }
    
    @Override
    public Boolean sys_stack_full() {
    	String message_received = "";
		try {
			mutex.acquire();
			message_received = message_input;
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}

    	if(message_received.length()>0) {
    		if(P2PConstants.SYS_CODEC.equals(message_received.substring(0,P2PConstants.SYS_CODEC.length()))) {
    			return true;
    		}else {
    			return false;
    		}
    	}else {
    		return false;
    	}
    }
    
	// P2P Interface Implementation - Bizingo Getters
	@Override
	public String get_chat_msg() {
    	String message_received = "";
		try {
			mutex.acquire();
			message_received = message_input;
			message_input = "";
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}
    	return message_received.substring(P2PConstants.CHAT_CODEC.length(),message_received.length());
	}

	@Override
	public String get_game_mov() {
    	String message_received = "";
		try {
			mutex.acquire();
			message_received = message_input;
			message_input = "";
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}
    	return message_received.substring(P2PConstants.GAME_CODEC.length(),message_received.length());
	}

	@Override
	public String get_sys_cmd() {
    	String message_received = "";
		try {
			mutex.acquire();
			message_received = message_input;
			message_input = "";
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}
    	return message_received.substring(P2PConstants.SYS_CODEC.length(),message_received.length());
	}
	
	// P2P Interface Implementation - Calls
	@Override
	public void send_chat_msg_call(String msg) {
		try {
			message_output = P2PConstants.CHAT_CODEC + msg;
			output_stream.writeUTF(message_output);
			output_stream.flush();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void move_game_piece_call(String mov) {
		try {
			message_output = P2PConstants.GAME_CODEC + mov;
			output_stream.writeUTF(message_output);
			output_stream.flush();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void sys_restart_request_call() {
		try {
			message_output = P2PConstants.SYS_CODEC + P2PConstants.SYS_RESTART_REQUEST;
			output_stream.writeUTF(message_output);
			output_stream.flush();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void sys_restart_response_ok_call() {
		try {
			message_output = P2PConstants.SYS_CODEC + P2PConstants.SYS_RESTART_RESPONSE_OK;
			output_stream.writeUTF(message_output);
			output_stream.flush();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void sys_restart_response_fail_call() {
		try {
			message_output = P2PConstants.SYS_CODEC + P2PConstants.SYS_RESTART_RESPONSE_FAIL;
			output_stream.writeUTF(message_output);
			output_stream.flush();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
    
}

