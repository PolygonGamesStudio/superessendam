package hw2.p7;


import hw2.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Frontend extends HttpServlet {

    private String login = "";
    private String password = "";
    private HttpSession localSession;   // here
    private AtomicLong userIdGenerator = new AtomicLong();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put("lastLogin", login);

        localSession = request.getSession();
        Long sessionId = (Long) localSession.getAttribute("sessionId");
        if (sessionId == null) {
            sessionId = userIdGenerator.getAndIncrement();
            localSession.setAttribute("sessionId", sessionId);
        }
        pageVariables.put("sessionId", sessionId);

        // аналогично ли это sessionId ?
        String userId = request.getSession().getId();
        pageVariables.put("userId", userId);

        response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        login = request.getParameter("login");
        password = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("lastLogin", login);

        Long sessionId = (Long) localSession.getAttribute("sessionId");
        if (sessionId == null) {
            sessionId = userIdGenerator.getAndIncrement();
            localSession.setAttribute("sessionId", sessionId);
        }
        pageVariables.put("sessionId", sessionId);

        String userId = request.getSession().getId();
        pageVariables.put("userId", userId);

        response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
    }
}
