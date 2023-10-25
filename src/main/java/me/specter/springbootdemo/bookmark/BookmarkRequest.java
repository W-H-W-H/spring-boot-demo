package me.specter.springbootdemo.bookmark;

public record BookmarkRequest(Integer userId, String userEmail, String bookId) {
    
}
