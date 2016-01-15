package com.skywell.websockets.clients;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by viv on 15.01.2016.
 */
@ClientEndpoint
public class ClientApplication {

    public static void main(String[] args) throws DeploymentException, IOException, URISyntaxException, InterruptedException {
        WebSocketContainer wsContainer =  ContainerProvider.getWebSocketContainer();
        Session session = wsContainer.connectToServer(ClientApplication.class, new URI("ws://localhost:8080/websockets/clock"));
        session.getBasicRemote().sendText("Here is a message!");
        Thread.sleep(1000);
        session.close();
    }

    @OnMessage
    public void processMessage(String message){
        System.out.println(message);
    }

}