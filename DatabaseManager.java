
import java.sql.*;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class DatabaseManager {

  //Question on parameters
  //1) what parameters? playtime, result size, genres, metacritic
  //2) which are necessary? playtime
  //3) which are optional? result size (set default to 10-20), genres (get randomized set if null), metacritic (could be specific number or maybe just above a certain number)
  //4) this can done with something like a criteria def, where we set if its not null, and then just pass that 'params' arg in and check each variable if its not null, and then edit query accordingly

    public static JSONObject getGames(QueryParameters params) throws SQLException {
      //query parameters will need to have all checks for data validity in api service
      //instead of catching the exception, should eventually throw it

        String query = buildQuery(params).toString();
        Connection conn = DriverManager.getConnection(DatabaseConfig.getJDBCUrl());
        PreparedStatement prep = conn.prepareStatement(query);
        ResultSet resultSet = prep.executeQuery();

        JSONObject jsonResult = convertToJSONObject(resultSet);

        resultSet.close();
        prep.close();
        conn.close();

        return jsonResult;

    }

    private static StringBuilder buildQuery(QueryParameters params) throws SQLException {
      StringBuilder qry = new StringBuilder();
      if (params.getGenresList() != null) {
        qry.append("SELECT DISTINCT games.name, games.metacritic, games.released, games.playtime, games.background_image FROM games JOIN genres ON games.id = genres.id WHERE genres.name IN ").append("(").append(String.join(",", params.getGenresList())).append(") AND ");
      } else {
        qry.append("SELECT * FROM games WHERE ");
      }

      if(params.getPlaytimeLeniency() != null) {
        int under = params.getPlaytime() + params.getPlaytimeLeniency();
        int over = Math.max(0, params.getPlaytime() - params.getPlaytimeLeniency());
        qry.append("(games.playtime >= '").append(over).append("' ").append("AND ");
        qry.append("games.playtime <= '").append(under).append("')");
      } else {
        qry.append("games.playtime = '").append(params.getPlaytime()).append("'");
      }

      if (params.getGenresList() != null) {
        String genreCount = String.valueOf(params.getGenresList().size());
        qry.append(" GROUP BY genres.id HAVING COUNT(DISTINCT genres.name) = ").append(genreCount);
      }
      qry.append(" LIMIT '").append(params.getResult_size()).append("'");

      return qry;
    }

    private static JSONObject convertToJSONObject(ResultSet results) throws SQLException {
      //if this is not very efficient, will want to look into external libraries such as Spring Data JDBC or asynch programming

      JSONArray joGamesArray = new JSONArray();

      while(results.next()) {
        JSONObject game = new JSONObject();
        game.put("name", results.getString("name"));
        game.put("metascore", results.getInt("metacritic"));
        game.put("released", results.getString("released"));
        game.put("playtime", results.getInt("playtime"));
        game.put("img", results.getString("background_image"));
        joGamesArray.put(game);
      }

      JSONObject response = new JSONObject();
      response.put("data", joGamesArray);

      return response;

    }

    public static void main(String[] args) {
      // QueryParameters params = new QueryParameters();
      // params.setPlaytime(10);
      // params.setPlaytime_leniency(3);
      // params.setResult_size(10);
      // getGames(params);

      QueryParameters params2 = new QueryParameters();
      params2.setPlaytime(10);
      params2.setPlaytimeLeniency(5);
      params2.setResult_size(10);
      ArrayList<String> genres = new ArrayList<String>();
      genres.add("action"); //need to reimport database but these need to be lowercase
      genres.add("adventure");
      params2.setGenresList(genres);
      try {
        getGames(params2);
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

}
