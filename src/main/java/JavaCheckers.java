import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import checker.Box;
import checker.Coord;
import checker.Game;
import checker.Ranges;

public class JavaCheckers extends JFrame {
    private Game game;
    private JPanel panel;
    private final int COL = 9;
    private  final int ROW = 9;
    private final int IMAGE_SIZE = 70;

    public static void main(String[] args) {
        new JavaCheckers();
    }

    private JavaCheckers() {
        game = new Game(COL, ROW);
        game.start();
        setImages();
        initPanel();
        initFrame();
    }

    private void initPanel() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int y = 0; y < 9; y++) {
                    for (int x = 0; x < 9; x++) {
                        g.drawImage((Image) game.getBox(new Coord(x, y)).image,
                                x * IMAGE_SIZE, y * IMAGE_SIZE, this);
                    }
                }
                if (Game.finish() == 1) {
                    String str = "BLACK WINNER";
                    Font f = new Font("Arial", Font.BOLD, 50);
                    g.setColor(Color.BLUE);
                    g.setFont(f);
                    g.drawString(str, 150, 300);
                }
                if (Game.finish() == 2) {
                    String str = "WHITE WINNER";
                    Font f = new Font("Arial", Font.BOLD, 50);
                    g.setColor(Color.BLUE);
                    g.setFont(f);
                    g.drawString(str, 150, 300);
                }
            }
        };
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;
                Coord coord = new Coord(x, y);
                if (e.getButton() == MouseEvent.BUTTON1)
                    Game.pressLeftButton(coord);
                panel.repaint();
            }
        });
        panel.setPreferredSize(new Dimension(
                Ranges.getSize().x * IMAGE_SIZE,
                Ranges.getSize().y * IMAGE_SIZE));
        add(panel);
    }

    private void initFrame() {
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Russian Checkers");
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setIconImage(getImage("blackqueen"));
    }

    private void setImages() {
        for (Box box : Box.values())
            box.image = getImage(box.name().toLowerCase());
    }

    private Image getImage(String name) {
        String fileName = "img/" + name + ".jpg";
        ImageIcon icon = new ImageIcon(getClass().getResource(fileName));
        return icon.getImage();
    }

}
