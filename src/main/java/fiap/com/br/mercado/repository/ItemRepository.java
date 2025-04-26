package fiap.com.br.mercado.repository;
 
import java.util.List;
import java.util.Optional;
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import fiap.com.br.mercado.model.ItemModel;
 
public interface ItemRepository extends JpaRepository<ItemModel, Long> {
 
    List<ItemModel> findByName(String name);
 
    List<ItemModel> findByTipo(String tipo);
 
    List<ItemModel> findByRaridade(String raridade);
 
    List<ItemModel> findByPrecoBetween(Double precoMinimo, Double precoMaximo);
 
    Optional<ItemModel> findByDono(String dono);
 
}