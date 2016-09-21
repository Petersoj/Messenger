package messenger.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerSelf extends Thread{
	
	private Frame mainFrame;
	private List<ClientConnection> clients;
	
	private ServerSocket serverSocket;
	
	public ServerSelf(Frame frame){
		this.mainFrame = frame;
		clients = new ArrayList<ClientConnection>();
		try {
			serverSocket = new ServerSocket(frame.port);
			mainFrame.showMessage("\nStarted listeneing on port: " + mainFrame.port);
		} catch (IOException e) {
			mainFrame.showMessage("\nERROR 2 in " + this.getClass().getName() + " - " + e.getMessage());
		}
	}
	
	@Override
	public void run(){
		while(!this.isInterrupted()){
			try {
				Socket socket = serverSocket.accept();
				mainFrame.showMessage("\nAccepted Connection on " + socket.getInetAddress().getHostName() + " on  port " + mainFrame.port);
				ClientConnection client = new ClientConnection(mainFrame, socket);
				clients.add(client);
				client.start();
				mainFrame.textField.setEditable(true);
			} catch (IOException e) {
				mainFrame.showMessage("\nERROR 1 in " + this.getClass().getName() + " - " + e.getMessage());
			}
		}
	}
	
	public void onRecieveMessageFromClient(String message){
		for(ClientConnection client : clients){
			client.sendMessageToClient(message);
		}
		mainFrame.showMessage(message);
	}
	
	public void closeSocketsToClients(){
		for(ClientConnection client : clients){
			client.interrupt();
			client.closeConnectionToClient();
		}
		clients.clear();
	}

}
