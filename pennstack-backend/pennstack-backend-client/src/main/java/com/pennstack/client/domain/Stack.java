package com.pennstack.client.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="stack")
public class Stack {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String category;
    private String licenceType;
    
    protected Stack() {}

    public Stack(String name, String category,String licenceType)
    {
    	this.name=name;
    	this.category=category;
    	this.licenceType=licenceType;
    }
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLicenceType() {
		return licenceType;
	}
	public void setLicenceType(String licenceType) {
		this.licenceType = licenceType;
	}

}
