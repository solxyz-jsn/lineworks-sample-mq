package example.consumer;

import com.rabbitmq.client.*;

import jakarta.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
@PropertySource("classpath:application.properties")
public class AmazonMqConsumeController{

  // application.properties の設定値を基に変数の値を定義
  /** Amazon MQのブローカーに対応するユーザー名 */
  @Value("${amazonmq.username}")
  private String amazonMqUsername;

  /** Amazon MQのブローカーに対応するパスワード */
  @Value("${amazonmq.password}")
  private String amazonMqPassword;

  /** Amazon MQのブローカーに対応するホストURL */
  @Value("${amazonmq.host}")
  private String amazonMqHost;

  /** Amazon MQのブローカーに対応するポート番号 */
  @Value("${amazonmq.port}")
  private Integer amazonMqPort;

  /** Amazon MQへの接続方式 */
  @Value("${amazonmq.sslprotocol}")
  private String amazonMqSslProtocol;

  /** ヘッダー情報 */
  private HttpHeaders headers;

  /**
   * MQからメッセージ受取を実行。MQにメッセージが入り次第受け取る。
   * プロジェクトを起動させることで、特定MQの監視状態に入る
   */
  @PostConstruct
  public void receiveMessageFromQueue() {
    try {
      // Amazon MQ内の監視するキュー指定（パブリッシャー側と同じ値にすること）
      String exchangeName = "exchange-sample";
      String queueName = "queue-sample";
      String bindingKey = "key-sample";

      // Amazon MQの送信先を設定する
      ConnectionFactory factory = new ConnectionFactory();
      factory.setUsername(amazonMqUsername);
      factory.setPassword(amazonMqPassword);
      factory.setHost(amazonMqHost);
      factory.setPort(amazonMqPort);
      factory.useSslProtocol(amazonMqSslProtocol);

      // Amazon MQへの接続開始
      Connection conn = factory.newConnection();
      Channel channel = conn.createChannel();

      // エクスチェンジ名とキュー名を指定して新規作成（既に存在する場合は上書き）
      channel.exchangeDeclare(exchangeName, "direct", true);
      channel.queueDeclare(queueName, true, false, false, null);
      channel.queueBind(queueName, exchangeName, bindingKey);

      // Amazon MQの指定したキューからメッセージを受信したときの処理
      boolean autoAck = false;
      channel.basicConsume(
          queueName,
          autoAck,
          new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                throws IOException {
              long deliveryTag = envelope.getDeliveryTag();
              // LINE WORKS Botへ引数のメッセージを送信
              sendMessageToLineWorks(new String(body));
              channel.basicAck(deliveryTag, false);
            }
          });
    } catch (IOException e) {
      System.out.println("Amazon MQへの接続に失敗しました");
      e.printStackTrace();
    } catch (TimeoutException e) {
      System.out.println("Amazon MQへの接続がタイムアウトしました");
      e.printStackTrace();
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      System.out.println("通信方式の設定に失敗しました");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("予期せぬ例外が発生しました");
      e.printStackTrace();
    }
  }

  /**
   * **************************************************************
   * LINE WORKS APIを用いて、Botへメッセージ送信を行う。
   * **************************************************************
   * 
   * @see https://developers.worksmobile.com/jp/docs/bot-user-message-send
   * @param message LINE WORKS Botへ送信するメッセージ
   */
  public void sendMessageToLineWorks(String message) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      // JSONからBot ID取得
      String botId = mapper.readTree(message).get("botId").asText();
      // JSONからLINE WORKSユーザーID取得
      String userId = mapper.readTree(message).get("userId").asText();
      // JSONからアクセストークン情報取得
      String accessToken = mapper.readTree(message).get("accessToken").asText();

      // メッセージ送信時に用いるURLのパスパラメータ置換
      String lineworksSendMessageUrl = "https://www.worksapis.com/v1.0/bots/{botId}/users/{userId}/messages";
      lineworksSendMessageUrl = lineworksSendMessageUrl.replace("{botId}", botId);
      lineworksSendMessageUrl = lineworksSendMessageUrl.replace("{userId}", userId);

      // ヘッダー作成
      headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(accessToken);

      // リクエスト作成
      RequestEntity<String> requestEntity = RequestEntity.post(new URI(lineworksSendMessageUrl)).headers(headers)
          .body(message);

      // リクエスト送信
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.exchange(requestEntity, String.class);
      return;
    } catch (HttpClientErrorException | HttpServerErrorException e) {
      System.out.println("HTTPリクエスト送信時にエラーが発生しました");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("予期せぬ例外が発生しました");
      e.printStackTrace();
    }
  }
}
