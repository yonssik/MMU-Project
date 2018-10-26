package com.hit.view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class CacheUnitView {

	private PropertyChangeSupport listener;
	private JFrame frame;
	private CacheUnitPanel panel;
	
	//C'tor
	public CacheUnitView() {
		listener = new PropertyChangeSupport(this);
		panel = new CacheUnitPanel(listener);
		frame = new JFrame("CacheUnitUI");
		
	}
	
	//Add Listener
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		listener.addPropertyChangeListener(pcl);
	}
	
	//Remove Listner
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		listener.removePropertyChangeListener(pcl);
	}
	
	//initiallize the frame
	public void start() {
		SwingUtilities.invokeLater(new Runnable() {	
			public void run() {
				frame.add(panel.getPanel());
				frame.setSize(500, 400);
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
			}
		});
	}
	
	
	/**
	 * Recive data from observers and print to Screen
	 * @param t
	 */
	public <T> void updateUIData(T t) {
			panel.textArea.setText((String) t);
	}
	
}
