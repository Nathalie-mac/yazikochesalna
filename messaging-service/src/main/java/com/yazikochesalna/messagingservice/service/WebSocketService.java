package com.yazikochesalna.messagingservice.service;

import com.yazikochesalna.messagingservice.dto.SendRequestMessageDTO;
import com.yazikochesalna.messagingservice.dto.SendResponseResultType;
import org.springframework.web.socket.WebSocketSession;

public interface WebSocketService {

    void addSession(WebSocketSession session);

    void removeSession(WebSocketSession session);

    void sendMessage(WebSocketSession session, SendRequestMessageDTO sendRequestMessageDTO);

    void receiveSendResponse(WebSocketSession session, SendResponseResultType sendResponseResultType);

    void receiveMessage();
}
