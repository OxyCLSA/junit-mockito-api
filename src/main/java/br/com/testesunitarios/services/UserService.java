package br.com.testesunitarios.services;

import br.com.testesunitarios.domain.User;
import br.com.testesunitarios.domain.dto.UserDTO;

import java.util.List;

public interface UserService {
    User findById(Long id);
    List<User> findAll();
    User create(UserDTO obj);
    User update(UserDTO obj);
}
