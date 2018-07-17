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

![2018-07-18 0 28 52](https://user-images.githubusercontent.com/4632508/42827546-9ac1de5c-8a21-11e8-88bf-04923e1d3061.png)
