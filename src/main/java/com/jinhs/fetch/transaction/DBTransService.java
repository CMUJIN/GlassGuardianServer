package com.jinhs.fetch.transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
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

import com.google.appengine.api.datastore.Key;
import com.jinhs.fetch.bo.CacheNoteBo;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.bo.ZoneRateBo;
import com.jinhs.fetch.entity.NoteCacheEntity;
import com.jinhs.fetch.entity.NoteEntity;
import com.jinhs.fetch.entity.RateRecordEntity;
import com.jinhs.fetch.entity.ZoneRateEntity;

@Service
@Transactional
public class DBTransService {
	private static final int MAX_CHACE_DELETE_QUERY_THRESHOLD = 1000;

	private static final Logger LOG = Logger.getLogger(DBTransService.class.getSimpleName());

	@PersistenceContext
	EntityManager em;
	
	@Transactional(readOnly = true)
	public NoteBo fetchFirstNoteByCoordinate(double latitude, double longtitude) throws PersistenceException{
		List<NoteEntity> result;
		try{
			Query query = em.createQuery(
					"select c from NoteEntity c where c.latitude=:latitude and c.longtitude=:longtitude order by c.date asc");
			query.setParameter("latitude", latitude);
			query.setParameter("longtitude", longtitude);
			query.setMaxResults(1);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return null;
		}
		if(result==null||result.size()==0)
			return null;
		List<NoteBo> noteList = convertToNoteBoList(result);
		return noteList.get(0);
	}
	
	@Transactional(readOnly = true)
	public List<NoteBo> fetchNotesByCoordinate(double latitude, double longtitude) throws PersistenceException{
		List<NoteEntity> result;
		try{
			Query query = em.createQuery(
					"select c from NoteEntity c where c.latitude=:latitude and c.longtitude=:longtitude order by c.date desc");
			query.setParameter("latitude", latitude);
			query.setParameter("longtitude", longtitude);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return Collections.EMPTY_LIST;
		}
		List<NoteBo> noteList = convertToNoteBoList(result);
		return noteList;
	}
	
	@Transactional(readOnly = true)
	public List<NoteBo> fetchFirstGroupNotesByCoordinate(String userId, double latitude, double longtitude, int maxNum) throws PersistenceException{
		List<NoteEntity> result;
		try{
			Query query = em.createQuery(
					"select c from NoteEntity c where c.latitude=:latitude and c.longtitude=:longtitude and c.user_id=:userid order by c.date desc ");
			query.setParameter("latitude", latitude);
			query.setParameter("longtitude", longtitude);
			query.setParameter("userid", userId);
			query.setMaxResults(maxNum);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return Collections.EMPTY_LIST;
		}
		List<NoteBo> noteList = convertToNoteBoList(result);
		return noteList;
	}
	
	@Transactional(readOnly = true)
	public List<NoteBo> fetchNotesByAddress(String address) throws PersistenceException{
		List<NoteEntity> result;
		try{
			Query query = em.createQuery(
					"select c from NoteEntity c where c.address=:address order by c.date desc");
			query.setParameter("address", address);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.info("fetchNotesByAddress DB exception "+e.getMessage());
			return Collections.EMPTY_LIST;
		}
		List<NoteBo> noteList = convertToNoteBoList(result);
		return noteList;
	}
	
	@Transactional(readOnly = true)
	public List<NoteBo> fetchNotesByZip(String zipCode) throws PersistenceException{
		List<NoteEntity> result;
		try{
			Query query = em.createQuery(
					"select c from NoteEntity c where c.zip_code=:zipcode order by c.date desc");
			query.setParameter("zipcode", zipCode);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.info("fetchNotes DB exception "+e.getMessage());
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
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return Collections.EMPTY_LIST;
		}
		List<NoteBo> noteList = convertCacheToNoteBoList(result);
		return noteList;
	}
	
