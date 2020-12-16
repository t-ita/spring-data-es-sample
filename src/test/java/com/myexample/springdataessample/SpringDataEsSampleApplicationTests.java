package com.myexample.springdataessample;

import com.myexample.springdataessample.domain.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.index.AliasAction;
import org.springframework.data.elasticsearch.core.index.AliasActionParameters;
import org.springframework.data.elasticsearch.core.index.AliasActions;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SpringDataEsSampleApplicationTests {

    @Autowired
    SpringDataEsSampleApplication sut;

    @Qualifier("elasticsearchOperations")
    @Autowired
    ElasticsearchOperations operations;

    @Test
    void testSaveWithIndexName() {
        var testData = Member.builder()
                .id("2")
                .name("member-2")
                .skills(List.of("skill-2", "skill-3", "skill-4"))
                .build();
        IndexCoordinates indexCoordinates = IndexCoordinates.of("member-index");
        IndexQuery createIndexQuery = new IndexQueryBuilder()
                .withObject(testData)
                .build();
        operations.index(createIndexQuery, indexCoordinates);

    }

    @Test
    void testAddAlias() {
        IndexCoordinates indexCoordinates = IndexCoordinates.of("member-index");
        operations.indexOps(indexCoordinates)
                .alias(new AliasActions(
                        new AliasAction.Add(
                                AliasActionParameters.builder()
										.withIndices(indexCoordinates.getIndexName())
										.withAliases("test-alias")
										.build()
                        )));
    }

    @Test
	void testRemoveAlias() {
		IndexCoordinates indexCoordinates = IndexCoordinates.of("member-index");
		operations.indexOps(indexCoordinates)
				.alias(new AliasActions(
						new AliasAction.Remove(
								AliasActionParameters.builder()
										.withIndices(indexCoordinates.getIndexName())
										.withAliases("test-alias")
										.build()
						)));
	}

    @Test
    void testSave() {
        var testData = Member.builder()
                .id("1")
                .name("member-1")
                .skills(List.of("skill-1", "skill-2", "skill-3"))
                .build();
        sut.save().apply(testData);
    }

    @Test
    void testFindByID() {
        var expected = Member.builder()
                .id("1")
                .name("member-1")
                .skills(List.of("skill-1", "skill-2", "skill-3"))
                .build();
        var actual = sut.findById().apply("2");
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

}
