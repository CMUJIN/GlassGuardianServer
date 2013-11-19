package com.jinhs.safeguard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.appengine.api.datastore.Key;

@Table(name="GUARD_MODE")
@Entity
public class GuardModeEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	
	@Column(name="EMAIL")
	private String email;

	@Column(name="GUARD_MODE")
	private int mode;
	
	@Column(name="GUARD_ID")
	private String guardId;
}
