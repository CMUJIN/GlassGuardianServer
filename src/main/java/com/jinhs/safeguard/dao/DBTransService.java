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
	
	@SuppressWarnings("unchecked")
	public List<TrackingDataBO> getData(String userId) throws PersistenceException {
		List<TrackingDataEntity> result;
		try{
			Query query = em.createQuery(
					"select c from TrackingDataEntity c where c.userId = :userId order by c.creationDate asc");
			query.setParameter(userId, "userId");
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
