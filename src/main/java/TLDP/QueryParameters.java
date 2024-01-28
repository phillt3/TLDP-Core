package src.main.java.TLDP;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This class defines a structure to convert the unformatted API request parameters to be easily handled in database management.
 */
public class QueryParameters {

    public enum Age {
        NEW,
        MODERN,
        NOSTALGIC,
        VINTAGE,
        ANTIQUE,
        NONE;

        public static Age fromString(String age) {

            if (age == null) {
                return Age.NONE; 
            }

            try {
                return Age.valueOf(age.toUpperCase());
            } catch (IllegalArgumentException e) {
                return Age.NONE;
            }
        }
    }

    private int result_size; //required within request
    private int playtime; //required within request
    private Integer playtime_leniency;
    private ArrayList<String> genresList;
    private ArrayList<String> platformList;
    private Age age;

    public QueryParameters() {
        
    }

    public int getResult_size() {
        return result_size;
    }
    public void setResult_size(int result_size) {
        this.result_size = result_size;
    }
    public int getPlaytime() {
        return playtime;
    }
    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }
    public Integer getPlaytimeLeniency() {
        return playtime_leniency;
    }
    public void setPlaytimeLeniency(int playtime_leniency) {
        this.playtime_leniency = playtime_leniency;
    }

    public ArrayList<String> getGenresList() {
        return genresList;
    }

    public void setGenresList(ArrayList<String> genresList) {
        this.genresList = genresList.stream().map(item -> "'" + item + "'").collect(Collectors.toCollection(ArrayList::new));
    }

    public void setGenresList(String genresString) {
        ArrayList<String> genreList = new ArrayList<String>(Arrays.asList(genresString.split("\\s*,\\s*")));
        this.setGenresList(genreList);
    }

    public ArrayList<String> getPlatformList() {
        return platformList;
    }

    public void setPlatformList(ArrayList<String> platformList) {
        this.platformList = platformList.stream().map(item -> "'" + item + "'").collect(Collectors.toCollection(ArrayList::new));
    }

    public void setPlatformList(String platformString) {
        ArrayList<String> platformList = new ArrayList<String>(Arrays.asList(platformString.split("\\s*,\\s*")));
        this.setPlatformList(platformList);
    }

    public void setAge(String age) {
        this.age = Age.fromString(age);
    }

    public Age getAge() {
        return age;
    }
}
