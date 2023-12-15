package example.publisher.json;

import lombok.Builder;
import lombok.Data;

/** リンクメッセージの送信用DTO */
@Data
@Builder
public class LwLinkRequest {
    /** コンテント */
    private LwLinkContent content;
    /** メッセージ送信者のLINE WORKSユーザーID（送信先の指定に使用） */
    private String userId;
    /** メッセージ送信先LINE WORKS BotのBot ID（送信先の指定に使用） */
    private String botId;
    /** アクセストークン（LINE WORKS API実行時に必要） */
    private String accessToken;
}
