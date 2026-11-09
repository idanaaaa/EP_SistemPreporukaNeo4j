package ptf.si.ep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptf.si.ep.model.StavkaNarudzbe;

import java.util.List;

public interface StavkaNarudzbeRepository extends JpaRepository<StavkaNarudzbe, Long> {
    List<StavkaNarudzbe> findByNarudzbaId(Long narudzbaId);
}
