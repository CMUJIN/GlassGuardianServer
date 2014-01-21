package com.jinhs.safeguard.handler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinhs.safeguard.common.AuthExchangeResponse;
import com.jinhs.safeguard.dao.DBTransService;

@Component
public class UserAccountHandler {
	private static final Logger LOG = Logger.getLogger(UserAccountHandler.class.getSimpleName());
	
	@Autowired
	private DBTransService transService;
	
	public void signIn(AuthExchangeResponse exchangeResponse, String email, String code){
		LOG.info("UserAccountHandler user info upsert");
		transService.upsertUser(exchangeResponse, email);
	}
}
