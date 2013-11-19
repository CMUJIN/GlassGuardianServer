package com.jinhs.safeguard.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinhs.safeguard.common.AccountInfoBO;
import com.jinhs.safeguard.common.AlertConfigInfoBO;
import com.jinhs.safeguard.common.AlertItem;
import com.jinhs.safeguard.common.UserBO;
import com.jinhs.safeguard.dao.DBTransService;

@Component
public class AccountHandler {
	@Autowired
	DBTransService dbTransService;
	public void upsertUserInfo(AccountInfoBO accountInfo){
		UserBO userInfo = accountInfo.getUser();
		AlertConfigInfoBO  alertConfig= accountInfo.getAlertConfig();
		
		if(dbTransService.isUserExisted(userInfo.getEmail())){
			//update
			dbTransService.updateOldUser(userInfo);
		}
		else{
			//new user
			dbTransService.insertNewUser(userInfo);
			for(AlertItem item:alertConfig.getAlertInfo())
				dbTransService.insertNewAlertConfigInfo(userInfo.getEmail(), item);
		}
	}
}
