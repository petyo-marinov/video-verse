package model;

public class Video {

    private int id;
    private String title;
    private String url;
    private User owner;
    private int views;

    public Video(int id, String title, String url, User owner, int views) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.owner = owner;
        this.views = views;
    }

    public int getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String  getTitle() {
        return title;
    }
}
