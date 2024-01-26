package src.main.java.TLDP;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * This class defines a DTO to translate a data record from the database into a form that can be converted to a JSON Object
 * Although the db records can be inserted directly into a JSON Object, this DTO allows more felxible implementation in the future.
 */
public class Game {

    private String name;
    private int metaScore;
    private String releasedDate;
    private int avgPlayTime;
    private String description;
    private String imageURL;
    private ArrayList<String> platforms;

    /*
     * Method to validate the game data. Validation can be expanded depending on needs.
     */
    public static Game createGame(String name, int metaScore, String releasedDate, int avgPlayTime, String description, String imageURL, ArrayList<String> platforms) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (imageURL == null || imageURL.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return new Game(name, metaScore, releasedDate, avgPlayTime, description, imageURL, platforms);
    }

    public Game(String name, int metaScore, String releasedDate, int avgPlayTime, String description, String imageURL, ArrayList<String> platforms) {
        this.name = name;
        this. metaScore = metaScore;
        this.releasedDate = releasedDate;
        this.avgPlayTime = avgPlayTime;
        this.description = description;
        this.imageURL = imageURL;
        this.platforms = platforms;
    }

    public String getName() {
        return name;
    }

    public int getMetaScore() {
        return metaScore;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public int getAvgPlayTime() {
        return avgPlayTime;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageURL;
    }

    public ArrayList<String> getPlatforms() {
        return platforms;
    }

    /**
     *  Conver the Game DTO into a JSON Object
     */
    public JSONObject toJSON(){
        JSONObject jo = new JSONObject();
        jo.put("name", this.getName());
        jo.put("metascore", this.getMetaScore());
        jo.put("released", this.getReleasedDate());
        jo.put("playtime", this.getAvgPlayTime());
        jo.put("description", this.getDescription());
        jo.put("platforms", this.getPlatforms());
        jo.put("img", this.getImageUrl());
        return jo;
    }
}
