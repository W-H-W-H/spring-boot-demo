package me.specter.springbootdemo.testing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityManager;
import me.specter.springbootdemo.book.Book;
import me.specter.springbootdemo.book.BookRepository;
import me.specter.springbootdemo.bookmark.Bookmark;
import me.specter.springbootdemo.bookmark.BookmarkRepository;
import me.specter.springbootdemo.bookmark.BookmarkService;
import me.specter.springbootdemo.role.AppRole;
import me.specter.springbootdemo.role.AppRoleRepository;
import me.specter.springbootdemo.token.Token;
import me.specter.springbootdemo.token.TokenRepository;
import me.specter.springbootdemo.user.AppUser;
import me.specter.springbootdemo.user.AppUserRepository;

@RestController
@RequestMapping("/api/v1/crud-test")
public class CrudTesting {
    private final AppRoleRepository appRoleRepository;
    private final AppUserRepository appUserRepository;
    private final TokenRepository tokenRepository;
    private final BookRepository bookRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkService bookmarkService;

    public CrudTesting(
        AppRoleRepository appRoleRepository, 
        AppUserRepository appUserRepository,
        TokenRepository tokenRepository,
        BookRepository bookRepository,
        BookmarkRepository bookmarkRepository,
        BookmarkService bookmarkService
    ){
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.tokenRepository = tokenRepository;
        this.bookRepository = bookRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.bookmarkService = bookmarkService;
    }

    @GetMapping("/roles")
    public List<AppRole> getAllRoles(){
        return this.appRoleRepository.findAllByRoleNameIn(List.of("ADMIN", "USER").stream().map(AppRole.mapper).toList());
    }

    @PostMapping("/roles")
    public void deleteOneRole(){
        List<AppUser> users = this.appUserRepository.findAll();
        AppRole targetRole = this.appRoleRepository.findById("R0002").get();

        users.forEach(
            user -> {
                Set<AppRole> roles = user.getRoles();
                if (roles.contains(targetRole)){
                    roles.remove(targetRole);
                    this.appUserRepository.save(user);
                }
            }
        );


        this.appRoleRepository.deleteById("R0002");
    }

    @PostMapping("/tokens/{token}")
    public void deleteToken(@PathVariable String token){
        Token tokenInDb = this.tokenRepository.findByToken(token).get();
        this.tokenRepository.delete(tokenInDb);
        
    }

    @GetMapping("bookmarks")
    public List<Book> findAllBookmarks(){
        return bookmarkService.findBookmarkedBook("specterfbells@gmail.com");
    }

    @PostMapping("/bookmarks")
    public void addBookmarks(){
         bookmarkService.addBookmark(2, "B00001");
         bookmarkService.addBookmark(2, "B00002");
    }

    @DeleteMapping("/bookmarks")
    public void deleteBookmarks(){
        this.bookmarkService.deleteBookmark(1, "B00001");
    }

}
