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

        //ovdje smo napravili instancu od inner klase Market koja se nalazi unutar Crypto klase te smo u nju pohranili
        //ono sto se odvilo s desne strane.Na desnoj strani imamo marketList koji je ArrayList objekata tipa Market i
        //s pomocu metode get() te njenog parametra position se tocno zna koji objekt treba predati jer unutar ArrayList-a
        //postoji vise objekata i svaki u sebi sadrzi razlicite informacije, a razlikujemo ih prema broju indexa
        Crypto.Market market = marketList.get(position);

        //da bi lakse shvatili sljedeci dio koda moramo poceti od Market klase, od Market klase se moze napraviti beskonacno
        //mnogo instanci i svaka u sebi sadrzi tri string varijable koje su sve razlicite, a mi smo te tri varijable nazvali
        //txtCoin, txtMarket, txtPrice te s pomocu metode setText() mi postavljamo sta ce biti napisano unutar tih TextView-ova
        //ono sto zelimo da bude napisano to prilozimo kao argument
        holder.txtCoin.setText(market.coinName);
        holder.txtMarket.setText(market.market);
        holder.txtPrice.setText("$" + String.format("%.2f",Double.parseDouble(market.price)));

        //ovdje provjeravamo određeni string, u nasem slucaju provjeravamo sta je pohranjeno u string varijablu coinName koja
        //se nalazi u svakom objektu, dva stringa su jednaka ako im je duljina jednaka i ako su karakteri jednaki, velika i
        //mala slova se zanemaruju.U nasem slucaju ako tekst koji je pohranjen unutar varijable coinName je jednak argumentu
        //kojeg smo predali metodi equalsIgnoreCase() onda se izvrsa dio koda unutar if() bloka te se cardView boja u sivo, a
        //ako dio u if zagradi nije istinit onda se izvrsava dio bloka else te se cardView boja u zeleno
        //equalsIgnoreCase() --> ova metoda nam sluzi da usporedi dva stringa jesu li jednaka te vraca true ako jesu jednaki
        //                       a ako su razliciti vraca false, dva stringa su jednaka ako su jednake duljine i ako imaju
        //                       iste karaktere, velika i mala slova se zanemaruju
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

    //ova metoda sluzi kako bi postavila sve podatke unutar marketList objekta
    public void setData(List<Crypto.Market> data){
        this.marketList.addAll(data);

        //ova metoda daje doznanja da se nesto promjenilo, a ta promjena koja se dogodila u ovom slucaju je da
        //su se unijeli podaci unutar marketList objekta
        notifyDataSetChanged();
    }

    //ova klasa nam sluzi kao nekakav kontejner unutar kojeg drzimo sve one elemente koji se nalaze u jednom retku
    //naseg RecyclerView-a.Ako pogledamo unutar XML layout recyclerview_item_layout datoteku vidjet cemo da se unutar
    //nje nalaze 4 elementa kao sto smo i naveli ovdje unutar ove klase
    public class ViewHolder extends RecyclerView.ViewHolder{

        //u sljedecem bloku koda smo definirali preko kojih imena cemo dozivati objekte u memoriji onih tipova podataka
        //kojih smo definirali u layout-u.Znaci definirali smo da ce nas redak se sastojati od tri tekstualna dijela
        //te smo tako stvorili tri objekta TextView tipa i svakome dali specificno ime kako bi ih lakse razlikovali
        //te smo organizirali da ta tri TextView-a budu unutar CardView-a
        public TextView txtCoin;
        public TextView txtMarket;
        public TextView txtPrice;
        public CardView cardView;

        //ovo nam je custom konstruktor u cijem tijelu smo atributima iz XML-a dodjelili java objekte
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //u sljedecem dijelu koda smo definirali da onim varijablama koje smo definirali na pocetku ove klase da
            //dodljeljujemo atribute iz XML-a pomocu findViewById() metode.Ta metoda odlazi u XML i trazi one atribute
            //po ID-ovima s obzirom na ono sto smo joj predali kao argument, te kada nade ono sto smo joj predali onda
            //to pohranjuje u objekt
            txtCoin = itemView.findViewById(R.id.txtCoin);
            txtMarket = itemView.findViewById(R.id.txtMarket);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

}
