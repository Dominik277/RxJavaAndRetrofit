package rx.java;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

//ova je klasa napravljena kako bi pomocu njenog objekta napravili adapter za RecyclerView, objekt
//ove klase u sebi ce sadrzavati sve one podatke koje RecyclerView treba prikazati kao i nacin na
//koji ce prikazati te podatke,posto smo napravili svoj custom izgled jednog retka onda moramo i
//napravili adapter koji se potpomaze s tim retkom koji smo definirali.Unutar ove klase imamo jos
//jednu klasu imena ViewHolder unutar koje pohranjujemo sve View-ove koji se koriste u tom jednom
//retku te ih pomocu metode findViewById() pohranjujemo u java objekte
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //ovdje smo definirali da cemo napraviti objekt tipa List unutar kojeg cemo pohranjivati objekte
    //klase Market i samo smo definirali da cemo taj objekt referencirati s imenom marketList iako jos
    //nismo stvorili objekt
    //Crypto.Market --> ovaj dio koda nam govori da imamo klasu Crypto unutar koje se nalazi jos jedna
    //klasa Market kojoj smo pristupili tako pomocu . operatora te da cemo u List-u spremati objekte
    //te klase
    private List<Crypto.Market> marketList;

    //ovo nam je custom konstruktor koji koristimo prilikom kreiranja objekta i unutar njega definiramo
    //sve sto je potrebno za kreaciju objekta.U nasem slucaju nam ovo znaci da prilikom kreacije adaptera
    //za RecyclerView mi cemo automatski stvoriti i objekt u memoriju u obliku ArrayList-a i referencirati
    //cemo ga pomocu imena marketList
    public RecyclerViewAdapter(){
        marketList = new ArrayList<>();
    }

    //ova metoda se poziva kada je aplikaciji potreban jos jeddn readak, mozemo to zamisliti ovako, korisnik
    //pocne skrolati kroz aplikaciju i aplikacija treba imati jos redaka kako bi korisnik mogao kroz njih
    //skrolati, a ova metoda se poziva kada korisnik zaskrola ekran i nema niti jednog retka u pripravnosti
    //na cekanju i onda ova metoda stvara jedan redak koji je potreban
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //ovaj blok koda nam je jednostavan za razumiti, sve operacije koje se odvijaju se spremaju u objekt
        //view tipa View te se on vraca kao rezultat ove metode
        //kod na desnoj strani se odvija tako da inflate() metodi kao parametar predajemo recyclerview_item_layout
        //upravo onaj koji smo sami definirali kako zelimo da nam izgleda jedan redak, te nam ovo potvrduje ono gore
        //sto sam objasnjavao da je ova metoda zasluzna za to da se stvori jos jedan redak, a kako ce ona znati kakav
        //redak e pa to joj govorimo u parametru inflate() metode, ova metoda pravi prazan redak bez podataka, a metoda
        //onBindViewHolder se koristi kako bi opskrbila jedan redak s podacima
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_layout,parent,false);
        return new ViewHolder(view);
    }

    //najlakse nam je zapamtiti ako vizualiziramo, a ovu metodu mozemo vizualizirati na nacin tako da zamislimo jedan
    //redak koji treba biti prikazan na zaslonu, s metodom onCreateViewHolder() smo stvorili jedan rekad koji je prazan
    //odnosno bez podataka,a ova metoda nam sluzi upravo za to, kako bi napunila taj redak podacima, a kako zna s kojim
    //podacima to joj opskrbimo kao parametar position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Crypto.Market market = marketList.get(position);

        holder.txtCoin.setText(market.coinName);
        holder.txtMarket.setText(market.market);
        holder.txtPrice.setText("$" + String.format("%.2f",Double.parseDouble(market.price)));
        if (market.coinName.equalsIgnoreCase("eth")){
            holder.cardView.setCardBackgroundColor(Color.GRAY);
        }else {
            holder.cardView.setCardBackgroundColor(Color.GREEN);
        }
    }

    //ova metod nam sluzi kako bi vratila integer koji nam govori koliko ima elemenata u nekoj kolekciji podataka, u
    //nasem slucaju se govori o ArrayListu, znaci ova metoda vraca broj elemenata unutar ArrayList-a
    @Override
    public int getItemCount() {
        return 0;
    }

    public void setData(List<Crypto.Market> data){
        this.marketList.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtCoin;
        public TextView txtMarket;
        public TextView txtPrice;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCoin = itemView.findViewById(R.id.txtCoin);
            txtMarket = itemView.findViewById(R.id.txtMarket);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

}
