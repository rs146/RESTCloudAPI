package org.magnum.mobilecloud.video.repository;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for Videos in the Mongo database.
 *
 * @author rahulsingh
 */
@Repository
public class VideoRepository {
	
	@Autowired
	private MongoOperations mongo;

	/**
	 * Save video.
	 *
	 * @param video video to be saved as BSON
	 */
	public void save(Video video) {
		long id = findAll().size() + 1;
		video.setId(id);
		mongo.save(video, "videos");
	}

	/**
	 * Find all videos in Mongo collection.
	 *
	 * @return list of videos
	 */
	public Collection<Video> findAll() {
		return mongo.findAll(Video.class, "videos");
	}

	/**
	 * Find all videos by title.
	 *
	 * @param title title
	 * @return list of videos
	 */
	public Collection<Video> findByTitle(String title) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(title));
		return mongo.find(query, Video.class);
	}

	/**
	 * Find all videos by duration less than max duration.
	 *
	 * @param maxDuration max duration
	 * @return list of videos
	 */
	public Collection<Video> findByDurationLessThan(long maxDuration) {
		Query query = new Query();
		query.addCriteria(Criteria.where("duration").lt(maxDuration));
		return mongo.find(query, Video.class);
	}

	/**
	 * Find video by it's long id; not BigInteger videoId.
	 *
	 * @param id id
	 * @return Video object
	 */
	public Video findVideo(long id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return mongo.findOne(query, Video.class);
	}

	/**
	 * Add user name to the set of likes for the video.
	 *
	 * @param video the video to be updated
	 * @param user the user name to be added to the video's likes
	 * @return if video updated - user has liked it before
	 */
	public boolean addUserLike(Video video, String user) {
		if (video.getUserLikes().contains(user)) {
			return false;
		}
	    video.addUserLike(user);
	    long likes = video.getLikes();
	    video.setLikes(likes++);
	    mongo.save(video, "videos");
	    return true;
	}

	
	public Video removeUserLike(Video video, String user) {
		video.removeUserLike(user);
		long likes = video.getLikes();
		video.setLikes(likes--);
		mongo.save(video, "videos");
		return video;
	}
}