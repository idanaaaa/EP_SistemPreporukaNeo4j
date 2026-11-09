package ptf.si.ep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ptf.si.ep.model.Proizvod;

import java.math.BigDecimal;
import java.util.List;

public interface ProizvodRepository extends JpaRepository<Proizvod, Long> {

    // 1) Derived queries - Spring generiše upit iz naziva metode
    List<Proizvod> findByNazivContainingIgnoreCase(String naziv);

    List<Proizvod> findByKategorijasId(Long kategorijaId);

    List<Proizvod> findByCijenaBetween(BigDecimal min, BigDecimal max);

    List<Proizvod> findByNaStanjuGreaterThan(Integer kolicina);

    // 2) JPQL - piše se nad entitetima i poljima (Proizvod, p.kategorijas), ne nad tabelama
    @Query("SELECT p FROM Proizvod p JOIN p.kategorijas k WHERE k.naziv = :naziv ORDER BY p.cijena")
    List<Proizvod> findByNazivKategorije(@Param("naziv") String naziv);

    // 3) JPQL s agregatnom funkcijom
    @Query("SELECT AVG(p.cijena) FROM Proizvod p JOIN p.kategorijas k WHERE k.id = :kategorijaId")
    BigDecimal prosjecnaCijenaUKategoriji(@Param("kategorijaId") Long kategorijaId);

    // 4) Native SQL - pravi MySQL upit nad tabelama i kolonama (proizvod, na_stanju)
    @Query(value = "SELECT * FROM proizvod WHERE na_stanju < :prag ORDER BY na_stanju", nativeQuery = true)
    List<Proizvod> findPredNestankom(@Param("prag") int prag);

    // 5) Native SQL s JOIN-om i GROUP BY - najprodavaniji proizvodi
    @Query(value = """
            SELECT p.*
            FROM proizvod p
            JOIN stavka_narudzbe s ON s.proizvod_id = p.id
            GROUP BY p.id
            ORDER BY SUM(s.kolicina) DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<Proizvod> findNajprodavanije(@Param("limit") int limit);

    // 6) UPDATE upit - zahtijeva @Modifying i transakciju (@Transactional u servisu)
    @Modifying
    @Query("UPDATE Proizvod p SET p.naStanju = p.naStanju - :kolicina WHERE p.id = :id AND p.naStanju >= :kolicina")
    int smanjiStanje(@Param("id") Long id, @Param("kolicina") int kolicina);
}
