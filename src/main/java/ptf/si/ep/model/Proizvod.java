package ptf.si.ep.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "proizvod")
public class Proizvod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "naziv", nullable = false, length = 120)
    private String naziv;

    @Column(name = "opis", columnDefinition = "TEXT")
    private String opis;

    @Column(name = "cijena", nullable = false, precision = 10, scale = 2)
    private BigDecimal cijena;

    @ColumnDefault("0")
    @Column(name = "na_stanju", nullable = false)
    private Integer naStanju = 0;

    @ManyToMany
    @JoinTable(name = "proizvod_kategorija",
            joinColumns = @JoinColumn(name = "proizvod_id"),
            inverseJoinColumns = @JoinColumn(name = "kategorija_id"),
            foreignKey = @ForeignKey(name = "fk_pk_proizvod"),
            inverseForeignKey = @ForeignKey(name = "fk_pk_kategorija"))
    private Set<Kategorija> kategorijas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "proizvod")
    private Set<StavkaNarudzbe> stavkaNarudzbes = new LinkedHashSet<>();
}