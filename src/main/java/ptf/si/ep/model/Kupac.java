package ptf.si.ep.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "kupac", uniqueConstraints = {@UniqueConstraint(name = "email",
        columnNames = {"email"})})
public class Kupac {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ime", nullable = false, length = 80)
    private String ime;

    @Column(name = "email", nullable = false, length = 120)
    private String email;

    @Column(name = "ulica", length = 120)
    private String ulica;

    @Column(name = "grad", length = 60)
    private String grad;

    @Column(name = "postanski_broj", length = 10)
    private String postanskiBroj;

    // BCrypt hash, nikad čista lozinka; DTO-ovi je ne vraćaju
    @Column(name = "lozinka", length = 100)
    private String lozinka;

    @ColumnDefault("'KUPAC'")
    @Column(name = "uloga", nullable = false, length = 20)
    private String uloga = Uloga.KUPAC.name();

    @CreationTimestamp
    @Column(name = "kreirano", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime kreirano;

    @OneToMany(mappedBy = "kupac")
    private Set<Narudzba> narudzbas = new LinkedHashSet<>();

    @OneToOne(mappedBy = "kupac")
    private Profil profil;
}