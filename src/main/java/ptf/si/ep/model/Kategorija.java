package ptf.si.ep.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "kategorija", uniqueConstraints = {@UniqueConstraint(name = "naziv",
        columnNames = {"naziv"})})
public class Kategorija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "naziv", nullable = false, length = 60)
    private String naziv;

    @ManyToMany(mappedBy = "kategorijas")
    private Set<Proizvod> proizvods = new LinkedHashSet<>();
}