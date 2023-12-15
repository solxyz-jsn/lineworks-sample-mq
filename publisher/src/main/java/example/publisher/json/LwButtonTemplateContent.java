package example.publisher.json;

import lombok.Builder;
import lombok.Data;

/** ボタンテンプレートのコンテント送信用DTO */
@Data
@Builder
public class LwButtonTemplateContent {
    /** タイプ */
    private String type;
    /** ボタン上部のメッセージ本文 */
    private String contentText;
    /** ボタン部（配列） */
    private LwButtonActions[] actions;
}
