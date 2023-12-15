package example.publisher.json;

import lombok.Builder;
import lombok.Data;

/** テキストメッセージのコンテキスト用DTO */
@Data
@Builder
public class LwTextContent {
  /** タイプ */
  private String type;
  /** メッセージ本文 */
  private String text;
}
