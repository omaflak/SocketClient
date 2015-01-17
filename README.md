SocketClient
============

Very simple way to use sockets in java. For example you can easily create a chat between several persons.

/!\ Warning /!\

Before starting, if you are going to use SocketClient to send your custom class, be sure that your class is exactly the same on the client and on the server sides. Even the packages names must be the sames!
On Eclipse you can easily rename the package by doing this : right click on your package > Refactor > Rename...

/!\ Warning /!\

1) Declare your socket : 

    SocketClient<T> socket = new SocketClient<T>("ip_address", port);
    // T is the class name of the objects you want to send via the socket. T must implements Serializable.
    // For example if you only want to send strings you declare your client like this :
    SocketClient<String> socket = new SocketClient<String>("ip_address", port);
    // The constructor also take a socket as parameter, for example if you're on the server and you're receiving connections, you can do:
    SocketClient<String> socket = new SocketClient<T>(sever.accept());
    
2) Connect the socket

    ...
    socket.connect();
    
3) Set the listener to receive messages :
    
    ...
    socket.setOnReceiveMessageListener(new SocketClient.OnReceiveMessageListener<T>(){  // T still the same T as above
  		@Override
  		public void OnReceiveMessage(T message, SocketClient<T> sender) {
  			// TODO Auto-generated method stub
  		}
    });
	
	// You have also OnConnectionClosedListener<T> to know when the socket is disconnected from the server
	// And OnConnectedListener<T> to know when the socket is connected to the server. You must set this listener before calling connect() method.
      
3) Send messages as simple as this :

    socket.send(T);

4) Finnaly disconnect the socket with :

    socket.disconnect();
    
    

Extras: Group class
============

Group is a usefull class if you want to handle several sockets at the same time.

1) First, declare the class as follow: 

    Group<T> group = new Group<T>(); // T still the same as above
    group.setName("My Group"); // Optional
    
2) Set listener

    group.setOnGroupListener(new Group.OnGroupListener<T>(){
		public void OnReceiveMessageListener(Group<T> group, T message, SocketClient<T> sender) {
			// Called when a socket from the group send a message
		}
		
		public void OnConnectionClosedListener(Group<T> group, SocketClient<T> socket) {
			// called when a socket from the group is disconnected
		}
    });
    
3) Add & remove person

    group.addPerson(SocketClient<T> socket);
    group.removePerson(SocketClient<T> socket);
    
3) Send message to the group

    group.sendMessageToGroup(T message, SocketClient<T> exception); 
    group.sendMessageToGroup(T message); // is equivalent to sendMessageToGroup(message, null);
    
