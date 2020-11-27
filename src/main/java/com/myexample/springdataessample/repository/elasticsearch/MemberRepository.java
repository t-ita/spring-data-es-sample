package com.myexample.springdataessample.repository.elasticsearch;

import com.myexample.springdataessample.domain.model.Member;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MemberRepository extends ElasticsearchRepository<Member, String> {

    List<Member> findAllBySkillsContains(String skill);

}
