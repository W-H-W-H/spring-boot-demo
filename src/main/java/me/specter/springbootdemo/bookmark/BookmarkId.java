package me.specter.springbootdemo.bookmark;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class BookmarkId implements Serializable{

    private static final long serialVersionUID = 1L;

    private String bookId;
    private Integer userId;

    public BookmarkId(String bookId, Integer userId){
        super();
        this.bookId = bookId;
        this.userId = userId;
    }

    public BookmarkId(){

    }

    public String getBookId() {
        return this.bookId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
        if(this.getClass() != obj.getClass()){
            return false;
        }
        BookmarkId other = (BookmarkId) obj;
        return Objects.equals(this.getBookId(), other.getBookId()) 
            && Objects.equals(this.getUserId(), other.getUserId());

    }
}