package server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import resource.ResourceSystemImpl;
import server.base.ResourceSystem;
import server.message.MessageSystem;
import server.service.AccountServiceImpl;
import server.service.FrontendImpl;
import server.service.GameMechanicsImpl;
import server.socket.PingPongSocket;

import javax.websocket.server.ServerContainer;


public class Main {

    public static void main(String[] args) throws Exception {
        MessageSystem messageSystem = new MessageSystem();

        FrontendImpl frontend = new FrontendImpl(messageSystem);
        ResourceSystem resourceSystem = new ResourceSystemImpl();
        AccountServiceImpl accountService = new AccountServiceImpl(messageSystem, resourceSystem, "dbaccess.xml");
        GameMechanicsImpl gameMechanics = new GameMechanicsImpl(messageSystem);


        (new Thread(frontend)).start();
        (new Thread(accountService)).start();
        (new Thread(gameMechanics)).start();

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        server.setHandler(context);
//        context.addServlet(new ServletHolder(frontend), "/game");
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

//            wsContainer.addEndpoint((ServerEndpointConfig) frontend);

//            wsContainer.addEndpoint(ChatSocketWithUserAgentToken.class);
            wsContainer.addEndpoint(PingPongSocket.class);
//            wsContainer.addEndpoint(FrontendImpl.class);
        } catch (Throwable throwable) {
            throwable.printStackTrace(System.err);
        }

        server.start();
        server.join();

    }
}
