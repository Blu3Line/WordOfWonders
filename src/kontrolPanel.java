
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
public class kontrolPanel extends JPanel implements MouseMotionListener,MouseListener{

  private int x,y,w,h;
  private int startx,starty,exitx,exity;//çizgilerin koordinatları
  
  //aşşağıdaki pointler sayesinde çizgiler için hafızaya atılan koordinatları arraylistte tutuyor
  private ArrayList<Integer> x1pointler;
  private ArrayList<Integer> y1pointler;
  private ArrayList<Integer> x2pointler;
  private ArrayList<Integer> y2pointler;
  
  
  //yuvarlak panelleri bir arraylistte tutuluyor çünkü yeni yuvarlak panel eklerken dinamiklik olsun 
  private ArrayList<YuvarlakPaneller> daireTuslar;

  
  public String kelime="";
  public int bolum=0;
  public int sonbolum;
  private String[][] kelimeler;//bolum kelimeleri bu arrayde tutmak için
  
  public boolean kelimeEklensinMı=false;//bulunan kelimeleri oyun classındaki panelde yazdırmak için 
  public int kelimeY = 50;//bulununan kelimelerin y koordinatı
  public ArrayList<String> bulunanlar;//bulunan kelimelerin tutulduğu array

  private int puan=0;
  public int getPuan(){//puana bu şekilde ulaşabiliyoruz
    return puan;
  }

  public kontrolPanel(int x,int y,int w,int h){//kontrol panelin kurucu ayarları
    this.x=x;
    this.y=y;
    this.w=w;
    this.h=h;
    setBounds(x, y, w, h);
    setLayout(null);
    
    setOpaque(false);//frame background resimi için paneli saydam yaptım
    daireTuslar= new ArrayList<YuvarlakPaneller>(0);
    
    //kelimelere göre bolum sayısı oto artıp azalabilir.
    sonbolum = getDB("src/kelimeler.txt").length-1;

    //çizgilerin koordinatlarını hafızada dinamik olarak tutmak için arraylist kullanıldı
    x1pointler = new ArrayList<Integer>(0);
    y1pointler = new ArrayList<Integer>(0);
    x2pointler = new ArrayList<Integer>(0);
    y2pointler = new ArrayList<Integer>(0);

    
    kelimeler = kelimeler_setup();
    tuslar_setup(kelimeler[0][0]);
    
    bulunanlar = new ArrayList<String>(10);
    

    addMouseListener(this);
    addMouseMotionListener(this);
  }



  //her bolumün kelimelerini ayrı ayrı incelemek için yapılmıştır 0 yazılan yerler ise kelime bulundu ya da bulumadı durumu için
  private String[][] kelimeler_setup(){//çalışıyor
    String arr[] = getDB("src/kelimeler.txt")[bolum];
    String result[][] = new String[arr.length][2];
    
    for(int i = 0 ; i<result.length ; i++){
      for(int j=0 ; j<2 ; j++){
        if(j==0){result[i][j]=arr[i];}
        if(j==1){result[i][j]="0";}
      }
    }
    
  
    return result;
  }
  //bütün kelimelerin karşılığını kontrol eder eğer hepsi 1 ise true değil ise false returnler bunun sayesinde bölüm atlarız
  private Boolean bolumChecker(String[][] pArr){
    for(int i = 0 ; i<pArr.length ; i++){
      for(int j = 0 ; j<pArr[i].length ; j++){
        if(pArr[i][1].equals("0"))return false;
      }
    }
    return true;
  }

  //mouse basmayı bırakınca çalışacak kod parçası
  private void when_released(){

    
    for(int i = 0 ; i<kelimeler_setup().length ; i++){
      if(kelime.equalsIgnoreCase(kelimeler[i][0]) && kelimeler[i][1].equals("0")){//dogru kelimeyi bulmak ve önceden bulunmamış olmasını arıyor
        kelimeEklensinMı=true;//bunun sayesinde oyun classındaki thread aktifleşip kelimeyi panele eklicek
        kelimeler[i][1]="1";//bunun sayesinde kelime 1 kere bulunabilecek 
        puan+=kelimeler[i][0].length();//kelime boyu kadar puan eklenicek
        bulunanlar.add(kelime);//bulunanlar arrayine eklenicek
        kelimeEklensinMı=false;//oyun classındaki thread habire çalışmasın diye eski haline geçer
      }
    }
    if(bolumChecker(kelimeler)){//bütün kelimeler bulunursa ileri bölüme geçmek için kontrol yapıyor
      bulunanlar.clear();
      bolum+=1;
      if(bolum>sonbolum){System.out.println("oyun bitti skorunuz : "+puan);System.exit(0);}//oyun bitti mi diye kontrol yapıyor eğer daha kelime kalmazsa burda oyun biter
      kelimeler = kelimeler_setup();//yeni bolum setup
      tuslar_setup(kelimeler[0][0]);//yeni tuslar setup
     
    }
    
    kelime ="";
  }

