package com.testtechnique.football;

import com.testtechnique.football.user.JpaUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationContextIT {

    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
        // smoke test : démarre le contexte Spring; l'absence d'exception signifie que le wiring est correct
        // vérifications supplémentaires : UserDetailsService bean présent et de type JpaUserDetailsService
        assertThat(context).isNotNull();
        // il doit y avoir un bean de type UserDetailsService
        UserDetailsService uds = context.getBean(UserDetailsService.class);
        assertThat(uds).isNotNull();
        // et il doit être une instance de JpaUserDetailsService
        assertThat(uds).isInstanceOf(JpaUserDetailsService.class);
    }
}
