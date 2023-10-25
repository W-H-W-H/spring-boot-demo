package me.specter.springbootdemo.bookmark;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.specter.springbootdemo.book.Book;

@RestController
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService){
        this.bookmarkService = bookmarkService;
    }

    @GetMapping // Check User name
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #request.userEmail() == authentication.name)")
    public List<Book> findAllBookmarks(@RequestBody BookmarkRequest request){
        return this.bookmarkService.findBookmarkedBook(request.userEmail());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #request.userEmail() == authentication.name)")
    public ResponseEntity<Void> addBookmarks(@RequestBody BookmarkRequest request){
         this.bookmarkService.addBookmark(request.userId(), request.bookId());
         return ResponseEntity.created(null).build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #request.userEmail() == authentication.name)")
    public void deleteBookmarks(@RequestBody BookmarkRequest request){
        this.bookmarkService.deleteBookmark(request.userId(), request.bookId());
    }
    
}
