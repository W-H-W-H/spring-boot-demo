package me.specter.springbootdemo.book;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import me.specter.springbootdemo.bookmark.Bookmark;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @NotBlank
    private String title;

    @NotBlank
    private String isbn;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore // Use JsonIgnore to aviod infinite recursion
    private List<Bookmark> bookmarks;

    // @NoArgsConstructor
    public Book(){

    }

    // @AllArgsConstructor
    public Book(String id, String title, String isbn){
        this.id = id;
        this.title = title;
        this.isbn = isbn;
    }

    // @Getter
    public String getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getIsbn(){
        return this.isbn;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    // @Setter
    public void setId(String id){
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setBookmarks(List<Bookmark> bookmark) {
        this.bookmarks = bookmark;
    }

}
