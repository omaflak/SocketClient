package com.omaflak.socketclient;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur implements Group.OnGroupListener<Message>{
	private int port;
	private boolean continuer=true;
	private ServerSocket server;
	private Group<Message> group = new Group<Message>();
	
	Serveur(int port) {
		this.port = port;
		group.setOnGroupListener(this);
		group.setName("My Group");
	}
	
	public void start() throws IOException{
		server = new ServerSocket(port);
		new ReceptionClient().start();
		System.out.println("Server started");
	}
	
	public void stop() throws IOException{
		group.diconnectGroup();
		server.close();
	}
	
	class ReceptionClient extends Thread implements Runnable{
		public void run(){
			while(continuer){
				try {
					Socket sock = server.accept();
					SocketClient<Message> socket = new SocketClient<Message>(sock);
					group.addPerson(socket);
				} catch (IOException e) {
					System.out.println("Error accept socket : "+e.getMessage());
				}
			}
		}
	}

	public void OnReceiveMessageListener(Group<Message> group, Message message, SocketClient<Message> sender) {
		group.sendMessageToGroup(message, sender);
	}

	public void OnConnectionClosedListener(Group<Message> group, SocketClient<Message> socket) {
		group.sendMessageToGroup(new Message(socket.getName(), "disconnected!"));
	}
}
