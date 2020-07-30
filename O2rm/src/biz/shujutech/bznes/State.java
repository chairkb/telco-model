package biz.shujutech.bznes;

import biz.shujutech.base.Connection;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import java.util.concurrent.CopyOnWriteArrayList;
import biz.shujutech.reflect.ReflectField;
import biz.shujutech.reflect.ReflectIndex;
import org.joda.time.DateTime;
import biz.shujutech.db.object.Lookup;

public class State extends Clasz implements Lookup {
	@ReflectField(type=FieldType.STRING, size=64, indexes={@ReflectIndex(indexName="idx_state_descr", indexNo=0, indexOrder=SortOrder.ASC, isUnique=false)}) public static String Descr;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.City", prefetch=true, lookup=true) public static String City; 
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.bznes.Holiday") public static String Holiday; 

	public static CopyOnWriteArrayList<Lookup> LookupList = new CopyOnWriteArrayList<>();

	public State() throws Exception {
		super();
	}

	public State(String aState) throws Exception {
		this.setDescr(aState);
	}

	@Override
	public void initialize(Connection aConn) throws Exception {
		// do nothing
	}

	@Override
	public String getValueStr() throws Exception {
		return(this.getValueStr(Descr));
	}

	@Override
	public String getDescr() throws Exception {
		return(this.getValueStr(Descr));
	}

	@Override
	public void setDescr(String aDescr) throws Exception {
		this.setValueStr(Descr, aDescr);
	}

	@Override
	public CopyOnWriteArrayList<Lookup> getLookupList() {
		return(LookupList);
	}

	public static CopyOnWriteArrayList<Lookup> GetStateList() {
		return(LookupList);
	}

	public void addCities(Connection aConn) throws Exception {
		State.AddCities(aConn, this);
	}

