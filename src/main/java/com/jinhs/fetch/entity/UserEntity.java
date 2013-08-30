package com.jinhs.fetch.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.appengine.api.datastore.Key;

public class UserEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	
	@NotNull
	@Column(name="USER_ID")
	private String user_email;
	
	@NotNull
	@Column(name="USER_PASSWORD")
	private String user_password;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CREATE_DATE")
	private Date create_date;
}
