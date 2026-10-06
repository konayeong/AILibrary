package com.nhnacademy.ailibraryteam3batch.repository.book;

import com.nhnacademy.ailibraryteam3batch.domain.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Long>, CustomizedBookRepository {

    @Modifying(clearAutomatically = true)
    @Query("""
                    update Book b
                    set b.embedding = :embedding
                    where b.id = :id
            """)
    int updateEmbeddingById(@Param("id") Long id, @Param("embedding") float[] embedding);
}
