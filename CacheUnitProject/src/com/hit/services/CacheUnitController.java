package com.hit.services;

import com.hit.dm.DataModel;

public class CacheUnitController<T> {
	
	private static CacheUnitController<String> instance;
	private CacheUnitService<T> cus;
	// C'tor
	private CacheUnitController() {
		cus = new CacheUnitService<>();
	}
	// Singenlton Pattern
	public static CacheUnitController<String> getInstance() {
		if(instance == null) {
			instance = new CacheUnitController<>();
		}
		return instance;
	}
	// Delete DataModel
	public boolean delete(DataModel<T>[] dataModels) {
		return cus.delete(dataModels) ? true : false;
	}
	// Get DataModel
	public DataModel<T>[] get(DataModel<T>[] dataModels){
		DataModel<T>[] getData;
		getData = cus.get(dataModels);
		
		return getData;
	}
	// Add DataModel
	public boolean update(DataModel<T>[] dataModels) {
		return cus.update(dataModels) ? true : false;
	}
	// Provide configuration from CLI to Services
	public void config(String[] conf) {
		cus.config(conf);
	}
	// Show all necessary statistics
	public String showStatistic() {
		return cus.showStatistic();
	}


	// Clean the cache to Hard Disk
	public void cacheCleaner() {
		cus.cacheCleaner();
	}

}
