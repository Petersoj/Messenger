package messenger.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection extends Thread{
	
	private Frame mainFrame;
	public Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	
	public ClientConnection(Frame frame, Socket socket){
		this.mainFrame = frame;
		this.socket = socket;
		try{
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
		}catch(IOException e){
			mainFrame.showMessage("\nERROR 1 in " + this.getClass().getName() + " - " + e.getMessage());
			this.interrupt();
		}
	}
	
	@Override
	public void run(){
		while(!this.isInterrupted()){
			try{
				String str = (String) input.readObject();
				if(str.endsWith("- END")){
					this.closeConnectionToClient();
					this.interrupt();
				}
				mainFrame.serverSelf.onRecieveMessageFromClient(str);
				mainFrame.textArea.setCaretPosition(mainFrame.textArea.getDocument().getLength());
			}catch(IOException | ClassNotFoundException e){
				mainFrame.showMessage("\nERROR 4 in " + this.getClass().getName() + " - " + e.getMessage());
				this.interrupt();
			}
		}
	}
	
	public void sendMessageToClient(String message){
		try{
			output.writeObject(message);
			output.flush();
		}catch(IOException e){
			mainFrame.showMessage("\nERROR 2 in " + this.getClass().getName() + " - " + e.getMessage());
		}
	}
	
	public void closeConnectionToClient(){
		if(input != null){
			try{
				input.close();
				output.flush();
				output.close();
				socket.close();
			}catch(IOException e){
				mainFrame.showMessage("\nERROR 3 in " + this.getClass().getName() + " - " + e.getMessage());
			}
		}
	}
}
