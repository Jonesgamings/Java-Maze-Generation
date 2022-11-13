import java.awt.*;
import java.util.Scanner;;
import javax.swing.*;

public class Screen extends JFrame
{
    int width;
    int height;
    int offset;
    Maze maze;

    public Screen(int width, int height, int offset, Maze maze)
    {
        this.width = width;
        this.height = height;
        this.offset = offset;
        this.maze = maze;
        this.maze.setGridSize(this.width, this.height, this.offset);

        this.setTitle("Maze");
        this.setResizable(false);
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        for (Integer[] linePos : this.maze.getWalls())
            g2.drawLine(linePos[0], linePos[1], linePos[2], linePos[3]);
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input Maze Size: ");
        int mazeSize = scanner.nextInt();

        Maze maze = new Maze(mazeSize, mazeSize);
        maze.generateMaze();
        Screen screen = new Screen(1350, 1350, 5, maze);
    }
}
