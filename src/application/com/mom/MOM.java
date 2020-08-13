package application.com.mom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MOM {
	
	public Boolean set;
	public String url;
	public String nickname;
	public String ip;
	public int port;
	
	private Boolean is_online;
	private String contactNickname;
	
	Semaphore mutex = new Semaphore(1);
	
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private Destination destination;
	private MessageConsumer consumer;
	private MessageProducer producer;
	private Message message;
	private TextMessage text_message;
	
	public MOM() {
		this.set = false;
		this.url = "";
		this.nickname = "";
		this.ip = "";
		this.port = -1;
		
		this.is_online = false;
		this.contactNickname = "";
		
		this.mutex = new Semaphore(1);
	}
	
	public Boolean is__online() {
		try {
			Boolean b;
			mutex.acquire();
			b = is_online;
			mutex.release();
			return b;
		} catch (InterruptedException e) {}
		return false;
	}
	
	public void set_online(Boolean b) {
		try {
			mutex.acquire();
			is_online = b;
			mutex.release();
		} catch (InterruptedException e) {}
	}
	
	public String get_contact_nickname() {
		try {
			String s;
			mutex.acquire();
			s = contactNickname;
			mutex.release();
			return s;
		} catch (InterruptedException e) {}
		return "";
	}
	
	public void set_contact_nickname(String s) {
		try {
			mutex.acquire();
			contactNickname = s;
			mutex.release();
		} catch (InterruptedException e) {}
	}
	
	public void setup(String url, String nickname, String ip, int port) {
		this.url = url;
		this.nickname = nickname;
		this.ip = ip;
		this.port = port;
		//Create queue
		try {
			//Connection
			this.connectionFactory = new ActiveMQConnectionFactory(this.url);
			this.connection = this.connectionFactory.createConnection();
			this.connection.start();
			//Session
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.session.createQueue(this.nickname);
		} catch (JMSException e) {
			this.set = false;
		}
		this.set = true;
	}
	
	public void send(String queue, String msg) {
		if (this.set != true) {
			return;
		}
		try {
			open_connection(this.url, queue);
			this.producer = session.createProducer(this.destination);
			this.text_message = this.session.createTextMessage(msg);
			this.producer.send(this.text_message);
			this.producer.close();
			close_connection();
		} catch (JMSException e) {}
	}
	
	public String receive() {
		if (this.set != true) {
			return "err:-1";
		}
		try {
			open_connection(this.url, this.nickname);
			this.consumer = this.session.createConsumer(this.destination);
			this.text_message = (TextMessage) this.consumer.receiveNoWait();
			String msg = this.text_message.getText();
			this.consumer.close();
			close_connection();
			return msg;
		} catch (JMSException e) {
			return "err:-2";
		}
	}
	
	public List<String> receiveQueue() {
		List<String> queue = new ArrayList<String>();
		if (this.set != true) {
			return queue;
		}
		try {
			open_connection(this.url, this.nickname);
			this.consumer = this.session.createConsumer(this.destination);
			while(true) {
				this.message = this.consumer.receive(MOMConstants.RECEIVE_TIMEOUT_MILLIS);
				if (this.message==null) {
					break;
				}
				this.text_message = (TextMessage) this.message;
				queue.add(this.text_message.getText());
			}
			this.consumer.close();
			close_connection();
			return queue;
		} catch (JMSException e) {
			return queue;
		}
	}
	
	private void open_connection(String url, String queue) throws JMSException {
		//Connection
		//this.connectionFactory = new ActiveMQConnectionFactory(url);
		this.connection = this.connectionFactory.createConnection();
		this.connection.start();
		//Session
		this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		this.destination = this.session.createQueue(queue);
	}
	
	private void close_connection() throws JMSException {
		this.session.close();
		this.connection.close();
	}
	
}
