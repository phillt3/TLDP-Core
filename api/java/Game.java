package api.java;

public class Game {

    private String name;
    private String metaScore;
    private String releasedDate;
    private int avgPlayTime;
    private String imageURL;

    public Game(String name, String metaScore, String releasedDate, int avgPlayTime, String imageURL) {
        this.name = name;
        this. metaScore = metaScore;
        this.releasedDate = releasedDate;
        this.avgPlayTime = avgPlayTime;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public String getMetaScore() {
        return metaScore;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public int getAvgPlayTime() {
        return avgPlayTime;
    }

    public String getImageUrl() {
        return imageURL;
    }
}
