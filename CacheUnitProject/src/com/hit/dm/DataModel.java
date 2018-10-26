package com.hit.dm;

import java.io.Serializable;

public class DataModel<T> implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private Long dataModelId;
	private T content;
	
	//C'tor
	public DataModel(Long dataModelId, T content) {
		
		this.dataModelId = dataModelId;
		this.content = content;
		
	}
	
	// Copy constructor
	public DataModel(DataModel<T> other) {
		this.dataModelId = other.dataModelId;
		this.content = other.content;
	}
	
	@Override
	//equals by hashCode
	public boolean equals(Object obj) {
		return this.hashCode() == obj.hashCode(); 
	}
	
	//return the Content
	public T getContent() {
		return this.content;
	}
	
	//return the id 
	public Long getDataModelId() {
		return this.dataModelId;
	}
	
	@Override
	//return the hashCode
	public int hashCode() {
		return dataModelId.hashCode();
	}
	//Set the Content
	public void setContent(T content) {
		this.content = content;
	}
	//set the ID
	public void setDataModelId(Long dataModelId) {
		this.dataModelId = dataModelId;
	}
	
	@Override
	public String toString() {
		return "DataModel [ id = " + dataModelId + ", content = " + content + " ]";
	}
}