package example.publisher.json;

import lombok.Builder;
import lombok.Data;

/** ボタンを押した際に実行されるアクション（ボタンテンプレート、カルーセルテンプレートで使用） */
@Data
@Builder
public class LwButtonActions {
    /** タイプ */
    private String type;
    /** ボタンに表示するラベル */
    private String label;
    /** 項目を選択したときに送信されるテキスト */
    private String text;
    /** ボタン押下時のポストバック */
    private String postback;
    /** ボタン押下時に開くURI */
    private String uri;
}
