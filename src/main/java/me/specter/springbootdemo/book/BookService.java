package me.specter.springbootdemo.book;

import org.springframework.stereotype.Service;

import me.specter.springbootdemo.error.DataNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public Book findByIsbn(String isbn){

        Optional<Book> book = this.bookRepository.findByIsbn(isbn);

        if (book.isEmpty()){
            throw new DataNotFoundException("Book (isbn=%s) is not exist".formatted(isbn));
        }

        return book.get();
    }

    public List<Book> findAllByTitleContaining(String title){
        return this.bookRepository.findAllByTitleContaining(title);
    }

    public Book findById(String id){
        
        Optional<Book> book = this.bookRepository.findById(id);

        if (book.isEmpty()){
            throw new DataNotFoundException("Book %s is not exist");
        }

        return book.get();
    }

    public List<Book> findAll(){
        return this.bookRepository.findAll();
    }

    public Book createBook(Book book){
        book.setId(null);
        return this.bookRepository.save(book);
    }

    public void deleteBook(String id){
        // Doc: If the entity is not found in the persistence store it is silently ignored.
        this.bookRepository.deleteById(id);
    }

    public void updateBook(Book book){
        String id = book.getId();
        Optional<Book> bookFromDB = this.bookRepository.findById(id);
        if(bookFromDB.isPresent()){
            this.bookRepository.save(book);
        }else{
            throw new DataNotFoundException("book %s does not exist".formatted(id));
        }
    }

}
