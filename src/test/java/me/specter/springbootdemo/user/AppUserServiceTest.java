package me.specter.springbootdemo.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import me.specter.springbootdemo.role.AppRole;
import me.specter.springbootdemo.role.AppRole.RoleName;

public class AppUserServiceTest {
    
    @InjectMocks
    private AppUserService appUserService;

    @Mock
    private AppUserRepository appUserRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void returnAllUser(){
        // Given
        AppUser appUser = new AppUser(
            1, 
            "waiting.13@gmail.com", 
            "Wai Ting 13", 
            "", 
            false, 
            Set.of(new AppRole("R0002", RoleName.USER))
        );
        AppUserDto expected = new AppUserDto(appUser);

        // Mock the calls
        when(appUserRepository.findAll()).thenReturn(List.of(appUser));

        // When
        AppUserDto actual = appUserService.findAllUsers().get(0);

        // Then
        // Ensure this method executes only once
        verify(appUserRepository, times(1)).findAll();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDisplayName(), actual.getDisplayName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getIsEnabled(), actual.getIsEnabled());
        assertEquals(expected.getRoles(), actual.getRoles());
    }

}
