package messenger.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Frame extends JFrame{
	
	private JTextArea textArea;
	public JTextField textField;
	
	ApplicationType applicationType;
	
	private JMenuBar jMenuBar;
	private JMenu typeJMenu;
	private JRadioButtonMenuItem serverJItem;
	private JRadioButtonMenuItem clientJItem;
	private JMenu fileJMenu;
	private JMenuItem setNameJItem;
	private JMenuItem ipJItem;
	private JMenuItem portJItem;
	private JMenuItem connectJItem;
	
	public ServerConnection serverConnection;
	private ClientConnection clientConnection;
	
	String name;
	int port;
	String ip;
	
	public Frame(){
		super("Messenger");
		applicationType = ApplicationType.CLIENT;
		jMenuBar = new JMenuBar();
		typeJMenu = new JMenu("Type");
		serverJItem = new JRadioButtonMenuItem("Become a Server", false);
		serverJItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(serverJItem.isSelected()){
					clientJItem.setSelected(false);
					applicationType = ApplicationType.SERVER;
					setupApplication();
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
					applicationType = ApplicationType.CLIENT;
					setupApplication();
				}else{
					clientJItem.setSelected(true);
				}
			}
		});
		typeJMenu.add(serverJItem);
		typeJMenu.add(clientJItem);
		
		fileJMenu = new JMenu("File");
		setNameJItem = new JMenuItem("Set Display Name");
		setNameJItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				name = JOptionPane.showInputDialog(null, "Set your Display Name", "Name", 1);
			}
		});
		ipJItem = new JMenuItem("Set IP Address");
		ipJItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ip = JOptionPane.showInputDialog(null, "Set the IP Address to Connect to", "IP Address", 1);
			}
		});
		portJItem = new JMenuItem("Set Port");
		portJItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				port = Integer.parseInt(JOptionPane.showInputDialog(null, "Set the Port number", "Port", 1));
			}
		});
		connectJItem = new JMenuItem("Connect");
		connectJItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setupApplication();
			}
		});
		
		fileJMenu.add(setNameJItem);
		fileJMenu.addSeparator();
		fileJMenu.add(ipJItem);
		fileJMenu.add(portJItem);
		fileJMenu.addSeparator();
		fileJMenu.add(connectJItem);
		
		
		jMenuBar.add(fileJMenu);
		jMenuBar.add(typeJMenu);
		
		textArea = new JTextArea();
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = name + " - " + e.getActionCommand();
				if(applicationType == ApplicationType.CLIENT){
					clientConnection.sendMessageToServer(input);
				}else if(applicationType == ApplicationType.SERVER){
					showMessage(input);
					serverConnection.clientServerMessage = name + "- END";
					serverConnection.sendMessageToClients(input);
				}
			}
		});
		textField.setEditable(false);
		textArea.setEditable(false);
		
		port = 6666;
		ip = "127.0.0.1";
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e){
				// close stuff
			}
		});
		
		add(jMenuBar, BorderLayout.NORTH);
		add(new JScrollPane(textArea));
		add(textField, BorderLayout.SOUTH);
		
		setSize(500, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void setupApplication(){
		if(applicationType == ApplicationType.SERVER){ // SERVER
			serverConnection = new ServerConnection(this);
			serverConnection.start();
		}else if(applicationType == ApplicationType.CLIENT){ // CLIENT
			try {
				clientConnection = new ClientConnection(this, new Socket(InetAddress.getByName(ip), port));
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public void showMessage(String msg){
		textArea.append(msg);
	}
}
