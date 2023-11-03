package me.specter.springbootdemo.bookmark;

import java.time.LocalDateTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
/*
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
*/
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import me.specter.springbootdemo.book.Book;
import me.specter.springbootdemo.user.AppUser;

@Entity
public class Bookmark {

    // Must init (For unique relationship)
    @EmbeddedId
    private BookmarkId id = new BookmarkId();

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private Integer id;

    @ManyToOne
    // @JoinColumn(name = "book_id")
    @MapsId("bookId")
    private Book book;
    
    @ManyToOne
    // @JoinColumn(name = "user_id")
    @MapsId("userId")
    private AppUser user;

    private LocalDateTime lastUpdated;

    public Bookmark(Book book, AppUser user, LocalDateTime lastUpdated){
        this.book = book;
        this.user = user;
        this.lastUpdated = lastUpdated;
    }
    
    public Bookmark(){

    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    
    public void setUser(AppUser user) {
        this.user = user;
    }

    public void setId(BookmarkId id) {
        this.id = id;
    }

    public BookmarkId getId() {
        return this.id;
    }

    public LocalDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public Book getBook() {
        return this.book;
    }

    public AppUser getUser() {
        return this.user;
    }

    
}
