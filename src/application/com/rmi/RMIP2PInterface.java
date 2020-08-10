package application.com.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIP2PInterface extends Remote {
	
	// Chat
	void send_chat_msg(String msg) throws RemoteException;
	
	// Game
	void move_game_piece(String mov) throws RemoteException;
	
	// Sys
	void sys_restart_request() throws RemoteException;
	void sys_restart_response_ok() throws RemoteException;
	void sys_restart_response_fail() throws RemoteException;
  
	// Connection
	void call_server_lookup(String client_ip) throws RemoteException;
	void call_peer_disconnect() throws RemoteException;
	void call_peer_test_connection() throws RemoteException;
	
}