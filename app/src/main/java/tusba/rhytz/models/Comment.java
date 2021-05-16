package tusba.rhytz.models;

public class Comment {
    String commentId;
    String musicianId;
    String musicianName;
    String musicId;
    String userId;
    String userName;
    String text;

    public Comment(){}
    public Comment(String commentId, String musicianId, String musicianName, String musicId, String userId, String userName, String text) {
        this.commentId = commentId;
        this.musicianId = musicianId;
        this.musicianName = musicianName;
        this.musicId = musicId;
        this.userId = userId;
        this.userName = userName;
        this.text = text;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getMusicianId() {
        return musicianId;
    }

    public void setMusicianId(String musicianId) {
        this.musicianId = musicianId;
    }

    public String getMusicianName() {
        return musicianName;
    }

    public void setMusicianName(String musicianName) {
        this.musicianName = musicianName;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
