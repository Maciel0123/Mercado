package fiap.com.br.mercado.controller;

import java.util.List;
import java.util.Locale.Category;

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
import org.springframework.web.bind.annotation.RequestParam;


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

    @GetMapping("/{name}")
    public UserModel get(@PathVariable String name) {
        log.info("Buscando usuario" + name);
        return getUser(name);
    }

    @PutMapping("{name}")
    @CacheEvict(allEntries = true)
    public UserModel update(@PathVariable String name, @RequestBody UserModel user) {
        log.info("Atualizando nome do usuário " + name + " para " + user.getName());
        UserModel existingUser = getUser(name); // Método que busca o usuário pelo nome
        existingUser.setName(user.getName());
        return repository.save(existingUser);
    }

    @DeleteMapping("{name}")
    @CacheEvict(allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable String name) {
        log.info("Apagando usuario " + name);
        repository.delete(getUser(name));
    }

    private UserModel getUser(String name) {
        return repository
                .findByName(name)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Usuario " + name + "  não encontrado"));
    }


}
