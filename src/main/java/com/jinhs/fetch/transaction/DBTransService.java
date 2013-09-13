package com.jinhs.fetch.transaction;

import java.util.ArrayList;
import java.util.Collections;
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

import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.entity.NoteCacheEntity;
import com.jinhs.fetch.entity.NoteEntity;

@Service
@Transactional
public class DBTransService {
	private static final Logger LOG = Logger.getLogger(DBTransService.class.getSimpleName());

	@PersistenceContext
	EntityManager em;
	
	@Transactional(readOnly = true)
	public List<NoteBo> fetchNotesByCoordinate(String userId, double latitude, double longtitude) throws PersistenceException{
		List<NoteEntity> result;
		try{
			Query query = em.createQuery(
					"select c from NoteEntity c where c.latitude=:latitude and c.longtitude=:longtitude and c.user_id=:userid");
			query.setParameter("latitude", latitude);
			query.setParameter("longtitude", longtitude);
			query.setParameter("userid", userId);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception");
			return Collections.EMPTY_LIST;
		}
		List<NoteBo> noteList = convertToNoteBoList(result);
		return noteList;
	}
	
	@Transactional(readOnly = true)
	public List<NoteBo> fetchNotesByAddress(String userId, String address) throws PersistenceException{
		List<NoteEntity> result;
		try{
			Query query = em.createQuery(
					"select c from NoteEntity c where c.address=:address and c.user_id=:userid");
			query.setParameter("address", address);
			query.setParameter("userid", userId);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.info("fetchNotesByAddress DB exception");
			return Collections.EMPTY_LIST;
		}
		List<NoteBo> noteList = convertToNoteBoList(result);
		return noteList;
	}
	
	@Transactional(readOnly = true)
	public List<NoteBo> fetchNotesByZip(String userId, String zipCode) throws PersistenceException{
		List<NoteEntity> result;
		try{
			Query query = em.createQuery(
					"select c from NoteEntity c where c.zip_code=:zipcode and c.user_id=:userid");
			query.setParameter("zipcode", zipCode);
			query.setParameter("userid", userId);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.info("fetchNotes DB exception");
			return Collections.EMPTY_LIST;
		}
		List<NoteBo> noteList = convertToNoteBoList(result);
		return noteList;
	}
	

	public void insertNote(NoteBo noteBo) throws PersistenceException {
		NoteEntity entity = populateNoteEntity(noteBo);
		em.persist(entity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	
	@Transactional(readOnly = true)
	public List<NoteBo> fetchNotesFromCache(String identityKey, int sequenceId) throws PersistenceException{
		List<NoteCacheEntity> result;
		try{
			Query query = em.createQuery(
					"select c from NoteCacheEntity c where c.identity_key=:identity_key and c.sequence_id=:sequence_id");
			query.setParameter("identity_key", identityKey);
			query.setParameter("sequence_id", sequenceId);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception");
			return Collections.EMPTY_LIST;
		}
		List<NoteBo> noteList = convertCacheToNoteBoList(result);
		return noteList;
	}
	// TODO insert cache
	private List<NoteBo> convertCacheToNoteBoList(List<NoteCacheEntity> result) {
		List<NoteBo> noteBoList = new ArrayList<NoteBo>();
		for(NoteCacheEntity entity: result)
			noteBoList.add(populateNoteBoFromCache(entity));
		return noteBoList;
	}

	private NoteBo populateNoteBoFromCache(NoteCacheEntity entity) {
		NoteBo noteBo = new NoteBo();
		noteBo.setUser_id(entity.getUser_id());
		noteBo.setDate(entity.getDate());
		noteBo.setLatitude(entity.getLatitude());
		noteBo.setLongtitude(entity.getLongtitude());
		noteBo.setTimeline_id(entity.getTimeline_id());
		return noteBo;
	}

	private List<NoteBo> convertToNoteBoList(List<NoteEntity> result) {
		List<NoteBo> noteBoList = new ArrayList<NoteBo>();
		for(NoteEntity entity:result)
			noteBoList.add(populateNoteBo(entity));
		return noteBoList;
	}
	

	private NoteBo populateNoteBo(NoteEntity entity){
		NoteBo noteBo = new NoteBo();
		noteBo.setUser_id(entity.getUser_id());
		noteBo.setValuation(entity.getValuation());
		noteBo.setText_note(entity.getText_note());
		noteBo.setImage_note(entity.getImage_note());
		noteBo.setVideo_note(entity.getVideo_note());
		noteBo.setDate(entity.getDate());
		noteBo.setLatitude(entity.getLatitude());
		noteBo.setLongtitude(entity.getLongtitude());
		noteBo.setAccuracy(entity.getAccuracy());
		noteBo.setDisplay_name(entity.getDisplay_name());
		noteBo.setAddress(entity.getAddress());
		noteBo.setLocation_id(entity.getLocation_id());
		noteBo.setZip_code(entity.getZip_code());
		noteBo.setTimeline_id(entity.getTimeline_id());
		noteBo.setAttachment_id(entity.getAttachment_id());
		return noteBo;
	}

	private NoteEntity populateNoteEntity(NoteBo noteBo) {
		NoteEntity entity = new NoteEntity();
		entity.setUser_id(noteBo.getUser_id());
		entity.setValuation(noteBo.getValuation());
		entity.setText_note(noteBo.getText_note());
		entity.setImage_note(noteBo.getImage_note());
		entity.setVideo_note(noteBo.getVideo_note());
		entity.setDate(noteBo.getDate());
		entity.setLatitude(noteBo.getLatitude());
		entity.setLongtitude(noteBo.getLongtitude());
		entity.setAccuracy(noteBo.getAccuracy());
		entity.setDisplay_name(noteBo.getDisplay_name());
		entity.setAddress(noteBo.getAddress());
		entity.setLocation_id(noteBo.getLocation_id());
		entity.setZip_code(noteBo.getZip_code());
		entity.setTimeline_id(noteBo.getTimeline_id());
		entity.setAttachment_id(noteBo.getAttachment_id());
		return entity;
	}
	
}
