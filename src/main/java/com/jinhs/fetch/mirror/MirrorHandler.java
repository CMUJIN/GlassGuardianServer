package com.jinhs.fetch.mirror;

import java.io.IOException;

public interface MirrorHandler {
	public void insertSubscription(String userId, String collection) throws IOException;
	public void deleteSubscription(String userId, String subscriptionId) throws IOException;
	public void insertItem(String userId) throws IOException;
	public void insertItemWithAction(String userId) throws IOException;
	public void insertContact(String userId) throws IOException;
	public void deleteContact(String userId) throws IOException;
	public void insertItemAllUsers(String userId) throws IOException;
}
