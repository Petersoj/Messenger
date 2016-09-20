package messenger.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSelf extends Thread{
	
	private Frame mainFrame;
	
	private Socket socket;
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	public ClientSelf(Frame frame){
		this.mainFrame = frame;
		try {
			mainFrame.showMessage("\nTrying to connect...");
			socket = new Socket(frame.ip, frame.port);
			mainFrame.showMessage("\nConnected to " + frame.ip + " on port " + frame.port);
			output = new ObjectOutputStream(socket.getOutputStream());
			output.flush();
			input = new ObjectInputStream(socket.getInputStream());
			mainFrame.textField.setEditable(true);
		} catch (IOException e) {
			mainFrame.showMessage("\nERROR 1 in " + this.getClass().getName() + " - " + e.getMessage());
		}
	}
	
	@Override
	public void run(){
		while(!this.isInterrupted()){
			try {
				if(input != null){
					String message = (String) input.readObject();
					mainFrame.showMessage(message);
					if(message.equals(mainFrame.getName() + " - END")){
						this.closeConnectionToServer();
						this.interrupt();
					}
				}
			} catch (ClassNotFoundException | IOException e) {
				mainFrame.showMessage("\nERROR 2 in " + this.getClass().getName() + " - " + e.getMessage());
			}
		}
	}
	
	public void sendMessageToServer(String message){
		try {
			output.writeObject(message);
			output.flush();
		} catch (IOException e) {
			mainFrame.showMessage("\nERROR 3 in " + this.getClass().getName() + " - " + e.getMessage());
		}
	}
	
	public void closeConnectionToServer(){
		if(input != null){
			try{
				input.close();
				output.flush();
				output.close();
				socket.close();
			}catch(IOException e){
				mainFrame.showMessage("\nERROR 4 in " + this.getClass().getName() + " - " + e.getMessage());
			}
		}
	}
	

}
