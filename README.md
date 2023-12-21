# LINE WORKSのBotにMQを実装 サンプルコード<!-- omit in toc -->

## 目次 <!-- omit in toc -->

- [概要](#概要)
- [使用方法](#使用方法)
  - [前提条件](#前提条件)
  - [事前準備](#事前準備)
  - [サンプルリポジトリのクローン](#サンプルリポジトリのクローン)
  - [アプリケーション起動](#アプリケーション起動)
    - [パブリッシャー](#パブリッシャー)
      - [パブリッシャー起動](#パブリッシャー起動)
      - [パブリッシャー起動後設定](#パブリッシャー起動後設定)
        - [ポート転送設定](#ポート転送設定)
        - [Callback URL設定](#callback-url設定)
      - [パブリッシャー動作確認方法](#パブリッシャー動作確認方法)
    - [コンシューマー](#コンシューマー)
      - [コンシューマー起動](#コンシューマー起動)
      - [コンシューマー動作確認方法](#コンシューマー動作確認方法)
- [問合せ先](#問合せ先)

## 概要

コース「LINE WORKSのBotにMQを実装」のサンプルコードを、2つのサンプルプロジェクトに分割してメッセージキュー（MQ）を導入するようにしたサンプルコードです。

- LINE WORKSからのメッセージ受取部（パブリッシャー）：メッセージ送信先をLINE WORKSからAmazon MQに変更する
- Amazon MQからのメッセージ受取部（コンシューマー）：Amazon MQからメッセージを受け取り、そのメッセージをLINE WORKS APIを用いてBotへ送信する

LINE WORKS Botへ送信されるメッセージは「LINE WORKSのBotにMQを実装」のサンプルコードと同一です。

## 使用方法

### 前提条件

- Visual Studio Code（バージョン1.82以上）がインストール済み
- Gradleがインストール済み
- Java SE 17（LTS）がインストール済み
- GitHubのアカウントを作成済み
- Gitがインストール済み

各種のインストール方法は省略します。

### 事前準備

- AWSからAmazon MQを新規作成

Amazon MQホーム画面：<https://ap-northeast-1.console.aws.amazon.com/amazon-mq/home?region=ap-northeast-1#/>

- LINE WORKSのアカウントを新規開設

アカウント新規作成：<https://join.worksmobile.com/jp/joinup/step1>

- Developer Consoleから`Bot`と`アプリ`を新規作成

Developer Console：<https://dev.worksmobile.com/jp/console/openapi/v2/app/list/view>

- 本プロジェクト内の`application.properties`で設定している環境変数の値を更新（値はすべてDeveloper Consoleから取得できます）

  注意として、プロジェクト内の環境変数は、フォルダ`consumer`と`publisher`のそれぞれに存在する`src/main/resources/application.properties`で設定されています。**両方のパスにある`application.properties`の値を更新**してください。

### サンプルリポジトリのクローン

コマンドライン実行画面から、次のコマンドを実行してください。

```shell
git clone https://github.com/solxyz-jsn/lineworks-sample-mq
```

### アプリケーション起動

本サンプルリポジトリは、パブリッシャー（`publisher`）とコンシューマー（`consumer`）の2プロジェクトで構成されています。

パブリッシャーとコンシューマーの両方を起動させることで、LINE WORKS APIを利用したメッセージ送信が行えるようになります。

#### パブリッシャー

##### パブリッシャー起動

Visual Studio Codeでフォルダ`publisher`を開いてください。

左メニューの`JAVA PROJECT`から、プロジェクト`punlisher`を右クリックして`Run`を選択してください。

<画像>

（画像のVisual Studio Codeはバージョン1.83です）

##### パブリッシャー起動後設定

起動後に行う設定の詳細は、コース「LINE WORKSのBotにMQを実装」でも説明を記載しています。詳細はそちらを参照してください。

###### ポート転送設定

LINE WORKSからのHTTPS POSTリクエストを受信するために、ポート転送の設定を行う必要があります。この手順には、**GitHubのアカウントが必須**となります。

Visual Studio Code上でターミナルを開いてください。

タブ`ポート`を押下して、`ポートの転送`を押下してください。ポート入力欄には、[application.properties](./src/main/resources/application.properties)の`server.port`で設定しているポート番号（デフォルトでは`8080`）を入力してください。

<画像>

初めてポート転送設定を行う場合は、GitHubアカウントによるサインインを確認するポップアップが表示されるので、`許可`を押下してGitHubアカウントのサインインを行ってください。

<画像>

しばらく待機すると、URLが発行されます。

次に、LINE WORKSからの通信を許可するために、表示範囲を変更します。右クリックから`ポートの表示範囲`を`公開`に設定してください。

<画像>

###### Callback URL設定

LINE WORKSがCallbackをアプリケーションへ送信できるように、Developer ConsoleのBot画面からCallbackの送信先URL（以降、Callback URL）を設定します。

Developer Console Bot画面：<https://dev.worksmobile.com/jp/console/bot/view>

Callback URL入力欄には、次の値を入力してください。

```txt
{Visual Studio Codeで発行したアドレス} + callback
```

入力例は次のようになります。

<画像>

##### パブリッシャー動作確認方法

Amazon MQのWebコンソール画面を開きます。

<画像>

この状態で、LINE WORKSからメッセージを送信してください。

<画像>

キューにメッセージが貯まっていきます。

<画像>

#### コンシューマー

##### コンシューマー起動

Visual Studio Codeでフォルダ`consumer`を開いてください。

左メニューの`JAVA PROJECT`から、プロジェクト`consumer`を右クリックして`Run`を選択してください。

<画像>

補足として、Visual Studio Code上でターミナルを開いていれば、ターミナルから次のコマンドを実行しても起動できます。

パブリッシャーとコンシューマーのいずれも可能です。

```shell
./gradlew bootRun
```

##### コンシューマー動作確認方法

コンシューマーが起動すると、Amazon MQのメッセージ数が減ることが確認できます。

<画像>

Amazon MQにキューイングされていたメッセージはコンシューマーに渡され、LINE WORKS  APIによってBotとのトークルーム上にメッセージが表示されます。

<画像>

## 問合せ先

`jsn-support@solxyz.co.jp`までお問合せください。
