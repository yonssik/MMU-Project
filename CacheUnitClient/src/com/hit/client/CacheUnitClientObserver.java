package com.hit.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;
import com.hit.view.CacheUnitView;

public class CacheUnitClientObserver implements PropertyChangeListener, EventListener{

	private CacheUnitClient cuc;
	private CacheUnitView cuv;
	
	//C'tor
	public CacheUnitClientObserver() {
		cuc = new CacheUnitClient();
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String response;
		response = cuc.send((String) e.getNewValue());	//Receive Respond from the server
		cuv = (CacheUnitView) e.getSource();
		cuv.updateUIData(response);		//Send the Respond to CacheUnitView
	}
}
