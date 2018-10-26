package com.hit.services;

import java.util.Arrays;

import javax.xml.crypto.Data;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.NRUAlgoCacheImpl;
import com.hit.algorithm.RandomAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;
import com.hit.util.CLI;

public class CacheUnitService<T> {
	
	private IAlgoCache<Long, DataModel<T>> algo;
	private DaoFileImpl<T> HD;
	private CacheUnit<T> cache;
	private String[] conf;
	
	private static int modelCount = 0; // Counting whole incoming dataModels
	private static int swapsCount = 0; // Counting swaps from Cache to Disk
	private static int requestCount = 0; // Counting incoming requests
	
	CacheUnitService() {
		conf = new String[2]; // Configurations From CLI
		HD = new DaoFileImpl<>("resources/datasource.txt");
	}
	
	// Set Algorithm and Capacity that was preassigned by user
	public void config(String[] confCLI) {
		conf[0] = confCLI[1]; // Algorithm
		conf[1] = confCLI[2]; // Capacity
		switch(conf[0]) { 
		case CLI.LRU : algo = new LRUAlgoCacheImpl<>(Integer.parseInt(conf[1])); break;
		case CLI.NRU : algo = new NRUAlgoCacheImpl<>(Integer.parseInt(conf[1])); break;
		case CLI.RANDOM : algo = new RandomAlgoCacheImpl<>(Integer.parseInt(conf[1]));
		}
		cache = new CacheUnit<>(algo);
	}
	
	public boolean delete(DataModel<T>[] dataModels) {
		requestCount++;	
		modelCount += dataModels.length;
		
		try {
			Long[] ids = new Long[dataModels.length];
		
			for(int i = 0; i < ids.length; i++) {
				ids[i] = dataModels[i].getDataModelId();
			}
			cache.removeDataModels(ids);
			for(DataModel item : dataModels) {
				HD.delete(item);
			}
		}catch (Exception e) { return false; }
		return true;
	}
	
	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		requestCount++;
		modelCount += dataModels.length;
		Long[] ids = new Long[dataModels.length];
		DataModel<T>[] dataInCache;
		for(int i = 0; i < ids.length; i++) {
			ids[i] = dataModels[i].getDataModelId();
		}
		
		dataInCache = cache.getDataModels(ids);
		DataModel<T>[] dataNotInCache;
		if (dataInCache == null) {
			dataNotInCache = dataModels;
		} else {
			dataNotInCache = new DataModel[Math.abs(dataModels.length - dataInCache.length)];
			// Checks if the data wasn't in cache and if not go to HD to get the data
			boolean in = false; 
			int k = 0;
			for(int i = 0; i < dataModels.length; i++) {
				for(int j = 0; j < dataInCache.length; j++) {
					if(dataModels[i].equals(dataInCache[j])) {
						in = true;
					}
					if(!in && j == dataInCache.length - 1) {
						dataNotInCache[k++] = HD.find(dataModels[i].getDataModelId());
					}
				}
				in = false;
			}
		}
		
		if( dataInCache != null) {
			DataModel<T>[] dataToReturn = Arrays.copyOf(dataInCache, dataInCache.length + dataNotInCache.length);
			System.arraycopy(dataNotInCache, 0, dataToReturn, dataInCache.length, dataNotInCache.length);
			return dataToReturn;
		}
		for(int i = 0; i < dataModels.length; i++) {
			dataNotInCache[i] = HD.find(dataModels[i].getDataModelId());
		}
		return dataNotInCache;
	}
	
	public boolean update(DataModel<T>[] dataModels) {
		requestCount++;
		modelCount += dataModels.length;
		try {
			DataModel<T>[] updated = cache.putDataModels(dataModels);
			if(updated != null) {
				for(DataModel item : updated) {
					swapsCount++;
					HD.save(item);
				}
			}
		}catch (Exception e) { return false; }
		return true;
	}
	
	public String showStatistic() {
		String stat = "Capacity: " + conf[1] + "\n" + "Algorithm: " + conf[0] + "\n" 
				+ "Total number of requests: " + requestCount + "\n" 
				+ "Total number of DataModels (GET/DELETE/UPDATE) requests: "
				+ modelCount + "\n" + "Total number of DataModel swaps (from Cache to Disk): "
				+ swapsCount;
		return stat;

	}
	
	public void cacheCleaner() {
		DataModel<T>[] cleaner = new DataModel[Integer.parseInt(conf[1])];
		for(int i = 0; i < cleaner.length; i++) {
			cleaner[i] = new DataModel<>((long) -i, (T) "default");
		}
		cleaner = cache.putDataModels(cleaner);	
		for(DataModel<T> data : cleaner) {
			HD.save(data);
		}
	}
	
}


