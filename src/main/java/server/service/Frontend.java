package server.service;

import server.*;
import server.message.MessageSystem;
import server.message.MsgGetUserId;

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
import java.util.concurrent.ConcurrentHashMap;

public class Frontend extends HttpServlet implements Subscriber, Runnable {
    private MessageSystem messageSystem;
    private final Address address;
    private Map<String, UserSession> sessionIdToUserSession = new ConcurrentHashMap<>();
    private Map<Integer, UserSession> userIdToUserSession = new ConcurrentHashMap<>();

//    private static AtomicLong handleCount = new AtomicLong(0);
//    private static Logger log = Logger.getLogger(Frontend.class.getLogin());

    public Frontend(MessageSystem messageSystem) {
        this.address = new Address();
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
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
        userSession.setAuthResponseFromServer();
    }

    private static String getTime() {
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

    private void responseChatPage(HttpServletResponse response, String userState) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("userState", userState);
        response.getWriter().println(PageGenerator.getPage("chat.tml", pageVariables));
    }
    // TODO: master slave
    private void responseMasterPage(HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("nothing", "nothing");
        response.getWriter().println(PageGenerator.getPage("master.tml", pageVariables));
    }
    private void responseSlavePage(HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("nothing", "nothing");
        response.getWriter().println(PageGenerator.getPage("slave.tml", pageVariables));
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
//        handleCount.incrementAndGet();

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());

        if (request.getPathInfo().equals("/userid")) {
            if (userSession == null || userSession.getUserId() == null) {
                response.sendRedirect("/auth");
                return;
            }
            responseUserPage(response, "name = " + userSession.getLogin() + ", id = " + userSession.getUserId());
            return;
        }

        if (request.getPathInfo().equals("/chat")) {
            responseChatPage(response, "nothing");
            return;
        }
        // TODO: master slave
        if (request.getPathInfo().equals("/master")) {
            responseMasterPage(response);
            return;
        }
        if (request.getPathInfo().equals("/slave")) {
            responseSlavePage(response);
            return;
        }

        if (request.getPathInfo().equals("/auth")) {
            if (userSession == null) {
                responseAuthPage(response, "New session. Hello!");
                return;
            }
            if (userSession.getUserId() == null) {
                if (userSession.gotAuthResponse()) {
                    responseAuthPage(response, "Incorrect user/password");
                    return;
                }
                responseAuthPage(response, "Waiting for authorization");
                return;
            }
            response.sendRedirect("/userid");
        }

        if (request.getPathInfo().equals("/logout")) {
            String sessionId = request.getSession().getId();
            if (sessionIdToUserSession.get(sessionId) != null)
                sessionIdToUserSession.remove(sessionId);
            response.sendRedirect("/");
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
            UserSession userSession = new UserSession(messageSystem.getAddressService().getAddress(), login, sessionId);
            sessionIdToUserSession.put(sessionId, userSession);

            Address frontendAddress = getAddress();
            Address accountServiceAddress = userSession.getAddress();

            messageSystem.sendMessage(new MsgGetUserId(frontendAddress, accountServiceAddress, login, password, sessionId));

            response.sendRedirect("/userid");
        }
    }


    public void run() {
        while (true) {
            messageSystem.execForSubscriber(this);
            TimeHelper.sleep(100);
//            log.info(String.valueOf(handleCount));
        }
    }
}
