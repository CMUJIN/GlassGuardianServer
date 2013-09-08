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
import com.jinhs.fetch.entity.NoteEntity;

@Service
@Transactional
public class DBTransService {
	private static final Logger LOG = Logger.getLogger(DBTransService.class.getSimpleName());

	@PersistenceContext
	EntityManager em;
	
	@Transactional(readOnly = true)
	public List<NoteBo> fetchNotes(String userId, String zipCode) throws PersistenceException{
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
	
	private List<NoteBo> convertToNoteBoList(List<NoteEntity> result) {
		List<NoteBo> noteBoList = new ArrayList<NoteBo>();
		for(NoteEntity entity:result)
			noteBoList.add(populateNoteBo(entity));
		return noteBoList;
	}
	

	public void insertNote(NoteBo noteBo) throws PersistenceException {
		NoteEntity entity = populateNoteEntity(noteBo);
		em.persist(entity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
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
		entity.setZip_code(noteBo.getZip_code());
		entity.setTimeline_id(noteBo.getTimeline_id());
		entity.setAttachment_id(noteBo.getAttachment_id());
		return entity;
	}
	
}
