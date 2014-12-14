package org.magnum.mobilecloud.video.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 * Video Controller.
 *
 * @author rahulsingh
 *
 */
@Controller
public class VideoController {

	@Autowired
	private VideoRepository videoRepo;
	
	/**
	 * Find all videos in the repository.
	 *
	 * @return Collection of videos in the data store
	 */
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Video> findAllVideos() {
		return videoRepo.findAll();
	}

	/**
	 * Save a video's metadata.
	 *
	 * @param video the video passed in as JSON into the Request Body
	 * @return the video as JSON
	 */
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
	public @ResponseBody Video saveVideo(@RequestBody Video video) {
		videoRepo.save(video);
		return video;
	}

	/**
	 * Retrieve a video by it's long id.
	 *
	 * @param id the long id; not the BigInteger video id for the BSON object in Mongo
	 * @return
	 */
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}", method = RequestMethod.GET)
	public @ResponseBody Video findVideo(@PathVariable("id") long id) {
		return videoRepo.findVideo(id);
	}

	/**
	 * Find videos by title/name.
	 *
	 * @param title title as request parameter
	 * @return Collection of Videos as JSON
	 */
	@RequestMapping(value = VideoSvcApi.VIDEO_TITLE_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Video> findVideosByTitle(
			@RequestParam("title") String title) {

		return videoRepo.findByTitle(title);
	}

	/**
	 * Find Videos by duration less than that of the duration given in
	 * the request parameter.
	 *
	 * @param duration max duration request parameter
	 * @return Collection of Videos as JSON
	 */
	@RequestMapping(value = VideoSvcApi.VIDEO_DURATION_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Video> findVideosByDurationLessThan(
			@RequestParam("duration") long duration) {

		return videoRepo.findByDurationLessThan(duration);
	}

	/**
	 * A user likes a video.
	 *
	 * @param id video long id
	 * @param p principal User information
	 * @return String response
	 * @throws NoSuchRequestHandlingMethodException if video not found
	 */
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/like", method = RequestMethod.POST)
	public @ResponseBody HttpServletResponse likeVideo(@PathVariable("id") long id, Principal p,
			HttpServletResponse response) throws NoSuchRequestHandlingMethodException {
	    String username = p.getName();
	    Video video = videoRepo.findVideo(id);
	    
	    if (video == null) {
	        throw new NoSuchRequestHandlingMethodException("There is no video with long id: " + id, VideoController.class);
	    }
	    boolean successful = videoRepo.addUserLike(video, username);
	    
	    if (!successful) {
	    	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	    }
	    
	    response.setStatus(HttpServletResponse.SC_OK);
	    return response;
	}
	
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/likedby", method = RequestMethod.GET)
	public @ResponseBody Set<String> videoLikedBy(@PathVariable("id") long id) throws NoSuchRequestHandlingMethodException {
		Video video = videoRepo.findVideo(id);
		
		if (video == null) {
			throw new NoSuchRequestHandlingMethodException("There is no video with long: " + id, VideoController.class);
		}
		
		return video.getUserLikes();
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/unlike", method = RequestMethod.POST)
	public @ResponseBody Video unlikeVideo(@PathVariable("id") long id, Principal p) throws NoSuchRequestHandlingMethodException {
	    String username = p.getName();
	    Video video = videoRepo.findVideo(id);
	    
	    if (video == null) {
	        throw new NoSuchRequestHandlingMethodException("There is no video with long id: " + id, VideoController.class);
	    }
	    videoRepo.removeUserLike(video, username);
	    
	    return video;
	}
	
}
