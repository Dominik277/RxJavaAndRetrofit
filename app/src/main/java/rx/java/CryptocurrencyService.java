package rx.java;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

//ovo nam je interfejs unutar kojega smo definirali metodu getCoinData koja s neta skida informacije
//vezane za podacima o coin-u.Dodatne sposobnosti ovoj metodi daju anotacije @GET i @PATH
public interface CryptocurrencyService {

    //ovdje smo samo definirali string imena BASE_URL unutar kojeg smo spremili API s kojeg cemo
    //skidati podatke unutar JSON formata, nista specijalno
    String BASE_URL = "https://api.cryptonator.com/api/full/";

    //@GET --> ova anotacija je zasluzna za ostvarivanje zahtjeva za podacima koje klijent zeli
    //         od servera
    @GET("{coin}-usd")

    //@PATH --> ova anotacija nam sluzi kako bi pomocu nje odredili gdje ce java klasa biti ugoscen
    //          argument unutar ove zagrade mora biti jednak onom argumentu unutar @GET anotacije
    //Znaci @PATH anotacija nam oznacava put to do lokacije s koje trebamo skinuti neke podatke dok
    //@GET anotacija nam sluzi kako bi u potpunosti skinuli te podatke s te lokacije
    //Observable --> mozemo ga zamisliti kao kolekciju razlicitih podataka, Observable pruza potporu
    //               za slanje podataka prema razlicijim dijelovima unutar aplikacije
    Observable<Crypto> getCoinData(@Path("coin") String coin);

}
