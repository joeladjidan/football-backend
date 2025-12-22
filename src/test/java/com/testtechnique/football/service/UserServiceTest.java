package com.testtechnique.football.service;

import com.testtechnique.football.domain.User;
import com.testtechnique.football.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUserRepository repo;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService svc;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    void setUp() {
    }

    @Test
    void create_encodes_password_and_saves() {
        User in = new User();
        in.setUsername("bob");
        in.setPassword("secret");
        in.setRoles("ROLE_USER");

        when(encoder.encode("secret")).thenReturn("encoded");
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        User created = svc.create(in);

        verify(repo).save(userCaptor.capture());
        User saved = userCaptor.getValue();
        assertThat(saved.getPassword()).isEqualTo("encoded");
        assertThat(created.getUsername()).isEqualTo("bob");
    }

    @Test
    void findAll_returns_list() {
        User a = new User(); a.setUsername("u1");
        User b = new User(); b.setUsername("u2");
        when(repo.findAll()).thenReturn(Arrays.asList(a,b));
        List<User> res = svc.findAll();
        assertThat(res).hasSize(2);
    }

    @Test
    void delete_calls_repository() {
        svc.delete(42L);
        verify(repo).deleteById(42L);
    }

    @Test
    void update_changes_fields() {
        User existing = new User(); existing.setId(1L); existing.setUsername("old"); existing.setPassword("p"); existing.setRoles("ROLE_USER");
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(encoder.encode(any())).thenReturn("enc");

        User payload = new User(); payload.setUsername("new"); payload.setPassword("np"); payload.setRoles("ROLE_ADMIN");
        User updated = svc.update(1L, payload);
        assertThat(updated.getUsername()).isEqualTo("new");
        assertThat(updated.getRoles()).isEqualTo("ROLE_ADMIN");
        verify(repo).save(any());
    }
}

