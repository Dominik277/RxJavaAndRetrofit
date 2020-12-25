package rx.java;

import com.google.gson.annotations.SerializedName;
import java.util.List;

//ovo je klasa koja unutar sebe ima jos dvije klase Market i Ticker
//sve tri klase su nam POJO klase koje sadrze najbitnije podatke vezane za aplikaciju
//ove klase imaju member varijable koje ce opisivati objekte koji se instanciraju od te klase
//@SerializedName --> ova enotacija nam govori da cemo ove member varijable također imati prikazane
//                    i u JSON zapisu te smo s ovom anotacijom odredili kako ce biti prikazani podaci
//                    unutar ovog JSon formata, znaci onako kako zelimo da izgleda member varijabla
//                    unutar JSON-a to navedemo kao string argument ovoj anotaciji
public class Crypto {

    @SerializedName("ticker")
    public Ticker ticker;
    @SerializedName("timestamp")
    public Integer timestamp;
    @SerializedName("success")
    public Boolean success;
    @SerializedName("error")
    public String error;


    public class Market{
        @SerializedName("market")
        public String market;
        @SerializedName("price")
        public String price;
        @SerializedName("volume")
        public Float volume;

        public String coinName;
    }

    public class Ticker{
        @SerializedName("base")
        public String base;
        @SerializedName("target")
        public String target;
        @SerializedName("price")
        public String price;
        @SerializedName("volume")
        public String volume;
        @SerializedName("change")
        public String change;
        @SerializedName("markets")
        public List<Market> markets = null;
    }

}

