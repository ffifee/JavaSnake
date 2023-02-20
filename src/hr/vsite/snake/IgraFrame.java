package hr.vsite.snake;

import javax.swing.*;

/**
 * Okvir igre (Java programa)
 */
public class IgraFrame extends JFrame {
    IgraFrame(){
    IgraPanel panel = new IgraPanel();
    this.add(panel);
    this.setTitle("Zmija");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.pack();
    this.setVisible(true);
    this.setLocationRelativeTo(null);
    }
}
