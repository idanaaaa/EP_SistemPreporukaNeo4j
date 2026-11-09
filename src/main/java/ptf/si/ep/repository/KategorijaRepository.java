package ptf.si.ep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptf.si.ep.model.Kategorija;

import java.util.Optional;

public interface KategorijaRepository extends JpaRepository<Kategorija, Long> {
    Optional<Kategorija> findByNaziv(String naziv);

    boolean existsByNaziv(String naziv);
}
