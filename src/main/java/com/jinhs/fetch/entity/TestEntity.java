package com.jinhs.fetch.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.appengine.api.datastore.Key;


@Table(name="test_table")
@Entity
public class TestEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	
	private String test;
	
	public Key getKey() {
        return key;
        
    }
	
	public String getTest(){
		return this.test;
	}
	
	public void setTest(String str){
		this.test = str;
	}
}
