package com.omaflak.socketclient;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Serveur implements Group.OnGroupListener<Message>{
	private int port;
	private boolean continuer=true;
	private ServerSocket server;
	private List<Group<Message>> groups = new ArrayList<Group<Message>>();
	
	Serveur(int port) {
		this.port = port;
		for (int i=0 ; i<10 ; i++){
			Group<Message> group = new Group<Message>();
			group.setName("Group "+i);
			group.setOnGroupListener(this);
			groups.add(group);
		}
	}
	
	public void start() throws IOException{
		server = new ServerSocket(port);
		new ReceptionClient().start();
		System.out.println("Server started");
	}
	
	public void stop() throws IOException{
		for (int i=0 ; i<groups.size() ; i++)
			groups.get(i).diconnectGroup();
		server.close();
	}
	
	class ReceptionClient extends Thread implements Runnable{
		public void run(){
			while(continuer){
				try {
					Socket sock = server.accept();
					SocketClient<Message> socket = new SocketClient<Message>(sock);
					Random r = new Random();
					int index = r.nextInt(groups.size()-1);
					groups.get(index).addPerson(socket);
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
