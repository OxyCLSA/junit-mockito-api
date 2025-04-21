package br.com.testesunitarios.services.implementation;

import br.com.testesunitarios.domain.User;
import br.com.testesunitarios.domain.dto.UserDTO;
import br.com.testesunitarios.repositories.UserRepository;
import br.com.testesunitarios.services.exceptions.DataIntegratyViolationException;
import br.com.testesunitarios.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    public static final long ID = 3;
    public static final String name = "Carlos";
    public static final String email = "carlos@email.com";
    public static final String password = "Carlos123";
    public static final String OBJETO_NAO_ENCONTRADO = "Objeto não encontrado";

    @InjectMocks
    private UserServiceImpl service;
    @Mock
    private UserRepository repository;
    @Mock
    private ModelMapper mapper;

    private User user;

    private UserDTO userDTO;

    private Optional<User> optionalUser;


    @BeforeEach
    void setUp() {
        startUser();
    }

    @Test
    void whenFindByIdThenReturnAnUserInstance() {
        when(repository.findById(anyLong())).thenReturn(optionalUser);

        User response = service.findById(ID);

        //Verificações
        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(name, response.getName());
        assertEquals(email, response.getEmail());
    }

    @Test
    void whenFindByIdThenReturnAnObjectNotFoundException() {
        when(repository.findById(anyLong())).thenThrow(new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO));

        try{
            service.findById(ID);
        } catch (Exception ex) {
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(OBJETO_NAO_ENCONTRADO, ex.getMessage());
        }
    }

    @Test
    void whenFindAllThenReturnAnListOfUsers() {
        when(repository.findAll()).thenReturn(List.of(user));

        List<User> response = service.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(User.class, response.get(0).getClass());
        assertEquals(name, response.get(0).getName());
        assertEquals(email, response.get(0).getEmail());


    }

    @Test
    void whenCreateThenReturnSuccess() {
        when(repository.save(Mockito.any())).thenReturn(user);

        User response = service.create(userDTO);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(name, response.getName());
        assertEquals(email, response.getEmail());
        assertEquals(password, response.getPassword());
    }

    @Test
    void whenCreateThenReturnDataIntegrityViolationException() {
        when(repository.findByEmail(Mockito.anyString())).thenReturn(optionalUser);

        try {
            service.create(userDTO);
        } catch (Exception ex) {
            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals("Email já cadastrado no sistema", ex.getMessage());
        }
    }

    @Test
    void whenUpdateThenReturnSuccess() {
        when(repository.save(Mockito.any())).thenReturn(user);

        User response = service.update(userDTO);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(name, response.getName());
        assertEquals(email, response.getEmail());
        assertEquals(password, response.getPassword());
    }
    @Test
    void whenUpdateThenReturnDataIntegrityViolationException() {
        when(repository.findByEmail(Mockito.anyString())).thenReturn(optionalUser);

        try {
            UserDTO differentIdDTO = new UserDTO(ID + 1, name, email, password);
            service.update(differentIdDTO);
        } catch (Exception ex) {
            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals("Email já cadastrado no sistema", ex.getMessage());
        }
    }

    @Test
    void deleteWithSuccess() {
        when(repository.findById(anyLong())).thenReturn(optionalUser);

        Mockito.doNothing().when(repository).deleteById(anyLong());

        service.delete(ID);

        Mockito.verify(repository, Mockito.times(1)).deleteById(anyLong());
    }

    @Test
    void deleteWithObjectNotFoundException() {
        when(repository.findById(anyLong())).thenThrow(new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO));

        try {
            service.delete(ID);
        } catch (Exception ex) {
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(OBJETO_NAO_ENCONTRADO, ex.getMessage());
        }
    }

    private void startUser() {
        user = new User(ID, name, email, password);
        userDTO = new UserDTO(ID, name, email, password);
        optionalUser = Optional.of(new User(ID, name, email, password));
    }
}