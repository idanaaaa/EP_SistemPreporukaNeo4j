package ptf.si.ep.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "profil", uniqueConstraints = {@UniqueConstraint(name = "kupac_id",
        columnNames = {"kupac_id"})})
public class Profil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kupac_id", nullable = false, foreignKey = @ForeignKey(name = "fk_profil_kupac"))
    private Kupac kupac;

    @Column(name = "telefon", length = 30)
    private String telefon;

    @Column(name = "rodjen")
    private LocalDate rodjen;
}