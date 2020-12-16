package com.myexample.springdataessample.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "test-alias", createIndex = false)
public class Member {
    @Id
    String id;
    String name;
    List<String> skills;
}
