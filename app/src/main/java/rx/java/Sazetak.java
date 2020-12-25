/*

Ova aplikacija se zasniva na RxJava library-u koji nam omogucuje lakse odrađivanje asinkronih zadataka te je ova library
zapravo abstraction layer preko AsyncTask-a s pomocu kojeg su otklonjeni svi nedostatci AsyncTask-a.RxJava je također
implementacija Reactive Programming paradigme.Unutar ove aplikacije koristimo jos i Retrofit koji nam sluzi za skidanje
podataka s interneta tako sto klijent posalje zahtjev serveru za određenim podacima i onda ga server opskrbljuje tim podacima.

-----------
Crypto
-----------
Ovo nam je najobicnija POJO klasa unutar koje se nalaze jos dvije POJO klase Ticker i Market.Sve te klase imaju u sebi member varijable
koje ce opisivati objekt koji bude instanciran od njih.Također imamo i anotaciju @SerializedName pomocu koje definiramo kako ce biti
napisani member varijable unutar JSON zapisa

--------------
MainActivity
--------------

----------------------
CryptocurrencyService
----------------------
Ovo je interfejs unutar kojega smo definirali metodu getCoinData koja ima anotaciju @GET koja je zasluzna za to da da kao klijent posalje
zahtjev serveru kako bi nam server poceo slati podatke koje zelimo.@PATH anotacija nam sluzi kako bi definirali protokol s kojeg cemo
skidati podatke.Te smo također napravili string varijablu BASE_URL unutar koje smo pohranili link s kojeg cemo dokucivati podatke, i svaki
sljedeci put kada budemo htjeli taj link negdje upotrijebiti referencirat cemo ga s pomocu varijable BASE_URL, a ne da upisujemo svaki put
taj dugacki link.

---------------------
RecyclerViewAdapter
---------------------
Ovo nam je najobicnija klasa koju moramo koristiti uvijek kada koristimo i RecyclerView.RecylcerView nam je prikaz podataka slican
kao ListView samo sto je RecyclerView puno efikasniji zbog toga sto bolje iskoristava memoriju.On radi na nacin da ako je na zaslonu
prikazano 5 redaka on ce u pripravnosti imati jos dva retka te ce reciklirati sve retke koji nestanu sa zaslona za razliku od ListView-a
koji moze u pripravnosti imati 1000 redaka te to moze jako zauzimati memoriju.U ovoj klasi na pocetku moramo definirati da nasljeđujemo
klasu RecyclerView.Adapter te zbog toga automatski moramo implementirati tri metode te napraviti jos jednu inner klasu.Te tri metode su
onCreateViewHolder(), onBindViewHolder() i getItemCount(), a inner klasa je ViewHolder.
onCreateViewHolder() metoda nam sluzi kako bi kreirala jedan redak koji treba biti u pripravnosti s onim elementima koje smo definirali
u XML datoteci, znaci ova metoda kreira redak sa svim view-ovima, ali bez ikakvih podataka pohranjenih u te view-ove
onBindViewHolder() ova metoda sluzi da kada redak koji je kreiran putem metode onCreateViewHolder() treba prikazati na zaslonu on je tada
prazan sto se tice podataka, e upravo ova metoda je zasluzna za to da se u taj novonastali redak unesu podaci
getItemCount() ova metoda nam jednostavno vraca integer koji predstavlja broj koliko elemenata ima unutar neke kolekcije podataka
I na kraju imamo ViewHolder klasu koju mozemo zamisliti kao nekakav kontejner koji u sebi sadrzi sve view-ove koji ce biti prikazani u jednom
retku i s pomocu metode findViewById() mi stvaramo View objekte od XML elemenata

 */