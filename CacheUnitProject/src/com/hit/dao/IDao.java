package com.hit.dao;

import java.io.Serializable;

public interface IDao<ID extends Serializable, T> {
	
	// Deletes a given entity.
	public void delete(T entity);	
	// Retrieves an entity by its id.
	public T find(ID id);
	// Saves a given entity.
	public void save(T entity);

}