package br.com.testesunitarios.services;

import br.com.testesunitarios.domain.User;

import java.util.List;

public interface UserService {
    User findById(Long id);
    List<User> findAll();
}
