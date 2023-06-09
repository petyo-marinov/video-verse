package dao;

import model.Video;

import java.util.ArrayList;

public interface VideoDAO {

    Video getById(int id) throws Exception;
    Video insertVideo(String title, String url, int userId) throws Exception;
    ArrayList<Video> getVideosForOwner(int userId);
    Video editVideo(Video v);
    int likeVideo(int videoId, int userId);
    int dislikeVideo(int videoId, int userId);

    int removeDislikeVideo(int videoId, int userId);

    boolean alreadyLiked(int videoId, int userId);

    int unlikeVideo(int videoId, int userId);

    ArrayList<Video> getLikedByUser(int userId) throws Exception;


}