	@Transactional(readOnly = true)
	public ZoneRateBo getRateByCoordinate(double latitude, double longtitude) throws PersistenceException{
		List<ZoneRateEntity> result;
		try{
			Query query = em.createQuery(
					"select c from ZoneRateEntity c where c.latitude=:latitude and c.longtitude=:longtitude");
			query.setParameter("latitude", latitude);
			query.setParameter("longtitude", longtitude);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return null;
		}
		if(result.isEmpty())
			return null;
		return convertToZoneRateBo(result.get(0));
	}
	
	@Transactional(readOnly = true)
	public ZoneRateBo getRateByAddress(String address) throws PersistenceException{
		List<ZoneRateEntity> result;
		try{
			Query query = em.createQuery(
					"select c from ZoneRateEntity c where c.address=:address");
			query.setParameter("address", address);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return null;
		}
		if(result.isEmpty())
			return null;
		return convertToZoneRateBo(result.get(0));
	}
	
	@Transactional(readOnly = true)
	public ZoneRateBo getRateByZip(String zip_code) throws PersistenceException{
		List<ZoneRateEntity> result;
		try{
			Query query = em.createQuery(
					"select c from ZoneRateEntity c where c.zip_code=:zip_code ");
			query.setParameter("zip_code", zip_code);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return null;
		}
		if(result.isEmpty())
			return null;
		return convertToZoneRateBo(result.get(0));
	}

