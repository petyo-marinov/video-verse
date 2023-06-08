package controller;

import dao.UserDBDAO;
import dao.VideoDBDAO;
import model.Video;

public class VideoController {

    private static VideoController instance;
    private VideoController() {}

    public static VideoController getInstance() {
        if(instance == null){
            instance = new VideoController();
        }
        return instance;
    }

    public Video uploadVideo(String title, String url, int userId) throws Exception {
        //check if owner id is existing user
        if(UserDBDAO.getInstance().getById(userId) == null){
            throw new Exception("User does not exist");
        }
        //validate title and url

        //insert
        return VideoDBDAO.getInstance().insertVideo(title, url, userId);
    }

    public int likeVideo(int videoId, int userId) throws Exception {
        if(UserDBDAO.getInstance().getById(userId) == null){
            throw new Exception("User does not exist");
        }
        if(VideoDBDAO.getInstance().getById(videoId) == null){
            throw new Exception("Video does not exist");
        }
        if(VideoDBDAO.getInstance().alreadyLiked(videoId, userId)){
            throw new Exception("User already liked this video");
        }
        return VideoDBDAO.getInstance().likeVideo(videoId, userId);
    }

    public int dislikeVideo(int videoId, int userId) throws Exception {
        if(UserDBDAO.getInstance().getById(userId) == null){
            throw new Exception("User does not exist");
        }
        if(VideoDBDAO.getInstance().getById(videoId) == null){
            throw new Exception("Video does not exist");
        }
        if(VideoDBDAO.getInstance().alreadyDisliked(videoId, userId)){
            throw new Exception("User already disliked this video");
        }
        return VideoDBDAO.getInstance().dislikeVideo(videoId, userId);
    }

    public int removeDislikeVideo(int videoId, int userId) throws Exception {
        if(UserDBDAO.getInstance().getById(userId) == null){
            throw new Exception("User does not exist");
        }
        if(VideoDBDAO.getInstance().getById(videoId) == null){
            throw new Exception("Video does not exist");
        }
        if(!VideoDBDAO.getInstance().alreadyDisliked(videoId, userId)){
            throw new Exception("User is not disliked this video");
        }
        return VideoDBDAO.getInstance().removeDislikeVideo(videoId, userId);
    }

    public int unlikeVideo(int videoId, int userId) throws Exception {
        if(UserDBDAO.getInstance().getById(userId) == null){
            throw new Exception("User does not exist");
        }
        if(VideoDBDAO.getInstance().getById(videoId) == null){
            throw new Exception("Video does not exist");
        }
        if(!VideoDBDAO.getInstance().alreadyLiked(videoId, userId)){
            throw new Exception("User is not liked this video");
        }
        return VideoDBDAO.getInstance().unlikeVideo(videoId, userId);
    }
}
