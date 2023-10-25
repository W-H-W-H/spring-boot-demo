package me.specter.springbootdemo.bookmark;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import me.specter.springbootdemo.book.Book;
import me.specter.springbootdemo.book.BookRepository;
import me.specter.springbootdemo.error.DataNotFoundException;
import me.specter.springbootdemo.error.UserNotFoundException;
import me.specter.springbootdemo.user.AppUser;
import me.specter.springbootdemo.user.AppUserRepository;

@Service
public class BookmarkService {
    
    private final AppUserRepository appUserRepository;
    private final BookRepository bookRepository;
    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(
        AppUserRepository appUserRepository,
        BookmarkRepository bookmarkRepository,
        BookRepository bookRepository
    
    ){
        this.appUserRepository = appUserRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.bookRepository = bookRepository;
    }

    public List<Book> findBookmarkedBook(String userEmail){
        List<Book> returnedObject = this.appUserRepository
            .findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException(
                "User with email(%s) is not found".formatted(userEmail)
            ))
            .getBookmarks()
            .stream()
            .map( bm -> {
                return bm.getBook();
            } )
            .toList();
        
        return returnedObject;
        
    }

    public Book addBookmark(Integer userId, String bookId){
        Book book = this.bookRepository
            .findById(bookId)
            .orElseThrow(
                () -> new DataNotFoundException("Book with bookId (%s)".formatted(bookId))
            );
        AppUser user = this.appUserRepository
            .findById(userId)
            .orElseThrow( 
                () -> new UserNotFoundException("User with userId=(%d) is not found".formatted(userId)) 
            );

        Bookmark bookmark = new Bookmark(book, user, LocalDateTime.now());

        this.bookmarkRepository.save(bookmark);

        return book;

    }

    public void deleteBookmark(Integer userId, String bookId){

        Bookmark bookmark = this.bookmarkRepository
            .findById(new BookmarkId(bookId, userId))
            .orElseThrow(() -> 
                new DataNotFoundException("bookmark is not found"
            ));

        this.bookmarkRepository.delete(bookmark);

    }

}
