import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Maze {

    public int width;
    public int height;
    public int numCells;
    public int gridX;
    public int gridY;
    public int screenOffset;
    public Map<Pos, Walls> cells;
    public List<Pos> pathCells;
    public List<Pos> nonePathCells;

    public Maze(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.numCells = this.width * this.height;
        this.cells = new HashMap<Pos, Walls>();
        this.pathCells = new ArrayList<Pos>();
        this.nonePathCells = new ArrayList<Pos>();
        this.generateBlank();
    }

    public void setGridSize(int screenWidth, int screenHeight, int screenOffset)
    {
        this.gridX = (screenWidth - screenOffset) / this.width;
        this.gridY = (screenHeight - screenOffset) / this.height;
        this.screenOffset = screenOffset;
    }

    public void addToPath(Pos pos)
    {
        if (this.nonePathCells.contains(pos))
        {
            this.pathCells.add(pos);
            this.nonePathCells.remove(pos);
        }
    }

    public List<Pos> getNeighbours(Pos pos, boolean allowingPathCells)
    {
        List<Pos> neighbours = new ArrayList<Pos>();
        neighbours.add(new Pos(pos.x - 1, pos.y));
        neighbours.add(new Pos(pos.x + 1, pos.y));
        neighbours.add(new Pos(pos.x, pos.y - 1));
        neighbours.add(new Pos(pos.x, pos.y + 1));

        List<Pos> toReturn = new ArrayList<Pos>();
        for (Pos neighbour : neighbours)
        {
            Walls walls = this.cells.get(neighbour);
            if (walls != null)
            {
                if (!this.pathCells.contains(neighbour) || allowingPathCells)
                {
                    toReturn.add(neighbour);
                }
            }
        }
        return toReturn;
    }

    public void generateBlank()
    {
        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < this.width; x++)
            {
                Pos pos = new Pos(x, y);
                Walls walls = new Walls(1, 1, 1, 1);
                this.cells.put(pos, walls);
                this.nonePathCells.add(pos);
            }
        }
    }

    public void breakWalls(Pos start, Pos end)
    {
        if (this.cells.get(start) != null && this.cells.get(end) != null)
        {
            if (start.x > end.x) {
                this.cells.get(start).Break(WallType.LEFT);
                this.cells.get(end).Break(WallType.RIGHT);
            } else if (start.x < end.x) {
                this.cells.get(start).Break(WallType.RIGHT);
                this.cells.get(end).Break(WallType.LEFT);
            } else {
                if (start.y > end.y) {
                    this.cells.get(start).Break(WallType.UP);
                    this.cells.get(end).Break(WallType.DOWN);
                } else if (start.y < end.y) {
                    this.cells.get(start).Break(WallType.DOWN);
                    this.cells.get(end).Break(WallType.UP);
                }
            }
        }
        else
        {
            System.out.println(start.toString() + " ---> " + end.toString());
        }
    }

    public void fillNonePaths()
    {
        for (int i = 0; i < this.nonePathCells.size(); i++)
        {
            Pos pos = this.nonePathCells.get(i);
            if (pos != null)
            {
                this.generatePath(pos, false);
            }
        }
    }

    public void fillSingleCells()
    {
        for (Pos pos : this.cells.keySet())
        {
            if (pos != null)
            {
                Walls walls = this.cells.get(pos);
                if (walls.equals(new Walls(1, 1, 1, 1)))
                {
                    List<Pos> neighbours = this.getNeighbours(pos, true);
                    int index = new Random().nextInt(neighbours.size());
                    Pos neighbour = neighbours.get(index);
                    this.breakWalls(pos, neighbour);
                    this.addToPath(pos);
                }
            }
        }
    }

    public void addStartFinish()
    {
        List<Pos> starts = new ArrayList<>();
        List<Pos> finishes = new ArrayList<>();
        for (Pos pos : this.pathCells)
        {
            if (pos.x == 0)
                starts.add(pos);

            if (pos.x == this.height - 1)
                finishes.add(pos);
        }

        double minDist = this.width;
        double maxDist = 0;
        Pos closestStart = null;
        Pos furthestFinish = null;

        for (Pos start : starts)
        {
            double startDist = Math.sqrt((start.x ^ 2) + (start.y) ^ 2);
            if (startDist < minDist)
            {
                closestStart = start.clone();
                minDist = startDist;
            }
        }

        for (Pos finish : finishes)
        {
            double finishDist = Math.sqrt((finish.x ^ 2) + (finish.y) ^ 2);
            if (finishDist > maxDist)
            {
                furthestFinish = finish.clone();
                maxDist = finishDist;
            }
        }

        this.cells.get(closestStart).Break(WallType.UP);
        this.cells.get(furthestFinish).Break(WallType.DOWN);
    }

    public void generatePath(Pos start, boolean fillingEmpty)
    {
        Pos currentPos = start.clone();
        List<Pos> currentPath = new ArrayList<>();
        for (int i = 0; i < this.numCells; i++) {
            List<Pos> neighbours = this.getNeighbours(currentPos, false);
            if (neighbours.size() > 0)
            {
                int index = new Random().nextInt(neighbours.size());
                Pos neighbour = neighbours.get(index);
                this.breakWalls(currentPos, neighbour);
                this.addToPath(currentPos);
                currentPath.add(currentPos);
                currentPos = neighbour.clone();
            }
            else
            {
                if (fillingEmpty)
                {
                    neighbours = this.getNeighbours(currentPos, true);
                    for (Pos pos : neighbours) {
                        if (!currentPath.contains(pos))
                            this.breakWalls(currentPos, pos);
                    }
                }
                this.addToPath(currentPos);
                break;
            }
        }
    }

    public void generateMaze()
    {
        Pos start = new Pos(this.width / 2, this.height / 2);
        for (int i = 0; i < this.numCells; i++)
        {
            if (this.pathCells.size() == 0)
            {
                this.generatePath(start, false);
            }
            else
            {
                int index = new Random().nextInt(this.pathCells.size());
                Pos newStart = this.pathCells.get(index);
                this.generatePath(newStart, false);
            }
        }
        this.addStartFinish();
        this.fillNonePaths();
        this.fillSingleCells();
    }

    public List<Integer[]> getWalls()
    {
        List<Integer[]> walls = new ArrayList<>();
        for (Pos key : this.cells.keySet())
        {
            int x = (key.x * this.gridX) + this.screenOffset + 20;
            int y = (key.y * this.gridY) + this.screenOffset + 40;
            Walls wallSet = this.cells.get(key);

            if (wallSet.up == 1)
            {
                walls.add(new Integer[] {x, y, x + this.gridX, y});
            }
            if (wallSet.down == 1)
            {
                walls.add(new Integer[] {x, y + this.gridY, x + this.gridX, y + this.gridY});
            }
            if (wallSet.left == 1)
            {
                walls.add(new Integer[] {x, y, x, y + this.gridY});
            }
            if (wallSet.right == 1)
            {
                walls.add(new Integer[] {x + this.gridX, y, x + this.gridX, y + this.gridY});
            }
        }
        return walls;
    }
}
