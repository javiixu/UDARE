package com.example.udare.data.model;

public class PostData {

    public String profilePic;
    public String username;
    public Post post;

    public PostData(Post post, String profilePic, String username){
        this.post = post;
        this.profilePic = profilePic;
        this.username = username;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
