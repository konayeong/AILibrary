package com.nhnacademy.ailibraryteam3batch.repository.cache;

import com.nhnacademy.ailibraryteam3batch.domain.cache.BookSearchCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface BookSearchCacheRepository extends JpaRepository<BookSearchCache, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = """
            delete from book_search_cache
            where last_accessed_at + (ttl * interval '1 second') < current_timestamp
            """, nativeQuery = true)
    int deleteExpiredCaches();

    @Query(value = """
            select *
            from book_search_cache
            where last_accessed_at + (ttl * interval '1 second') >= current_timestamp
              and (1 - (query_embedding <=> cast(:queryEmbedding as vector))) >= :threshold
            order by query_embedding <=> cast(:queryEmbedding as vector)
            limit 1
            """, nativeQuery = true)
    Optional<BookSearchCache> findMostSimilarCache(
            String queryEmbedding,
            double threshold
    );
}
