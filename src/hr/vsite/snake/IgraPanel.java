package hr.vsite.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class IgraPanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 500;
    static final int SCREEN_HEIGHT = 500;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 90;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int velicinaZmije = 4;
    int krugPojedeno;
    int krugX;
    int krugY;

    char smjer = 'D';
    boolean pokrenuto = false;
    Timer timer;
    Random random;

    IgraPanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.darkGray);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startIgra();
    }
    public void startIgra(){
        noviKrug();
        pokrenuto = true;
        timer = new Timer(DELAY,this);
        timer.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        crtanje(g);

    }

    /**
     *
     * @param g
     * crtanje objekata unutar GUI-a
     */
    public void crtanje(Graphics g){
        if(pokrenuto) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            /*crtanje krugova*/
            g.setColor(Color.white);
            g.fillOval(krugX, krugY, UNIT_SIZE, UNIT_SIZE);

            /*crtanje zmije*/
            for (int i = 0; i < velicinaZmije; i++) {
                if (i == 0) {
                    g.setColor(Color.white);
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(30, 140, 0));
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.yellow);
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            FontMetrics metrika = getFontMetrics(g.getFont());
            g.drawString("Bodovi: " + krugPojedeno, (SCREEN_WIDTH - metrika.stringWidth("Bodovi: " + krugPojedeno))/2, SCREEN_HEIGHT/1);
        } else {
            igraGotova(g);
        }
    }

    /**
     * Kretanje zmije po X,Y kordinatama.
     */
    public void kretanje() {
        for(int i = velicinaZmije;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(smjer){
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
        }

    }

    /**
     * Generiranje novog kruga
     */
    public void noviKrug(){
        /* krug nastaje po x kordinati u granicama SCREEN_WIDTH i UNIT_SIZE */
        krugX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        /* krug nastaje po y kordinat u granicama SCREEN_HEIGHT i UNIT_SIZE*/
        krugY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    /**
     *  Provjera da li smo pojeli krug x[0] i y[0] pokazuju na glavu zmije.
     *  Ukoliko je krug pojeden, velicina zmije se povecava i brojac za pojedene krugove se povecava
     *  generira se novi krug na polju.
     */
    public void provjeraKrug(){
        if((x[0] == krugX) && (y[0] == krugY)){
            velicinaZmije++;
            krugPojedeno++;
            noviKrug();
        }
    }

    /**
     * Provjera da li se zmija zabila u u granice okvira ili tijelo
     */
    public void provjeraSudar(){
        /* provjera da li smo se zabili u lijevu granicu */
        if(x[0] < 0) {
            pokrenuto = false;
        }
        /* provjera da li smo se zabili u desnu granicu */
        if(x[0] > SCREEN_WIDTH) {
            pokrenuto = false;
        }
        /* provjera da li smo se zabili u gornju granicu */
        if(y[0] < 0) {
            pokrenuto = false;
        }
        /* provjera da li smo se zabili u donju granicu */
        if(y[0] > SCREEN_HEIGHT) {
            pokrenuto = false;
        }
        /* provjera da li smo se zabili u tijelo zmije */
        for(int i = velicinaZmije;i>0;i--){
            if((x[0] == x[i]) && (y[0] == y[i])) {
                pokrenuto = false;
            }
        }
        /* ako postane false, timer prestaje*/
        if(!pokrenuto){
            timer.stop();
        }
    }

    /**
     *
     * @param g
     * Prima Graphics object.
     * Prikazuje kada je igra gotova.
     */
    public void igraGotova(Graphics g){
        /* Prikaz teksta */
        g.setColor(Color.yellow);
        g.setFont(new Font("Consolas", Font.BOLD, 40));
        FontMetrics metrika = getFontMetrics(g.getFont());
        g.drawString("IGRA GOTOVA", (SCREEN_WIDTH - metrika.stringWidth("IGRA GOTOVA"))/2, SCREEN_HEIGHT/2);

        /* Prikaz bodova */
        g.setColor(Color.yellow);
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        FontMetrics metrikaB = getFontMetrics(g.getFont());
        g.drawString("Bodovi: " + krugPojedeno, (SCREEN_WIDTH - metrikaB.stringWidth("Bodovi: " + krugPojedeno))/2, SCREEN_HEIGHT/1);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(pokrenuto){
            kretanje();
            provjeraKrug();
            provjeraSudar();
        }
        repaint();
    }

    /**
     *  Registriranje tipki za kretanje zmije.
     *  Moguće je igrati sa tipkama WASD ili strelicama
     */
    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                if(smjer != 'R') {
                     smjer = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(smjer != 'L') {
                    smjer = 'R';
                }
                break;
            case KeyEvent.VK_UP:
                if(smjer != 'D') {
                    smjer = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if(smjer != 'U') {
                    smjer = 'D';
                }
                break;
            case KeyEvent.VK_A:
                if(smjer != 'R') {
                    smjer = 'L';
                }
                break;
            case KeyEvent.VK_D:
                if(smjer != 'L') {
                    smjer = 'R';
                }
                break;
            case KeyEvent.VK_W:
                if(smjer != 'D') {
                    smjer = 'U';
                }
                break;
            case KeyEvent.VK_S:
                if(smjer != 'U') {
                    smjer = 'D';
                }
                break;
        }
        }
    }
}
