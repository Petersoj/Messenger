package messenger.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection extends Thread{

	private Frame mainFrame; 
	private Socket socket;
	
	private String message;
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	public ClientConnection(Frame frame, Socket socket){
		this.mainFrame = frame;
		this.socket = socket;
		try{
			output = new ObjectOutputStream(socket.getOutputStream());
			output.flush();
			input = new ObjectInputStream(socket.getInputStream());
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		do{
			try{
				message = (String) input.readObject();
				mainFrame.showMessage(message);
				if(mainFrame.applicationType == ApplicationType.SERVER){
					mainFrame.serverConnection.sendMessageToClients(message);
				}
			}catch(IOException | ClassNotFoundException e){
				mainFrame.showMessage("\n ERROR - " + e.getStackTrace().toString());
			}
		}while(!message.endsWith("- END"));
		closeUpSockets();
	}
	
	public void sendMessageToServer(String message){
		try {
			output.writeObject(mainFrame.name + " - " + message);
			this.message = message;
			mainFrame.showMessage(message);
		} catch (IOException e) {
			mainFrame.showMessage("\n ERROR - " + e.getStackTrace().toString());
		}
	}
	
	public void closeUpSockets(){
		try{
			if(output != null){
				output.flush();
				output.close();
				input.close();
			}
			if(socket != null){
				socket.close();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
