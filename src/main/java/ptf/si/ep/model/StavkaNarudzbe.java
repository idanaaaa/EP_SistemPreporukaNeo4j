package ptf.si.ep.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "stavka_narudzbe")
public class StavkaNarudzbe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "narudzba_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stavka_narudzba"))
    private Narudzba narudzba;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proizvod_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stavka_proizvod"))
    private Proizvod proizvod;

    @Column(name = "kolicina", nullable = false)
    private Integer kolicina;

    @Column(name = "cijena", nullable = false, precision = 10, scale = 2)
    private BigDecimal cijena;
}