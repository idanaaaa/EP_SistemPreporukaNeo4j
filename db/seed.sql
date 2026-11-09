SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE stavka_narudzbe;
TRUNCATE TABLE narudzba;
TRUNCATE TABLE profil;
TRUNCATE TABLE kupac;
TRUNCATE TABLE proizvod_kategorija;
TRUNCATE TABLE proizvod;
TRUNCATE TABLE kategorija;

SET FOREIGN_KEY_CHECKS = 1;

-- Categories
INSERT INTO kategorija (id, naziv) VALUES
                                       (1, 'Electronics'),
                                       (2, 'Computers & Accessories'),
                                       (3, 'Home Appliances'),
                                       (4, 'Books'),
                                       (5, 'Sports & Outdoors'),
                                       (6, 'Food & Groceries');

-- Products (prices in BAM)
INSERT INTO proizvod (id, naziv, opis, cijena, na_stanju) VALUES
                                                              (1,  'Lenovo IdeaPad 5 Laptop', 'Laptop with a 15.6" display, 16 GB RAM and a 512 GB SSD.', 1499.00, 8),
                                                              (2,  'Logitech M185 Wireless Mouse', 'Compact wireless mouse with a USB receiver.', 39.90, 50),
                                                              (3,  'Mechanical Keyboard', 'Keyboard with mechanical switches and a US layout.', 129.00, 20),
                                                              (4,  'Dell 27" Monitor', '27-inch IPS monitor, 2560x1440 resolution.', 449.00, 12),
                                                              (5,  'Samsung Galaxy A55 Smartphone', 'Phone with 128 GB storage and a triple camera.', 799.00, 15),
                                                              (6,  'Espresso Machine', 'Espresso machine with 15 bar pressure and a milk frother.', 349.00, 10),
                                                              (7,  'Bosch Vacuum Cleaner', 'Bagless vacuum cleaner, 700 W.', 259.00, 7),
                                                              (8,  'Copper Coffee Pot', 'Handmade copper coffee pot from Bascarsija, serves 4.', 35.00, 40),
                                                              (9,  'The Bridge on the Drina - Ivo Andric', 'Novel, hardcover.', 25.00, 30),
                                                              (10, 'Death and the Dervish - Mesa Selimovic', 'Novel, hardcover.', 27.50, 25),
                                                              (11, 'Football', 'Size 5 ball for play on grass.', 45.00, 35),
                                                              (12, 'Mountain Bike', 'Bike with an aluminium frame and 21 gears.', 899.00, 5),
                                                              (13, 'Wildflower Honey 1 kg', 'Local honey from the mountains around Konjic.', 28.00, 60),
                                                              (14, 'Ground Coffee 500 g', 'Roasted and ground coffee for brewing in a coffee pot.', 12.50, 100);

INSERT INTO proizvod_kategorija (proizvod_id, kategorija_id) VALUES
                                                                 (1, 1), (1, 2),
                                                                 (2, 1), (2, 2),
                                                                 (3, 1), (3, 2),
                                                                 (4, 1), (4, 2),
                                                                 (5, 1),
                                                                 (6, 3),
                                                                 (7, 3),
                                                                 (8, 3),
                                                                 (9, 4),
                                                                 (10, 4),
                                                                 (11, 5),
                                                                 (12, 5),
                                                                 (13, 6),
                                                                 (14, 6);

-- Customers
INSERT INTO kupac (id, ime, email, ulica, grad, postanski_broj, kreirano) VALUES
                                                                              (1, 'Amra Hodzic',      'amra.hodzic@example.com',      '12 Zmaja od Bosne St', 'Sarajevo',   '71000', '2026-06-10 09:15:00'),
                                                                              (2, 'Emir Begovic',     'emir.begovic@example.com',     '45 Marsala Tita St',   'Mostar',     '88000', '2026-06-22 14:30:00'),
                                                                              (3, 'Lejla Mujic',      'lejla.mujic@example.com',      '8 Turalibegova St',    'Tuzla',      '75000', '2026-07-03 11:00:00'),
                                                                              (4, 'Adnan Kovacevic',  'adnan.kovacevic@example.com',  '20 Masarykova St',     'Zenica',     '72000', '2026-07-18 16:45:00'),
                                                                              (5, 'Selma Hadzic',     'selma.hadzic@example.com',     '15 Kralja Petra I St', 'Banja Luka', '78000', '2026-08-01 10:20:00'),
                                                                              (6, 'Haris Delic',      'haris.delic@example.com',      '3 Bosanska St',        'Bihac',      '77000', '2026-09-12 18:05:00');

-- Profiles (customer 6 has no profile yet)
INSERT INTO profil (id, kupac_id, telefon, rodjen) VALUES
                                                       (1, 1, '+387 61 123 456', '1995-03-14'),
                                                       (2, 2, '+387 62 234 567', '1988-11-02'),
                                                       (3, 3, '+387 61 345 678', '2000-07-21'),
                                                       (4, 4, '+387 63 456 789', '1992-01-30'),
                                                       (5, 5, '+387 65 567 890', '1997-09-09');

-- Orders (ukupno = sum of order items)
-- Status values stay in Bosnian to match the application default ('NOVA' in Narudzba.java)
INSERT INTO narudzba (id, kupac_id, status, ukupno, kreirano) VALUES
                                                                  (1, 1, 'ISPORUCENA', 1538.90, '2026-09-02 10:12:00'),
                                                                  (2, 2, 'POSLANA',      77.50, '2026-09-15 13:40:00'),
                                                                  (3, 3, 'NOVA',        399.00, '2026-10-20 08:55:00'),
                                                                  (4, 1, 'U_OBRADI',    119.00, '2026-10-18 19:30:00'),
                                                                  (5, 4, 'ISPORUCENA',  989.00, '2026-08-25 12:00:00'),
                                                                  (6, 5, 'OTKAZANA',    799.00, '2026-10-05 15:25:00'),
                                                                  (7, 6, 'NOVA',        578.00, '2026-10-22 21:10:00');

-- Order items (cijena = product price at the time of purchase)
INSERT INTO stavka_narudzbe (id, narudzba_id, proizvod_id, kolicina, cijena) VALUES
                                                                                 (1,  1, 1,  1, 1499.00),
                                                                                 (2,  1, 2,  1,   39.90),
                                                                                 (3,  2, 9,  2,   25.00),
                                                                                 (4,  2, 10, 1,   27.50),
                                                                                 (5,  3, 6,  1,  349.00),
                                                                                 (6,  3, 14, 4,   12.50),
                                                                                 (7,  4, 13, 3,   28.00),
                                                                                 (8,  4, 8,  1,   35.00),
                                                                                 (9,  5, 12, 1,  899.00),
                                                                                 (10, 5, 11, 2,   45.00),
                                                                                 (11, 6, 5,  1,  799.00),
                                                                                 (12, 7, 3,  1,  129.00),
                                                                                 (13, 7, 4,  1,  449.00);

-- Demo nalozi (BCrypt hash): svi kupci imaju lozinku lozinka123, a admin@epshop.ba lozinku admin1234
UPDATE kupac SET lozinka = '$2a$10$r8Cu0GbSPlsoNnlxRncGj.KcZ6O9xbo6wJh/1QGbBXDXC72hKusVC';
INSERT INTO kupac (id, ime, email, lozinka, uloga, kreirano) VALUES
    (7, 'Administrator', 'admin@epshop.ba', '$2a$10$P9s8L2NbTMbsxQYNmMyzKe/PUNCTmXP9Y7tST4QRCFSsRIDg4Hl4K', 'ADMIN', '2026-06-01 08:00:00');
