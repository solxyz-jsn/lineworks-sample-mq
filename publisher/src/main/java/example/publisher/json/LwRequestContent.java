package example.publisher.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/** LINE WORKSからのCallbackを受信 - 送信内容格納DTO */
@Data
public class LwRequestContent {
  /** メッセージタイプ */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String type;
  /** メッセージ本文 */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String text;
  /** postbackメッセージ */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String postback;
}
