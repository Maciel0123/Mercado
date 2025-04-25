package fiap.com.br.mercado.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fiap.com.br.mercado.model.ItemModel;

public interface ItemRepository extends JpaRepository<ItemModel, Long> {

    Optional<ItemModel> findByName(String name);

}