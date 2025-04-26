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
 
    @GetMapping("/nome/{name}")
    public List<ItemModel> getNome(@PathVariable String name) {
        log.info("Buscando item: " + name);
        return getAllItem(name);
    }
 
    @GetMapping("/type/{tipo}")
    public List<ItemModel> getPorTipo(@PathVariable String tipo) {
        log.info("Buscando item pelo tipo: " + tipo);
        return getItemTipo(tipo);
    }
 
    @GetMapping("/preco/{min}/{max}")
    public List<ItemModel> getPreco(@PathVariable("min") Double precoMinimo,
            @PathVariable("max") Double precoMaximo) {
        log.info("Buscando itens com preço entre " + precoMinimo + " e " + precoMaximo);
        return getItemPreco(precoMinimo, precoMaximo);
    }
   
    @GetMapping("/raridade/{raridade}")
    public List<ItemModel> getRaridade(@PathVariable String raridade) {
        log.info("Buscando item pela raridade: " + raridade);
        return getItemRaridade(raridade);
    }
 
    @PutMapping("/{id}")
    @CacheEvict(allEntries = true)
    public ItemModel update(@PathVariable Long id, @RequestBody ItemModel item) {
        log.info("Atualizando dados do item " + id);
 
       
        ItemModel existingItem = getItem(id);
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
 
    @DeleteMapping("{id}")
    @CacheEvict(allEntries = true)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("Apagando item " + id);
        repositoryItem.delete(getItem(id));
    }
 
    private List<ItemModel> getAllItem(String name) {
        List<ItemModel> items = repositoryItem.findByName(name);
        if (items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Item " + name + "  não encontrado");
        }
        return items;
    }

    private ItemModel getItem(Long id) {
        return repositoryItem
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Item " + id + " não encontrado"));
    }
 
    private List<ItemModel> getItemTipo(String tipo) {
        List<ItemModel> items = repositoryItem.findByTipo(tipo);
        if (items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Item do tipo " + tipo + " não encontrado");
        }
        return items;
    }
 
    private List<ItemModel> getItemPreco(Double precoMinimo, Double precoMaximo) {
        List<ItemModel> items = repositoryItem.findByPrecoBetween(precoMinimo, precoMaximo);
        if (items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhum item encontrado com o preço entre " + precoMinimo + " e " + precoMaximo);
        }
        return items;
    }

    private List<ItemModel> getItemRaridade(String raridade){
        List<ItemModel> items = repositoryItem.findByRaridade(raridade);
        if (items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhum item encotrado na categoria:" + raridade);
        }
        return items;
    }
}