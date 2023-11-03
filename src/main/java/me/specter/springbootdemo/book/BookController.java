package me.specter.springbootdemo.book;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<Book> findAllBooks(){
        return this.bookService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public Book findBookById(@PathVariable String id){
        return this.bookService.findById(id);
    }

    // Url: http://localhost:8080/api/v1/books/search?title=<#some title#>
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public List<Book> findAllBooksByTitleContaining(
        @RequestParam(
            value = "title", 
            required = true, 
            defaultValue = ""
        ) 
        String title
    ){
        return this.bookService.findAllByTitleContaining(title);
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> createBook(@RequestBody @Valid Book book){ 
        this.bookService.createBook(book);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteBook(@PathVariable String id){
        this.bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @PreAuthorize("hasRole('MANAGER')")
    public void updateBook(@RequestBody Book book) {    
        this.bookService.updateBook(book);
    }

}
