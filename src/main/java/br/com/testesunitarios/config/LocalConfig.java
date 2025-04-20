package br.com.testesunitarios.config;

import br.com.testesunitarios.domain.User;
import br.com.testesunitarios.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("local")
public class LocalConfig {

    @Autowired
    private UserRepository repository;

    @Bean
    CommandLineRunner startDB() {
        return args -> {
            User u1 = new User(null, "Maria", "maria@email.com", "Maria123");
            User u2 = new User(null, "Joao", "joao@email.com", "Joao123");

            repository.saveAll(List.of(u1, u2));
        };
    }
}
