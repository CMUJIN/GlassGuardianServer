package com.jinhs.safeguard.dao;

import java.util.ArrayList;
import java.util.Collections;
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

import com.jinhs.safeguard.common.TrackingDataBO;
import com.jinhs.safeguard.entity.NotificationEmailEntity;
import com.jinhs.safeguard.entity.TrackingDataEntity;

@Service
@Transactional
public class DBTransService {
	private static final Logger LOG = Logger.getLogger(DBTransService.class.getSimpleName());

	@PersistenceContext
	EntityManager em;
	
	public void insertData(TrackingDataBO data) throws PersistenceException {
		TrackingDataEntity entity = populateDataEntity(data);
		em.persist(entity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	public boolean isEmailExisted(String userId, String email) {
		try{
			Query query = em.createQuery(
					"select c from NotificationEmailEntity c where c.userId=:userId and c.email=:email");
			query.setParameter("userId", userId);
			query.setParameter("email", email);
			List<NotificationEmailEntity> result = query.getResultList();
			if(result!=null&&result.size()!=0)
				return true;
		}catch(ClassNotResolvedException e){
			LOG.error("isRateBefore DB exception "+e.getMessage());
		}
		return false;
	}
	
	public void insertAlertEmail(String userId, String email){
		NotificationEmailEntity entity = new NotificationEmailEntity();
		entity.setUserId(userId);
		entity.setEmail(email);
		em.persist(entity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}

	public void deleteAlertEmail(String userId, String email) {
		try{
			Query query = em.createQuery(
					"delete from NotificationEmailEntity c where c.userId=:userId and c.email=:email");
			query.setParameter("userId", userId);
			query.setParameter("email", email);
			query.executeUpdate();
		}catch(ClassNotResolvedException e){
			LOG.error("isRateBefore DB exception "+e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAlertEmail(String userId){
		List<String> result;
		try{
			Query query = em.createQuery(
					"select c.email from NotificationEmailEntity c where c.userId=:userId");
			query.setParameter("userId", userId);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("isRateBefore DB exception "+e.getMessage());
			return Collections.EMPTY_LIST;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<TrackingDataBO> getData(String email) throws PersistenceException {
		List<TrackingDataEntity> result;
		try{
			Query query = em.createQuery(
					"select c from TrackingDataEntity c where c.userId=:userId order by c.creationDate asc");
			query.setParameter("userId", email);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("isRateBefore DB exception "+e.getMessage());
			return Collections.EMPTY_LIST;
		}
		
		return populateDataBO(result);
	}
	
	@SuppressWarnings("unchecked")
	private List<TrackingDataBO> populateDataBO(List<TrackingDataEntity> result) {
		if(result==null)
			return Collections.EMPTY_LIST;
		List<TrackingDataBO> list = new ArrayList<TrackingDataBO>();
		for(TrackingDataEntity entity: result){
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
