package com.example.udare.data.model;

public class Profile {
    private String nombre;
    private String bio;
    private String profilePic;
    private String[] followers;
    private String[] following;

    private int pointsSport;
    private int pointsSocial;
    private int pointsCulture;
    private int pointsGrowth;
    private int pointsCooking;

    public Profile(String nombre, String bio, String profilePic, String[] followers, String[] following,
                   int pointsSport, int pointsSocial, int pointsCulture, int pointsGrowth, int pointsCooking) {
        this.nombre = nombre;
        this.bio = bio;
        this.profilePic = profilePic;
        this.followers = followers;
        this.following = following;
        this.pointsSport = pointsSport;
        this.pointsSocial = pointsSocial;
        this.pointsCulture = pointsCulture;
        this.pointsGrowth = pointsGrowth;
        this.pointsCooking = pointsCooking;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUrlFoto() {
        return profilePic;
    }

    public void setUrlFoto(String urlFoto) {
        this.profilePic = urlFoto;
    }

    public String[] getFollowers() {
        return followers;
    }

    public void setFollowers(String[] followers) {
        this.followers = followers;
    }

    public String[] getFollowing() {
        return following;
    }

    public void setFollowing(String[] following) {
        this.following = following;
    }


    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getPointsSport() {
        return pointsSport;
    }

    public void setPointsSport(int pointsSport) {
        this.pointsSport = pointsSport;
    }

    public int getPointsSocial() {
        return pointsSocial;
    }

    public void setPointsSocial(int pointsSocial) {
        this.pointsSocial = pointsSocial;
    }

    public int getPointsCulture() {
        return pointsCulture;
    }

    public void setPointsCulture(int pointsCulture) {
        this.pointsCulture = pointsCulture;
    }

    public int getPointsGrowth() {
        return pointsGrowth;
    }

    public void setPointsGrowth(int pointsGrowth) {
        this.pointsGrowth = pointsGrowth;
    }

    public int getPointsCooking() {
        return pointsCooking;
    }

    public void setPointsCooking(int pointsCooking) {
        this.pointsCooking = pointsCooking;
    }

    public int getAllPoints(){
        return (pointsCooking + pointsGrowth +  pointsSport +  pointsCulture + pointsSocial);
    }
}

