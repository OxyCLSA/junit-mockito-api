package br.com.testesunitarios.resources;

import br.com.testesunitarios.domain.User;
import br.com.testesunitarios.domain.dto.UserDTO;
import br.com.testesunitarios.services.UserService;
import br.com.testesunitarios.services.exceptions.DataIntegratyViolationException;
import br.com.testesunitarios.services.exceptions.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserResourceTest {

    public static final long ID = 1L;
    public static final String NAME = "Carlos";
    public static final String EMAIL = "carlos@email.com";
    public static final String PASSWORD = "123456";
    public static final String OBJETO_NAO_ENCONTRADO = "Objeto não encontrado";
    public static final String EMAIL_JA_CADASTRADO = "Email já cadastrado no sistema";
    public static final int INDEX = 0;

    private MockMvc mockMvc;

    @InjectMocks
    private UserResource resource;

    @Mock
    private UserService service;

    @Mock
    private ModelMapper mapper;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(resource).build();
        startUser();
    }

    @Test
    void whenFindByIdThenReturnSuccess() throws Exception {
        Mockito.when(service.findById(anyLong())).thenReturn(user);
        Mockito.when(mapper.map(any(), any())).thenReturn(userDTO);

        mockMvc.perform(get("/users/" + ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    void whenFindByIdThenReturnNotFound() throws Exception {
        Mockito.when(service.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO));

        mockMvc.perform(get("/users/" + ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenFindAllThenReturnAListOfUserDTO() throws Exception {
        // Arrange
        List<User> userList = List.of(user);
        UserDTO expectedDto = new UserDTO(ID, NAME, EMAIL, null); // Password deve ser null no DTO de resposta

        Mockito.when(service.findAll()).thenReturn(userList);
        Mockito.when(mapper.map(user, UserDTO.class)).thenReturn(expectedDto);

        // Act & Assert
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(NAME))
                .andExpect(jsonPath("$[0].email").value(EMAIL))
                .andExpect(jsonPath("$[0].password").doesNotExist());
    }

    @Test
    void whenCreateThenReturnCreated() throws Exception {
        Mockito.when(service.create(any())).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void whenCreateThenReturnDataIntegrityViolationException() throws Exception {
        Mockito.when(service.create(any()))
                .thenThrow(new DataIntegratyViolationException(EMAIL_JA_CADASTRADO));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void whenUpdateThenReturnSuccess() throws Exception {
        Mockito.when(service.update(any())).thenReturn(user);
        Mockito.when(mapper.map(any(), any())).thenReturn(userDTO);

        mockMvc.perform(put("/users/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    void whenUpdateThenReturnDataIntegrityViolationException() throws Exception {
        Mockito.when(service.update(any()))
                .thenThrow(new DataIntegratyViolationException(EMAIL_JA_CADASTRADO));

        mockMvc.perform(put("/users/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void whenDeleteThenReturnSuccess() throws Exception {
        Mockito.doNothing().when(service).delete(anyLong());

        mockMvc.perform(delete("/users/" + ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteThenReturnNotFound() throws Exception {
        Mockito.doThrow(new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO))
                .when(service).delete(anyLong());

        mockMvc.perform(delete("/users/" + ID))
                .andExpect(status().isNotFound());
    }

    private void startUser() {
        user = new User(ID, NAME, EMAIL, PASSWORD);
        userDTO = new UserDTO(ID, NAME, EMAIL, PASSWORD);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}