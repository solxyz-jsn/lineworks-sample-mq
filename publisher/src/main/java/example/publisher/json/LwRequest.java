package example.publisher.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** LINE WORKSからのCallbackを受信するDTO */
@Data
@EqualsAndHashCode
public class LwRequest {
  /** リクエストタイプ */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String type;
  /** 送信者情報 */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private LwRequestSource source;
  /** 作成日時(yyyy-MM-dd'T'HH:mm:ss.SSSz) */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String issuedTime;
  /** 送信内容 */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private LwRequestContent content;
}
