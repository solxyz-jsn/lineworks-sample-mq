package example.publisher.json;

import lombok.Builder;
import lombok.Data;

/** カルーセルテンプレートのオブジェクトリスト用DTO */
@Data
@Builder
public class LwCarouselColumns {
    /** 画像URL */
    private String originalContentUrl;
    /** 画像タイトル */
    private String title;
    /** 画像キャプション用テキスト */
    private String text;
    /** カルーセルテンプレート内のボタン部（配列） */
    private LwButtonActions[] actions;
}
