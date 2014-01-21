package com.jinhs.safeguard.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.datanucleus.exceptions.ClassNotResolvedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.jinhs.safeguard.common.AuthExchangeResponse;
import com.jinhs.safeguard.common.TrackingDataBO;
import com.jinhs.safeguard.entity.NotificationEmailEntity;
import com.jinhs.safeguard.entity.TrackingDataEntity;
import com.jinhs.safeguard.entity.TrackingLinkSequenceEntity;
import com.jinhs.safeguard.entity.UserAccountEntity;

@Service
@Transactional
public class DBTransService {
	private static final Logger LOG = Logger.getLogger(DBTransService.class
			.getSimpleName());

	@PersistenceContext
	EntityManager em;

	public String findTrackingLinkUserId(String keyStr) {
		TrackingLinkSequenceEntity trackingEntity = null;
		Key key = KeyFactory.stringToKey(keyStr);
		try {
			trackingEntity = em.find(TrackingLinkSequenceEntity.class, key);
		} catch (ClassNotResolvedException e) {
			LOG.error("isRateBefore DB exception " + e.getMessage());
			return null;
		}
		if(trackingEntity!=null)
			return trackingEntity.getUserId();
		else 
			return null;
	}
	
	public void cleanTrackingLinkData(TrackingLinkSequenceEntity deleteEntity)throws PersistenceException {
		em.remove(deleteEntity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	public List<TrackingLinkSequenceEntity> getOldTrackingLinkData(Date startDate)
			throws PersistenceException {
		List<TrackingLinkSequenceEntity> result;
		try {
			Query query = em
					.createQuery("select c from TrackingLinkSequenceEntity c where c.creationDate<:startDate");
			query.setParameter("startDate", startDate);
			result = query.getResultList();
		} catch (ClassNotResolvedException e) {
			LOG.error("isRateBefore DB exception " + e.getMessage());
			return Collections.EMPTY_LIST;
		}

		return result;
	}
	
	public String createTrackingLinkSequence(String userId){
		TrackingLinkSequenceEntity entity = new TrackingLinkSequenceEntity();
		entity.setCreationDate(new Date());
		entity.setUserId(userId);
		em.persist(entity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
		return KeyFactory.keyToString(entity.getKey());
	}

	public boolean isUserAccountExisted(String email) {
		try {
			Query query = em
					.createQuery("select c from UserAccountEntity c where c.userId=:userId");
			query.setParameter("userId", email);
			List<UserAccountEntity> result = query.getResultList();
			if (result != null && result.size() != 0)
				return true;
		} catch (ClassNotResolvedException e) {
			LOG.error("isRateBefore DB exception " + e.getMessage());
		}
		return false;
	}

	public void upsertUser(AuthExchangeResponse exchangeResponse, String email)
			throws PersistenceException {
		UserAccountEntity entity = null;
		try {
			Query query = em
					.createQuery("select c from UserAccountEntity c where c.userId=:userId");
			query.setParameter("userId", email);
			entity = (UserAccountEntity) query.getSingleResult();
		} catch (ClassNotResolvedException e) {
			LOG.error("fetchNotesByCoordinate DB exception " + e.getMessage());
			return;
		} catch (NoResultException e) {
			entity = null;
		}
		if (entity == null) {
			entity = new UserAccountEntity();
			entity.setUserId(email);
			entity.setAccessToken(exchangeResponse.getAccess_token());
			entity.setRefreshToken(exchangeResponse.getRefresh_token());
			entity.setExpirationTime(exchangeResponse.getExpires_in());
			Date creationDate = new Date();
			entity.setCreationDate(creationDate);
			entity.setLastModifiedDate(creationDate);
		} else {
			entity.setAccessToken(exchangeResponse.getAccess_token());
			entity.setRefreshToken(exchangeResponse.getRefresh_token());
			entity.setExpirationTime(exchangeResponse.getExpires_in());
			entity.setLastModifiedDate(new Date());
		}
		em.persist(entity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}

	public void insertData(TrackingDataBO data) throws PersistenceException {
		TrackingDataEntity entity = populateDataEntity(data);
		em.persist(entity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}

	public List<TrackingDataEntity> getOldData(Date startDate)
			throws PersistenceException {
		List<TrackingDataEntity> result;
		try {
			Query query = em
					.createQuery("select c from TrackingDataEntity c where c.creationDate<:startDate");
			query.setParameter("startDate", startDate);
			result = query.getResultList();
		} catch (ClassNotResolvedException e) {
			LOG.error("isRateBefore DB exception " + e.getMessage());
			return Collections.EMPTY_LIST;
		}

		return result;
	}

	public void cleanData(TrackingDataEntity deleteEntity)
			throws PersistenceException {
		em.remove(deleteEntity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}

	public boolean isNotificationEmailExisted(String userId, String email) {
		try {
			Query query = em
					.createQuery("select c from NotificationEmailEntity c where c.userId=:userId and c.email=:email");
			query.setParameter("userId", userId);
			query.setParameter("email", email);
			List<NotificationEmailEntity> result = query.getResultList();
			if (result != null && result.size() != 0)
				return true;
		} catch (ClassNotResolvedException e) {
			LOG.error("isRateBefore DB exception " + e.getMessage());
		}
		return false;
	}

	public void insertAlertEmail(String userId, String email) {
		NotificationEmailEntity entity = new NotificationEmailEntity();
		entity.setUserId(userId);
		entity.setEmail(email);
		em.persist(entity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}

	public void deleteAlertEmail(String userId, String email) {
		try {
			Query query = em
					.createQuery("delete from NotificationEmailEntity c where c.userId=:userId and c.email=:email");
			query.setParameter("userId", userId);
			query.setParameter("email", email);
			query.executeUpdate();
		} catch (ClassNotResolvedException e) {
			LOG.error("isRateBefore DB exception " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> getAlertEmail(String userId) {
		List<String> result;
		try {
			Query query = em
					.createQuery("select c.email from NotificationEmailEntity c where c.userId=:userId");
			query.setParameter("userId", userId);
			result = query.getResultList();
		} catch (ClassNotResolvedException e) {
			LOG.error("isRateBefore DB exception " + e.getMessage());
			return Collections.EMPTY_LIST;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<TrackingDataBO> getData(String email)
			throws PersistenceException {
		List<TrackingDataEntity> result;
		try {
			Query query = em
					.createQuery("select c from TrackingDataEntity c where c.userId=:userId order by c.creationDate asc");
			query.setParameter("userId", email);
			result = query.getResultList();
		} catch (ClassNotResolvedException e) {
			LOG.error("isRateBefore DB exception " + e.getMessage());
			return Collections.EMPTY_LIST;
		}

		return populateDataBO(result);
	}

	@SuppressWarnings("unchecked")
	private List<TrackingDataBO> populateDataBO(List<TrackingDataEntity> result) {
		if (result == null)
			return Collections.EMPTY_LIST;
		List<TrackingDataBO> list = new ArrayList<TrackingDataBO>();
		for (TrackingDataEntity entity : result) {
			TrackingDataBO data = new TrackingDataBO();
			data.setImagePath(entity.getImage());
			data.setAudioPath(entity.getAudio());
			data.setEmail(entity.getUserId());
			data.setLatitude(entity.getLatitude());
			data.setLongtitude(entity.getLongtitude());
			data.setCreationDate(entity.getCreationDate());
			list.add(data);
		}

		return list;
	}

	private TrackingDataEntity populateDataEntity(TrackingDataBO data) {
		TrackingDataEntity entity = new TrackingDataEntity();
		entity.setUserId(data.getEmail());
		entity.setImage(data.getImagePath());
		entity.setAudio(data.getAudioPath());
		entity.setAddress(data.getAddress());
		entity.setLatitude(data.getLatitude());
		entity.setLongtitude(data.getLongtitude());
		entity.setCreationDate(new Date());
		return entity;
	}

}
