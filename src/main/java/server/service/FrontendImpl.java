package server.service;

import org.eclipse.jetty.websocket.servlet.*;
import server.*;
import server.base.Frontend;
import server.message.MessageSystem;
import server.message.MsgGetUserId;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@ServerEndpoint(value = "/gamemechanics")
//@WebServlet(name = "GM", value = "/gamemechanics")
//@SuppressWarnings("serial")

public class FrontendImpl extends WebSocketServlet implements Subscriber, Runnable, Frontend {
    //public class FrontendImpl extends HttpServlet implements Subscriber, Runnable, Frontend {
    private final MessageSystem messageSystem;
    private final Address address;
    private Map<String, UserSession> sessionIdToUserSession = new ConcurrentHashMap<>();
    private Map<Long, UserSession> idToUserSession = new ConcurrentHashMap<>();

    public FrontendImpl(MessageSystem messageSystem) {
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

    private void response404Page(HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("userState", "nothing");
        response.getWriter().println(PageGenerator.getPage("404.tml", pageVariables));
    }

    // mobile --> Frontend course
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

    private static final String USERID = "/userid";
    private static final String CHAT = "/chat";
    private static final String GAME = "/game";
    private static final String AUTH = "/auth";
    private static final String LOGOUT = "/logout";
    private static final String MOBILE_MASTER = "/master";
    private static final String MOBILE_SLAVE = "/slave";

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
//        handleCount.incrementAndGet();

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession();
        UserSession userSession = sessionIdToUserSession.get(session.getId());

        switch (request.getPathInfo()) {
            case USERID:
                if (userSession == null || userSession.getUserId() == null) {
                    response.sendRedirect("/auth");
                    return;
                }
                Cookie userCookie = new Cookie("user_id", URLEncoder.encode(String.valueOf(userSession.getUserId()), "UTF-8"));
                response.addCookie(userCookie); // FIXME: hash userSession.getUserId() + salt
                responseUserPage(response, "name = " + userSession.getLogin() + ", id = " + userSession.getUserId());
                return;
            case CHAT:
                responseChatPage(response);
                return;
            case GAME:
                if (userSession == null || userSession.getUserId() == null) {
                    response.sendRedirect("/auth");
                    return;
                }
                idToUserSession.put(userSession.getUserId(), userSession);
                responseGamePage(response);
                return;
            case AUTH:
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
                return;
            case LOGOUT:
                String sessionId = request.getSession().getId();
                if (sessionIdToUserSession.get(sessionId) != null)
                    sessionIdToUserSession.remove(sessionId);
                response.sendRedirect("/");
                return;
            // for mobile --> Frontend course
            case MOBILE_MASTER:
                responseMasterPage(response);
                return;
            case MOBILE_SLAVE:
                responseSlavePage(response);
                return;
            default:
                response404Page(response);
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


    public void run() {
        while (true) {
            messageSystem.execForSubscriber(this);
            TimeHelper.sleep(100);
//            log.info(String.valueOf(handleCount));
        }
    }

    // TODO: fix WebSockets from here
    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.getPolicy().setIdleTimeout(10000);
        webSocketServletFactory.setCreator(new WebSocketCreator() {
            @Override
            public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) {
                return null;
            }
        });
    }

//    @Override
//    public WebSocket doWebSocketConnect(HttpServletRequest httpServletRequest, String s) {
//        return null;
//    }

//    @Override
//    public WebSocket doWebSocketConnect(HttpServletRequest httpServletRequest, String s) {
//        return new GMSocket(); // TODO: return socket
//    }
//
//    private class GMSocket implements WebSocket.OnTextMessage {
//        @Override
//        public void onMessage(String s) {
//            System.out.println("Received: " + s);
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(s);
//                System.out.println("            id: " + jsonObject.getString("id"));
//                System.out.println("       message: " + jsonObject.getString("message"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            // TODO: send message
//
////        sessionIdToUserSession.get(sessionId, userSession);
////        Address frontendAddress = getAddress();
////        Address gameMechanicsAddress = userSession.getAddressGM();
////        messageSystem.sendMessage(new MsgSendEvent(frontendAddress, accountServiceAddress, message));
//        }
//
//        @Override
//        public void onOpen(Connection connection) {
//            System.out.println("Opened");
//        }
//
//        @Override
//        public void onClose(int i, String s) {
//            System.out.println("Closed");
//        }
//    }
}
