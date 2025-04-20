package br.com.testesunitarios.services;

import br.com.testesunitarios.domain.User;

public interface UserService {
    User findById(int id);
}
