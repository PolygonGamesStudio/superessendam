package server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import server.message.MessageSystem;
import server.service.AccountService;
import server.service.Frontend;
import server.socket.ChatSocketWithUserAgentToken;

import javax.websocket.server.ServerContainer;

public class Main {

    public static void main(String[] args) throws Exception {
        MessageSystem messageSystem = new MessageSystem();

        Frontend frontend = new Frontend(messageSystem);
        AccountService accountService = new AccountService(messageSystem);

        (new Thread(frontend)).start();
        (new Thread(accountService)).start();

//        String workingDir = System.getProperty("user.dir"); // log относительно текущей директории
//        ThreadPool threadPool = new ThreadPool(7, workingDir + "/static/log/server.log");

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
        context.addServlet(new ServletHolder(frontend), "/*");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);

        // socket realization from here on...
        try {
            ServerContainer wsContainer = WebSocketServerContainerInitializer.configureContext(context);

            wsContainer.addEndpoint(ChatSocketWithUserAgentToken.class);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace(System.err);
        }

        server.start();
        server.join();

    }
}
