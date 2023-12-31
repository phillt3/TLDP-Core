import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class DatabaseManagerTest  {

    @Test
    public void testRequired() throws SQLException {
        QueryParameters params = new QueryParameters();
        params.setResult_size(2);
        params.setPlaytime(3);

        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        
        Assert.assertEquals(games.length(), 2);

        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            Assert.assertEquals(jo.getInt("playtime"), 3);
        }
    }

    @Test
    public void testPlayTimeLeniency() throws SQLException {
        QueryParameters params = new QueryParameters();
        params.setResult_size(10);
        params.setPlaytime(10);
        params.setPlaytimeLeniency(2);

        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        
        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            Assert.assertTrue(jo.getInt("playtime") >= 8 && jo.getInt("playtime") <= 12);
        }
    }

    @Test
    public void testGenres() throws SQLException {
        //this test has given me additional considerations:
        //currenlty genres are considered independently
        //so an action rpg game will appear in a list of games that are just action or just rpg as well
        //need to look into reformatting the database prioritize games that are both?
        QueryParameters params = new QueryParameters();
        params.setResult_size(10);
        params.setPlaytime(1);
        params.setGenresList("action, adventure, simulation, arcade, indie, platformer");

        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            Assert.assertEquals(jo.getString("name"), "Waking Mars");
        }
    }

    @Test
    public void testEdgeCases() throws SQLException {
        //The strategy is that however fault the parameter object is the sql will not product errors that will terminate
        //the program. This way the only way the program fails is upon parameter validation, where it can be handled 
        //and sent back to the user with proper warnings and codes.
        QueryParameters params = new QueryParameters();

        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        Assert.assertTrue(games.isEmpty());

        params = new QueryParameters();
        params.setPlaytime(1);

        result = DatabaseManager.getGames(params);
        games = result.getJSONArray("data");
        Assert.assertTrue(games.isEmpty());

        params = new QueryParameters();
        params.setResult_size(1);

        result = DatabaseManager.getGames(params);
        games = result.getJSONArray("data");
        Assert.assertTrue(games.length() == 1);

        params = new QueryParameters();
        params.setResult_size(10);
        params.setPlaytimeLeniency(3);

        result = DatabaseManager.getGames(params);
        games = result.getJSONArray("data");
        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            Assert.assertTrue(jo.getInt("playtime") >= 0 && jo.getInt("playtime") <= 3);
        }

        params = new QueryParameters();
        params.setResult_size(5);
        params.setPlaytimeLeniency(10);
        params.setGenresList("**genredne**, //genredne//");

        result = DatabaseManager.getGames(params);
        games = result.getJSONArray("data");
        Assert.assertTrue(games.isEmpty());
    }
}
