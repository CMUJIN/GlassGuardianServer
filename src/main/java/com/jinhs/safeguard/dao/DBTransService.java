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

import com.jinhs.safeguard.common.DataBO;
import com.jinhs.safeguard.entity.DataEntity;

@Service
@Transactional
public class DBTransService {
	private static final int MAX_CHACE_DELETE_QUERY_THRESHOLD = 1000;

	private static final Logger LOG = Logger.getLogger(DBTransService.class.getSimpleName());

	@PersistenceContext
	EntityManager em;
	
	public void insertData(DataBO data) throws PersistenceException {
		DataEntity entity = populateDataEntity(data);
		em.persist(entity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	public List<DataBO> getData() throws PersistenceException {
		List<DataEntity> result;
		try{
			Query query = em.createQuery(
					"select c from DataEntity c order by c.date asc");
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("isRateBefore DB exception "+e.getMessage());
			return Collections.EMPTY_LIST;
		}
		
		return populateDataBO(result);
	}
	
	private List<DataBO> populateDataBO(List<DataEntity> result) {
		if(result==null)
			return Collections.EMPTY_LIST;
		List<DataBO> list = new ArrayList<DataBO>();
		for(DataEntity entity: result){
			DataBO data = new DataBO();
			data.setAudio(entity.getAudio());
			data.setDate(entity.getDate());
			data.setEmail(entity.getEmail());
			data.setLatitude(entity.getLatitude());
			data.setLongtitude(entity.getLongtitude());
			data.setSnapshot(entity.getSnapshot());
			list.add(data);
		}
			
		return list;
	}

	private DataEntity populateDataEntity(DataBO data) {
		DataEntity entity = new DataEntity();
		entity.setEmail(data.getEmail());
		entity.setDate(new Date());
		entity.setLatitude(data.getLatitude());
		entity.setLongtitude(data.getLongtitude());
		entity.setAudio(data.getAudio());
		entity.setSnapshot(data.getSnapshot());
		return entity;
	}
}
