package server.socket;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GetUserAgentConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config,
                                HandshakeRequest request,
                                HandshakeResponse response)
    {
        config.getUserProperties().put("User-Agent", request.getHeaders().get("User-Agent").get(0));
        // TODO: проверить, будет ли путанница в User-Agent-ах при большом количестве запросов
    }
}
