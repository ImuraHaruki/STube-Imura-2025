# STube-Imura-2025

成瀬元先生作成の沈降管用粒度分析ソフトSTubeのソースコードをもとに，最新のjavaで動くように改良しました．

## 改良点
- シリアル通信規格をRXTXからjSerialCommに変更．
- ファイル構成を変更
- 文字コードをShift-JISからUTF-8に変更．
- 秤からマイナスの値が来た時，0扱いとするように変更．

## 使用したjavaのバージョン
Java 25

## 起動方法
STube.batを実行  
初回使用時はコンパイル用バッチファイル（compile.bat）を実行する

## Windows exe版
`dist\STube\STube.exe` をダブルクリックすると起動します。
Java実行環境を同梱しているため、起動時のコンパイルやJavaの別途インストールは不要です。
配布・移動するときは、exeだけでなく `dist\STube` フォルダー全体をコピーしてください。
ソースコードを変更した場合のみ、exeを再作成します。

### exeの作成（開発者向け）
JDK 25以上がインストールされたWindowsで、次を実行します。

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\build-exe.ps1
```

既存の `dist\STube` がある場合は、別の場所へ移動してから再作成してください。
