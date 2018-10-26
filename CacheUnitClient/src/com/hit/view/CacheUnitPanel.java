package com.hit.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.*;



public class CacheUnitPanel extends JPanel implements ActionListener {
	
	
	 // Features  
	private static final long serialVersionUID = 1L;
	private PropertyChangeSupport listener;
	private JPanel container;
	private JPanel panel;
	private JPanel panel2;
	private JButton loadButton;
	private JButton statisticButton;
	private JButton clearButton;
	protected JTextArea textArea;
	private ButtonListener buttonListener;
	private JScrollPane scroll;
	
	//C'tor
	public CacheUnitPanel(PropertyChangeSupport listener) {
		super();
		this.listener = listener;
		initializeUI();		//Initialize the panel
	}
	
	//Return the panel
	public JPanel getPanel() {
		return container;
	}
	
	/**
	 * initialize the panel with Features
	 */
	public void initializeUI() {
		
		// Initialize swing components
		container = new JPanel();
		panel = new JPanel();
		panel2 = new JPanel();
		loadButton = new JButton("Load a Request", new ImageIcon("resources/icons/folder3.png"));
		statisticButton = new JButton("Show Statistics", new ImageIcon("resources/icons/graph1.png"));
		clearButton = new JButton(new ImageIcon("resources/icons/clear-icon.png"));
		buttonListener = new ButtonListener();
		textArea = new JTextArea(15, 32);
		scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		// Components settings
		textArea.setFont(new Font("Arial", Font.BOLD, 16));
		textArea.setEditable(false);
		// Add swing components to content pane
		panel.add(loadButton);
		panel.add(statisticButton);
		panel.add(clearButton);
		panel2.add(scroll);
		container.add(panel);
		container.add(panel2);
		// Add listeners
		loadButton.addActionListener(buttonListener);
		statisticButton.addActionListener(buttonListener);
		clearButton.addActionListener(buttonListener);
	}
	
	
	/**
	 *  Set button 
	 *
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch(e.getActionCommand()) {
			case "Load a Request" : 	//if Load a Request Pressed
			{
				String filePath = null;
				JFileChooser chooser = new JFileChooser("resources/json_files.");	//Choose Json file
				int status = chooser.showOpenDialog(null);
				if(status != JFileChooser.APPROVE_OPTION) {	//Check if chosen File or cancel
					textArea.setText("No file selected");
				}
				else {
					File file = chooser.getSelectedFile();
					filePath = file.getAbsolutePath();
				try {
					Scanner scan = new Scanner(new FileReader(filePath));	//read the file
					String json = scan.next();
					while(scan.hasNext()) {		//Convert the Json file to String
						json += (String) scan.next();
						
					}
					listener.firePropertyChange(null, null, json);	//Send the Json String to ClientObserver
					scan.close();	//close the scanner
				} 
				catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
				break;
			}
			
			case "Show Statistics" :	//if Show Statistics Pressed
			{
				String stats = "stat";
				listener.firePropertyChange(null, null, stats);	//Send the Statistics to ClientObserver
				break;
			}
			case "" :	//if eraser is pressed
				textArea.setText("");	//clean the Text Screen
			}
			
		}

	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
