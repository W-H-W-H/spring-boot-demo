package me.specter.springbootdemo.bookmark;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("bookmarks/{userId}") // Check User name
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public List<Book> findAllBookmarks(@PathVariable String userEmail){
        return this.bookmarkService.findBookmarkedBook(userEmail);
    }

    @PostMapping("/bookmarks")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #request.email() = authentication.name)")
    public ResponseEntity<Void> addBookmarks(@RequestBody BookmarkRequest request){
         this.bookmarkService.addBookmark(request.userId(), request.bookId());
         return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/bookmarks")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public void deleteBookmarks(@RequestBody BookmarkRequest request){
        this.bookmarkService.deleteBookmark(request.userId(), request.bookId());
    }
    
}