SocketClient
============

Very simple way to use sockets in java. For example you can easily create a chat between several persons.

/!\ Warning /!\

Before starting, if you are going to use SocketClient to send your custom class, be sure that this class is exactly the same on the client and server sides. Even the packages names must be the sames!
On Eclipse you can easily create a new package and move your files into it by doing this : right click > new package. Then Select your files > right click > Refactor > Move > choose the new package.

/!\ Warning /!\

1) Declare your socket : 

    SocketClient<T> socket = new SocketClient<T>("ip_address", port);
    // T is the class name of the objects you want to send via the socket. T must implements Serializable.
    // For exemple if you only want to send strings you declare your client like this :
    SocketClient<String> socket = new SocketClient<String>("ip_address", port);
    // You can also declare it like this :
    SocketClient<String> socket = new Socket(...);
    
2) Connect the socket

    ...
    socket.connect();
    
3) Set the listener to receive messages :
    
    ...
    socket.setOnReceiveListener(new SocketClient.OnReceiveMessageListener<T>(){  // T still the same T as above
  		@Override
  		public void OnReceiveMessage(T message, SocketClient<T> sender) {
  			// TODO Auto-generated method stub
  		}
    });
	
	// You have also OnConnectionClosedListener<T> to know when the socket is disconnected from the server
      
3) Send messages as simple as this :

    socket.send(T);

4) Finnaly disconnect the socket with :

    socket.disconnect();
