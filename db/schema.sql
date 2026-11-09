CREATE TABLE kupac (
                       id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                       ime            VARCHAR(80)  NOT NULL,
                       email          VARCHAR(120) NOT NULL UNIQUE,
                       ulica          VARCHAR(120),
                       grad           VARCHAR(60),
                       postanski_broj VARCHAR(10),
                       kreirano       DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 1 : 1 s kupcem: posebna FK kolona s UNIQUE ograničenjem
CREATE TABLE profil (
                        id       BIGINT AUTO_INCREMENT PRIMARY KEY,
                        kupac_id BIGINT NOT NULL UNIQUE,
                        telefon  VARCHAR(30),
                        rodjen   DATE,
                        CONSTRAINT fk_profil_kupac FOREIGN KEY (kupac_id) REFERENCES kupac(id)
) ENGINE=InnoDB;

CREATE TABLE kategorija (
                            id    BIGINT AUTO_INCREMENT PRIMARY KEY,
                            naziv VARCHAR(60) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE proizvod (
                          id        BIGINT AUTO_INCREMENT PRIMARY KEY,
                          naziv     VARCHAR(120)  NOT NULL,
                          opis      TEXT,
                          cijena    DECIMAL(10,2) NOT NULL,
                          na_stanju INT NOT NULL DEFAULT 0
) ENGINE=InnoDB;

-- M : N međutabela
CREATE TABLE proizvod_kategorija (
                                     proizvod_id   BIGINT NOT NULL,
                                     kategorija_id BIGINT NOT NULL,
                                     PRIMARY KEY (proizvod_id, kategorija_id),
                                     CONSTRAINT fk_pk_proizvod   FOREIGN KEY (proizvod_id)   REFERENCES proizvod(id),
                                     CONSTRAINT fk_pk_kategorija FOREIGN KEY (kategorija_id) REFERENCES kategorija(id)
) ENGINE=InnoDB;

CREATE TABLE narudzba (
                          id       BIGINT AUTO_INCREMENT PRIMARY KEY,
                          kupac_id BIGINT NOT NULL,
                          status   VARCHAR(20) NOT NULL DEFAULT 'NOVA',
                          ukupno   DECIMAL(10,2),
                          kreirano DATETIME DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_narudzba_kupac FOREIGN KEY (kupac_id) REFERENCES kupac(id)
) ENGINE=InnoDB;

-- M : N s atributima (količina, cijena) = poseban entitet
CREATE TABLE stavka_narudzbe (
                                 id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 narudzba_id BIGINT NOT NULL,
                                 proizvod_id BIGINT NOT NULL,
                                 kolicina    INT NOT NULL,
                                 cijena      DECIMAL(10,2) NOT NULL,
                                 CONSTRAINT fk_stavka_narudzba FOREIGN KEY (narudzba_id) REFERENCES narudzba(id),
                                 CONSTRAINT fk_stavka_proizvod FOREIGN KEY (proizvod_id) REFERENCES proizvod(id)
) ENGINE=InnoDB;