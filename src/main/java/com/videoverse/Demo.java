package com.videoverse;

import controller.UserController;
import controller.VideoController;
import dao.VideoDBDAO;
import model.User;
import model.Video;

import java.util.List;

public class Demo {

    public static void main(String[] args) {

        //Server receives this data as a request

        try {
            //user = UserController.getInstance().register("detelina@abv.bg", "Detelina", "123123Ab-Cd", 28, "F");

            //Server returns the generated user object as a response
            //if(user != null){
              //  System.out.println(user.getId());
            //}

            //Video video = VideoController.getInstance().uploadVideo("Top 10 race car", "/videos/raceCar",3);

            //System.out.println(video.getId());
            //System.out.println(video.getOwner().getUsername());

//            int likes = VideoController.getInstance().likeVideo(2,2);
//            System.out.println(likes);
            int unlike = VideoController.getInstance().removeDislikeVideo(4,2);
            System.out.println(unlike);


        } catch (Exception e) {
            System.out.println("Register failed - " + e.getMessage());
        }

        List<Video> likedVideos = null;
        try {
            likedVideos = UserController.getInstance().getLikedVideos(3);
        } catch (Exception e) {
            System.out.println("Getting liked videos failed - " + e.getMessage());
        }
        for(Video v : likedVideos){
            System.out.println(v.getId() + " - " + v.getTitle());
        }


    }

}
