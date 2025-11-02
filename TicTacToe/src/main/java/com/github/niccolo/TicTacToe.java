package com.github.niccolo;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import java.io.InputStream;

public class TicTacToe implements ActionListener {
    Random random = new Random();
    JFrame frame = new JFrame("Tris");
    JPanel pannelloTitolo = new JPanel();
    JPanel pannelloBottoni = new JPanel();
    JLabel casellaTesto = new JLabel();
    JButton[] caselle = new JButton[9];
    boolean player1_turn;
    JButton nuovaPartita = new JButton("Nuova Partita");
    boolean giocoFinito = false;

    TicTacToe() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.BLACK);

        try {
            java.net.URL iconURL = getClass().getResource("/photos/iconaTris.png");
            if (iconURL == null) throw new Exception("Icona non trovata nel classpath!");
            Image icon = Toolkit.getDefaultToolkit().getImage(iconURL);
            frame.setIconImage(icon);
        } catch (Exception e) {
            System.out.println("Impossibile caricare l'icona personalizzata.");
        }

        casellaTesto.setBackground(Color.BLACK);
        casellaTesto.setForeground(Color.WHITE);
        casellaTesto.setHorizontalAlignment(JLabel.CENTER);
        casellaTesto.setOpaque(true);
        try (InputStream fontStream = getClass().getResourceAsStream("/fonts/ProstoOne-Regular.ttf")) {
            if (fontStream == null) throw new Exception("Font non trovato nel classpath!");
            Font prostoOne = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 30f);
            casellaTesto.setFont(prostoOne);
        } catch (Exception e) {
            e.printStackTrace();
            casellaTesto.setFont(new Font("SansSerif", Font.BOLD, 30));
        }
        casellaTesto.setText("Tris");

        pannelloTitolo.setLayout(new BorderLayout());
        pannelloTitolo.add(casellaTesto, BorderLayout.CENTER);
        frame.add(pannelloTitolo, BorderLayout.NORTH);

        pannelloBottoni.setLayout(new GridLayout(3, 3));
        pannelloBottoni.setBackground(Color.BLUE);

        Font fontXO;
        try (InputStream fontStreamXO = getClass().getResourceAsStream("/fonts/ProstoOne-Regular.ttf")) {
            if (fontStreamXO == null) throw new Exception("Font X/O non trovato nel classpath!");
            fontXO = Font.createFont(Font.TRUETYPE_FONT, fontStreamXO).deriveFont(Font.BOLD, 120f);
        } catch (Exception e) {
            fontXO = new Font("Arial", Font.BOLD, 120);
            System.out.println("Impossibile caricare il font personalizzato per X e O.");
        }

        for (int i = 0; i < 9; i++) {
            caselle[i] = new JButton();
            caselle[i].setFont(fontXO);
            caselle[i].setFocusable(false);
            caselle[i].setBackground(new Color(117, 241, 231));
            caselle[i].setForeground(Color.DARK_GRAY);
            caselle[i].addActionListener(this);
            pannelloBottoni.add(caselle[i]);
        }
        frame.add(pannelloBottoni, BorderLayout.CENTER);

        nuovaPartita.setVisible(false);
        nuovaPartita.setFocusable(false);
        nuovaPartita.setFont(new Font("SansSerif", Font.BOLD, 30)); // più grande
        nuovaPartita.addActionListener(e -> reset());
        frame.add(nuovaPartita, BorderLayout.SOUTH);

        frame.setVisible(true);

        primoTurno();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (giocoFinito) return;

        for (int i = 0; i < 9; i++) {
            if (e.getSource() == caselle[i] && caselle[i].getText().equals("")) {
                if (player1_turn) {
                    caselle[i].setForeground(Color.DARK_GRAY);
                    caselle[i].setText("X");
                    player1_turn = false;
                    casellaTesto.setText("Tocca a O");
                } else {
                    caselle[i].setForeground(Color.WHITE);
                    caselle[i].setText("O");
                    player1_turn = true;
                    casellaTesto.setText("Tocca a X");
                }
                controlla();
            }
        }
    }

    public void primoTurno() {
        if (random.nextInt(2) == 0) {
            player1_turn = true;
            casellaTesto.setText("Tocca a X");
        } else {
            player1_turn = false;
            casellaTesto.setText("Tocca a O");
        }
    }

    public void controlla() {
        int[][] combinazioni = {
                {0,1,2}, {3,4,5}, {6,7,8}, // righe
                {0,3,6}, {1,4,7}, {2,5,8}, // colonne
                {0,4,8}, {2,4,6}           // diagonali
        };

        boolean vincitore = false;

        for (int[] combo : combinazioni) {
            String a = caselle[combo[0]].getText();
            String b = caselle[combo[1]].getText();
            String c = caselle[combo[2]].getText();

            if (!a.equals("") && a.equals(b) && b.equals(c)) {
                vincitore = true;
                if (a.equals("X")) xVince(combo[0], combo[1], combo[2]);
                else oVince(combo[0], combo[1], combo[2]);
            }
        }

        boolean pieno = true;
        for (JButton btn : caselle) {
            if (btn.getText().equals("")) {
                pieno = false;
                break;
            }
        }

        if (!vincitore && pieno) {
            giocoFinito = true;
            casellaTesto.setText("Pareggio!");
            for (JButton btn : caselle) btn.setEnabled(false);
            nuovaPartita.setVisible(true);
        }
    }

    public void xVince(int a, int b, int c) {
        giocoFinito = true;
        caselle[a].setForeground(Color.BLACK);
        caselle[b].setForeground(Color.BLACK);
        caselle[c].setForeground(Color.BLACK);

        for (int i = 0; i < caselle.length; i++) {
            if (i != a && i != b && i != c) {
                caselle[i].setForeground(Color.DARK_GRAY);
            }
        }

        caselle[a].setBackground(Color.GREEN);
        caselle[b].setBackground(Color.GREEN);
        caselle[c].setBackground(Color.GREEN);

        for (JButton btn : caselle) btn.setEnabled(false);
        casellaTesto.setText("X vince!");
        nuovaPartita.setVisible(true);
    }

    public void oVince(int a, int b, int c) {
        giocoFinito = true;
        caselle[a].setForeground(Color.BLACK);
        caselle[b].setForeground(Color.BLACK);
        caselle[c].setForeground(Color.BLACK);

        for (int i = 0; i < caselle.length; i++) {
            if (i != a && i != b && i != c) {
                caselle[i].setForeground(Color.DARK_GRAY);
            }
        }

        caselle[a].setBackground(Color.GREEN);
        caselle[b].setBackground(Color.GREEN);
        caselle[c].setBackground(Color.GREEN);

        for (JButton btn : caselle) btn.setEnabled(false);
        casellaTesto.setText("O vince!");
        nuovaPartita.setVisible(true);
    }

    public void reset() {
        giocoFinito = false;
        for (JButton btn : caselle) {
            btn.setText("");
            btn.setEnabled(true);
            btn.setBackground(new Color(117, 241, 231));
            btn.setForeground(Color.DARK_GRAY);
        }
        nuovaPartita.setVisible(false);
        primoTurno();
    }
}