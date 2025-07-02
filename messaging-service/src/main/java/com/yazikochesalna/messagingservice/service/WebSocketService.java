package com.yazikochesalna.messagingservice.service;

import com.yazikochesalna.messagingservice.dto.messaging.notification.ReceiveMessageDTO;
import com.yazikochesalna.messagingservice.dto.messaging.request.SendRequestMessageDTO;
import com.yazikochesalna.messagingservice.dto.messaging.response.SendResponseResultType;
import org.springframework.web.socket.WebSocketSession;

public interface WebSocketService {

    void addSession(WebSocketSession session);

    void removeSession(WebSocketSession session);

    void sendMessage(WebSocketSession session, SendRequestMessageDTO sendRequestMessageDTO);

    void receiveSendResponse(WebSocketSession session, SendResponseResultType sendResponseResultType);

    void receiveMessage(WebSocketSession session, ReceiveMessageDTO receiveMessageDTO);

    void sendMessageToChat(Long chatId, ReceiveMessageDTO receiveMessage);
}
