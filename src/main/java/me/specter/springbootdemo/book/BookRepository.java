package me.specter.springbootdemo.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String>{
    Optional<Book> findByIsbn(String isbn);

    // LIKE Operation
    List<Book> findAllByTitleContaining(String title);
}

