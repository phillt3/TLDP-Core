import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.json.JSONObject;

public class GameApiService extends HttpServlet  {
    //we need to check the validity of an api get request, parse out and format the parameters and return response
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        QueryParameters params = validateParameters(request, response);

        if (response.getStatus() == HttpServletResponse.SC_BAD_REQUEST) {
            return;
        }

        try {
            //get logic here
            JSONObject result = DatabaseManager.getGames(params);
            PrintWriter out = response.getWriter();
            out.print(result.toString());
        } catch (Exception e) {
            //something wrong happened when performing get logic
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

    }

    private QueryParameters validateParameters(HttpServletRequest request, HttpServletResponse response) {
        
        QueryParameters params = new QueryParameters();

        String resultSize = request.getParameter("result_size");

        if (resultSize != null) {
            try {
                int size = Integer.parseInt(resultSize);
                params.setResult_size(size);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        String playTime = request.getParameter("playtime");

        if (playTime != null) {
            try {
                int time = Integer.parseInt(playTime);
                params.setPlaytime(time);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        String playtimeLeniency = request.getParameter("playtime_leniency");

        if (playtimeLeniency != null) {
            try {
                int time = Integer.parseInt(playTime);
                params.setPlaytime(time);
            } catch (NumberFormatException e) {
                response.setHeader("Warning: PlayTime Leniency", "Was not numeric and was not included in filter.");
            }
        }

        String genres = request.getParameter("genres");

        if (genres != null) {
            ArrayList<String> genreList = new ArrayList<String>(Arrays.asList(genres.split("\\s*,\\s*")));
            params.setGenresList(genreList);
        }

        return params;
    }
}
