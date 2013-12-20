package server.service;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.servlet.*;
import org.json.JSONException;
import org.json.JSONObject;
import server.*;
import server.base.Frontend;
import server.message.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class FrontendImpl extends WebSocketServlet implements Subscriber, Runnable, Frontend {
    //public class FrontendImpl extends HttpServlet implements Subscriber, Runnable, Frontend {
    private final MessageSystem messageSystem;
    private final Address address;
    private Map<String, UserSession> sessionIdToUserSession = new ConcurrentHashMap<>();
    private Map<Long, UserSession> idToUserSession = new ConcurrentHashMap<>();
    private Map<String, Set<GMSocket>> nameToRoom = new ConcurrentHashMap<String, Set<GMSocket>>(); // TODO: commemnts
    private Map<Long, GMSocket> userIdToSocket = new ConcurrentHashMap<>();

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

    @Override
    public void closeSocket(Long idForSocket) {
        userIdToSocket.get(idForSocket).onWebSocketClose(418, "error");
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
//            case USERID:
//                if (userSession == null || userSession.getUserId() == null) {
//                    response.sendRedirect("/auth");
//                    return;
//                }
//                Cookie userCookie = new Cookie("user_id", URLEncoder.encode(String.valueOf(userSession.getUserId()), "UTF-8"));
//                response.addCookie(userCookie); // FIXME: hash userSession.getUserId() + salt
//                responseUserPage(response, "name = " + userSession.getLogin() + ", id = " + userSession.getUserId());
//                return;
            case CHAT:
                responseChatPage(response);
                return;
            case GAME:
                if (userSession == null || userSession.getUserId() == null) {
                    response.sendRedirect(AUTH);
                    return;
                }
                idToUserSession.put(userSession.getUserId(), userSession);
                Cookie gamerCookie = new Cookie("user_id", URLEncoder.encode(String.valueOf(userSession.getUserId()), "UTF-8"));
                response.addCookie(gamerCookie); // FIXME: hash userSession.getUserId() + salt
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
                response.sendRedirect(GAME + "/1"); // FIXME: here
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
                if (request.getPathInfo().matches(GAME + "/[A-Za-z0-9]{1,512}")) {
                    if (userSession == null || userSession.getUserId() == null) {
                        response.sendRedirect(AUTH);
                        return;
                    }
                    idToUserSession.put(userSession.getUserId(), userSession);
                    Cookie gamerCookieRoom = new Cookie("user_id", URLEncoder.encode(String.valueOf(userSession.getUserId()), "UTF-8"));
                    response.addCookie(gamerCookieRoom); // FIXME: hash userSession.getUserId() + salt
                    responseGamePage(response);
                } else
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
            UserSession userSession = new UserSession(messageSystem.getAddressService().getAddressFE(), messageSystem.getAddressService().getAddressGM(), messageSystem.getAddressService().getAddressAS(), login, sessionId);
            sessionIdToUserSession.put(sessionId, userSession);

            Address frontendAddress = getAddress();
            Address accountServiceAddress = userSession.getAddressAS();
            messageSystem.sendMessage(new MsgGetUserId(frontendAddress, accountServiceAddress, login, password, sessionId));

            // FIXME: possible bug with addresses

            response.sendRedirect(GAME + "/1"); // FIXME: here
        }

        if (request.getPathInfo().equals("/newuser")) {
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            String sessionId = request.getSession().getId();
            UserSession userSession = new UserSession(messageSystem.getAddressService().getAddressFE(), messageSystem.getAddressService().getAddressGM(), messageSystem.getAddressService().getAddressAS(), login, sessionId);
            sessionIdToUserSession.put(sessionId, userSession);

            Address frontendAddress = getAddress();
            Address accountServiceAddress = userSession.getAddressAS();
            messageSystem.sendMessage(new MsgToPutUser(frontendAddress, accountServiceAddress, login, password, sessionId));

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
        webSocketServletFactory.getPolicy().setIdleTimeout(100000);
        webSocketServletFactory.setCreator(new WebSocketCreator() {
            @Override
            public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) {
                // TODO: check for correct url
                String path = servletUpgradeRequest.getRequestPath();
                String room = path.substring("/gamemechanics".length() + 1);
//                servletUpgradeResponse.addHeader("roomName",room);
                Long userId = Long.valueOf(servletUpgradeRequest.getCookies().get(1).getValue());
                GMSocket socket = new GMSocket(room, userId);
                // msg to GM -> room / count player
                if (nameToRoom.get(room) == null) {
                    Set roomSet = new HashSet();
                    roomSet.add(socket);
                    nameToRoom.put(room, roomSet);
                } else {
                    nameToRoom.get(room).add(socket);
                }
                return socket;
            }
        });
    }

    private class GMSocket implements WebSocketListener {
        private Session session;
        private String roomName;
        private Long userId;

        public GMSocket(String roomName, Long userId) {
            this.roomName = roomName;
            this.userId = userId;
        }

        private void sendMessage(String message) {
            try {
                session.getRemote().sendString(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void broadcast(String message) {
            for (GMSocket socket : nameToRoom.get(roomName)) {
                if (this == socket)
                    continue;
                socket.sendMessage(message);
            }
        }

        @Override
        public void onWebSocketBinary(byte[] payload, int offset, int len) {

        }

        @Override
        public void onWebSocketClose(int statusCode, String reason) {
            if (nameToRoom.get(roomName) != null) {
                nameToRoom.get(roomName).remove(this);
            }
        }

        @Override
        public void onWebSocketConnect(Session session) {
            this.session = session;
            userIdToSocket.put(userId, this);
            messageSystem.sendMessage(new MsgUserAdded(messageSystem.getAddressService().getAddressFE(), messageSystem.getAddressService().getAddressGM(), roomName, userId));
            System.out.println("opened");
        }

        @Override
        public void onWebSocketError(Throwable cause) {
            System.out.println("Cause: " + cause);
            if (nameToRoom.get(roomName) != null) {
                nameToRoom.get(roomName).remove(this);
            }
        }

        @Override
        public void onWebSocketText(String message) {
            try {
                JSONObject jsonObject = new JSONObject(message);
                String id = jsonObject.getString("id");
                String msg = jsonObject.getString("message");
                System.out.println("            id: " + id);
                System.out.println("       message: " + msg);

                UserSession userSession = idToUserSession.get(Long.parseLong(id));
                String gamerLogin = userSession.getLogin();
                Address frontend = messageSystem.getAddressService().getAddressFE();
                Address gameMechanics = messageSystem.getAddressService().getAddressGM();
                messageSystem.sendMessage(new MsgSendEvent(frontend, gameMechanics, gamerLogin + ": " + msg));
//                System.out.println("Socket session:");
//                System.out.println(session);
//                System.out.println("User session");
//                System.out.println(userSession);

            } catch (JSONException e) {
//                e.printStackTrace();
                System.out.println("Smth got wrong with casting message to json");
            }
            broadcast("Hello");
        }
    }
}
