
import java.sql.*;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class responsible for handling and executing calls to the database for game data.
 */
public class DatabaseManager {

  /**
   * This is the main method of this class responsible for making the database call to retrieve a list of games based on parameters,
   * and formatting that data to JSON. This will remain static for now so that there are no delays between opening a connection and closing it.
   * 
   * @param params
   * @return
   * @throws SQLException
   */
    public static JSONObject getGames(QueryParameters params) throws SQLException {

        Connection conn = DriverManager.getConnection(DatabaseConfig.getJDBCUrl());
        PreparedStatement prep = buildStatement(conn, params);
        ResultSet resultSet = prep.executeQuery();

        JSONObject jsonResult = finalizeJSONObject(conn, resultSet);

        resultSet.close();
        prep.close();
        conn.close();

        return jsonResult;
    }

    /**
     * Based on parameters given, this method is responsible for constructing the SQL to be executed.
     * 
     * @param conn
     * @param params
     * @return
     * @throws SQLException
     */
    private static PreparedStatement buildStatement(Connection conn, QueryParameters params) throws SQLException {
      boolean joinFlag = false;
      boolean groupFlag = false;
      StringBuilder select = new StringBuilder("SELECT DISTINCT games.id, games.name, games.metacritic, games.released, games.playtime, games.background_image FROM games");
      StringBuilder join = new StringBuilder();
      StringBuilder where = new StringBuilder(" WHERE");
      StringBuilder group = new StringBuilder(" GROUP BY");

      if (params.getPlaytimeLeniency() != null) {
        where.append(" (games.playtime >= ? AND games.playtime <= ?)");
      } else {
        where.append(" games.playtime = ?");
      }

      //This would not work as parameters, but worth a revisit
      if(params.getGenresList() != null && !params.getGenresList().isEmpty()) {
        joinFlag = true;
        groupFlag = true;
        join.append(" JOIN genres ON games.id = genres.id");
        where.append(" AND (genres.name IN (").append(String.join(",", params.getGenresList())).append("))");
        group.append(" genres.id HAVING COUNT(DISTINCT genres.name) = ?");
      }

      if(params.getPlatformList() != null && !params.getPlatformList().isEmpty()) {
        joinFlag = true;
        join.append(" JOIN platforms ON games.id = platforms.id");
        where.append(" AND (platforms.name IN (").append(String.join(",", params.getPlatformList())).append("))");
      }

      if (joinFlag) {
        select.append(join);
      }

      select.append(where);
      if (groupFlag) {
        select.append(group);
      }
      select.append(" ORDER BY RANDOM() LIMIT ?;");

      return buildPreparedStatement(conn, params, select.toString(), groupFlag);
    }

    /**
     * This method performs the logic to set the parameters initially added to the SQL.
     * 
     * @param conn
     * @param params
     * @param select
     * @param groupFlag - this could better done, at the moment would just require additional condition checks
     * @return
     * @throws SQLException
     */
    private static PreparedStatement buildPreparedStatement(Connection conn, QueryParameters params, String select, boolean groupFlag) throws SQLException {
      PreparedStatement prepStatement = conn.prepareStatement(select.toString());

      int paramIndex = 1;

      //paramIndex++ updates the value after that line (after it has been used)
      if (params.getPlaytimeLeniency() != null) {
        prepStatement.setInt(paramIndex++, Math.max(0, params.getPlaytime() - params.getPlaytimeLeniency()));
        prepStatement.setInt(paramIndex++, params.getPlaytime() + params.getPlaytimeLeniency());
      } else {
        prepStatement.setInt(paramIndex++, params.getPlaytime());
      }

      //GROUPBY parameter
      if(groupFlag) {
        if(params.getGenresList() != null && !params.getGenresList().isEmpty()) {
          prepStatement.setInt(paramIndex++, params.getGenresList().size());
        }
      }

      //LIMIT parameter
      prepStatement.setInt(paramIndex++, params.getResult_size());

      return prepStatement;
    }

    /**
     * Convert the results of the main execution into a JSON object but also performs additional DB reads to add platform specific data.
     * 
     * @param conn
     * @param results
     * @return
     * @throws SQLException
     */
    private static JSONObject finalizeJSONObject(Connection conn, ResultSet results) throws SQLException {
      //if this is not very efficient, will want to look into external libraries such as Spring Data JDBC or asynch programming

      JSONArray joGamesArray = new JSONArray();

      while(results.next()) {
        
        try {
          Game game = Game.createGame(results.getString("name"), results.getInt("metacritic"), results.getString("released"), results.getInt("playtime"), results.getString("background_image"), getPlatformsForGame(conn, results.getString("id")));
          joGamesArray.put(game.toJSON());
          
        } catch (SQLException e) {
          //in this case, something wrong occurred while grabbing the platforms, send the rest of the data
          Game game = Game.createGame(results.getString("name"), results.getInt("metacritic"), results.getString("released"), results.getInt("playtime"), results.getString("background_image"), new ArrayList<>());
          joGamesArray.put(game.toJSON());
        } catch (IllegalArgumentException e) {
          //in this case, bad data record, so ignore adding it to resulting JSON object
        }
      }

      JSONObject response = new JSONObject();
      response.put("data", joGamesArray);

      return response;

    }

    /**
     * Retrieve platforms associated for game from database.
     * 
     * @param conn
     * @param id
     * @return
     * @throws SQLException
     */
	  private static ArrayList<String> getPlatformsForGame(Connection conn, String id) throws SQLException {
		  PreparedStatement prepStatement = conn.prepareStatement("SELECT name FROM platforms WHERE platforms.id = ?");
		  prepStatement.setString(1, id);
		  ResultSet resultSet = prepStatement.executeQuery();

		  ArrayList<String> platforms = new ArrayList<>();

		  while(resultSet.next()) {
			  platforms.add(resultSet.getString("name"));
		  }

		  return platforms;
	  }
}
