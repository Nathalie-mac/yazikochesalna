package com.yazikochesalna.fileservice.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MetadataKeys {
    ORIGINAL_FILENAME("original-filename"),
    CHAT_ID("chat-id"),
    MESSAGE_UUID("message-uuid"),
    USER_ID("user-id");

    private final String key;
}
