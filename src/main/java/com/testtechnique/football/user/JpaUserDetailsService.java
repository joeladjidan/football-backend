/**
 * Implémentation de UserDetailsService basée sur JPA pour charger les utilisateurs.
 * <p>
 * @author Joël ADJIDAN
 * @since 2025-12-11
 */

package com.testtechnique.football.user;

import com.testtechnique.football.domain.User;
import com.testtechnique.football.repository.UserAccountRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserAccountRepository repository;

    public JpaUserDetailsService(UserAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User ua = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable: " + username));
        List<GrantedAuthority> authorities = Arrays.stream(ua.getRoles().split(","))
                .map(String::trim)
                .filter(r -> !r.isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(ua.getUsername(), ua.getPassword(), authorities);
    }
}
