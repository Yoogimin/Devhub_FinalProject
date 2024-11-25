package com.icia.devhub.dto.Event;

import lombok.Data;

@Data
public class ChatMessage {
    private String type;        // 메시지의 유형을 나타내는 필드 ("JOIN", "CHAT", 등)
    private String content;     // 메시지의 내용을 담는 필드
    private String profile;     // 프로필사진을 담는 필드
    private String sender;      // 메시지를 보낸 사용자의 이름을 담는 필드
}
