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

import fiap.com.br.mercado.model.ItemModel;
import fiap.com.br.mercado.repository.ItemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/item")
@Slf4j
@Cacheable(value = "item")

public class ItemController {

    @Autowired
    private ItemRepository repositoryItem;

    @GetMapping
    @Cacheable
    public List<ItemModel> index() {
        return repositoryItem.findAll();
    }

    @PostMapping
    @CacheEvict(allEntries = true)
    @Operation(responses = @ApiResponse(responseCode = "400"))
    @ResponseStatus(HttpStatus.CREATED)
    public ItemModel create(@RequestBody @Valid ItemModel itemModel) {
        log.info("Cadastrando item " + itemModel.getName());
        return repositoryItem.save(itemModel);
    }

    @GetMapping("/{name}")
    public ItemModel getNome(@PathVariable String name) {
        log.info("Buscando item: " + name);
        return getItem(name);
    }

    @PutMapping("/{name}")
    @CacheEvict(allEntries = true)
    public ItemModel update(@PathVariable String name, @RequestBody ItemModel item) {
        log.info("Atualizando dados do item " + name);

        ItemModel existingItem = getItem(name);
        if (item.getName() != null) {
            existingItem.setName(item.getName());
        }
        if (item.getTipo() != null) {
            existingItem.setTipo(item.getTipo());
        }
        if (item.getRaridade() != null) {
            existingItem.setRaridade(item.getRaridade());
        }
        if (item.getPreco() != null) {
            existingItem.setPreco(item.getPreco());
        }
        if (item.getDono() != null) {
            existingItem.setDono(item.getDono());
        }

        return repositoryItem.save(existingItem);
    }

    @DeleteMapping("{name}")
    @CacheEvict(allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable String name) {
        log.info("Apagando item " + name);
        repositoryItem.delete(getItem(name));
    }

    private ItemModel getItem(String name) {
        return repositoryItem
                .findByName(name)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Item " + name + "  não encontrado"));
    }

}
