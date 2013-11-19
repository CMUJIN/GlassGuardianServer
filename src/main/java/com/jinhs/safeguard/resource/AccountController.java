package com.jinhs.safeguard.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.jinhs.safeguard.common.AccountInfoBO;
import com.jinhs.safeguard.handler.AccountHandler;

@RequestMapping("/account")
@Controller
public class AccountController {
	private static final Logger LOG = Logger.getLogger(AccountController.class.getSimpleName());
	 
	@Autowired
	AccountHandler accountHandler;
	
	@RequestMapping(value="/create",method = RequestMethod.POST)
	public void createUser(@RequestBody String payload, HttpServletResponse httpResponse) throws IOException {
		httpResponse.getOutputStream().close();
	
		AccountInfoBO accountInfo = new Gson().fromJson(payload, AccountInfoBO.class);
		accountHandler.upsertUserInfo(accountInfo);
	}
}

