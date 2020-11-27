# Spring Data Elasticsearch + Spring Cloud Function サンプル

## テスト用 Elasticsearch 起動

### Elasticsearch 起動
設定は `docker-compose.yml` を参照

```
cd docker
docker-compose up
```

#### 起動確認
以下にアクセス

elasticsearch: http://localhost:9200/

kibana: http://localhost:5601/

## プロジェクト作成

### InteilliJ の SpringIntializr でプロジェクト作成
* Gradle プロジェクトとして作成
* Spring 2.4.0 を利用
* 以下を依存性に追加
    * SpringBoot DevTools
    * Lombok
    * Spring Data Elasticsearch
    * Spring Cloud Function
* 上記に加えて、以下を build.gradle に追加
    * `implementation 'org.springframework.cloud:spring-cloud-starter-function-web'`

## コード作成
### Elasticsearch に登録するデータ型を作成

domain/model/Member.java

```Java
package com.myexample.springdataessample.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Data // Spring Cloud Function / Spring Data ES に必要
@Builder
@AllArgsConstructor // Spring Data ES に必要
@NoArgsConstructor // Spring Data ES に必要
@Document(indexName = "member-index") // ElasticSearch の Index を指定
public class Member {
    @Id // ElasticSearch データ型の ID であることを指定
    String id;
    String name;
    List<String> skills;
}
```

### Elasticsearch の操作を行うクラスを作成

repository/MemberRepository.java

```Java
package com.myexample.springdataessample.repository;

import com.myexample.springdataessample.domain.model.Member;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

// ElasticsearchRepository を継承することで、save / delete / findById メソッドを持つ
// 操作クラス（の受け口となるインターフェース）が作られる
public interface MemberRepository extends ElasticsearchRepository<Member, String> {

    // 基本メソッド以外に追加することもできる
    // 入力時にメソッド名はレコメンドされる（！）
    List<Member> findAllBySkillsContains(String skill);

}
```

### Spring Cloud Function で、Elasticsearch Repository を呼び出す Web API を作成する
メインクラス

SpringDataEsSampleApplication.java

```Java
package com.myexample.springdataessample;

import com.myexample.springdataessample.domain.model.Member;
import com.myexample.springdataessample.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class SpringDataEsSampleApplication {

	@Autowired
	MemberRepository memberRepository; // Repository を Autowired することで、必要なインスタンスが自動的に作られる

	public static void main(String[] args) {
		SpringApplication.run(SpringDataEsSampleApplication.class, args);
	}

	@Bean
	public Function<Member, Member> save() {
		return member -> memberRepository.save(member);
	}

	@Bean
	public Function<String, Optional<Member>> findById() {
		return id -> memberRepository.findById(id);
	}

	@Bean
	public Consumer<Member> delete() {
		return value -> memberRepository.delete(value);
	}

	@Bean
	public Function<String, List<Member>> findBySkillsContains() {
		return skill -> memberRepository.findAllBySkillsContains(skill);
	}

}
```

## テスト
SAVE

```
curl -H "Content-Type: application/json" localhost:8080/save -d '{"id":"1","name":"Itagaki", "skills": ["java", "python", "delphi"]}'
```

FIND BY ID

```
curl -H "Content-Type: application/json" localhost:8080/findById -d 1
```

FIND BY SKILLS CONTAINS

```
curl -H "Content-Type: application/json" localhost:8080/findBySkillsContains -d delphi
```