	public static void AddCities(Connection aConn, State aState) throws Exception {
		if (aState.getDescr().equals("Selangor")) {
			aState.addCity(aConn, "Ampang");
			aState.addCity(aConn, "Bandar Baru Bangi");
			aState.addCity(aConn, "Bandar Puncak Alam");
			aState.addCity(aConn, "Banting");
			aState.addCity(aConn, "Batang Kali");
			aState.addCity(aConn, "Batu Arang");
			aState.addCity(aConn, "Batu Caves");
			aState.addCity(aConn, "Beranang");
			aState.addCity(aConn, "Bestari Jaya");
			aState.addCity(aConn, "Bukit Rotan");
			aState.addCity(aConn, "Cheras");
			aState.addCity(aConn, "Cyberjaya");
			aState.addCity(aConn, "Dengkil");
			aState.addCity(aConn, "Hulu Langat");
			aState.addCity(aConn, "Jenjarom");
			aState.addCity(aConn, "Jeram");
			aState.addCity(aConn, "Kajang");
			aState.addCity(aConn, "Kapar");
			aState.addCity(aConn, "Kerling");
			aState.addCity(aConn, "Klang");
			aState.addCity(aConn, "KLIA");
			aState.addCity(aConn, "Kuala Kubu Baru");
			aState.addCity(aConn, "Kuala Selangor");
			aState.addCity(aConn, "Kuang");
			aState.addCity(aConn, "Pelabuhan Klang");
			aState.addCity(aConn, "Petaling Jaya");
			aState.addCity(aConn, "Puchong");
			aState.addCity(aConn, "Pulau Carey");
			aState.addCity(aConn, "Pulau Indah");
			aState.addCity(aConn, "Pulau Ketam");
			aState.addCity(aConn, "Rasa");
			aState.addCity(aConn, "Rawang");
			aState.addCity(aConn, "Sabak Bernam");
			aState.addCity(aConn, "Sekinchan");
			aState.addCity(aConn, "Semenyih");
			aState.addCity(aConn, "Sepang");
			aState.addCity(aConn, "Serdang");
			aState.addCity(aConn, "Serendah");
			aState.addCity(aConn, "Seri Kembangan");
			aState.addCity(aConn, "Shah Alam");
			aState.addCity(aConn, "Subang Jaya");
			aState.addCity(aConn, "Sungai Ayer Tawar");
			aState.addCity(aConn, "Sungai Besar");
			aState.addCity(aConn, "Sungai Buloh");
			aState.addCity(aConn, "Sungai Pelek");
			aState.addCity(aConn, "Tanjong Karang");
			aState.addCity(aConn, "Tanjung Sepat");
			aState.addCity(aConn, "Telok Panglima Garang");
		} else if (aState.getDescr().equals("Wilayah Perseketuan")) {
			aState.addCity(aConn, "Kuala Lumpur");
		} else if (aState.getDescr().equals("Wilayah Putrajaya")) {
			aState.addCity(aConn, "Putrajaya");
		} else if (aState.getDescr().equals("Wilayah Labuan")) {
			aState.addCity(aConn, "Labuan");
		} else if (aState.getDescr().equals("Penang")) {
			aState.addCity(aConn, "Ayer Itam");
			aState.addCity(aConn, "Balik Pulau");
			aState.addCity(aConn, "Batu Ferringhi");
			aState.addCity(aConn, "Batu Maung");
			aState.addCity(aConn, "Bayan Lepas");
			aState.addCity(aConn, "Bukit Mertajam");
			aState.addCity(aConn, "Butterworth");
			aState.addCity(aConn, "Gelugor");
			aState.addCity(aConn, "Jelutong");
			aState.addCity(aConn, "Kepala Batas");
			aState.addCity(aConn, "Kubang Semang");
			aState.addCity(aConn, "Nibong Tebal");
			aState.addCity(aConn, "Penaga");
			aState.addCity(aConn, "Penang Hill");
			aState.addCity(aConn, "Perai");
			aState.addCity(aConn, "Permatang Pauh");
			aState.addCity(aConn, "Pulau Pinang");
			aState.addCity(aConn, "Simpang Ampat");
			aState.addCity(aConn, "Sungai Jawi");
			aState.addCity(aConn, "Tanjong Bungah");
			aState.addCity(aConn, "Tanjung Bungah");
			aState.addCity(aConn, "Tasek Gelugor");
			aState.addCity(aConn, "Tasek Gelugur");
			aState.addCity(aConn, "USM Pulau Pinang");
		} else if (aState.getDescr().equals("Johor")) {
			aState.addCity(aConn, "Ayer Baloi");
			aState.addCity(aConn, "Ayer Hitam");
			aState.addCity(aConn, "Ayer Tawar 2");
			aState.addCity(aConn, "Bandar Penawar");
			aState.addCity(aConn, "Bandar Tenggara");
			aState.addCity(aConn, "Batu Anam");
			aState.addCity(aConn, "Batu Pahat");
			aState.addCity(aConn, "Bekok");
			aState.addCity(aConn, "Benut");
			aState.addCity(aConn, "Bukit Gambir");
			aState.addCity(aConn, "Bukit Pasir");
			aState.addCity(aConn, "Chaah");
			aState.addCity(aConn, "Endau");
			aState.addCity(aConn, "Gelang Patah");
			aState.addCity(aConn, "Gerisek");
			aState.addCity(aConn, "Gugusan Taib Andak");
			aState.addCity(aConn, "Jementah");
			aState.addCity(aConn, "Johor Bahru");
			aState.addCity(aConn, "Kahang");
			aState.addCity(aConn, "Kluang");
			aState.addCity(aConn, "Kota Tinggi");
			aState.addCity(aConn, "Kukup");
			aState.addCity(aConn, "Kulai");
			aState.addCity(aConn, "Labis");
			aState.addCity(aConn, "Layang-Layang");
			aState.addCity(aConn, "Masai");
			aState.addCity(aConn, "Mersing");
			aState.addCity(aConn, "Muar");
			aState.addCity(aConn, "Nusajaya");
			aState.addCity(aConn, "Pagoh");
			aState.addCity(aConn, "Paloh");
			aState.addCity(aConn, "Panchor");
			aState.addCity(aConn, "Parit Jawa");
			aState.addCity(aConn, "Parit Raja");
			aState.addCity(aConn, "Parit Sulong");
			aState.addCity(aConn, "Pasir Gudang");
			aState.addCity(aConn, "Pekan Nenas");
			aState.addCity(aConn, "Pengerang");
			aState.addCity(aConn, "Pontian");
			aState.addCity(aConn, "Pulau Satu");
			aState.addCity(aConn, "Rengam");
			aState.addCity(aConn, "Rengit");
			aState.addCity(aConn, "Segamat");
			aState.addCity(aConn, "Semerah");
			aState.addCity(aConn, "Senai");
			aState.addCity(aConn, "Senggarang");
			aState.addCity(aConn, "Seri Gading");
			aState.addCity(aConn, "Seri Medan");
			aState.addCity(aConn, "Simpang Rengam");
			aState.addCity(aConn, "Sungai Mati");
			aState.addCity(aConn, "Tangkak");
			aState.addCity(aConn, "Ulu Tiram");
			aState.addCity(aConn, "Yong Peng");
		} else if (aState.getDescr().equals("Kedah")) {
			aState.addCity(aConn, "Alor Setar");
			aState.addCity(aConn, "Ayer Hitam");
			aState.addCity(aConn, "Baling");
			aState.addCity(aConn, "Bandar Baharu");
			aState.addCity(aConn, "Bedong");
			aState.addCity(aConn, "Bukit Kayu Hitam");
			aState.addCity(aConn, "Changloon");
			aState.addCity(aConn, "Gurun");
			aState.addCity(aConn, "Jeniang");
			aState.addCity(aConn, "Jitra");
			aState.addCity(aConn, "Karangan");
			aState.addCity(aConn, "Kepala Batas");
			aState.addCity(aConn, "Kodiang");
			aState.addCity(aConn, "Kota Kuala Muda");
			aState.addCity(aConn, "Kota Sarang Semut");
			aState.addCity(aConn, "Kuala Kedah");
			aState.addCity(aConn, "Kuala Ketil");
			aState.addCity(aConn, "Kuala Nerang");
			aState.addCity(aConn, "Kuala Pegang");
			aState.addCity(aConn, "Kulim");
			aState.addCity(aConn, "Kupang");
			aState.addCity(aConn, "Langgar");
			aState.addCity(aConn, "Langkawi");
			aState.addCity(aConn, "Lunas");
			aState.addCity(aConn, "Merbok");
			aState.addCity(aConn, "Padang Serai");
			aState.addCity(aConn, "Pendang");
			aState.addCity(aConn, "Pokok Sena");
			aState.addCity(aConn, "Serdang");
			aState.addCity(aConn, "Sik");
			aState.addCity(aConn, "Simpang Empat");
			aState.addCity(aConn, "Sungai Petani");
			aState.addCity(aConn, "Universiti Utara Malaysia");
			aState.addCity(aConn, "Yan");
		} else if (aState.getDescr().equals("Negeri Sembilan")) {
			aState.addCity(aConn, "Bahau");
			aState.addCity(aConn, "Bandar Enstek");
			aState.addCity(aConn, "Bandar Seri Jempol");
			aState.addCity(aConn, "Batu Kikir");
			aState.addCity(aConn, "Gemas");
			aState.addCity(aConn, "Gemencheh");
			aState.addCity(aConn, "Johol");
			aState.addCity(aConn, "Kota");
			aState.addCity(aConn, "Kuala Klawang");
			aState.addCity(aConn, "Kuala Pilah");
			aState.addCity(aConn, "Labu");
			aState.addCity(aConn, "Linggi");
			aState.addCity(aConn, "Mantin");
			aState.addCity(aConn, "Nilai");
			aState.addCity(aConn, "Port Dickson");
			aState.addCity(aConn, "Pusat Bandar Palong");
			aState.addCity(aConn, "Rantau");
			aState.addCity(aConn, "Rembau");
			aState.addCity(aConn, "Rompin");
			aState.addCity(aConn, "Seremban");
			aState.addCity(aConn, "Si Rusa");
			aState.addCity(aConn, "Simpang Durian");
			aState.addCity(aConn, "Simpang Pertang");
			aState.addCity(aConn, "Tampin");
			aState.addCity(aConn, "Tanjong Ipoh");
		} else if (aState.getDescr().equals("Melaka")) {
			aState.addCity(aConn, "Alor Gajah");
			aState.addCity(aConn, "Asahan");
			aState.addCity(aConn, "Ayer Keroh");
			aState.addCity(aConn, "Bemban");
			aState.addCity(aConn, "Durian Tunggal");
			aState.addCity(aConn, "Jasin");
			aState.addCity(aConn, "Kem Trendak");
			aState.addCity(aConn, "Kuala Sungai Baru");
			aState.addCity(aConn, "Lubok China");
			aState.addCity(aConn, "Masjid Tanah");
			aState.addCity(aConn, "Melaka");
			aState.addCity(aConn, "Merlimau");
			aState.addCity(aConn, "Selandar");
			aState.addCity(aConn, "Sungai Rambai");
			aState.addCity(aConn, "Sungai Udang");
			aState.addCity(aConn, "Tanjong Kling");
		} else if (aState.getDescr().equals("Pahang")) {
			aState.addCity(aConn, "Balok");
			aState.addCity(aConn, "Bandar Bera");
			aState.addCity(aConn, "Bandar Pusat Jengka");
			aState.addCity(aConn, "Bandar Tun Abdul Razak");
			aState.addCity(aConn, "Benta");
			aState.addCity(aConn, "Bentong");
			aState.addCity(aConn, "Brinchang");
			aState.addCity(aConn, "Bukit Fraser");
			aState.addCity(aConn, "Bukit Goh");
			aState.addCity(aConn, "Bukit Kuin");
			aState.addCity(aConn, "Chenor");
			aState.addCity(aConn, "Chini");
			aState.addCity(aConn, "Damak");
			aState.addCity(aConn, "Dong");
			aState.addCity(aConn, "Gambang");
			aState.addCity(aConn, "Genting Highlands");
			aState.addCity(aConn, "Jerantut");
			aState.addCity(aConn, "Karak");
			aState.addCity(aConn, "Kemayan");
			aState.addCity(aConn, "Kuala Krau");
			aState.addCity(aConn, "Kuala Lipis");
			aState.addCity(aConn, "Kuala Rompin");
			aState.addCity(aConn, "Kuantan");
			aState.addCity(aConn, "Lanchang");
			aState.addCity(aConn, "Lurah Bilut");
			aState.addCity(aConn, "Maran");
			aState.addCity(aConn, "Mentakab");
			aState.addCity(aConn, "Muadzam Shah");
			aState.addCity(aConn, "Padang Tengku");
			aState.addCity(aConn, "Pekan");
			aState.addCity(aConn, "Raub");
			aState.addCity(aConn, "Ringlet");
			aState.addCity(aConn, "Sega");
			aState.addCity(aConn, "Sungai Koyan");
			aState.addCity(aConn, "Sungai Lembing");
			aState.addCity(aConn, "Tanah Rata");
			aState.addCity(aConn, "Temerloh");
			aState.addCity(aConn, "Triang");
		} else if (aState.getDescr().equals("Perak")) {
			aState.addCity(aConn, "Ayer Tawar");
			aState.addCity(aConn, "Bagan Datoh");
			aState.addCity(aConn, "Bagan Serai");
			aState.addCity(aConn, "Bandar Seri Iskandar");
			aState.addCity(aConn, "Batu Gajah");
			aState.addCity(aConn, "Batu Kurau");
			aState.addCity(aConn, "Behrang Stesen");
			aState.addCity(aConn, "Bidor");
			aState.addCity(aConn, "Bota");
			aState.addCity(aConn, "Bruas");
			aState.addCity(aConn, "Changkat Jering");
			aState.addCity(aConn, "Changkat Keruing");
			aState.addCity(aConn, "Chemor");
			aState.addCity(aConn, "Chenderiang");
			aState.addCity(aConn, "Chenderong Balai");
			aState.addCity(aConn, "Chikus");
			aState.addCity(aConn, "Enggor");
			aState.addCity(aConn, "Gerik");
			aState.addCity(aConn, "Gopeng");
			aState.addCity(aConn, "Hutan Melintang");
			aState.addCity(aConn, "Intan");
			aState.addCity(aConn, "Ipoh");
			aState.addCity(aConn, "Jeram");
			aState.addCity(aConn, "Kampar");
			aState.addCity(aConn, "Kampung Gajah");
			aState.addCity(aConn, "Kampung Kepayang");
			aState.addCity(aConn, "Kamunting");
			aState.addCity(aConn, "Kuala Kangsar");
			aState.addCity(aConn, "Kuala Kurau");
			aState.addCity(aConn, "Kuala Sepetang");
			aState.addCity(aConn, "Lambor Kanan");
			aState.addCity(aConn, "Langkap");
			aState.addCity(aConn, "Lenggong");
			aState.addCity(aConn, "Lumut");
			aState.addCity(aConn, "Malim Nawar");
			aState.addCity(aConn, "Manong");
			aState.addCity(aConn, "Matang");
			aState.addCity(aConn, "Padang Rengas");
			aState.addCity(aConn, "Pangkor");
			aState.addCity(aConn, "Pantai Remis");
			aState.addCity(aConn, "Parit");
			aState.addCity(aConn, "Parit Buntar");
			aState.addCity(aConn, "Pengkalan Hulu");
			aState.addCity(aConn, "Pusing");
			aState.addCity(aConn, "Rantau Panjang");
			aState.addCity(aConn, "Sauk");
			aState.addCity(aConn, "Selama");
			aState.addCity(aConn, "Selekoh");
			aState.addCity(aConn, "Seri Manjung");
			aState.addCity(aConn, "Simpang");
			aState.addCity(aConn, "Simpang Ampat Semanggol");
			aState.addCity(aConn, "Sitiawan");
			aState.addCity(aConn, "Slim River");
			aState.addCity(aConn, "Sungai Siput");
			aState.addCity(aConn, "Sungai Sumun");
			aState.addCity(aConn, "Sungkai");
			aState.addCity(aConn, "Taiping");
			aState.addCity(aConn, "Tanjong Malim");
			aState.addCity(aConn, "Tanjong Piandang");
			aState.addCity(aConn, "Tanjong Rambutan");
			aState.addCity(aConn, "Tanjong Tualang");
			aState.addCity(aConn, "Tapah");
			aState.addCity(aConn, "Tapah Road");
			aState.addCity(aConn, "Teluk Intan");
			aState.addCity(aConn, "Temoh");
			aState.addCity(aConn, "TLDM Lumut");
			aState.addCity(aConn, "Trolak");
			aState.addCity(aConn, "Trong");
			aState.addCity(aConn, "Tronoh");
			aState.addCity(aConn, "Ulu Bernam");
			aState.addCity(aConn, "Ulu Kinta");
		} else if (aState.getDescr().equals("Terengganu")) {
			aState.addCity(aConn, "Ajil");
			aState.addCity(aConn, "Al Muktatfi Billah Shah");
			aState.addCity(aConn, "Ayer Puteh");
			aState.addCity(aConn, "Bukit Besi");
			aState.addCity(aConn, "Bukit Payong");
			aState.addCity(aConn, "Ceneh");
			aState.addCity(aConn, "Chalok");
			aState.addCity(aConn, "Cukai");
			aState.addCity(aConn, "Dungun");
			aState.addCity(aConn, "Jerteh");
			aState.addCity(aConn, "Kampung Raja");
			aState.addCity(aConn, "Kemasek");
			aState.addCity(aConn, "Kerteh");
			aState.addCity(aConn, "Ketengah Jaya");
			aState.addCity(aConn, "Kijal");
			aState.addCity(aConn, "Kuala Berang");
			aState.addCity(aConn, "Kuala Besut");
			aState.addCity(aConn, "Kuala Terengganu");
			aState.addCity(aConn, "Marang");
			aState.addCity(aConn, "Paka");
			aState.addCity(aConn, "Permaisuri");
			aState.addCity(aConn, "Sungai Tong");
		} else if (aState.getDescr().equals("Perlis")) {
			aState.addCity(aConn, "Arau");
			aState.addCity(aConn, "Kaki Bukit");
			aState.addCity(aConn, "Kangar");
			aState.addCity(aConn, "Kuala Perlis");
			aState.addCity(aConn, "Padang Besar");
			aState.addCity(aConn, "Simpang Ampat");
		} else if (aState.getDescr().equals("Kelantan")) {
			aState.addCity(aConn, "Ayer Lanas");
			aState.addCity(aConn, "Bachok");
			aState.addCity(aConn, "Cherang Ruku");
			aState.addCity(aConn, "Dabong");
			aState.addCity(aConn, "Gua Musang");
			aState.addCity(aConn, "Jeli");
			aState.addCity(aConn, "Kem Desa Pahlawan");
			aState.addCity(aConn, "Ketereh");
			aState.addCity(aConn, "Kota Bharu");
			aState.addCity(aConn, "Kuala Balah");
			aState.addCity(aConn, "Kuala Krai");
			aState.addCity(aConn, "Machang");
			aState.addCity(aConn, "Melor");
			aState.addCity(aConn, "Pasir Mas");
			aState.addCity(aConn, "Pasir Puteh");
			aState.addCity(aConn, "Pulai Chondong");
			aState.addCity(aConn, "Rantau Panjang");
			aState.addCity(aConn, "Selising");
			aState.addCity(aConn, "Tanah Merah");
			aState.addCity(aConn, "Temangan");
			aState.addCity(aConn, "Tumpat");
			aState.addCity(aConn, "Wakaf Bharu");
		} else if (aState.getDescr().equals("Sarawak")) {
			aState.addCity(aConn, "Asajaya");
			aState.addCity(aConn, "Balingian");
			aState.addCity(aConn, "Baram");
			aState.addCity(aConn, "Bau");
			aState.addCity(aConn, "Bekenu");
			aState.addCity(aConn, "Belaga");
			aState.addCity(aConn, "Belawai");
			aState.addCity(aConn, "Betong");
			aState.addCity(aConn, "Bintangor");
			aState.addCity(aConn, "Bintulu");
			aState.addCity(aConn, "Dalat");
			aState.addCity(aConn, "Daro");
			aState.addCity(aConn, "Debak");
			aState.addCity(aConn, "Engkilili");
			aState.addCity(aConn, "Julau");
			aState.addCity(aConn, "Kabong");
			aState.addCity(aConn, "Kanowit");
			aState.addCity(aConn, "Kapit");
			aState.addCity(aConn, "Kota Samarahan");
			aState.addCity(aConn, "Kuching");
			aState.addCity(aConn, "Lawas");
			aState.addCity(aConn, "Limbang");
			aState.addCity(aConn, "Lingga");
			aState.addCity(aConn, "Long Lama");
			aState.addCity(aConn, "Lubok Antu");
			aState.addCity(aConn, "Lundu");
			aState.addCity(aConn, "Lutong");
			aState.addCity(aConn, "Matu");
			aState.addCity(aConn, "Miri");
			aState.addCity(aConn, "Mukah");
			aState.addCity(aConn, "Nanga Medamit");
			aState.addCity(aConn, "Niah");
			aState.addCity(aConn, "Pusa");
			aState.addCity(aConn, "Roban");
			aState.addCity(aConn, "Saratok");
			aState.addCity(aConn, "Sarikei");
			aState.addCity(aConn, "Sebauh");
			aState.addCity(aConn, "Sebuyau");
			aState.addCity(aConn, "Serian");
			aState.addCity(aConn, "Sibu");
			aState.addCity(aConn, "Siburan");
			aState.addCity(aConn, "Simunjan");
			aState.addCity(aConn, "Song");
			aState.addCity(aConn, "Spaoh");
			aState.addCity(aConn, "Sri Aman");
			aState.addCity(aConn, "Sundar");
			aState.addCity(aConn, "Tatau");
		} else if (aState.getDescr().equals("Sabah")) {
			aState.addCity(aConn, "Beaufort");
			aState.addCity(aConn, "Beluran");
			aState.addCity(aConn, "Beverly");
			aState.addCity(aConn, "Bongawan");
			aState.addCity(aConn, "Inanam");
			aState.addCity(aConn, "Keningau");
			aState.addCity(aConn, "Kota Belud");
			aState.addCity(aConn, "Kota Kinabalu");
			aState.addCity(aConn, "Kota Kinabatangan");
			aState.addCity(aConn, "Kota Marudu");
			aState.addCity(aConn, "Kuala Penyu");
			aState.addCity(aConn, "Kudat");
			aState.addCity(aConn, "Kunak");
			aState.addCity(aConn, "Lahad Datu");
			aState.addCity(aConn, "Likas");
			aState.addCity(aConn, "Membakut");
			aState.addCity(aConn, "Menumbok");
			aState.addCity(aConn, "Nabawan");
			aState.addCity(aConn, "Pamol");
			aState.addCity(aConn, "Papar");
			aState.addCity(aConn, "Penampang");
			aState.addCity(aConn, "Putatan");
			aState.addCity(aConn, "Ranau");
			aState.addCity(aConn, "Sandakan");
			aState.addCity(aConn, "Semporna");
			aState.addCity(aConn, "Sipitang");
			aState.addCity(aConn, "Tambunan");
			aState.addCity(aConn, "Tamparuli");
			aState.addCity(aConn, "Tanjung Aru");
			aState.addCity(aConn, "Tawau");
			aState.addCity(aConn, "Tenghilan");
			aState.addCity(aConn, "Tenom");
			aState.addCity(aConn, "Tuaran");
		} else if (aState.getDescr().equals("Singapore")) {
			aState.addCity(aConn, "Singapore");
		} else {
			//throw new Hinderance("Fail to create instant of the class STATE for: " + aState.getDescr());
		}
	}