  public void tuslar_setup(String kelime){//bunun sayesinde bolumler arttıkça dinamik şekilde yuvarlak tus panelleri yer değişecek
    
    daireTuslar.clear();
    //aşağıda gelen kelimeyi char list yapıp karıştırıyoruz
    char[] c = kelime.toUpperCase().toCharArray();
    ArrayList<Character> list = new ArrayList<Character>(0);
    for(char a : c){
      list.add(a);
    }
    Collections.shuffle(list);//bunun sayesinde arraylisti karıştırıyoruz

      daireTuslar.add(new YuvarlakPaneller(10, this.h/2+150, list.get(0)));
      daireTuslar.add(new YuvarlakPaneller(this.w-80-10, this.h/2+150, list.get(1)));
      daireTuslar.add(new YuvarlakPaneller(this.w/3-40, 370, list.get(2)));
      daireTuslar.add(new YuvarlakPaneller(this.w/3-40, this.h-80-10, list.get(3)));
      daireTuslar.add(new YuvarlakPaneller(this.w/3+90, this.h-80-10, list.get(4)));
      daireTuslar.add(new YuvarlakPaneller(this.w/3+90, 370, list.get(5)));
      add(daireTuslar.get(0));
      add(daireTuslar.get(1));
      add(daireTuslar.get(2));
      add(daireTuslar.get(3));
      add(daireTuslar.get(4));
      add(daireTuslar.get(5));
  }

  public void paint(Graphics g){
    super.paint(g);


    

    Graphics2D g2d = (Graphics2D) g;//sadece çekilen çizgiyi kalınlaştırmak için kullanıldı.
    g2d.setStroke(new BasicStroke(5));

    g.setColor(new Color(128,0,128));
    g.drawLine(startx,starty,exitx,exity);//çekilen çizgiyi burda yapıyoruz
    for(int i = 0 ; i<x1pointler.size() ; i++){
      g.drawLine(x1pointler.get(i),y1pointler.get(i),x2pointler.get(i),y2pointler.get(i));
    }

    for(int i = 0 ; i<daireTuslar.size() ; i++){
      daireTuslar.get(i).paintlencekYuvarlaklar(g);
    }
    
  }


