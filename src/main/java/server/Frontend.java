package server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class Frontend extends HttpServlet implements Subscriber, Runnable {
    private MessageSystem messageSystem;
    private Address address;
    private Map<String, UserSession> sessionIdToUserSession = new HashMap<>();

    private static AtomicLong handleCount = new AtomicLong(0);
    private static Logger log = Logger.getLogger(Frontend.class.getName());

    public Frontend(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        this.address = new Address();
        // TODO: code something with address
    }

    public Address getAddress() {
        return address;
    }

    public void setId(String sessionId, Long userId) {
        UserSession userSession = sessionIdToUserSession.get(sessionId);
        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }
        userSession.setUserId(userId);
    }

    // TODO: resume from here

    private String login = "";
    private String password;
    private AtomicLong userIdGenerator = new AtomicLong();

    public static String getTime() {
        Date date = new Date();
        date.getTime();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        handleCount.incrementAndGet();

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

        Long id = (Long) session.getAttribute("userId");
        if (id == null) {
            pageVariables.put("userId", "knock-knock");
        } else {
            pageVariables.put("userId", id);
        }

        if (request.getPathInfo().equals("/timer")) {
            pageVariables.put("refreshPeriod", "1000");
            pageVariables.put("serverTime", getTime());
            response.getWriter().println(PageGenerator.getPage("timer.tml", pageVariables));
            return;
        }

        response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        handleCount.incrementAndGet();

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
            pageVariables.put("refreshPeriod", "1000");
            pageVariables.put("serverTime", getTime());
//            response.getWriter().println(server.PageGenerator.getPage("timer.tml", pageVariables));
            response.sendRedirect("/timer");
        } else {
            pageVariables.put("userId", "you shall not pass");
            response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
        }
    }


    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(String.valueOf(handleCount));
        }
//        Вопросы:
//          имеет ли смысл счетчик времени делать вне Frontend-а?
//          почему, когда два таймера работают, +15 handle count?
    }


}
