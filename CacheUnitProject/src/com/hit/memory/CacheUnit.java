package com.hit.memory;

import com.hit.algorithm.IAlgoCache;

import com.hit.dm.DataModel;
import java.util.LinkedList;

public class CacheUnit<T> {
	
	
	private IAlgoCache<Long, DataModel<T>> cacheAlgo;
	
	//C'tor
	public CacheUnit(IAlgoCache<Long, DataModel<T>> algo) {
		this.cacheAlgo = algo;
	}
	

	/**
	 * Get the DataModels in Cache
	 * @param ids
	 * @return [ ] DataModel 
	 */
	public DataModel<T>[] getDataModels(Long[] ids){
		
		LinkedList<DataModel<T>> idsList = new LinkedList<DataModel<T>>();
		for(Long id : ids) {
			if(cacheAlgo.getElement(id) != null) {	// if not null insert to DataModell Array
				DataModel<T> data = new DataModel((DataModel) cacheAlgo.getElement(id));
				idsList.add(data);
			}
		}
		if(idsList.size() != 0) {	//if list not empty return DataModel Array
			DataModel<T>[] datamodels = new DataModel[idsList.size()];
			for(int i = 0; i < datamodels.length; i++) {
				datamodels[i] = idsList.getFirst();
				idsList.removeFirst();
			}
			return datamodels;
		}
		return null;	//if empty return null
	}
	
	
	/**
	 * put DataModels to Cache
	 * @param datamodels
	 * @return DataModels
	 */
	@SuppressWarnings("unchecked")
	public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) {
		LinkedList<DataModel<T>> dataList = new LinkedList<DataModel<T>>();
		for(DataModel<T> data : datamodels) {
			// Saving the values that returned from cache
			DataModel<T> temp = new DataModel((DataModel) cacheAlgo.putElement(data.getDataModelId(), data));
			if(!temp.equals(data)) {
				if(!dataList.contains(temp)) {
					dataList.add(temp);
				}
			}
		}
		
		DataModel<T>[] dm = new DataModel[dataList.size()];
		for(int i = 0; i < dm.length; i++) {
			dm[i] = dataList.getFirst();
			dataList.removeFirst();
		}
		
		return dm;	
	}
	
	/**
	 * remove dataModel from Cache by ids
	 * @param [ ] ids
	 */
	public void removeDataModels(Long[] ids) {
		for(Long id : ids) {
			cacheAlgo.removeElement(id);
		}
		
	}

}
