//package com.skywell.websockets.config;
//
//import com.skywell.websockets.endpoint.WebSocketClock;
//
//import javax.websocket.HandshakeResponse;
//import javax.websocket.server.HandshakeRequest;
//import javax.websocket.server.ServerEndpointConfig;
//
///**
// * Created by viv on 15.01.2016.
// */
//public class ServerEndPointConfigurator extends ServerEndpointConfig.Configurator {
//
//    @Override
//    public void modifyHandshake(ServerEndpointConfig conf, HandshakeRequest req, HandshakeResponse resp) {
//        conf.getUserProperties().put("handshakereq", req);
//    }
//
//}
