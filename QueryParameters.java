import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class QueryParameters {
    private int result_size; //required within request
    private int playtime; //required within request
    private Integer playtime_leniency;
    private ArrayList<String> genresList;

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
}
