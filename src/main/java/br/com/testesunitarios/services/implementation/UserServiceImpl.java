package br.com.testesunitarios.services.implementation;

import br.com.testesunitarios.domain.User;
import br.com.testesunitarios.domain.dto.UserDTO;
import br.com.testesunitarios.repositories.UserRepository;
import br.com.testesunitarios.services.UserService;
import br.com.testesunitarios.services.exceptions.DataIntegratyViolationException;
import br.com.testesunitarios.services.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public User findById(Long id) {
        Optional<User> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User create(UserDTO obj) {
        findByEmail(obj);
        return repository.save(mapper.map(obj, User.class));
    }

    @Override
    public User update(UserDTO obj) {
        findByEmail(obj);
        return repository.save(mapper.map(obj, User.class));
    }

    @Override
    public void delete(Long id) {
        User byId = findById(id);
        repository.deleteById(id);
    }

    private void findByEmail(UserDTO obj) {
      Optional<User> user = repository.findByEmail(obj.getEmail());
      if(user.isPresent() && !user.get().getId().equals(obj.getId())) {
          throw  new DataIntegratyViolationException("Email já cadastrado no sistema");
      }
    }
}
