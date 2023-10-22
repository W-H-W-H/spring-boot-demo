package me.specter.springbootdemo.book;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @NotBlank
    private String title;

    @NotBlank
    private String isbn;

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

}
