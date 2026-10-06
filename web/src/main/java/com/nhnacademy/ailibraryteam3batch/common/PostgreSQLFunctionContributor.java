package com.nhnacademy.ailibraryteam3batch.common;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.StandardBasicTypes;

/**
 * PostgreSQL 전문 검색 함수를 Hibernate에 등록
 * Hibernate에게 ts_match_korean이라는 함수는 실제로 이 SQL이라고 알려줌
 */
public class PostgreSQLFunctionContributor implements FunctionContributor {
    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry()
                .registerPattern(
                        "ts_match_korean",
                        "to_tsvector('korean', ?1) @@ plainto_tsquery('korean', ?2)",
                        functionContributions.getTypeConfiguration()
                                .getBasicTypeRegistry()
                                .resolve(StandardBasicTypes.BOOLEAN)
                );
        functionContributions.getFunctionRegistry()
                .registerPattern(
                        "vector_cosine_similarity",
                        "(1.0 - (?1 <=> cast(?2 as vector)))",
                        functionContributions.getTypeConfiguration()
                                .getBasicTypeRegistry()
                                .resolve(StandardBasicTypes.DOUBLE)
                );
    }
}
