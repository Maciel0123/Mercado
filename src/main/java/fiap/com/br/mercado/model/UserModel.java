package fiap.com.br.mercado.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   
    @NotBlank(message = "o nome é obrigarório")
    private String name;
    private String classe;
    @Min(1)
    @Max(99)
    private Integer nivel;
    private Double moedas;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getClasse() {
        return classe;
    }
    public void setClasse(String classe) {
        this.classe = classe;
    }
    public Integer getNivel() {
        return nivel;
    }
    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }
    public Double getMoedas() {
        return moedas;
    }
    public void setMoedas(Double moedas) {
        this.moedas = moedas;
    }

}
