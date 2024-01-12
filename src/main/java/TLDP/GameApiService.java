package src.main.java.TLDP;
import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class responsible for handling incoming API requests and executing calls to DatabaseManager to grab data.
 */
public class GameApiService extends HttpServlet  {

    private int status;
    
    public GameApiService() {

    }
    
    /**
     * Handle GET API request.
     * 
     * @param request
     * @param response
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        QueryParameters params = validateParameters(request, response);
        PrintWriter out = response.getWriter();
        JSONObject result = new JSONObject();

        if (status == HttpServletResponse.SC_BAD_REQUEST) {

            result.put("status", "invalid request");
            result.put("code", status);
            result.put("data", new JSONArray());
            
            out.print(result.toString());
            response.setStatus(status);
            //may want to consider sending an error here instead
            return;
        }

        try {
            result = DatabaseManager.getGames(params);

            result.put("status", "success");
            result.put("code", status);
            response.setStatus(status);
            
            out.print(result.toString());
            return;
        } catch (Exception e) {
            //something wrong happened when performing get logic
            //this should never occur

            result.put("status", "internal error");
            result.put("code", response.getStatus());
            result.put("data", new JSONArray());

            out.print(result.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            //may want to consider sending an error here instead
            return;
        }

    }

    /**
     * Validate and sanitize the parameters in the API request.
     * 
     * @param request
     * @param response
     * @return
     */
    private QueryParameters validateParameters(HttpServletRequest request, HttpServletResponse response) {
        
        QueryParameters params = new QueryParameters();

        String resultSize = request.getParameter("result_size");

        if (resultSize != null) {
            try {
                int size = Integer.parseInt(resultSize);
                params.setResult_size(size);
            } catch (NumberFormatException e) {
                status = HttpServletResponse.SC_BAD_REQUEST;
                return null;
            }
        } else {
            status = HttpServletResponse.SC_BAD_REQUEST;
            return null;
        }

        String playTime = request.getParameter("playtime");

        if (playTime != null) {
            try {
                int time = Integer.parseInt(playTime);
                params.setPlaytime(time);
            } catch (NumberFormatException e) {
                status = HttpServletResponse.SC_BAD_REQUEST;
                return null;
            }
        } else {
            status = HttpServletResponse.SC_BAD_REQUEST;
            return null;
        }

        String playtimeLeniency = request.getParameter("playtime_leniency");

        if (playtimeLeniency != null) {
            try {
                int time = Integer.parseInt(playtimeLeniency);
                params.setPlaytimeLeniency(time);
            } catch (NumberFormatException e) {
                response.setHeader("Warning: PlayTime Leniency", "Was not numeric and was not included in filter.");
            }
        }

        String genres = request.getParameter("genres");

        if (genres != null) {
            ArrayList<String> genreList = new ArrayList<String>(Arrays.asList(genres.split("\\s*,\\s*")));
            params.setGenresList(genreList);
        }

        String platforms = request.getParameter("platforms");

        if (platforms != null) {
            ArrayList<String> platformList = new ArrayList<String>(Arrays.asList(platforms.split("\\s*,\\s*")));
            params.setPlatformList(platformList);
        }


        status = HttpServletResponse.SC_OK;
        return params;
    }
}
