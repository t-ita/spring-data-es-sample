package com.myexample.springdataessample;

import com.myexample.springdataessample.domain.model.Member;
import com.myexample.springdataessample.repository.elasticsearch.MemberRepository;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
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
	MemberRepository memberRepository;

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
