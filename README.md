# LINE WORKSのBotにMQを実装 サンプルコード<!-- omit in toc -->

## 目次 <!-- omit in toc -->

- [概要](#概要)
- [使用方法](#使用方法)
  - [前提条件](#前提条件)
  - [事前準備](#事前準備)
  - [サンプルリポジトリのクローン](#サンプルリポジトリのクローン)
  - [アプリケーション起動](#アプリケーション起動)
    - [パブリッシャー](#パブリッシャー)
      - [パブリッシャーの起動](#パブリッシャーの起動)
      - [パブリッシャーの起動後設定](#パブリッシャーの起動後設定)
        - [ポート転送設定](#ポート転送設定)
        - [Callback URL設定](#callback-url設定)
      - [パブリッシャーの動作確認方法](#パブリッシャーの動作確認方法)
    - [コンシューマー](#コンシューマー)
      - [コンシューマーの起動](#コンシューマーの起動)
      - [コンシューマーの動作確認方法](#コンシューマーの動作確認方法)
- [問合せ先](#問合せ先)

## 概要

コース「[LINE WORKS API応用](https://github.com/solxyz-jsn/lineworks-sample-advanced)」のサンプルコードに、メッセージキュー（MQ）を導入したサンプルコードです。LINE WORKSから受信してMQへ送信するプロジェクトと、MQから受信してLINE WORKSへ送信するプロジェクトの二つで構成しています。

- LINE WORKSからのメッセージ受取部（パブリッシャー）：メッセージ送信先をLINE WORKSからAmazon MQに変更する
- Amazon MQからのメッセージ受取部（コンシューマー）：Amazon MQからメッセージを受け取り、そのメッセージをLINE WORKS APIを用いてBotへ送信する

Botへ送信されるメッセージは「LINE WORKS API応用」のサンプルコードと同一です。

## 使用方法

### 前提条件

- [Visual Studio Code](https://code.visualstudio.com/download)（バージョン1.82以上）がインストール済み
- [Gradle](https://gradle.org/releases/)がインストール済み
- [Java SE 17（LTS）](https://www.oracle.com/jp/java/technologies/downloads/)がインストール済み
- [GitHub](https://github.co.jp/)のアカウントを作成済み
- [Git](https://git-scm.com/downloads)がインストール済み
- [Amazon Web Services](https://aws.amazon.com/jp/register-flow/)のアカウントを作成済み

各種のインストール方法およびアカウント作成方法はリンク先を参照してください。

### 事前準備

- [Amazon MQ](https://ap-northeast-1.console.aws.amazon.com/amazon-mq/home?region=ap-northeast-1#/)からブローカーを新規作成

- [LINE WORKSのアカウントを新規開設](https://join.worksmobile.com/jp/joinup/step1)

- [Developer Console](https://dev.worksmobile.com/jp/console/openapi/v2/app/list/view)から`Bot`と`アプリ`を新規作成

- 本プロジェクト内の`application.properties`で設定している環境変数の値を更新（値はすべてDeveloper Consoleから取得できます）

  ※プロジェクト内の環境変数は、フォルダ`consumer`と`publisher`のそれぞれに存在する`src/main/resources/application.properties`で設定されています。**両方のパスにある`application.properties`の値を更新**してください。

  - [application.properties（publisher）](./publisher/src/main/resources/application.properties)
  - [application.properties（consumer）](./consumer/src/main/resources/application.properties)

### サンプルリポジトリのクローン

コマンドライン実行画面から、次のコマンドを実行してください。

```shell
git clone https://github.com/solxyz-jsn/lineworks-sample-mq
```

### アプリケーション起動

本サンプルリポジトリは、パブリッシャー（`publisher`）とコンシューマー（`consumer`）の2プロジェクトで構成されています。

パブリッシャーとコンシューマーの両方を起動させることで、LINE WORKS APIを利用したメッセージ送信が行えるようになります。

#### パブリッシャー

##### パブリッシャーの起動

Visual Studio Codeでフォルダ`publisher`を開いてください。

左メニューの`JAVA PROJECT`から、プロジェクト`punlisher`を右クリックして`Run`を選択してください。

<img alt="プロジェクト起動" src="image\sample_bot_setting01.png" width="60%">

（画像のVisual Studio Codeはバージョン1.83です）

##### パブリッシャーの起動後設定

起動後に行う設定の詳細は、[SOLXYZ Academy](https://academy.solxyz.co.jp)内コース「LINE WORKSのBotにMQを実装」でも説明を記載しています。併せて参考にしてください。

###### ポート転送設定

LINE WORKSからのHTTPS POSTリクエストを受信するために、ポート転送の設定を行う必要があります。この手順には、**GitHubのアカウントが必須**となります。

Visual Studio Code上でターミナルを開いてください。

タブ`ポート`を押下して、`ポートの転送`を押下してください。ポート入力欄には、[application.properties](./publisher/src/main/resources/application.properties)の`server.port`で設定しているポート番号（デフォルトでは`8080`）を入力してください。

<img alt="ポート番号" src="image\sample_bot_setting02.png" width="80%">

初めてポート転送設定を行う場合は、GitHubアカウントによるサインインを確認するポップアップが表示されるので、`許可`を押下してGitHubアカウントのサインインを行ってください。

<img alt="許可" src="image\sample_bot_setting03.png" width="60%">

しばらく待機すると、URLが発行されます。

次に、LINE WORKSからの通信を許可するために、表示範囲を変更します。右クリックから`ポートの表示範囲`を`公開`に設定してください。

<img alt="公開" src="image\sample_bot_setting04.png" width="80%">

###### Callback URL設定

LINE WORKSがCallbackをアプリケーションへ送信できるように、[Developer ConsoleのBot画面](https://dev.worksmobile.com/jp/console/bot/view)からCallbackの送信先URL（以降、Callback URL）を設定します。

Callback URL入力欄には、次の値を入力してください。

```txt
{Visual Studio Codeで発行したアドレス} + callback
```

入力例は次のようになります。

<img alt="Callback URL" src="image\sample_bot_setting05.png" width="60%">

##### パブリッシャーの動作確認方法

パブリッシャーのメッセージがAmazon MQへ送信できているかを確認します。

Amazon MQのWebコンソール画面を開きます。

Webコンソール画面のURLは、ブローカーの詳細画面から確認できます。

<img alt="MQコンソール" src="image\sample_bot_setting06.png" width="60%">

この状態で、Botとのトークルームからメッセージを送信してください。使用するLINE WORKSは、Webブラウザ、Webアプリ、モバイルアプリいずれでも問題ありません。

<img alt="LW" src="image\sample_bot_setting07.png" width="60%">

Webコンソール画面を更新すると、グラフからキューにメッセージが貯まっている様子が確認できます。

<img alt="MQコンソール" src="image\sample_bot_setting08.png" width="70%">

#### コンシューマー

##### コンシューマーの起動

Visual Studio Codeでフォルダ`consumer`を開いてください。

左メニューの`JAVA PROJECT`から、プロジェクト`consumer`を右クリックして`Run`を選択してください。

<img alt="起動" src="image\sample_bot_setting09.png" width="70%">

補足として、Visual Studio Code上でターミナルを開いていれば、ターミナルから次のコマンドを実行しても起動できます（`build.gradle`と同一のディレクトリから実行すること）。

パブリッシャーとコンシューマーのいずれも実行可能です。

```shell
./gradlew bootRun
```

##### コンシューマーの動作確認方法

コンシューマーが起動すると、Amazon MQにキューイングされていたメッセージはコンシューマーに渡され、Botとのトークルーム上にメッセージが表示されます。

<img alt="LW" src="image\sample_bot_setting10.png" width="70%">

Amazon MQのWebコンソール画面を確認すると、キュー内のメッセージ数が減っていることが確認できます。

<img alt="MQ" src="image\sample_bot_setting11.png" width="70%">

## 問合せ先

`jsn-support@solxyz.co.jp`までお問合せください。
