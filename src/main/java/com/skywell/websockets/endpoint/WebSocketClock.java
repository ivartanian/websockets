package com.skywell.websockets.endpoint;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by viv on 15.01.2016.
 */
@ServerEndpoint(value = "/clock"/*, configurator=ServerEndPointConfigurator.class*/)
public class WebSocketClock {

    private Logger LOG = Logger.getLogger(this.getClass().getName());

    private static ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static Set<Session> allSessions = new HashSet<>();

    @OnOpen
    public void showTime(Session session, EndpointConfig conf) {

        LOG.info("Connected ... " + session.getId());
        allSessions.add(session);

        // start the scheduler on the very first connection
        // to call sendTimeToAll every second
        if (allSessions.size()>=1){
            timer.scheduleAtFixedRate(() -> sendTimeToAll(session),0,3, TimeUnit.SECONDS);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        allSessions.remove(session);
        timer.shutdown();
        LOG.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }

    @OnError
    public void error(Session session, Throwable e) {
        allSessions.remove(session);
        timer.shutdown();
        LOG.log(Level.INFO, "Connection error.");
        LOG.log(Level.INFO, "Error", e);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            for (Session sess : session.getOpenSessions()) {
                if (sess.isOpen())
                    sess.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Error", e);
        }
    }

    @OnMessage
    public void pongMessage(Session session, PongMessage msg) {
        LOG.info("Pong message: " + msg.getApplicationData().toString());
    }

    private void sendTimeToAll(Session session) {
        for (Session sess : allSessions) {
            try {
                if (session.isOpen()) {
                    sess.getBasicRemote().sendText("Local time: " + LocalTime.now().format(timeFormatter));
                }
            } catch (IOException e) {
                LOG.warning(e.getMessage());
                try {
                    sess.close();
                } catch (IOException e1) {
                    LOG.warning(e1.getMessage());
                }
            }
        }
    }
}