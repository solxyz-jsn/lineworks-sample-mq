package example.publisher.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class LwAccessTokenIssueResponse {
  public LwAccessTokenIssueResponse(
      @JsonProperty("access_token") final String accessToken,
      @JsonProperty("refresh_token") final String refreshToken,
      @JsonProperty("scope") final String scope,
      @JsonProperty("expires_in") final String expiresIn,
      @JsonProperty("token_type") final String tokenType) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.scope = scope;
    this.expiresIn = expiresIn;
    this.tokenType = tokenType;
  }

  /** アクセストークン */
  @JsonProperty("access_token")
  private final String accessToken;

  /** リフレッシュトークン */
  @JsonProperty("refresh_token")
  private final String refreshToken;

  /** 利用するscope情報 */
  @JsonProperty("scope")
  private final String scope;

  /** アクセストークンの有効期限 (24時間) */
  @JsonProperty("expires_in")
  private final String expiresIn;

  /** 生成されるトークンタイプ */
  @JsonProperty("token_type")
  private final String tokenType;
}
