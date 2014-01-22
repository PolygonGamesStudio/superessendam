package server.service;

import server.*;
import server.base.Frontend;
import server.message.MessageSystem;
import server.message.MsgGetRooms;
import server.message.MsgGetUserId;
import server.message.MsgToPutUser;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


//public class FrontendImpl extends WebSocketServlet implements Subscriber, Runnable, Frontend {
public class FrontendImpl extends HttpServlet implements Subscriber, Runnable, Frontend {
    private final MessageSystem messageSystem;
    private final Address address;
    private Map<String, UserSession> sessionIdToUserSession = new ConcurrentHashMap<>();
    private Map<Long, UserSession> idToUserSession = new ConcurrentHashMap<>();
    //    private Map<String, Set<GMSocket>> nameToRoom = new ConcurrentHashMap<String, Set<GMSocket>>(); // TODO: commemnts
//    private Map<Long, GMSocket> userIdToSocket = new ConcurrentHashMap<>();
    private Map<String, Long> roomsCountUsers = new HashMap<>();

    public FrontendImpl(MessageSystem messageSystem) {
        this.address = new Address();
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().setAddressFE(address);
    }


    public Address getAddress() {
        return address;
    }

    public void putDataForGameHall(Map gamesMap) {
        for (Object key : gamesMap.keySet()) {
            roomsCountUsers.put(key.toString(), (Long) gamesMap.get(key));
        }
    }

    @Override
    public void broadcastToSockets(Long userId, String response) {
//        userIdToSocket.get(userId).broadcast(response);
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
    public void makeDecisionAboutPersonInRoom(Long idForSocket, String roomName, boolean isConnectionToRoomAllowed) {
        if (isConnectionToRoomAllowed) {
            if (idToUserSession.get(idForSocket) != null) {
                idToUserSession.get(idForSocket).setRoomName(roomName);
            }
        }
    }

    private static String getTime() {
        Date date = new Date();
        date.getTime();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
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

    private void responseGamePage(HttpServletResponse response, String userState) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("userState", userState);
        response.getWriter().println(PageGenerator.getPage("game.tml", pageVariables));
    }

    private void responseGameHallPage(HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("roomsCountUsers", roomsCountUsers);
        response.getWriter().println(PageGenerator.getPage("game_hall.tml", pageVariables));
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

    private static final String CHAT = "/chat";
    private static final String GAME = "/game";
    private static final String AUTH = "/auth";
    private static final String LOGOUT = "/logout";
    private static final String GAMEHALL = "/game-hall";
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
                responseGamePage(response, "User state: name = " + userSession.getLogin() + ", id = " + userSession.getUserId());
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
            case GAMEHALL:
                if (userSession == null || userSession.getUserId() == null) {
                    response.sendRedirect(GAMEHALL);
                    messageSystem.sendMessage(new MsgGetRooms(getAddress(), messageSystem.getAddressService().getAddressGM())); // FIXME: переместить в место после установление сокета
                    return;
                }
                Address frontendAddress = getAddress();
                Address accountGM = userSession.getAddressGM();
//                response.
                responseGameHallPage(response);
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
                    responseGamePage(response, "User state: name = " + userSession.getLogin() + ", id = " + userSession.getUserId());
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response404Page(response);
                }
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

            response.sendRedirect(GAME + "/qwe"); // FIXME: here
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

            response.sendRedirect(GAME + "/qwe"); // FIXME: here
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
