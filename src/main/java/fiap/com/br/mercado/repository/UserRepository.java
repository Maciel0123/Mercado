package fiap.com.br.mercado.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fiap.com.br.mercado.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByName(String name);
    
}
