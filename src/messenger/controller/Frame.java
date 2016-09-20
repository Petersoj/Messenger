package messenger.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Frame extends JFrame{
	
	public enum ApplicationType{
		SERVER, CLIENT
	}
	
	private JTextArea textArea;
	public JTextField textField;
	
	public ApplicationType appType;
	
	private JMenuBar jMenuBar;
	private JMenu typeJMenu;
	private JRadioButtonMenuItem serverJItem;
	private JRadioButtonMenuItem clientJItem;
	private JMenu fileJMenu;
	private JMenuItem setNameJItem;
	private JMenuItem ipJItem;
	private JMenuItem portJItem;
	private JMenuItem connectJItem;
	
	private ClientSelf clientSelf;
	public ServerSelf serverSelf;
	
	String name;
	int port;
	String ip;
	
	public Frame(){
		super("Messenger");
		appType = ApplicationType.CLIENT;
		jMenuBar = new JMenuBar();
		typeJMenu = new JMenu("Type");
		serverJItem = new JRadioButtonMenuItem("Become a Server", false);
		serverJItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				appType = ApplicationType.SERVER;
				if(serverJItem.isSelected()){
					clientJItem.setSelected(false);
				}else{
					serverJItem.setSelected(true);
				}
			}
		});
		clientJItem = new JRadioButtonMenuItem("Become a Client", true);
		clientJItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				appType = ApplicationType.CLIENT;
				if(clientJItem.isSelected()){
					serverJItem.setSelected(false);
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
		connectJItem = new JMenuItem("Reset and Connect");
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
				String message = "\n" + name + " - " + e.getActionCommand();
				if(appType == ApplicationType.CLIENT){
					clientSelf.sendMessageToServer(message);
				}else if(appType == ApplicationType.SERVER){
					showMessage(message);
					serverSelf.onRecieveMessageFromClient(message);
				}
				textField.setText("");
			}
		});
		textField.setEditable(false);
		textArea.setEditable(false);
		
		port = 2555;
		ip = "127.0.0.1";
		name = "DefaultName";
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e){
				if(serverSelf != null){
					System.out.println("Close");
					serverSelf.onRecieveMessageFromClient(name + "- END");
					serverSelf.closeSocketsToClients();
					serverSelf.interrupt();
				}
				if(clientSelf != null){
					clientSelf.sendMessageToServer(name + "- END");
					clientSelf.closeConnectionToServer();
					clientSelf.interrupt();
				}
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
		this.textArea.setText("");
		this.textField.setText("");
		if(this.serverSelf != null){
			this.serverSelf.closeSocketsToClients();
			this.serverSelf.interrupt();
		}
		if(this.clientSelf != null){
			this.clientSelf.closeConnectionToServer();
			this.clientSelf.interrupt();
		}
		if(appType == ApplicationType.CLIENT){
			this.clientSelf = new ClientSelf(this);
			this.clientSelf.start();
		}else if(appType == ApplicationType.SERVER){
			this.serverSelf = new ServerSelf(this);
			this.serverSelf.start();
		}
	}
	
	public void showMessage(String msg){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textArea.append(msg);
			}
		});
	}
}