  public String[][] getDB(String strng){//kelimeleri txt dosyasından çekme
    int sutunSayisi=0;
    boolean tempBoolean = true;
    String temp="";
    String[] arr;
    File f;
    Scanner scn = null;
    Locale loc;

    try{
      //aşşağıdaki kodu text dosyasını utf-8 okumadığı için stackoverflow aldım
      loc = new Locale("es","ES");
      f = new File(strng);
      scn = new Scanner(new FileInputStream(f), "UTF-8");
      scn.useLocale(loc);



        while(scn.hasNext()){
            temp+=scn.nextLine()+"\n";
            if(tempBoolean){sutunSayisi=temp.split(" ").length;tempBoolean=false;}
            //bunun sayesinde sutun sayısı değişse bile dinamik olucak tamamen dinamik sutun ve satır sayısı
        }
        scn.close();
    }catch(Exception e){System.out.println("sorun oluştu: "+ e.getMessage());}
    arr = temp.split("\n");
    String[][] result= new String[arr.length][sutunSayisi];
    for(int i = 0 ; i<arr.length ; i++){
        for(int j = 0 ; j<sutunSayisi ; j++){
            result[i][j] = arr[i].split(" ")[j];
        }
    }
    return result;
  }
  ///////////////////////////////////////////////////////////////////////////////////////////////
  @Override
  public void mouseClicked(MouseEvent e) {}
  @Override
  public void mousePressed(MouseEvent e) {
    
    for(int i = 0 ; i<daireTuslar.size() ; i++){
      if(daireTuslar.get(i).hboxCheck((int)getMousePosition().getX(),(int) getMousePosition().getY())){
        
        startx=e.getX();
        starty=e.getY();
        daireTuslar.get(i).pressed=true;
      }
    
    }
    
      
      repaint();
    
  }
  @Override
  public void mouseReleased(MouseEvent e) {
    for(int i = 0 ; i<daireTuslar.size() ; i++){
      daireTuslar.get(i).pressed=false;
    }//bütün tusların renk eski hale gelir
    
    //eğer mouse tıklamayı bırakırsa hafızada duran pointleri temizler ve bazı şeyleri sıfırlar
    x1pointler.clear();
    y1pointler.clear();
    x2pointler.clear();
    y2pointler.clear();
    startx=0;starty=0;exitx=0;exity=0;
    for(int i = 0 ; i<daireTuslar.size() ; i++){daireTuslar.get(i).birKere=true;}
    when_released();
    
    
    
    repaint();
    
  }
  @Override
  public void mouseEntered(MouseEvent e) {}
  @Override
  public void mouseExited(MouseEvent e) {}
  @Override
  public void mouseDragged(MouseEvent e) {
    
    //bu thread hatalarını  kondisyon ile çevrele alttaki kodu
    exitx=(int)getMousePosition().getX();
    exity=(int)getMousePosition().getY();
    

    for(int i = 0 ; i<daireTuslar.size() ; i++){
      if(daireTuslar.get(i).hboxCheck((int)getMousePosition().getX(),(int) getMousePosition().getY()) && daireTuslar.get(i).birKere){
        kelime+=daireTuslar.get(i).getHarf();
        
        
        daireTuslar.get(i).pressed=true;
        x1pointler.add(startx);
        y1pointler.add(starty);
        x2pointler.add(exitx);
        y2pointler.add(exity);
        startx=exitx;
        starty=exity;
        
        //burda birkere yuvarlak tuşu görüp işlemleri yapması gerek yoksa kod şişer o yüzden false
        daireTuslar.get(i).birKere = false;
      }
    }
    for(int i = 0 ; i<daireTuslar.size() ; i++){
      if(daireTuslar.get(i).hboxCheck(startx,starty)){
      repaint();}}
  }
  @Override
  public void mouseMoved(MouseEvent e) {}
 
  //bu class kucuk yuvarlak tus panellerinden sorumludur.
  class YuvarlakPaneller extends JPanel{

      
      int xYuvarlak,yYuvarlak;//yuvarlak tus panel koordinatları
      char harf;//paneldeki olacak harf
      int genislik=80;//default genislik
      int boyut=80;//default boyut
      boolean pressed=false;//basılan tuşun renkleri ayarlamak için kullanılır.
      boolean birKere=true;//yukarda mouse draggedda her panelde bir kere işlem yapması gerekmekte(mouse dragged methodunda açıklandı)
      public YuvarlakPaneller(int x,int y,char harf){
        this.xYuvarlak=x;
        this.yYuvarlak=y;
        this.harf=harf;
        
        //bu yuvarlak panellerin arka planı saydam yapar
        setOpaque(false);
        
        setBounds(x, y, genislik, boyut);
        
      }
      
  
      public boolean hboxCheck(int x, int y){//yuvarlak panellerin içinde çizgi olması için hitbox
        int hboxwidth = this.genislik/2;
        int hboxheight = this.boyut/2;
        int x1=xYuvarlak+this.genislik/2-(hboxwidth/2);
        int x2 = x1+hboxwidth;
        int y1 = yYuvarlak+this.boyut/2-(hboxheight/2);
        int y2 = y1 + hboxheight;

        if(x>=x1 && x<=x2 && y>=y1 && y<=y2)return true;
        else return false;
      }

      public char getHarf(){//yuvarlak panelin harfine ulaşma
        return harf;
      }
    
      public void paintlencekYuvarlaklar(Graphics g){//kontrol panelindeki paint methodunda çalışması için hazırlanan paint method
        
        if(pressed)g.setColor(new Color(128,0,128));
        else g.setColor(Color.WHITE);
        g.fillOval(xYuvarlak, yYuvarlak, this.genislik, this.boyut);
        if(pressed)g.setColor(Color.white);
        else g.setColor(Color.black);
        Font font = new Font("Monospaced", Font.PLAIN, this.boyut);
        g.setFont(font);
        String temp = String.valueOf(harf);
        g.drawString(temp, xYuvarlak+this.genislik/2-this.genislik/3, yYuvarlak+this.boyut-this.boyut/4);
      }

  }
}
