package com.jinhs.resource.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.MenuValue;
import com.google.api.services.mirror.model.Notification;
import com.google.api.services.mirror.model.UserAction;
import com.jinhs.fetch.mirror.CustomActionConfigEnum;
import com.jinhs.fetch.mirror.MenuItemActionEnum;
import com.jinhs.fetch.resource.controller.NotificationController;

public class NotificationControllerTest {/*

	@Test
	public void test() {
		NotificationController controller = new NotificationController();
		Notification request = new Notification();
		request.setUserToken("");
		request.setItemId("");
		List<MenuItem> menuItemList = new ArrayList<MenuItem>();
		addMenuItem(menuItemList, MenuItemActionEnum.TOGGLE_PINNED);
		addCustomMenuItem(menuItemList, CustomActionConfigEnum.FETCH);
		addCustomMenuItem(menuItemList, CustomActionConfigEnum.PUSH);
		addCustomMenuItem(menuItemList, CustomActionConfigEnum.LIKE);
		addCustomMenuItem(menuItemList, CustomActionConfigEnum.DISLIKE);
		UserAction action = new UserAction();
		action.setType("CUSTOM");
		action.setPayload("");
		List<UserAction> list = new ArrayList<UserAction>();
		list.add(action);
		request.setUserActions(list);
		try {
			controller.getNotification(request, new MockHttpServletResponse());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void addMenuItem(List<MenuItem> menuItemList, MenuItemActionEnum action) {
		menuItemList.add(new MenuItem().setAction(action.getValue()));
	}
	
	private void addCustomMenuItem(List<MenuItem> menuItemList, CustomActionConfigEnum config){
		MenuItem customItem = new MenuItem();
		customItem.setAction(config.getType());
		customItem.setId(config.getName());
		customItem.setValues(buildMenuValues(config));
		menuItemList.add(customItem);
	}
	
	private List<MenuValue> buildMenuValues(CustomActionConfigEnum config) {
		List<MenuValue> menuValues = new ArrayList<MenuValue>();
		MenuValue defaultValue = new MenuValue();
		defaultValue.setDisplayName(config.getName());
		defaultValue.setState("DEFAULT");
		defaultValue.setIconUrl(config.getIconUrl());
		menuValues.add(defaultValue);

		if (config.hasPending()) {
			MenuValue pendingValue = new MenuValue();
			pendingValue.setDisplayName(config.getPendingName());
			pendingValue.setState("PENDING");
			pendingValue.setIconUrl(config.getPendingIconUrl());
			menuValues.add(defaultValue);
		}

		if (config.hasComplete()) {
			MenuValue completeValue = new MenuValue();
			completeValue.setDisplayName(config.getCompleteName());
			completeValue.setState("COMPLETE");
			completeValue.setIconUrl(config.getCompleteIconUrl());
			menuValues.add(defaultValue);
		}
		return menuValues;
	}
*/}
