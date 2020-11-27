package com.myexample.springdataessample.repository.dynamodb;

import com.myexample.springdataessample.domain.model.Member;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface DynamoDBMemberRepository extends CrudRepository<Member, String> {
}
