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
	
	public void signIn(AuthExchangeResponse exchangeResponse, String email){
		if(transService.isUserAccountExisted(email)){
			LOG.info("UserAccountHandler user info update");
			transService.updateUser(exchangeResponse, email);
		}
		else{
			LOG.info("UserAccountHandler new user info");
			transService.insertNewUser(exchangeResponse, email);
		}
	}
}
