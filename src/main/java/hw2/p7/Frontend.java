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

    private Map<String, Long> login_id = new HashMap<>();
    private Map<String, String> login_password = new HashMap<>();
    public Frontend() {
        String[] users = {"user0", "user1", "user2"};

        login_password.put(users[0], "user0pwd");
        login_password.put(users[1], "user1pwd");
        login_password.put(users[2], "user2pwd");
        login_id.put(users[0], Long.valueOf(0));
        login_id.put(users[1], Long.valueOf(1));
        login_id.put(users[2], Long.valueOf(2));
    }

    private String login = "";
    private String password;
    private AtomicLong userIdGenerator = new AtomicLong();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession();
        Long sessionId = (Long) session.getAttribute("sessionId");
        if (sessionId == null) {
            sessionId = userIdGenerator.getAndIncrement();
            session.setAttribute("sessionId", sessionId);
        }

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("lastLogin", login);
        pageVariables.put("sessionId", sessionId);

        Long id = (Long) session.getAttribute("userId");
        if (id == null) {
            pageVariables.put("userId", "knock-knock");
        }
        else {
            pageVariables.put("userId", id);
        }

        response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        login = request.getParameter("login");
        password = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession();
        Long sessionId = (Long) session.getAttribute("sessionId");
        if (sessionId == null) {
            sessionId = userIdGenerator.getAndIncrement();
            session.setAttribute("sessionId", sessionId);
        }

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("sessionId", sessionId);
        pageVariables.put("lastLogin", login);
        if (password.equals(login_password.get(login))) {
            Long id = login_id.get(login);
            pageVariables.put("userId", id);
            session.setAttribute("userId", id);
        }
        else {
            pageVariables.put("userId", "you shall not pass");
        }

        response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
    }
}
