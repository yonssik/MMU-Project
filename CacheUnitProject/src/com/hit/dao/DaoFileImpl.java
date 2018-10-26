package com.hit.dao;

import com.hit.dm.DataModel;

import java.util.HashMap;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {

	private String filePath;
	private int capacity;
	private HashMap<Long, T> entitysMap;
	

	//C'tor
	public DaoFileImpl(String filePath) {
		entitysMap = new HashMap<>();
		this.filePath = filePath;
		
		// Saving the existed data source to HashMap
		try(ObjectInputStream input = 
						new ObjectInputStream(new FileInputStream(filePath))){
			entitysMap = (HashMap<Long, T>) input.readObject();
			input.close();
		}catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//C'tor with capacity
	public DaoFileImpl(String filePath, int capacity) {
		
		entitysMap = new HashMap<>();
		this.filePath = filePath;
		this.capacity = capacity;
		
		// Saving the existed data source to HashMap
		try(ObjectInputStream input = 
				new ObjectInputStream(new FileInputStream(filePath))){
			entitysMap = (HashMap<Long, T>) input.readObject();
			input.close();
		}catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			}
		
	}
	
	/**
	 * Delete DataModel From the file 
	 * @param DatalModel
	 */
	@Override
	public void delete(DataModel<T> entity) {
		
		entity.setContent(null);	//set the Content to null  
		entitysMap.replace(entity.getDataModelId(), entity.getContent()); //add to file
		try (ObjectOutputStream out = 
				new ObjectOutputStream(new FileOutputStream(filePath))){
			out.writeObject(entitysMap); 
			out.close();
		} catch (IOException e) {
			System.out.print(e.getMessage());
		}
		
	}
	
	
	/**
	 * Search dataModel by id in File
	 * @param Long id
	 * @return DataModel
	 */
	@Override
	public DataModel<T> find(Long id) {
		DataModel<T> dataModel = new DataModel<T>(id, entitysMap.get(id));
		if(dataModel.getContent() == null) {
			dataModel.setContent((T) "Data Not Exist");
		}
		return dataModel;
	}

	/**
	 * Save DataModel to file
	 * @param DataModel
	 */
	@Override
	public void save(DataModel<T> entity) {
		
		try( ObjectOutputStream out 
				= new ObjectOutputStream(new FileOutputStream(filePath))){
			// Adding new element to data source
			entitysMap.put(entity.getDataModelId(), entity.getContent());
			out.writeObject(entitysMap);	//write to map
			out.flush();
			out.close();
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
		
	}

}
