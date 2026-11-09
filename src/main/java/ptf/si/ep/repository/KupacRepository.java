package ptf.si.ep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptf.si.ep.model.Kupac;

import java.util.List;
import java.util.Optional;

public interface KupacRepository extends JpaRepository<Kupac, Long> {
    Optional<Kupac> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Kupac> findByGradIgnoreCase(String grad);
}
