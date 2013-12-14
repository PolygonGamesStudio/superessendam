package server.service;

import server.*;
import server.message.MessageSystem;
import server.message.MsgGetUserId;
import server.message.MsgSendEvent;
import server.socket.FrontendConfigurator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint(value = "/gamemechanics", configurator = FrontendConfigurator.class)
public class Frontend extends HttpServlet implements Subscriber, Runnable {
    private MessageSystem messageSystem;
    private final Address address;
    private Map<String, UserSession> sessionIdToUserSession = new ConcurrentHashMap<>();
    private Map<Long, UserSession> idToUserSession = new ConcurrentHashMap<>();


    public Frontend(MessageSystem messageSystem) {
        this.address = new Address();
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
    }
    // WARNING Должно быть в топе, или doGet "сьест" сокет  и он не будет работать
    @OnOpen
    public void onWebSocketConnect(Session session, EndpointConfig config) {
        UserSession userSession = idToUserSession.get(config.getUserProperties().get("session"));
        System.out.println("Socket connected: " + session);
    }

    @OnMessage
    public void onWebSocketMessage(String message) {
        System.out.println("FE Received message: " + message);
        sessionIdToUserSession.get(sessionId, userSession);
        Address frontendAddress = getAddress();
        Address gameMechanicsAddress = userSession.getAddressGM();
        messageSystem.sendMessage(new MsgSendEvent(frontendAddress, accountServiceAddress, message));

    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        System.out.println("Socket closed because: " + reason);
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
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

    private void responseChatPage(HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("userState", "nothing");
        response.getWriter().println(PageGenerator.getPage("chat.tml", pageVariables));
    }

    private void responseGamePage(HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("userState", "nothing");
        response.getWriter().println(PageGenerator.getPage("game.tml", pageVariables));
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
            responseChatPage(response);
            return;
        }
        if (request.getPathInfo().equals("/game")) {
            if (userSession == null || userSession.getUserId() == null) {
                response.sendRedirect("/auth");
                return;
            }
            idToUserSession.put(userSession.getUserId(), userSession);
            responseGamePage(response);
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

        // mobile

        if (request.getPathInfo().equals("/master")) {
            responseMasterPage(response);
            return;
        }
        if (request.getPathInfo().equals("/slave")) {
            responseSlavePage(response);
            return;
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
            UserSession userSession = new UserSession(messageSystem.getAddressService().getAddressFE(), messageSystem.getAddressService().getAddressGM(), login, sessionId);
            sessionIdToUserSession.put(sessionId, userSession);

            Address frontendAddress = getAddress();
            Address accountServiceAddress = userSession.getAddressFE();

            messageSystem.sendMessage(new MsgGetUserId(frontendAddress, accountServiceAddress, login, password, sessionId));

            response.sendRedirect("/userid");
        }
    }

    //socket

    public void run() {
        while (true) {
            messageSystem.execForSubscriber(this);
            TimeHelper.sleep(100);
//            log.info(String.valueOf(handleCount));
        }
    }
}
