package ptf.si.ep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptf.si.ep.model.Narudzba;

import java.util.List;

public interface NarudzbaRepository extends JpaRepository<Narudzba, Long> {
    List<Narudzba> findByKupacIdOrderByKreiranoDesc(Long kupacId);

    List<Narudzba> findByStatus(String status);
}
