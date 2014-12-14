package org.magnum.mobilecloud.video.repository;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.Objects;

/**
 * A simple object to represent a video and its URL for viewing.
 * 
 * You probably need to, at a minimum, add some annotations to this
 * class.
 * 
 * You are free to add annotations, members, and methods to this
 * class. However, you probably should not change the existing
 * methods or member variables. If you do change them, you need
 * to make sure that they are serialized into JSON in a way that
 * matches what is expected by the auto-grader.
 * 
 * @author mitchell
 */
@Document(collection = "videos")
public class Video {

	@Id
	private BigInteger videoId;

	@Indexed(unique = true)
	private long id;
	private String name;
	private String url;
	private long duration;
	private long likes;
	private Set<String> userLikes = new HashSet<String>();
	
	public Video() {
	}

	public Video(String name, String url, long duration, long likes) {
		super();
		this.name = name;
		this.url = url;
		this.duration = duration;
		this.likes = likes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getLikes() {
		return likes;
	}
	
	public void setLikes(long likes) {
		this.likes = likes;
	}
	
	/**
	 * Two Videos will generate the same hashcode if they have exactly the same
	 * values for their name, url, and duration.
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(name, url, duration);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Video) {
			Video other = (Video) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(name, other.name)
					&& Objects.equal(url, other.url)
					&& duration == other.duration;
		} else {
			return false;
		}
	}

	public BigInteger getVideoId() {
		return videoId;
	}

	public void setVideoId(BigInteger videoId) {
		this.videoId = videoId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

    public Set<String> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(Set<String> users) {
        this.userLikes = users;
    }
    
    public void addUserLike(String user) {
        this.userLikes.add(user);
    }
    
    public void removeUserLike(String user) {
        this.userLikes.remove(user);
    }
}
