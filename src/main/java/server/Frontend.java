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

public class Frontend extends HttpServlet implements Subscriber, Runnable {
    private MessageSystem messageSystem;
    private Address address;
    private Map<String, UserSession> sessionIdToUserSession = new HashMap<>();

//    private static AtomicLong handleCount = new AtomicLong(0);
//    private static Logger log = Logger.getLogger(Frontend.class.getLogin());

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

//    private String login = "";
//    private String password;
//    private AtomicLong userIdGenerator = new AtomicLong();

    public static String getTime() {
        Date date = new Date();
        date.getTime();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
    }

    private void responseUserPage(HttpServletResponse response, String userState) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", getTime());
        pageVariables.put("userState", userState);
        response.getWriter().println(PageGenerator.getPage("timer.tml", pageVariables));
    }

    private void responseAuthPage(HttpServletResponse response, String userState) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("userState", userState);
        response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
//        handleCount.incrementAndGet();

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());

        if (request.getPathInfo().equals("/userid")) {
            if (userSession == null) {
                responseUserPage(response, "Auth error");
                return;
            }
            if (userSession.getUserId() == null) {
                responseUserPage(response, "Waitiing for authorization");
            }
            responseUserPage(response, "name = " + userSession.getLogin() + ", id = " + userSession.getUserId());
        }

        if (request.getPathInfo().equals("/auth")) {
            if (userSession == null) {
                responseAuthPage(response, "New session. Hello!");
                return;
            }
            if (userSession.getUserId() == null) {
                responseAuthPage(response, "I still don't know you");   // TODO: fix message here
                return;
            }
            response.sendRedirect("/userid");
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
//        handleCount.incrementAndGet();
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        if (request.getPathInfo().equals("/auth")) {
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            String sessionId = request.getSession().getId();
            UserSession userSession = new UserSession(messageSystem.getAddressService(), login, password, sessionId);

            Address frontendAddress = getAddress();
            Address accountServiceAddress = userSession.getAddress();

            // TODO: send message to specify whether user exists

            if (userSession.correctPassword(password)) {
                response.sendRedirect("/userid");
            } else {
                responseAuthPage(response, "Incorrect password");
            }
        }
    }

    public void run() {
        while (true) {
            // TODO: execute massage system
            // TODO: use time helper to sleep
//            log.info(String.valueOf(handleCount));
        }
    }
}