	public static void InitList(Connection aConn) throws Exception {
		ObjectBase.CreateObject(aConn, State.class); // check if table already exist, if not create it
		Lookup.ClearAndLoadList(aConn, State.class, LookupList);
	}

	public void addCity(Connection aConn, String aCityName) throws Exception {
		City dbCity = (City) ObjectBase.CreateObject(aConn, City.class);
		dbCity.setDescr(aCityName);
		if (!this.gotCity(aConn, aCityName)) {
			this.addValueObject(aConn, City, dbCity);
		}
		Lookup.Add2LookupList(dbCity); // city is already prefetch, only its lookup is not populated
	}

	public boolean gotCity(Connection aConn, String aCityName) throws Exception {
		boolean result = false;
		this.getCity().resetIterator(); // always do a reset before starting to loop for the objects
		while(this.getCity().hasNext(aConn)) {
			City eachCity = (City) this.getCity().getNext();
			if (eachCity.getDescr().toUpperCase().equals(aCityName.toUpperCase())) {
				result = true;
				break;
			}
		}
		return(result);
	}

	public FieldObjectBox getCity() throws Exception {
		return((FieldObjectBox) this.getField(City));
	}

	public City getCity(int aIndex) throws Exception {
		FieldObjectBox fob = (FieldObjectBox) this.getField(City);
		City areaCode = (City) fob.getObject(aIndex);
		return(areaCode);
	}

	public FieldObjectBox getHoliday() throws Exception {
		return((FieldObjectBox) this.getField(Holiday));
	}

	public void addHoliday(Connection aConn, Holiday aHoliday) throws Exception {
		this.addValueObject(aConn, Holiday, aHoliday);
	}

	public Holiday getHoliday(Connection aConn, DateTime aHolidayDate) throws Exception {
		return (biz.shujutech.bznes.Holiday.GetHoliday(aConn, this, aHolidayDate));
	}

	public String getName() throws Exception {
		return(this.getValueStr(Descr));
	}

}
