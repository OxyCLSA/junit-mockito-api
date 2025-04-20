package br.com.testesunitarios.services.implementation;

import br.com.testesunitarios.domain.User;
import br.com.testesunitarios.repositories.UserRepository;
import br.com.testesunitarios.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public User findById(int id) {
        Optional<User> obj = repository.findById(id);
        return obj.orElse(null);
    }
}
