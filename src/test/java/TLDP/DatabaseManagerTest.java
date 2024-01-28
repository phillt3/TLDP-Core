package src.test.java.TLDP;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import src.main.java.TLDP.DatabaseManager;
import src.main.java.TLDP.QueryParameters;

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
        Assert.assertFalse(games.isEmpty());
        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            Assert.assertTrue(jo.getInt("playtime") >= 8 && jo.getInt("playtime") <= 12);
        }
    }

    @Test
    public void testGenres() throws SQLException {
        //Genres are filtered dependently meaning that if the genre input is 'action,rpg' only games with both genres will be listed/
        QueryParameters params = new QueryParameters();
        params.setResult_size(10);
        params.setPlaytime(1);
        params.setGenresList("action, adventure, simulation, arcade, indie, platformer");

        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        Assert.assertFalse(games.isEmpty());
        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            Assert.assertEquals(jo.getString("name"), "Waking Mars");
        }
    }

    @Test
    public void testPlatforms() throws SQLException {
        //Platforms are filtered independently so that games that are on both the 3do and genesis will be listed, but also games that are only on one of the platforms
        QueryParameters params = new QueryParameters();
        params.setResult_size(10);
        params.setPlaytime(0);
        params.setPlatformList("3do, genesis");

        boolean doesContain = true;
        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        Assert.assertFalse(games.isEmpty());
        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            JSONArray platJA = jo.getJSONArray("platforms");
            List<String> containsList = new ArrayList<>();
            for (int i = 0; i < platJA.length(); i++) {
                containsList.add(platJA.getString(i));
            }

            if(!containsList.contains("3do") && !containsList.contains("genesis")) {
                doesContain = false;
            }
        }
        Assert.assertTrue(doesContain);
    }

    @Test
    public void testPlatformsAndGenres() throws SQLException {
        QueryParameters params = new QueryParameters();
        params.setResult_size(10);
        params.setPlaytime(10);
        params.setPlaytimeLeniency(10);
        params.setGenresList("action, adventure");
        params.setPlatformList("3do, jaguar");

        boolean doesContain = true;
        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        Assert.assertTrue(games.length() == 1);
        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            JSONArray platJA = jo.getJSONArray("platforms");
            List<String> containsList = new ArrayList<>();
            for (int i = 0; i < platJA.length(); i++) {
                containsList.add(platJA.getString(i));
            }

            if(!containsList.contains("3do") && !containsList.contains("jaguar")) {
                doesContain = false;
            }
        }
        Assert.assertTrue(doesContain);
    }

    @Test
    public void testDescription() throws SQLException {
        QueryParameters params = new QueryParameters();
        params.setResult_size(2);
        params.setPlaytime(3);

        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        Assert.assertEquals(games.length(), 2);

        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            Assert.assertEquals(jo.getInt("playtime"), 3);
            Assert.assertFalse(jo.getString("description").isEmpty());
        }
    }

    @Test
    public void testNewDateFilter() throws SQLException {
        QueryParameters params = new QueryParameters();
        params.setResult_size(10);
        params.setPlaytime(10);
        params.setPlaytimeLeniency(10);
        params.setAge("new");

        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        LocalDate currentDate = LocalDate.now();
        Assert.assertFalse(games.isEmpty());
        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            LocalDate released = LocalDate.parse(jo.getString("released"));
            Assert.assertTrue(released.isAfter(currentDate.minusYears(1)));
        }
    }

    @Test
    public void testModernDateFilter() throws SQLException {
        QueryParameters params = new QueryParameters();
        params.setResult_size(10);
        params.setPlaytime(10);
        params.setPlaytimeLeniency(10);
        params.setAge("modern");
        
        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        LocalDate currentDate = LocalDate.now();
        Assert.assertFalse(games.isEmpty());
        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            LocalDate released = LocalDate.parse(jo.getString("released"));
            Assert.assertTrue(released.isAfter(currentDate.minusYears(5)) && released.isBefore(currentDate.minusYears(1)));
        }
    }

    @Test
    public void testNostalgicDateFilter() throws SQLException {
        QueryParameters params = new QueryParameters();
        params.setResult_size(10);
        params.setPlaytime(10);
        params.setPlaytimeLeniency(10);
        params.setAge("nostalgic");

        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        LocalDate currentDate = LocalDate.now();
        Assert.assertFalse(games.isEmpty());
        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            LocalDate released = LocalDate.parse(jo.getString("released"));
            Assert.assertTrue(released.isAfter(currentDate.minusYears(15)) && released.isBefore(currentDate.minusYears(5)));
        }
    }

    @Test
    public void testVintageDateFilter() throws SQLException {
        QueryParameters params = new QueryParameters();
        params.setResult_size(10);
        params.setPlaytime(10);
        params.setPlaytimeLeniency(10);
        params.setAge("vintage");

        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        LocalDate currentDate = LocalDate.now();
        Assert.assertFalse(games.isEmpty());
        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            LocalDate released = LocalDate.parse(jo.getString("released"));
            Assert.assertTrue(released.isAfter(currentDate.minusYears(25)) && released.isBefore(currentDate.minusYears(15)));
        }
    }

    @Test
    public void testAntiqueDateFilter() throws SQLException {
        QueryParameters params = new QueryParameters();
        params.setResult_size(10);
        params.setPlaytime(10);
        params.setPlaytimeLeniency(10);
        params.setAge("antique");

        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        LocalDate currentDate = LocalDate.now();
        Assert.assertFalse(games.isEmpty());
        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            LocalDate released = LocalDate.parse(jo.getString("released"));
            Assert.assertTrue(released.isBefore(currentDate.minusYears(25)));
        }
    }

    @Test
    public void testDateWithOtherParams() throws SQLException {
        QueryParameters params = new QueryParameters();
        params.setResult_size(10);
        params.setPlaytime(10);
        params.setPlaytimeLeniency(10);
        params.setAge("modern");
        params.setGenresList("action, shooter");
        params.setPlatformList("playstation 4, playstation 5, pc");

        JSONObject result = DatabaseManager.getGames(params);
        JSONArray games = result.getJSONArray("data");
        LocalDate currentDate = LocalDate.now();
        Assert.assertFalse(games.isEmpty());
        for (Object obj : games) {
            JSONObject jo = (JSONObject) obj;
            LocalDate released = LocalDate.parse(jo.getString("released"));
            Assert.assertTrue(released.isAfter(currentDate.minusYears(5)) && released.isBefore(currentDate.minusYears(1)));
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

        params = new QueryParameters();
        params.setResult_size(1);
        params.setPlaytime(10);
        params.setPlaytimeLeniency(10);
        params.setAge(null);

        result = DatabaseManager.getGames(params);
        games = result.getJSONArray("data");
        Assert.assertTrue(!games.isEmpty());

        params = new QueryParameters();
        params.setResult_size(1);
        params.setPlaytime(10);
        params.setPlaytimeLeniency(10);
        params.setAge("");

        result = DatabaseManager.getGames(params);
        games = result.getJSONArray("data");
        Assert.assertTrue(!games.isEmpty());

        params = new QueryParameters();
        params.setResult_size(1);
        params.setPlaytime(10);
        params.setPlaytimeLeniency(10);
        params.setAge("not correct age value");

        result = DatabaseManager.getGames(params);
        games = result.getJSONArray("data");
        Assert.assertTrue(!games.isEmpty());
    }
}
