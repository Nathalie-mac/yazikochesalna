package com.yazikochesalna.chatservice.service;

import java.util.List;
import java.util.Map;

public interface MessageStorageServiceClient {

    Map<Long, Object> getLastMessages(List<Long> chatIds);
}
