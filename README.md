# Spring Boot でバッチ（Spring Batch）

## プロジェクト作成

`CTRL + Shift + P`でパレットを開き、`Spring Initializr: Generate a Gradle Project`を選択する。
プロジェクト名などを入力し、保存先を選択する。

- Springは最新を選択
- コンポーネント（？）は
    - Spring Batch
    - lombok・・・アノテーションでコードを省略してくれる

## とりあえず動かしてみる
`gradlew bootRun --stacktrace`
DataSourceのurlがみつからないというような内容のエラーが出るが、この時点では正常。
```
***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class
```

## 実行準備

DemoApplicationクラスにデータソース自動設定のアノテーションを追加する。
これはデータベースを利用しないときに設定して、DataSourceのurlが見つからないエラーを抑制する。

`@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })`

ここで実行すると、正常終了する。

## バッチ本体

Spring Bootでは`@Component`が呼び出されるので、そのためのクラスを作成する。
`service/BatchComponent.java`

このクラスではコンストラクトしたら実行するように設定する。
`@PostConstruct`

## CSVを作成する

### オブジェクトからCSVにする


### JSONからCSVにする

まず、`ObjectMapper`で`sample.json`のJSONを読み込み、`JsonNode`オブジェクトにする。

```
JsonNode jsonTree = new ObjectMapper().readTree(new File("src/main/resources/orderLines.json"));
```

次に、CSVファイルのヘッダのタイトルや型、順序を定義する`CsvSchema`を作る。
`CsvSchema`にはビルダーがあるので、ビルダーにJSONのフィールド名を設定する。

```
Builder csvSchemaBuilder = CsvSchema.builder();
JsonNode firstObject = jsonTree.elements().next();
firstObject.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );
CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
```

それから、`CsvMapper`を生成する。
`CsvMapper`には、先に作成した`CsvSchema`と、JSONから作成したJsonNodeを設定する。
`CsvMapper`に出力ファイルを設定し、ファイルを出力する。

```
CsvMapper csvMapper = new CsvMapper();
csvMapper.writerFor(JsonNode.class)
  .with(csvSchema)
  .writeValue(new File("src/main/resources/orderLines.csv"), jsonTree);
```
