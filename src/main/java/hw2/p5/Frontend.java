package hw2.p5;


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

    private String message = "Hello Server";
    private AtomicLong userIdGenerator = new AtomicLong();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            userId = userIdGenerator.getAndIncrement();
            session.setAttribute("userId", userId);
        }
        pageVariables.put("userId", userId);
        pageVariables.put("message", message);
        response.getWriter().println(PageGenerator.getPage("index_user_id.tml", pageVariables));
    }
}
