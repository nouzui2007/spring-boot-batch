package jp.isols.spring.batch.service;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonPropertyOrder({"ID", "名前", "プロフィール", "更新日時"})
@Data
public class Member {
    @JsonProperty("ID")
    private Long id;
    @JsonProperty("名前")
    private String name;
    @JsonProperty("プロフィール")
    private String desc;
    @JsonProperty("更新日時")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date modified;

    public Member() {}

    public Member(Long id, String name, String desc, Date modified) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.modified = modified;
    }
}