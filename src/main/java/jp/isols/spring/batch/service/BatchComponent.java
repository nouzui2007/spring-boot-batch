package jp.isols.spring.batch.service;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Column;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BatchComponent {

    @PostConstruct
    private void start() {
        log.info("Hello world");

        // Memberオブジェクトを作ってCSVにするサンプル
        try {
            final String csv = getCsvText();
            log.info(csv);
        } catch (final JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // JSONからCSVにするサンプル
        // try {
        //     final String csv = getCsvTextFromJson();
        //     log.info(csv);
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

        // JSONからOrderのリストを経てCSVにするサンプル
        try {
            final String csv = getCsvTextFromObjectList();
            log.info(csv);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        log.info("Success");
    }

    private String getCsvText() throws JsonProcessingException {
        final CsvMapper mapper = new CsvMapper();
        // 文字列にダブルクオートをつける
        mapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true);
        // ヘッダをつける
        final CsvSchema schema = mapper.schemaFor(Member.class).withHeader();
        // メンバーデータをダウンロードするイメージ。本来はDBからデータを取得する。
        final List<Member> members = new ArrayList<Member>();
        members.add(new Member(1L, "user01", "プロフィール１", new Date()));
        members.add(new Member(2L, "user02", "プロフィール２", new Date()));
        members.add(new Member(3L, "user03", "プロフィール３", new Date()));
        return mapper.writer(schema).writeValueAsString(members);
    }

    private String getCsvTextFromJson() throws JsonProcessingException, IOException {
        // Step1
        JsonNode jsonTree = new ObjectMapper().readTree(new File("src/main/resources/orders.json"));
        
        // Step2 
        Builder csvSchemaBuilder = CsvSchema.builder();

        // JsonNode firstObject = jsonTree.elements().next();
        // firstObject.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );

        // CSVカラムの名称（JSONと一致していないとNG）と順序を指定
        csvSchemaBuilder.addColumn("quantity")
                        .addColumn("item")
                        .addColumn("unitPrice");

        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

        // Step3
        CsvMapper csvMapper = new CsvMapper();

        // ファイルを出力する形式
        // csvMapper.writerFor(JsonNode.class)
        //   .with(csvSchema)
        //   .writeValue(new File("src/main/resources/orders.csv"), jsonTree);

        // 文字列を返す        
        return csvMapper.writerFor(JsonNode.class)
                        .with(csvSchema)
                        .writeValueAsString(jsonTree);
    }

    private String getCsvTextFromObjectList() throws JsonProcessingException, IOException {
        final CsvMapper mapper = new CsvMapper();
        // 文字列にダブルクオートをつける
        mapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true);
        // ヘッダをつける
        final CsvSchema schema = mapper.schemaFor(Order.class).withHeader();

        // JSONから取得する
        String json = Files.readString(Paths.get("src/main/resources/orders2.json"));
        final ObjectMapper jsonMapper = new ObjectMapper();
        final List<Order> orders = Arrays.asList(jsonMapper.readValue(json, Order[].class));
        return mapper.writer(schema).writeValueAsString(orders);
    }

}