package example.publisher.json;

import lombok.Builder;
import lombok.Data;

/** リンクメッセージのコンテント用DTO */
@Data
@Builder
public class LwLinkContent {
    /** タイプ */
    private String type;
    /** ボタン上部のメッセージ本文 */
    private String contentText;
    /** ボタンに表示するテキスト */
    private String linkText;
    /** linkTextクリック時の遷移先URL */
    private String link;
}
