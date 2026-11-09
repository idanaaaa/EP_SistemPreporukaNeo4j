package ptf.si.ep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptf.si.ep.model.Profil;

import java.util.Optional;

public interface ProfilRepository extends JpaRepository<Profil, Long> {
    Optional<Profil> findByKupacId(Long kupacId);
}
