package com.omaflak.socketclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketClient<T>{
	private Socket socket = new Socket();
	private SocketAddress addr;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private String ip;
	private int port;
	private OnReceiveMessageListener<T> listener=null;
	private OnConnectionClosedListener<T> listener2=null;
	private OnConnectedListener<T> listener3=null;
	private boolean bool=true;

	SocketClient(String ip, int port){
		this.ip=ip;
		this.port=port;
		addr = new InetSocketAddress(ip, port);
	}
	
	SocketClient(Socket socket) throws IOException{
		this.ip=socket.getInetAddress().getHostAddress();
		this.socket=socket;
		writer = new ObjectOutputStream(this.socket.getOutputStream());
        reader = new ObjectInputStream(this.socket.getInputStream());
	}
	
	private void connect_private() throws IOException{
		socket.connect(addr);
        reader = new ObjectInputStream(socket.getInputStream());
		writer = new ObjectOutputStream(socket.getOutputStream());
	}
	
	public void connect() throws IOException, InterruptedException{
		Connection thread = new Connection();
		thread.start();
		thread.join();
		if(thread.getException()!=null)
			throw thread.getException();
		if(listener3!=null)
			listener3.OnConnected(SocketClient.this);
		if(listener!=null)
			new Reception().start();
	}
	
	public void disconnect() throws IOException{
		bool=false;
		socket.close();
	}
	
	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}
	
	class Connection extends Thread implements Runnable{
		private IOException error=null;
		
		public void run() {
			try {
				connect_private();
			} catch (IOException e) {
				error=e;
			}
		}
		
		public IOException getException(){
			return this.error;
		}
	}
	
	public void send(T object) throws IOException{
		writer.writeObject(object);
	}
	
	@SuppressWarnings("unchecked")
	private T read() throws IOException, ClassNotFoundException{
		return (T)reader.readObject();
	}
	
	class Reception extends Thread implements Runnable{
		public void run(){
			while(bool){
				try {
					T obj = read();
					if(listener!=null)
						listener.OnReceiveMessage(obj, SocketClient.this);
				} catch (IOException e) {
					System.out.println("Err : "+e.getMessage());
					try {
						disconnect();
					} catch (IOException e1) {
						System.out.println("Err disconnect : "+e1.getMessage());
					}
					if(listener2!=null)
						listener2.OnConnectionClosed(SocketClient.this);
				} catch (ClassNotFoundException e) {
					System.out.println("Err obj : "+e.getMessage());
				}
			}
		}
	}
	
	// interface
	
	public interface OnReceiveMessageListener<T>{
		public void OnReceiveMessage(T message, SocketClient<T> sender);
	}
	
	public void setOnReceiveListener(OnReceiveMessageListener<T> listener) {
		this.listener = listener;
		if(socket.isConnected())
			new Reception().start();
	}
	
	public void removeOnReceiveMessageListener(){
		listener = null;
	}
	
	// interface 2
	
	public interface OnConnectionClosedListener<T>{
		public void OnConnectionClosed(SocketClient<T> socket);
	}
	
	public void setOnConnectionClosedListener(OnConnectionClosedListener<T> listener) {
		this.listener2 = listener;		
	}
	
	public void removeOnConnectionClosedListener(){
		listener2 = null;
	}
	
	// interface 3
	
	public interface OnConnectedListener<T>{
		public void OnConnected(SocketClient<T> socket);
	}
	
	public void setOnConnectedListener(OnConnectedListener<T> listener) {
		this.listener3 = listener;		
	}
	
	public void removeOnConnectedListener(){
		listener3 = null;
	}
}
