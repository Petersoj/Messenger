package messenger.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Frame extends JFrame{
	
	private JTextArea textArea;
	private JTextField textField;
	
	ApplicationType applicationType;
	
	private JMenuBar jMenuBar;
	private JMenu typeJMenu;
	private JRadioButtonMenuItem serverJItem;
	private JRadioButtonMenuItem clientJItem;
	private JMenu fileJMenu;
	private JMenuItem popupJItem;
	
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private ServerSocket serverSocket;
	private Socket socket;
	int port;
	String ip;
	
	public Frame(){
		super("Messenger");
		jMenuBar = new JMenuBar();
		typeJMenu = new JMenu("Type");
		serverJItem = new JRadioButtonMenuItem("Become a Server", false);
		serverJItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(serverJItem.isSelected()){
					clientJItem.setSelected(false);
					setupApplication(ApplicationType.SERVER);
				}else{
					serverJItem.setSelected(true);
				}
			}
		});
		clientJItem = new JRadioButtonMenuItem("Become a Client", true);
		clientJItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(clientJItem.isSelected()){
					serverJItem.setSelected(false);
					setupApplication(ApplicationType.CLIENT);
				}else{
					clientJItem.setSelected(true);
				}
			}
		});
		typeJMenu.add(serverJItem);
		typeJMenu.add(clientJItem);
		
		fileJMenu = new JMenu("File");
		popupJItem = new JMenuItem("Set connection settings");
		popupJItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		fileJMenu.add(popupJItem);
		
		jMenuBar.add(fileJMenu);
		jMenuBar.add(typeJMenu);
		
		textArea = new JTextArea();
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		textField.setEditable(false);
		textArea.setEditable(false);
		
		port = 6666;
		ip = "127.0.0.1";
		
		add(jMenuBar, BorderLayout.NORTH);
		add(new JScrollPane(textArea));
		add(textField, BorderLayout.SOUTH);
		
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void setupApplication(ApplicationType type){
		this.applicationType = type;
		if(type == ApplicationType.SERVER){
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			serverSocket = null;
		}
	}
	
	public void cleanUpSockets(){
		try{
			if(output != null){
				output.flush();
				output.close();
				input.close();
			}
			if(socket != null){
				socket.close();
			}
			if(serverSocket != null){
				serverSocket.close();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message){
		
	}

}
