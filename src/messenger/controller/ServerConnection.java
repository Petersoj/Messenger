package messenger.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerConnection extends Thread{
	
	private Frame mainFrame;
	private ServerSocket serverSocket;
	
	public String clientServerMessage;
	
	private List<ClientConnection> clients;
	
	public ServerConnection(Frame frame){
		this.mainFrame = frame;
		clients = new ArrayList<ClientConnection>();
		try {
			serverSocket = new ServerSocket(mainFrame.port);
		} catch (IOException e) {
			mainFrame.showMessage("\n ERROR - " + e.getStackTrace());
		}
		mainFrame.showMessage("\nStarted Listening on port: " + mainFrame.port);
		clientServerMessage = "none";
	}
	
	public void run(){
		do{
			getConnection();
		}while(clientServerMessage.endsWith("- END"));
		for(ClientConnection client : clients){
			client.closeUpSockets();
		}
	}
	
	public void getConnection(){
		try {
			mainFrame.showMessage("\nWaiting for someone to connect...");
			Socket socket = serverSocket.accept();
			mainFrame.showMessage("\n Connected Address: " + socket.getInetAddress().getHostAddress());
			ClientConnection client = new ClientConnection(mainFrame, socket);
			clients.add(client);
			client.start();
			mainFrame.textField.setEditable(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageToClients(String message){
		for(ClientConnection client : clients){
			client.sendMessageToServer(message);
		}
	}

}
