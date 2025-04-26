package fiap.com.br.mercado.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fiap.com.br.mercado.model.UserModel;
import java.util.List;




public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findById(Long id);
    List<UserModel> findByName(String name);
    List<UserModel> findByClasse(String classe);
    
}
