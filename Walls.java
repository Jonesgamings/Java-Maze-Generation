public class Walls
{
    int up;
    int down;
    int left;
    int right;

    public Walls(int up, int down, int left, int right)
    {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    public void Break(WallType type)
    {
        if (type.equals(WallType.UP))
            up = 0;

        if (type.equals(WallType.DOWN))
            down = 0;

        if (type.equals(WallType.LEFT))
            left = 0;

        if (type.equals(WallType.RIGHT))
            right = 0;
    }

    public int get(WallType type)
    {
        if (type.equals(WallType.UP))
            return up;

        if (type.equals(WallType.DOWN))
            return down;

        if (type.equals(WallType.LEFT))
            return left;

        if (type.equals(WallType.RIGHT))
            return right;

        else
            return -1;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + up;
        result = prime * result + down;
        result = prime * result + left;
        result = prime * result + right;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Walls other = (Walls) obj;
        if (up != other.up)
            return false;
        if (down != other.down)
            return false;
        if (left != other.left)
            return false;
        if (right != other.right)
            return false;

        return true;
    }

    @Override
    public String toString()
    {
        return up + ", " + down + ", " + left  + ", " + right;
    }

}
