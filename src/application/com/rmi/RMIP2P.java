package application.com.rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

import application.com.P2PInterface;
import application.com.P2PConstants;

public class RMIP2P extends UnicastRemoteObject implements P2PInterface, RMIP2PInterface, Runnable {
	
	private static final long serialVersionUID = 0L;
	private static RMIP2PInterface rmi_client;
	
	private Semaphore mutex;
	
    private String server_link, client_link;
    
    private String chat_msg, game_mov, sys_cmd;
    private Boolean chat_stack_full, game_stack_full, sys_stack_full;
    
    private Boolean is_connected;
    private String peer_type;
    private String ip, local_ip;
    private int port;
    
	public RMIP2P() throws RemoteException{
		super();
		
		this.mutex = new Semaphore(1);
		
		this.server_link = "";
		this.client_link = "";
		
		this.chat_stack_full = false;
		this.game_stack_full = false;
		this.sys_stack_full = false;
		
		this.is_connected = false;
		this.peer_type = "";
		this.ip = "";
		this.local_ip = "";
		this.port = -1;
	}

	// P2P Interface Implementation - Technology
	@Override
	public void set_technology(final String technology_name) {
		return;
	}
	
	@Override
	public String get_technology_name() {
		return P2PConstants.RMI;
	}
	
	// P2P Interface Implementation - Connection
	@Override
	public void setup(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public void setup(String ip, String local_ip, int port) {
		this.ip = ip;
		this.local_ip = local_ip;
		this.port = port;
	}
	
	@Override
	public Boolean connect() {
        try {
        	server_link = "rmi://"+ip+":"+String.valueOf(port)+"/"+P2PConstants.BIZINGO_RMI_SERVER_NAME;
        	int length = Naming.list(server_link).length;
        	if(length==0) {
    			peer_type = "server";
    			bind();
    			return true;
    		}
        	else if(length==1) {
        		peer_type = "client";
    			server_link = "rmi://"+local_ip+":"+String.valueOf(port)+"/"+P2PConstants.BIZINGO_RMI_CLIENT_NAME;
    			client_link = "rmi://"+ip+":"+String.valueOf(port)+"/"+P2PConstants.BIZINGO_RMI_SERVER_NAME;
    			bind();
    			lookup();
    			RMIP2P.rmi_client.call_server_lookup(local_ip);
    			return true;
    		}else {
    			return false;
    		}
        } catch(Exception e){
        	System.out.println(e);
        	return false;
        }
	}

	@Override
	public Boolean disconnect() {
		try {
			is_connected = false;
			unbind();
			RMIP2P.rmi_client.call_peer_disconnect();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// P2P Interface Implementation - Thread
	@Override
	public void thread_call() {
		return;
	}
	
	@Override
    public void run(){
		try {
            while(true){
            	Thread.sleep(P2PConstants.THREAD_SLEEP_TIME_MILLIS);
            	RMIP2P.rmi_client.call_peer_test_connection();
            }
        } catch(Exception e) {
            System.out.println(e);
            is_connected = false;
        }
    }
		
	// P2P Interface Implementation - Getters
	@Override
    public String get_peer_type() {
    	return peer_type;
    }
    
	@Override
	public String get_ip_address() {
		return local_ip;
	}
	
	@Override
	public Integer get_port_number() {
		return port;
	}
	
	@Override
    public Boolean is_server() {
    	if(peer_type.equals("server")) {
    		return true;
    	}
    	return false;
    }
    
	@Override
    public Boolean is_client() {
    	if(peer_type.equals("client")) {
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
    	Boolean stack_full = false;
		try {
			mutex.acquire();
			stack_full = chat_stack_full;
			if(stack_full == true) { chat_stack_full = false; }
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}
    	return stack_full;
    }
    
	@Override
    public Boolean game_stack_full() {
    	Boolean stack_full = false;
		try {
			mutex.acquire();
			stack_full = game_stack_full;
			if(stack_full == true) { game_stack_full = false; }
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}
    	return stack_full;
    }
    
	@Override
    public Boolean sys_stack_full() {
    	Boolean stack_full = false;
		try {
			mutex.acquire();
			stack_full = sys_stack_full;
			if(stack_full == true) { sys_stack_full = false; }
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}
    	return stack_full;
    }
    
	// P2P Interface Implementation - Bizingo Getters
	@Override
	public String get_chat_msg() {
		return chat_msg;
	}
	
	@Override
	public String get_game_mov() {
		return game_mov;
	}
	
	@Override
	public String get_sys_cmd() {
		return sys_cmd;
	}
	
	// P2P Interface Implementation - Calls
	@Override
	public void send_chat_msg_call(String msg) {
		try {
			RMIP2P.rmi_client.send_chat_msg(msg);
		} catch (RemoteException e) {
			System.out.println(e);
		}
    }
	
	@Override
	public void move_game_piece_call(String mov) {
		try {
			RMIP2P.rmi_client.move_game_piece(mov);
		} catch (RemoteException e) {
			System.out.println(e);
		}
	}
	
	@Override
	public void sys_restart_request_call() {
		try {
			RMIP2P.rmi_client.sys_restart_request();
		} catch (RemoteException e) {
			System.out.println(e);
		}
	}
	
	@Override
	public void sys_restart_response_ok_call() {
		try {
			RMIP2P.rmi_client.sys_restart_response_ok();
		} catch (RemoteException e) {
			System.out.println(e);
		}
	}
	
	@Override
	public void sys_restart_response_fail_call() {
		try {
			RMIP2P.rmi_client.sys_restart_response_fail();
		} catch (RemoteException e) {
			System.out.println(e);
		}
	}
	
	// RMI Interface Implementation
	@Override
	public void send_chat_msg(String msg) {
		chat_msg = msg;
		try {
			mutex.acquire();
			chat_stack_full = true;
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}
    }
	
	@Override
	public void move_game_piece(String mov) {
		game_mov = mov;
		try {
			mutex.acquire();
			game_stack_full = true;
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}
    }
	
	@Override
	public void sys_restart_request() {
		sys_cmd = P2PConstants.SYS_RESTART_REQUEST;
		try {
			mutex.acquire();
			sys_stack_full = true;
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@Override
	public void sys_restart_response_ok() {
		sys_cmd = P2PConstants.SYS_RESTART_RESPONSE_OK;
		try {
			mutex.acquire();
			sys_stack_full = true;
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@Override
	public void sys_restart_response_fail() {
		sys_cmd = P2PConstants.SYS_RESTART_RESPONSE_FAIL;
		try {
			mutex.acquire();
			sys_stack_full = true;
	    	mutex.release();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@Override
	public void call_server_lookup(String client_ip) {
		local_ip = client_ip;
		client_link = "rmi://"+local_ip+":"+String.valueOf(port)+"/"+P2PConstants.BIZINGO_RMI_CLIENT_NAME;
		lookup();
	}
	
	@Override
	public void call_peer_disconnect() {
		is_connected = false;
		unbind();
	}
	
	public void call_peer_test_connection() {
		return;
	}
	
	// RMI Connection Methods
	private Boolean bind() {
		try {
			Naming.bind(server_link, this);
			return true;
		} catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
	
	private Boolean unbind() {
		try {
			Naming.unbind(server_link);
			return true;
		} catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
	
	private Boolean lookup() {
		try {
			rmi_client = (RMIP2PInterface)Naming.lookup(client_link);
			is_connected = true;
			new Thread(this).start();
			return true;
		} catch(Exception e){
			System.out.println(e);
			return false;
		}
	}

}