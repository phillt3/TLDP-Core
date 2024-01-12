package src.test.java.TLDP;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import src.main.java.TLDP.GameApiService;

import static org.mockito.Mockito.*;

public class GameApiServiceTest {

    @Test
    public void testSimpleRequest() throws IOException, ServletException {
        GameApiService gameService = new GameApiService();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(req.getParameter("result_size")).thenReturn("5");
        when(req.getParameter("playtime")).thenReturn("5");
        when(resp.getWriter()).thenReturn(writer);

        gameService.doGet(req, resp);

        JSONObject result = new JSONObject(stringWriter.toString());
        Assert.assertEquals(result.getInt("code"), 200);
        Assert.assertEquals(result.getString("status"), "success");
        Assert.assertEquals(result.getJSONArray("data").length(), 5);
    }

    @Test
    public void testLargeRequest() throws IOException, ServletException {
        GameApiService gameService = new GameApiService();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(req.getParameter("result_size")).thenReturn("20");
        when(req.getParameter("playtime")).thenReturn("5");
        when(resp.getWriter()).thenReturn(writer);

        gameService.doGet(req, resp);

        JSONObject result = new JSONObject(stringWriter.toString());
        Assert.assertEquals(result.getInt("code"), 200);
        Assert.assertEquals(result.getString("status"), "success");
        Assert.assertEquals(result.getJSONArray("data").length(), 20);
    }

    @Test
    public void testComplexRequest() throws IOException, ServletException {
        GameApiService gameService = new GameApiService();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(req.getParameter("result_size")).thenReturn("5");
        when(req.getParameter("playtime")).thenReturn("5");
        when(req.getParameter("playtime_leniency")).thenReturn("5");
        when(req.getParameter("genres")).thenReturn("action, adventure");
        when(resp.getWriter()).thenReturn(writer);

        gameService.doGet(req, resp);

        JSONObject result = new JSONObject(stringWriter.toString());

        Assert.assertEquals(result.getInt("code"), 200);
        Assert.assertEquals(result.getString("status"), "success");
        Assert.assertEquals(result.getJSONArray("data").length(), 5);
    }

    @Test
    public void testComplexRequest2() throws IOException, ServletException {
        GameApiService gameService = new GameApiService();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(req.getParameter("result_size")).thenReturn("10");
        when(req.getParameter("playtime")).thenReturn("10");
        when(req.getParameter("playtime_leniency")).thenReturn("10");
        when(req.getParameter("genres")).thenReturn("action, adventure");
        when(req.getParameter("platforms")).thenReturn("3do, jaguar");
        when(resp.getWriter()).thenReturn(writer);

        gameService.doGet(req, resp);

        JSONObject result = new JSONObject(stringWriter.toString());

        Assert.assertEquals(result.getInt("code"), 200);
        Assert.assertEquals(result.getString("status"), "success");
        Assert.assertEquals(result.getJSONArray("data").length(), 1);
    }

    @Test
    public void testInvalidRequest1() throws IOException, ServletException {
        GameApiService gameService = new GameApiService();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(req.getParameter("result_size")).thenReturn(null);
        when(req.getParameter("playtime")).thenReturn("5");
        when(resp.getWriter()).thenReturn(writer);

        gameService.doGet(req, resp);

        JSONObject result = new JSONObject(stringWriter.toString());
        Assert.assertEquals(result.getInt("code"), 400);
        Assert.assertEquals(result.getString("status"), "invalid request");
        Assert.assertEquals(result.getJSONArray("data").length(), 0);
    }

    @Test
    public void testInvalidRequest2() throws IOException, ServletException {
        GameApiService gameService = new GameApiService();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(req.getParameter("result_size")).thenReturn("5");
        when(req.getParameter("playtime")).thenReturn(null);
        when(resp.getWriter()).thenReturn(writer);

        gameService.doGet(req, resp);

        JSONObject result = new JSONObject(stringWriter.toString());
        Assert.assertEquals(result.getInt("code"), 400);
        Assert.assertEquals(result.getString("status"), "invalid request");
        Assert.assertEquals(result.getJSONArray("data").length(), 0);
    }

    @Test
    public void testInvalidRequest3() throws IOException, ServletException {
        GameApiService gameService = new GameApiService();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(req.getParameter("result_size")).thenReturn("bad param");
        when(req.getParameter("playtime")).thenReturn("5");
        when(resp.getWriter()).thenReturn(writer);

        gameService.doGet(req, resp);

        JSONObject result = new JSONObject(stringWriter.toString());
        Assert.assertEquals(result.getInt("code"), 400);
        Assert.assertEquals(result.getString("status"), "invalid request");
        Assert.assertEquals(result.getJSONArray("data").length(), 0);
    }

    @Test
    public void testInvalidRequest4() throws IOException, ServletException {
        GameApiService gameService = new GameApiService();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(req.getParameter("result_size")).thenReturn("5");
        when(req.getParameter("playtime")).thenReturn("bad param");
        when(resp.getWriter()).thenReturn(writer);

        gameService.doGet(req, resp);

        JSONObject result = new JSONObject(stringWriter.toString());
        Assert.assertEquals(result.getInt("code"), 400);
        Assert.assertEquals(result.getString("status"), "invalid request");
        Assert.assertEquals(result.getJSONArray("data").length(), 0);
    }

    @Test
    public void testHandleBadParam1() throws IOException, ServletException {
        GameApiService gameService = new GameApiService();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(req.getParameter("result_size")).thenReturn("5");
        when(req.getParameter("playtime")).thenReturn("5");
        
        when(req.getParameter("playtime_leniency")).thenReturn("bad param");
        when(resp.getWriter()).thenReturn(writer);

        gameService.doGet(req, resp);

        JSONObject result = new JSONObject(stringWriter.toString());
        Assert.assertEquals(result.getInt("code"), 200);
        Assert.assertEquals(result.getString("status"), "success");
        Assert.assertEquals(result.getJSONArray("data").length(), 5);
    }

    @Test
    public void testHandleBadParam2() throws IOException, ServletException {
        GameApiService gameService = new GameApiService();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(req.getParameter("result_size")).thenReturn("5");
        when(req.getParameter("playtime")).thenReturn("5");
        
        when(req.getParameter("genres")).thenReturn("bad param");
        when(resp.getWriter()).thenReturn(writer);

        gameService.doGet(req, resp);

        JSONObject result = new JSONObject(stringWriter.toString());
        Assert.assertEquals(result.getInt("code"), 200);
        Assert.assertEquals(result.getString("status"), "success");
        Assert.assertEquals(result.getJSONArray("data").length(), 0);
    }
    
}
