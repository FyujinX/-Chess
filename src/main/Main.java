package main;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Create the main window (JFrame)
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.black); // Set background color to black
        frame.setLayout(new GridBagLayout()); // Use GridBagLayout for flexible layout
        frame.setMinimumSize(new Dimension(1200, 800)); // Set the minimum size of the window
        frame.setLocationRelativeTo(null); // Center the window on the screen

        // Create the game board and add it to the window
        Board board = new Board();
        frame.add(board);

        // Make the window visible
        frame.setVisible(true);
    }
}
