
import java.awt.*;

import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;

public class oyun extends JFrame implements Runnable{
  kontrolPanel ctrlpanel;//kontrolpanel sınıfından
  JLabel jLabel;//basılı tuttuğumuz şartıyla kelime öngösterimi yeri
  JPanel kelimePanel;//bulunan kelimeleri bu panelde repaintliyoruz
  JLabel jLabel2;//puanı bu labelde gösteriyoruz
  
  
  public oyun(){
    //ana frame ayarları
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(410,730);
    setTitle("Kelime Oyunu");
    setResizable(false);  
    setLocationRelativeTo(null);
    setLayout(null);

    //kod kalabalığı önlemek için fonksiyon
    altFonksiyon();

    //thread çalıştırma yeri
    (new Thread(this)).start();
    
  }
  private void altFonksiyon(){
    //ana frame eklenen bileşenler ve ayarları
    ctrlpanel = new kontrolPanel(0, 0, 400, 700);
    jLabel = new JLabel();
    jLabel.setBounds(0, 280, 400, 100);
    jLabel.setForeground(Color.white);
    jLabel.setFont(new Font("Monospaced", Font.BOLD,30));
    jLabel.setHorizontalAlignment(SwingConstants.CENTER);

    jLabel2 = new JLabel();
    jLabel2.setBounds(20, 20, 50, 50);
    jLabel2.setForeground(Color.white);
    jLabel2.setHorizontalAlignment(SwingConstants.CENTER);

    
    



    kelimePanel = new JPanel(){//bulunan kelimelerin olduğu panelin ayarlamaları
      public void paint(Graphics g){
        super.paint(g);
        setBackground(Color.LIGHT_GRAY);
        //opaklığı frame resmi gözüksün diye kapadım
        setOpaque(false);
        g.setColor(Color.white);
        Font font = new Font("Monospaced", Font.BOLD, 30);
        g.setFont(font);
        for(int y = 20 ,i=0; i<ctrlpanel.bulunanlar.size() ; y+=30,i++){
          g.drawString(ctrlpanel.bulunanlar.get(i), kelimePanel.getWidth()/2-50, y);
        }


      }
    };
    kelimePanel.setBounds(25, 60, 350, 250);
    

    
    //stackoverflowdan aldım aşşağıdaki image ekleme yerini bunun sayesinde arkaplan var
    try {
      setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src/resim.jpg")))));
    } catch (IOException e) {
      e.printStackTrace();
    }


    
    ctrlpanel.add(kelimePanel);
    ctrlpanel.add(jLabel2);
    ctrlpanel.add(jLabel);
    add(ctrlpanel);
  }


  public void run(){
    while(true){
    
    jLabel2.setText("Puan: "+ctrlpanel.getPuan());//puan dinamik şekilde değişiyor
    jLabel.setText(ctrlpanel.kelime);//basılı tutarak kelimenin öngörünmesini sağlıyor
    if(ctrlpanel.kelimeEklensinMı){//bu kondisyon true ise kelimepaneli repaintlenicek (bunun sayesinde bulunan kelimeleri ekliyoruz)
      kelimePanel.repaint();
    }
    
  }
  }
  public static void main(String[] args) {
    oyun n = new oyun();
    n.setVisible(true);
  }
  
}
