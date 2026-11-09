package ptf.si.ep.model;

// Kolona narudzba.status je VARCHAR, pa entitet čuva String; enum služi kao lista dozvoljenih vrijednosti
public enum StatusNarudzbe {
    NOVA, U_OBRADI, POSLANA, ISPORUCENA, OTKAZANA;

    public boolean jeZavrsen() {
        return this == ISPORUCENA || this == OTKAZANA;
    }
}
