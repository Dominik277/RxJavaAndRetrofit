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

        //
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

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

    private void handleResults(List<Crypto.Market> marketList){
        if (marketList != null && marketList.size() != 0){
            recyclerViewAdapter.setData(marketList);
        }else {
            Toast.makeText(this,"NO RESULTS FOUND",Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(Throwable t){
        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE.Try again"
              ,Toast.LENGTH_LONG).show();
    }

}