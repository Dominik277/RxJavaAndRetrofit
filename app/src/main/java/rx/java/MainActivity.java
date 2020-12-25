package rx.java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import static rx.java.CryptocurrencyService.BASE_URL;

public class MainActivity extends AppCompatActivity {

    //ovdje smo definirali koje cemo sve tipove podataka kasnije koristiti u programu te da cemo
    //od njih kasnije napraviti objekte, ovo su nam za sada varijable koje su prazne, ali kasnije ce
    //nam koristit kako bi pomocu njih mogli pozivati objekte koji su kreirani i pohranjeni unutar
    //memorije racunala
    RecyclerView recyclerView;
    Retrofit retrofit;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ovdje smo s pomocu metode findViewById() otisli u XML i taj element koji smo prilozili kao argument
        //ovoj metodi pohranili unutar recyclerView objekta, znaci u ovom koraku smo pretvorili XML element u
        //java objekt
        recyclerView = findViewById(R.id.recyclerView);

        //posto smo napravili objekt od RecyclerView-a mi sada imammo mogucnost raditi dosta stvari s njim, jedna
        //od stvari koju smo napravili u ovom koraku je da smo rekli da ce taj RecyclerView biti u LinearLayoutManager
        //konstituciji koji nam govori da ce podaci unutar RecyclerView- biti poredani u vodoravne trake kao u ListView-u
        //također imamo i FrameLayoutManager i CardViewLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //u ovoj liniji koda smo napravili objekt od nase klase RecyclerViewAdapter te smo rekli da cemo ga pozivati
        //preko imena recyclerViewAdapter
        recyclerViewAdapter = new RecyclerViewAdapter();

        //ovdje smo RecyclerView-u postavili adapter, a taj adapter je objekt klase RecyclerViewAdapter
        recyclerView.setAdapter(recyclerViewAdapter);

        //objekt ove klase, u nasem slucaju interceptor, sluzi kako bi bi zapisivao sve podatke koji se odvijaju tijekom
        //slanja zahtjeva klijenta prema serveru, pa informacije o odgovoru servera prema klijentu te također i informacije
        //koje se dobivaju tokom trajanja prenosenja podataka
        //setLevel() --> ova metoda nam sluzi kako bi pomocu nje odredili koliko zelimo informacija da objekt od klase
        //               HttpLoggingInterceptor zapisuje, imamo Levele:BODY(najvise),BASIC(srednje) i NONE(nista)
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //ovdje smo napravili objekt klase OkHttpClient koji je zasluzan za to da salje HTTP zahtjeve prema serveru te
        //slusa njihove odgovore, kao sto sam rekao ovaj objekt ima mogucnost samo slusanja odgovora servera prema klijentu
        //ali nema mogucnost zapisivanja tih informacija koje server daje klijentu, pa onda ako zelimo dodati ovom objektu
        //interceptor objekt to cemo napraviti pomocu metode addInterceptor() i kao argument joj dati interceptor objekt
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        //GSON je Java library koja se koristi kako bi pretvorili Java objekte u njihove JSON reprezentacije te također
        //ima mogucnost i kreiranja Java objekata od JSON zapisa.Kada zelimo nekom objektu dodati neke dodatne mogucnosti
        //onda samo na njegov konstruktor dodamo Builder te nakon toga pomocu . operatora dodajemo te dodatne mogucnosti koje
        //zelimo da taj objekt ima
        //setLenient() --> po defaultu GSON je strog te kao format prihvaca jedino JSON, da bi omogucili da ovaj objekt bude
        //                 malo popustljiviji onda pozivamo ovu metodu, znaci kada GSON serijalizira Java objekte onda se
        //                 dobije JSON zapis
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //ovdje pravimo objekt tipa Retrofit te kazemo da cemo ga referencirati pomocu imena retrofit, kao sto smo ranije naveli
        //kada zelimo dodati Retrofit objektu neke dodatne mogucnosti u odnosu na one koje bi imao po defaultu dodajemo Builder()
        //nastavak.Objekt ove klase ima za zadacu slanja zahtjeva i primanje poruke od servera je li prihvatio zahtjev ili nije,
        //ali pri kreiranju objekta te klase dodajemo mu par dodatnih mogucnosti
        //baseUrl() --> kao argument ovoj metodi prilazemo link od API-a s kojeg zelimo skidati podatke
        //client() --> ovoj metodi prilazemo klijenta koji je zasluzan za to da se serveru posalje zahtjev, klijent kojega prilazemo
        //             ovoj metodi kao argument je objekt klase OkHttpClient
        //addCallAdapterFactory() -->
        //addConverterFactory() -->
        //build() --> ova metoda nam sluzi kako bi upogonili sve ono sto smo prethodno deklarirali
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //u sljedecoj liniji koda pozivamo metodu callEndpoints() koju smo deklarirali izvan onCreate() metode
        callEndpoints();
    }

    @SuppressLint("CheckResult")
    private void callEndpoints() {

        CryptocurrencyService cryptocurrencyService = retrofit.create(CryptocurrencyService.class);
        Observable<List<Crypto.Market>> btcObservable = cryptocurrencyService
                .getCoinData("btc")
                .map(result -> Observable.fromIterable(result.ticker.markets))
                .flatMap(x -> x).filter(y -> {
                    y.coinName = "btc";
                    return true;
                }).toList().toObservable();

        Observable<List<Crypto.Market>> ethObservable = cryptocurrencyService
                .getCoinData("eth")
                .map(result -> Observable.fromIterable(result.ticker.markets))
                .flatMap(x -> x).filter(y ->{
                    y.coinName = "eth";
                    return true;
                }).toList().toObservable();

        Observable.merge(btcObservable, ethObservable)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError);
    }

    //ova metoda nam sluzi kako bi se brinula oko unesenih podataka koje je server opskrbio
    //kao argument ovoj metodi predajemo Listu objekata klase Market
    private void handleResults(List<Crypto.Market> marketList){

        //u if bloku koda provjeravamo unutar List-e ako ona nije prazna odnosno ako joj je velicina razlicita
        //od nule onda se izvrsava sljedeci dio koda unutar viticastih zagrada, a taj dio koda nam govori da
        //u recyclerViewAdapter objekt koji je zasluzan za postavljanje podataka u RecyclerView pomocu metode
        //setData dodajemo sve one objekte koji se nalaze unutar Liste-e Market objekata
        if (marketList != null && marketList.size() != 0){
            recyclerViewAdapter.setData(marketList);

        //ova linija koda unutar viticastih zagrada se izvrsava ako dio koda pod if dijelom nije istinit, odnosno
        //ovaj dio koda se izvrsava ako je List-a Market objekata prazna i ako joj je velicina jednaka nula, a dio
        //koda koji ce se izvesti je da se ispise Toast poruka na dnu ekrana s porukom "NO RESULTS FOUND"
        }else {
            Toast.makeText(this,"NO RESULTS FOUND",Toast.LENGTH_LONG).show();
        }
    }

    //ova metoda sadrzi kod koji se poziva kada se dogodi neka greska u HTTP protokolu, te smo unutar
    //ove metode definirali sta ce se desiti ako i dode do neke greske, definirali smo da ce se na dnu
    //ekrana ispisati Toast poruka koja ce imati tekst "ERROR IN FETCHING API RESPONSE.Try again"
    private void handleError(Throwable t){
        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE.Try again"
              ,Toast.LENGTH_LONG).show();
    }

}