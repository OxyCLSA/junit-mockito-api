package br.com.testesunitarios.resources;


import br.com.testesunitarios.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById(@PathVariable int id){
        return ResponseEntity.ok().body(new User(1, "name", "emai@email.com", "123"));
    }
}
