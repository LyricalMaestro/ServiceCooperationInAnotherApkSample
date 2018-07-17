# ServiceCooperationInAnotherApkSample
他のアプリに「バックグラウンドで動く処理をやるService」を提供する方法のサンプル


# 本プロジェクトを構成するモジュール

ともに実行可能なモジュール。

- samplehostapp
  - Serviceを以ってバックグランド処理を提供するためのアプリ。使用するクライアント側に「HELLO WORLD count = %d」という文字を定期的に送り続ける
- sampleclientapp
  - samlehostappのServiceを使用するアプリ。Serviceから受けた文字を画面に表示する。

# サンプルアプリの実行方法

1. このプロジェクトをcloneする。
2. samplehostappモジュールを実行し、端末にホストアプリをインストール。
3. sampleclientappモジュールを実行し、端末にクライアントアプリモジュールをインストール。


