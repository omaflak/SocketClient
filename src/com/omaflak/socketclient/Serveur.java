package com.omaflak.socketclient;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Serveur {
	private int port;
	private boolean continuer=true;
	private ServerSocket server;
	private List<Group<Message>> groups = new ArrayList<Group<Message>>();
	
	Serveur(int port) {
		this.port = port;
		for (int i=0 ; i<10 ; i++){
			Group<Message> group = new Group<Message>();
			group.setName("Group "+1);
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
					System.out.println("ACDEPT OK");
					SocketClient<Message> socket = new SocketClient<Message>(sock);
					System.out.println("New client connected");
					int index = chooseGroup(socket);
					groups.get(index).addPerson(socket);
				} catch (IOException e) {
					System.out.println("Error accept socket : "+e.getMessage());
				}
			}
		}
	}
	
	public int chooseGroup(SocketClient<Message> socket){
		Listener listener = new Listener();
		socket.setOnReceiveListener(listener);
		boolean continuer=true;
		
		while(continuer){
			if(listener.getId()!=null)
				continuer=false;
		}
		
		int id = Integer.valueOf(listener.getId());
		socket.removeOnReceiveMessageListener();

		if(id<groups.size())
			return id;
		
		return -1;
	}
	
	class Listener implements SocketClient.OnReceiveMessageListener<Message>{
		private String id=null;
		
		public void OnReceiveMessage(Message message, SocketClient<Message> sender) {
			id=message.getMessage();
			System.out.println(message.getSender()+" connected on chatroom "+id);
		}
		
		public String getId(){
			return id;
		}
	}
}
