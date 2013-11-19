package com.jinhs.safeguard.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.datanucleus.exceptions.ClassNotResolvedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinhs.safeguard.common.AlertItem;
import com.jinhs.safeguard.common.TrackerBO;
import com.jinhs.safeguard.common.UserBO;
import com.jinhs.safeguard.entity.AlertConfigEntity;
import com.jinhs.safeguard.entity.TrackerEntity;
import com.jinhs.safeguard.entity.UserEntity;

@Service
@Transactional
public class DBTransService {
	private static final int MAX_CHACE_DELETE_QUERY_THRESHOLD = 1000;

	private static final Logger LOG = Logger.getLogger(DBTransService.class.getSimpleName());

	@PersistenceContext
	EntityManager em;
	
	public void insertTracker(TrackerBO tracker) throws PersistenceException {
		TrackerEntity entity = populateTrackerEntity(tracker);
		em.persist(entity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	@Transactional(readOnly = true)
	public boolean isUserExisted(String email) throws PersistenceException{
		List<UserEntity> result;
		try{
			Query query = em.createQuery(
					"select c from UserEntity c where c.email=:email");
			query.setParameter("email", email);
			query.setMaxResults(1);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("isRateBefore DB exception "+e.getMessage());
			return false;
		}
		return !result.isEmpty();
	}
	
	public void insertNewUser(UserBO userBo) throws PersistenceException {
		UserEntity entity = populateUesrEntity(userBo);
		em.persist(entity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	public void insertNewAlertConfigInfo(String email, AlertItem item) throws PersistenceException {
		AlertConfigEntity entity = populateAlertConfigEntity(email, item);
		em.persist(entity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}

	public void updateOldUser(UserBO userBo) throws PersistenceException {
		List<UserEntity> result = null;
		try{
			Query query = em.createQuery(
					"select c from UserEntity c where c.email=:email");
			query.setParameter("email", userBo.getEmail());
			query.setMaxResults(1);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return;
		}
		UserEntity userEntity = result.get(0);
		userEntity.setLastUpdateDate(new Date());
		if(userBo.getProfileImage()!=null)
			userEntity.setProfileImage(userBo.getProfileImage());
		if(userBo.getUserName()!=null)
			userEntity.setUserName(userBo.getUserName());
		em.persist(userEntity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}

	
	private AlertConfigEntity populateAlertConfigEntity(String email,
			AlertItem item) {
		AlertConfigEntity entity = new AlertConfigEntity();
		entity.setEmail(email);
		entity.setAlertType(item.getAlertType());
		entity.setAlertInfo(item.getAlertInfo());
		return entity;
	}
	
	private UserEntity populateUesrEntity(UserBO userBo) {
		UserEntity entity = new UserEntity();
		entity.setEmail(userBo.getEmail());
		entity.setLastUpdateDate(new Date());
		entity.setProfileImage(userBo.getProfileImage());
		entity.setUserName(userBo.getUserName());
		return entity;
	}

	private TrackerEntity populateTrackerEntity(TrackerBO tracker) {
		TrackerEntity entity = new TrackerEntity();
		entity.setDate(new Date());
		entity.setEmail(tracker.getEmail());
		entity.setGuardId(tracker.getGuardId());
		entity.setLatitude(tracker.getLatitude());
		entity.setLongtitude(tracker.getLongtitude());
		entity.setSnapshot(tracker.getSnapshot());
		return entity;
	}
	
}
