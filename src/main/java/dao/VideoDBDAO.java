package dao;

import db.DBConnector;
import model.User;
import model.Video;

import java.sql.*;
import java.util.ArrayList;

public class VideoDBDAO implements VideoDAO{

    private static VideoDBDAO instance;
    private Connection connection;

    private VideoDBDAO() {
        connection = DBConnector.getInstance().getConnection();
    }
    public static VideoDBDAO getInstance() {
        if(instance == null) {
            instance = new VideoDBDAO();
        }
        return instance;
    }

    @Override
    public Video getById(int id) throws Exception {
        String sql = "SELECT id, title, url, owner_id, views FROM videos WHERE id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()) {
                Video video = new Video(resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("url"),
                        UserDBDAO.getInstance().getById(resultSet.getInt("owner_id")),
                        resultSet.getInt("views"));
                return video;
            }
            else {
                throw new Exception("Video not found");
            }
        } catch (SQLException e) {
            System.out.println("Error getting the user - " + e.getMessage());
            return null;
        }
    }

    @Override
    public Video insertVideo(String title, String url, int userId) throws Exception {
        String sql = "INSERT INTO videos (title, url, owner_id) VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, title);
            statement.setString(2, url);
            statement.setInt(3, userId);
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            int id = resultSet.getInt(1);
            User owner = UserDBDAO.getInstance().getById(id);

            return  new Video(id, title, url, owner,0);
        } catch (SQLException e) {
            System.out.println("Error inserting video" + e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<Video> getVideosForOwner(int userId) {
        return null;
    }

    @Override
    public Video editVideo(Video v) {
        return null;
    }

    @Override
    public int likeVideo(int videoId, int userId) {
        String insertLike = "INSERT INTO users_like_videos (user_id, video_id) VALUES (?, ?);";
        String selectLikes = "SELECT COUNT(*) FROM users_like_videos WHERE video_id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(insertLike);
             PreparedStatement ps2 = connection.prepareStatement(selectLikes)){
            //insert and delete must be in one transaction
            connection.setAutoCommit(false);
            if(alreadyDisliked(videoId, userId)){
                removeDislikeVideo(videoId, userId);
            }
            ps.setInt(1, userId);
            ps.setInt(2, videoId);
            ps.executeUpdate();
            connection.commit();
            ps2.setInt(1, videoId);
            ResultSet result = ps2.executeQuery();
            if(result.next()){
                return result.getInt(1);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("error while rollback -" + ex.getMessage());
            }
            System.out.println("Error liking video - " + e.getMessage());
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error while set autocommit - " + e.getMessage());
            }
        }
        return 0;
    }

    @Override
    public int dislikeVideo(int videoId, int userId) {
        String insertDislike = "INSERT INTO users_dislike_videos (user_id, video_id) VALUES (?, ?);";
        String selectDislikes = "SELECT COUNT(*) FROM users_dislike_videos WHERE video_id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(insertDislike);
             PreparedStatement ps2 = connection.prepareStatement(selectDislikes)){
            //insert and delete must be in one transaction
            connection.setAutoCommit(false);
            if(alreadyLiked(videoId, userId)){
                unlikeVideo(videoId, userId);
            }
            ps.setInt(1, userId);
            ps.setInt(2, videoId);
            ps.executeUpdate();
            connection.commit();
            ps2.setInt(1, videoId);
            ResultSet result = ps2.executeQuery();
            if(result.next()){
                return result.getInt(1);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("error while rollback -" + ex.getMessage());
            }
            System.out.println("Error disliking video - " + e.getMessage());
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    @Override
    public boolean alreadyLiked(int videoId, int userId) {
        String select = "SELECT COUNT(*) FROM users_like_videos WHERE video_id = ? AND user_id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(select)){
            ps.setInt(1, videoId);
            ps.setInt(2, userId);
            ResultSet result = ps.executeQuery();
            result.next();
            return result.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error liking video - " + e.getMessage());
        }
        return false;
    }

    public boolean alreadyDisliked(int videoId, int userId) {
        String select = "SELECT COUNT(*) FROM users_dislike_videos WHERE video_id = ? AND user_id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(select)){
            ps.setInt(1, videoId);
            ps.setInt(2, userId);
            ResultSet result = ps.executeQuery();
            result.next();
            return result.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error disliking video - " + e.getMessage());
        }
        return false;
    }

    @Override
    public int unlikeVideo(int videoId, int userId) {
        String deleteLike = "DELETE FROM users_like_videos WHERE video_id = ? AND user_id = ?;";
        String selectLikes = "SELECT COUNT(*) FROM users_like_videos WHERE video_id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(deleteLike);
             PreparedStatement ps2 = connection.prepareStatement(selectLikes)){
            ps.setInt(1, videoId);
            ps.setInt(2, userId);
            ps.execute();
            ps2.setInt(1, videoId);
            ResultSet result = ps2.executeQuery();
            if(result.next()){
                return result.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error unliking video - " + e.getMessage());
        }
        return 0;
    }

    @Override
    public ArrayList<Video> getLikedByUser(int userId) throws Exception {
        String sql = "SELECT id, title, url, owner_id, views" +
                " FROM videos" +
                " JOIN users_like_videos" +
                " ON videos.id = users_like_videos.video_id" +
                " WHERE user_id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, userId);
            ResultSet rows = ps.executeQuery();
            ArrayList<Video> videos = new ArrayList<>();
            while (rows.next()){
                videos.add(new Video(rows.getInt( "id"),
                                     rows.getString("title"),
                                     rows.getString("url"),
                                     UserDBDAO.getInstance().getById(rows.getInt("owner_id")),
                                     rows.getInt("views")));
            }
            return videos;
        } catch (SQLException e) {
            System.out.println("Error while retrieve liked videos." + e.getMessage());
        }
        return null;
    }

    @Override
    public int removeDislikeVideo(int videoId, int userId) {
        String deleteDislike = "DELETE FROM users_dislike_videos WHERE video_id = ? AND user_id = ?;";
        String selectDislikes = "SELECT COUNT(*) FROM users_dislike_videos WHERE video_id = ?;";

        try (PreparedStatement ps = connection.prepareStatement(deleteDislike);
             PreparedStatement ps2 = connection.prepareStatement(selectDislikes)){
            ps.setInt(1, videoId);
            ps.setInt(2, userId);
            ps.execute();
            ps2.setInt(1, videoId);
            ResultSet result = ps2.executeQuery();
            if(result.next()){
                return result.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error while removing dislike from video - " + e.getMessage());
        }
        return 0;
    }
}
