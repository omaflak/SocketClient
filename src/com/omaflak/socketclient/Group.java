package com.omaflak.socketclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.omaflak.socketclient.SocketClient;

public class Group<T> implements SocketClient.OnReceiveMessageListener<T>, SocketClient.OnConnectionClosedListener<T>{
	private List<SocketClient<T>> clients = new ArrayList<SocketClient<T>>();
	private String name="SocketClient Group";
	private OnGroupListener<T> OnGroupListener=null;
	
	public void addPerson(SocketClient<T> socket){
		clients.add(socket);
		clients.get(clients.size()-1).setOnReceiveMessageListener(this);
		clients.get(clients.size()-1).setOnConnectionClosedListener(this);
		SystemOut("ADD PERSON!");
	}

	public void removePerson(SocketClient<T> socket) throws IOException{
		for (int i=0 ; i<clients.size() ; i++){
			if(clients.get(i).equals(socket)){
				clients.get(i).disconnect();
				clients.remove(i);
				return;
			}
		}
	}
	
	public void diconnectGroup(){
		for (int i=0 ; i<clients.size() ; i++){
			try {
				removePerson(clients.get(i));
			} catch (IOException e) {
				SystemOut("Error remove client "+i);
			}
		}
	}
	
	public SocketClient<T> getPerson(int index){
		return clients.get(index);
	}
	
	public void sendMessageToGroup(T message, SocketClient<T> exception){
		for (int i=0 ; i<clients.size() ; i++){
			if(!clients.get(i).equals(exception)){
				try {
					clients.get(i).send(message);
				} catch (IOException e) {
					SystemOut("Error send message : "+e.getMessage());
				}
			}
		}
	}
	
	public void sendMessageToGroup(T message){
		sendMessageToGroup(message, null);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void SystemOut(String message){
		System.out.println(name+" > "+message);
	}
	
	public int size(){
		return clients.size();
	}
	
	// Listener
	
	public void OnReceiveMessage(T message, SocketClient<T> sender) {
		if(OnGroupListener!=null)
			OnGroupListener.OnReceiveMessageListener(this, message, sender);
		SystemOut("message "+message);
	}
	
	public void OnConnectionClosed(SocketClient<T> socket) {
		for (int i=0 ; i<clients.size() ; i++){
			if(clients.get(i).equals(socket)){
				clients.remove(i);
				SystemOut("client "+i+" removed");
			}
		}
		if(OnGroupListener!=null)
			OnGroupListener.OnConnectionClosedListener(this, socket);
	}
	
	// Interface
	
	public interface OnGroupListener<T>{
		public void OnReceiveMessageListener(Group<T> group, T message, SocketClient<T> sender);
		public void OnConnectionClosedListener(Group<T> group, SocketClient<T> socket);
	}
	
	public void setOnGroupListener(OnGroupListener<T> listener){
		this.OnGroupListener=listener;
	}
}
