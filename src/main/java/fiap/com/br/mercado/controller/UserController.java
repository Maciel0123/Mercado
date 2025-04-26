package fiap.com.br.mercado.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fiap.com.br.mercado.model.UserModel;
import fiap.com.br.mercado.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
@Cacheable(value = "user")
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping
    @Cacheable
    public List<UserModel> index() {
        return repository.findAll();
    }

    @PostMapping
    @CacheEvict(allEntries = true)
    @Operation(responses = @ApiResponse(responseCode = "400"))
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel create(@RequestBody @Valid UserModel userModel) {
        log.info("Cadastrando usuario " + userModel.getName());
        return repository.save(userModel);
    }

    @GetMapping("/nome/{name}")
    public List<UserModel> get(@PathVariable String name) {
        log.info("Buscando usuario" + name);
        return getUserName(name);
    }

    @GetMapping("classe/{classe}")
    public List<UserModel> getClasse(@PathVariable String classe) {
        log.info("Buscando usuario" + classe);
        return getUserClasse(classe);
    }

    @PutMapping("{id}")
    @CacheEvict(allEntries = true)
    public UserModel update(@PathVariable Long id, @RequestBody UserModel user) {
        log.info("Atualizando o id de usuario: " + id + " para " + user.getName());
        UserModel existingUser = getUserId(id);
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getClasse() != null) {
            existingUser.setClasse(user.getClasse());
        }
        if (user.getNivel() != null) {
            existingUser.setNivel(user.getNivel());
        }
        if (user.getMoedas() != null) {
            existingUser.setMoedas(user.getMoedas());
        }
       
        return repository.save(existingUser);
    }

    @DeleteMapping("{id}")
    @CacheEvict(allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("Apagando usuario " + id);
        repository.delete(getUserId(id));
    }

    private List<UserModel> getUserName(String name){
        List<UserModel> users = repository.findByName(name);
        if (users.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Usuario" + name + " não encontrado");
        }
        return users;
    }

    private List<UserModel> getUserClasse(String classe){
        List<UserModel> users = repository.findByClasse(classe);
        if (users.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhuma clase: " + classe + " encotrada");
        }
        return users;
                
    }

    private UserModel getUserId(Long id) {
        return repository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Usuario " + id + "  não encontrado"));
    }

}
