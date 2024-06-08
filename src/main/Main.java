package main;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setMinimumSize(new Dimension(1000,1000));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Board board = new Board();

        frame.add(board);
        frame.setVisible(true);
    }
}