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

public class GameMechanics extends HttpServlet implements Subscriber, Runnable {
    private MessageSystem messageSystem;
    private final Address address;
    private Map<String, UserSession> sessionIdToUserSession = new ConcurrentHashMap<>();
    private Map<Integer, UserSession> userIdToUserSession = new ConcurrentHashMap<>();

//    private static AtomicLong handleCount = new AtomicLong(0);
//    private static Logger log = Logger.getLogger(Frontend.class.getLogin());

    public GameMechanics(MessageSystem messageSystem) {
        this.address = new Address();
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
    }

    public Address getAddress() {
        return address;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
//        handleCount.incrementAndGet();
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }


    public void run() {
        while (true) {
            messageSystem.execForSubscriber(this);

            TimeHelper.sleep(100);
        }
    }
}