	public void addRateByCoordinate(double latitude, double longtitude, boolean isLike) throws PersistenceException{
		List<ZoneRateEntity> result = null;
		try{
			Query query = em.createQuery(
					"select c from ZoneRateEntity c where c.latitude=:latitude and c.longtitude=:longtitude");
			query.setParameter("latitude", latitude);
			query.setParameter("longtitude", longtitude);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return;
		}
		ZoneRateEntity rateEntity;
		if(result==null||result.isEmpty()){
			rateEntity = new ZoneRateEntity();
			rateEntity.setLatitude(latitude);
			rateEntity.setLongtitude(longtitude);
			if(isLike)
				rateEntity.setLike_hit(1);
			else
				rateEntity.setDislike_hit(1);
		}
		else{
			rateEntity = result.get(0);
			if(isLike)
				rateEntity.setLike_hit(rateEntity.getLike_hit()+1);
			else
				rateEntity.setDislike_hit(rateEntity.getDislike_hit()+1);
		}
		em.persist(rateEntity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	public void updateRateByCoordinate(double latitude, double longtitude, boolean isLike) throws PersistenceException{
		List<ZoneRateEntity> result = null;
		try{
			Query query = em.createQuery(
					"select c from ZoneRateEntity c where c.latitude=:latitude and c.longtitude=:longtitude");
			query.setParameter("latitude", latitude);
			query.setParameter("longtitude", longtitude);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return;
		}
		ZoneRateEntity rateEntity;
		if(result==null||result.isEmpty()){
			rateEntity = new ZoneRateEntity();
			rateEntity.setLatitude(latitude);
			rateEntity.setLongtitude(longtitude);
			if(isLike)
				rateEntity.setLike_hit(1);
			else
				rateEntity.setDislike_hit(1);
		}
		else{
			rateEntity = result.get(0);
			if(isLike){
				rateEntity.setLike_hit(rateEntity.getLike_hit()+1);
				rateEntity.setDislike_hit(rateEntity.getDislike_hit()-1);
			}
			else{
				rateEntity.setDislike_hit(rateEntity.getDislike_hit()+1);
				rateEntity.setLike_hit(rateEntity.getLike_hit()-1);
			}
		}
		em.persist(rateEntity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	public void addRateByAddress(String address, boolean isLike) throws PersistenceException{
		List<ZoneRateEntity> result = null;
		try{
			Query query = em.createQuery(
					"select c from ZoneRateEntity c where c.address=:address");
			query.setParameter("address", address);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return;
		}
		ZoneRateEntity rateEntity;
		if(result==null||result.isEmpty()){
			rateEntity = new ZoneRateEntity();
			rateEntity.setAddress(address);
			if(isLike)
				rateEntity.setLike_hit(1);
			else
				rateEntity.setDislike_hit(1);
		}
		else{
			rateEntity = result.get(0);
			if(isLike)
				rateEntity.setLike_hit(rateEntity.getLike_hit()+1);
			else
				rateEntity.setDislike_hit(rateEntity.getDislike_hit()+1);
		}
		em.persist(rateEntity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	public void updateRateByAddress(String address, boolean isLike) throws PersistenceException{
		List<ZoneRateEntity> result = null;
		try{
			Query query = em.createQuery(
					"select c from ZoneRateEntity c where c.address=:address");
			query.setParameter("address", address);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return;
		}
		ZoneRateEntity rateEntity;
		if(result==null||result.isEmpty()){
			rateEntity = new ZoneRateEntity();
			rateEntity.setAddress(address);
			if(isLike)
				rateEntity.setLike_hit(1);
			else
				rateEntity.setDislike_hit(1);
		}
		else{
			rateEntity = result.get(0);
			if(isLike){
				rateEntity.setLike_hit(rateEntity.getLike_hit()+1);
				rateEntity.setDislike_hit(rateEntity.getDislike_hit()-1);
			}
			else{
				rateEntity.setDislike_hit(rateEntity.getDislike_hit()+1);
				rateEntity.setLike_hit(rateEntity.getLike_hit()-1);
			}
		}
		em.persist(rateEntity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	public void addRateByZip(String zip_code, boolean isLike) throws PersistenceException{
		List<ZoneRateEntity> result = null;
		try{
			Query query = em.createQuery(
					"select c from ZoneRateEntity c where c.zip_code=:zip_code ");
			query.setParameter("zip_code", zip_code);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return;
		}
		ZoneRateEntity rateEntity;
		if(result==null||result.isEmpty()){
			rateEntity = new ZoneRateEntity();
			rateEntity.setZip_code(zip_code);
			if(isLike)
				rateEntity.setLike_hit(1);
			else
				rateEntity.setDislike_hit(1);
		}
		else{
			rateEntity = result.get(0);
			if(isLike)
				rateEntity.setLike_hit(rateEntity.getLike_hit()+1);
			else
				rateEntity.setDislike_hit(rateEntity.getDislike_hit()+1);
		}
		em.persist(rateEntity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	public void updateRateByZip(String zip_code, boolean isLike) throws PersistenceException{
		List<ZoneRateEntity> result = null;
		try{
			Query query = em.createQuery(
					"select c from ZoneRateEntity c where c.zip_code=:zip_code ");
			query.setParameter("zip_code", zip_code);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("fetchNotesByCoordinate DB exception "+e.getMessage());
			return;
		}
		ZoneRateEntity rateEntity;
		if(result==null||result.isEmpty()){
			rateEntity = new ZoneRateEntity();
			rateEntity.setZip_code(zip_code);
			if(isLike)
				rateEntity.setLike_hit(1);
			else
				rateEntity.setDislike_hit(1);
		}
		else{
			rateEntity = result.get(0);
			if(isLike){
				rateEntity.setLike_hit(rateEntity.getLike_hit()+1);
				rateEntity.setDislike_hit(rateEntity.getDislike_hit()-1);
			}
			else{
				rateEntity.setDislike_hit(rateEntity.getDislike_hit()+1);
				rateEntity.setLike_hit(rateEntity.getLike_hit()-1);
			}
		}
		em.persist(rateEntity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	@Transactional(readOnly = true)
	public int isRateBefore(String userId, double latitude, double longtitude) throws PersistenceException{
		List<RateRecordEntity> result;
		try{
			Query query = em.createQuery(
					"select c from RateRecordEntity c where c.latitude=:latitude and c.longtitude=:longtitude and c.user_id=:userId");
			query.setParameter("latitude", latitude);
			query.setParameter("longtitude", longtitude);
			query.setParameter("userId", userId);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("isRateBefore DB exception "+e.getMessage());
			return 0;
		}
		if(result.isEmpty())
			return 0;
		return result.get(0).getRate();
	}
	
	public void upsertRateRecord(String userId, double latitude, double longtitude, int rate) throws PersistenceException{
		List<RateRecordEntity> result = null;
		try{
			Query query = em.createQuery(
					"select c from RateRecordEntity c where c.latitude=:latitude and c.longtitude=:longtitude and c.user_id=:userId");
			query.setParameter("latitude", latitude);
			query.setParameter("longtitude", longtitude);
			query.setParameter("userId", userId);
			result = query.getResultList();
		}catch(ClassNotResolvedException e){
			LOG.error("updateRateRecord DB exception "+e.getMessage());
			return;
		}
		RateRecordEntity rateRecordEntity;
		if(result==null||result.isEmpty()){
			rateRecordEntity = new RateRecordEntity();
			rateRecordEntity.setUser_id(userId);
			rateRecordEntity.setLatitude(latitude);
			rateRecordEntity.setLongtitude(longtitude);
			rateRecordEntity.setRate(rate);
			rateRecordEntity.setDate(new Date());
			em.persist(rateRecordEntity);
		}
		else{
			rateRecordEntity = result.get(0);
			rateRecordEntity.setRate(rate);
			em.merge(rateRecordEntity);
		}
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	public List<NoteCacheEntity> getDeleteCacheEntity(Date cleanDate) throws PersistenceException {
		List<NoteCacheEntity> deleteList = null;
		try {
			Query query = em
					.createQuery("select c from NoteCacheEntity c where c.date < :date");
			query.setParameter("date",cleanDate);
			query.setMaxResults(MAX_CHACE_DELETE_QUERY_THRESHOLD);
			deleteList = query.getResultList();
		} catch (ClassNotResolvedException e) {
			LOG.error("cleanNoteCache DB exception " + e.getMessage());
		}
		return deleteList;
	}
	
	public void deleteNoteCache(NoteCacheEntity entity)throws PersistenceException {
		NoteCacheEntity deleteEntity = em.find(NoteCacheEntity.class, entity.getKey());
		em.remove(deleteEntity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();

	}

	private ZoneRateBo convertToZoneRateBo(ZoneRateEntity zoneRate) {
		ZoneRateBo rateBo = new ZoneRateBo();
		rateBo.setAddress(zoneRate.getAddress());
		rateBo.setLike_hit(zoneRate.getLike_hit());
		rateBo.setDislike_hit(zoneRate.getDislike_hit());
		rateBo.setLatitude(zoneRate.getLatitude());
		rateBo.setLongtitude(zoneRate.getLongtitude());
		rateBo.setZip_code(zoneRate.getZip_code());
		return rateBo;
	}
	
	public void insertCacheNote(CacheNoteBo cache) throws PersistenceException {
		NoteCacheEntity cacheEntity = populateNoteCacheEntity(cache);
		em.persist(cacheEntity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	public void deleteEmptyNote(Key key) {
		NoteEntity deleteEntity = em.find(NoteEntity.class, key);
		em.remove(deleteEntity);
		em.setFlushMode(FlushModeType.AUTO);
		em.flush();
	}
	
	private NoteCacheEntity populateNoteCacheEntity(CacheNoteBo cacheBo) {
		NoteCacheEntity cacheEntity = new NoteCacheEntity();
		NoteBo note = cacheBo.getNoteBo();
		cacheEntity.setTimeline_id(cacheBo.getNoteBo().getTimeline_id());
		cacheEntity.setUser_id(cacheBo.getNoteBo().getUser_id());
		cacheEntity.setDate(note.getDate());
		cacheEntity.setIdentity_key(cacheBo.getIdentity_key());
		cacheEntity.setLatitude(note.getLatitude());
		cacheEntity.setLongtitude(note.getLongtitude());
		cacheEntity.setSequence_id(cacheBo.getSequenceId());
		if(note.getAttachment_id()!=null)
			cacheEntity.setAttachment_id(note.getAttachment_id());
		return cacheEntity;
	}

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
		noteBo.setKey(entity.getKey());
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
