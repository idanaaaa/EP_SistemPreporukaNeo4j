package ptf.si.ep.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "narudzba")
public class Narudzba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kupac_id", nullable = false, foreignKey = @ForeignKey(name = "fk_narudzba_kupac"))
    private Kupac kupac;

    @ColumnDefault("'NOVA'")
    @Column(name = "status", nullable = false, length = 20)
    private String status = "NOVA";

    @Column(name = "ukupno", precision = 10, scale = 2)
    private BigDecimal ukupno;

    @CreationTimestamp
    @Column(name = "kreirano", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime kreirano;

    @OneToMany(mappedBy = "narudzba")
    private Set<StavkaNarudzbe> stavkaNarudzbes = new LinkedHashSet<>();
}