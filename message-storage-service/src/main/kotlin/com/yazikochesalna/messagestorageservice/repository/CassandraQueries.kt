package com.yazikochesalna.messagestorageservice.repository

object CassandraQueries {
    const val CURSOR_MESSAGE_SELECT = """
        SELECT * FROM messages 
        WHERE id = ?
    """

    const val BEFORE_CURSOR_SELECT = """
        SELECT * FROM messages_by_chat 
        WHERE chat_id = ? AND send_time < ? 
        ORDER BY send_time DESC 
        LIMIT ?
    """

    const val AFTER_CURSOR_SELECT = """
        SELECT * FROM messages_by_chat
        WHERE chat_id = ? AND send_time > ? 
        ORDER BY send_time ASC 
        LIMIT ?
    """

    const val WITHOUT_CURSOR_SELECT = """
        SELECT * FROM messages_by_chat
        WHERE chat_id = ?
        LIMIT ?
    """
}