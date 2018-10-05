import java.util.ArrayList;
import java.util.List;

public class Grid {
    private int[] parents_grid;
    private int[] openness_grid;

    private final int top_root = -1;
    private final int bottom_root = -2;
    private final int size;
    private boolean percolates = false;
    private final int[] offsets;
    private int numOfOpenSites = 0;


    public Grid(int size) {
        this.size = size;
        int length = size * size;

        parents_grid = new int[length];
        openness_grid = new int[length];

        for (int i = 0; i < length; i++) {
            parents_grid[i] = i;
            openness_grid[i] = 0;
        }
        offsets = new int[]{-size, -1, 1, size};
    }

    private int translateCoords(int row, int col) {
        checkCoords(row, col);
        return (row - 1) * size + col - 1;
    }


    private void checkCoords(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new java.lang.IllegalArgumentException(
                    String.format("The value for coordinates should be between %d and %d", 1, size)
            );
        }
    }

    private int valueAt(int row, int col, int[] grid) {
        return grid[translateCoords(row, col)];
    }


    private void setValueAt(int row, int col, int value, int[] grid) {
        grid[translateCoords(row, col)] = value;
    }


    public boolean isOpen(int row, int col) {
        return valueAt(row, col, openness_grid) == 1;
    }


    public boolean isFull(int row, int col) {
        return !isOpen(row, col);
    }


    public int numberOfOpenSites(){
        return numOfOpenSites;
    }

    public void open(int row, int col) {
        if(isOpen(row, col)) {
            return;
        }
        int index = translateCoords(row, col);

        if (row == 1) {
            parents_grid[index] = top_root;
        }
        if (row == size) {
            parents_grid[index] = bottom_root;
        }

        openness_grid[index] = 1;
        numOfOpenSites++;

        int[] neighbours = getNeighbours(index);
        checkPercolates(neighbours);
        int minParent = min(parents_grid[index], minParent(neighbours));
        parents_grid[index] = minParent;

        for(int i = 0; i < parents_grid.length; i++) {
            for(int parent : neighbours) {
                if(parent < 0) {
                    break;
                }
                if(parents_grid[i] == parent) {
                    parents_grid[i] = minParent;
                }
            }
        }
    }

    private void checkPercolates(int[] neighbours) {
        if(percolates) {
            return;
        }
        int counter = 0;
        for(int i : neighbours) {
            if(i < 0) {
                return;
            }
            if(i >= 0 && parents_grid[i] < 0) {
                counter++;
            }
        }
        if(counter == 2) {
            percolates = true;
            return;
        }
    }


    int min(int a, int b) {
        if(a < b) {
            return a;
        }
        return b;
    }


    private int minParent(int[] indexes) {
        int minParent = parents_grid.length;
        for (int i : indexes) {
            if (i >= 0) {
                minParent = min(parents_grid[i], minParent);
            }
        }
        return minParent;
    }


    private int modulo(int x) {
        if (x < 0) {
            return -x;
        }
        return x;
    }


    private int[] getNeighbours(int index) {
        int[] ret =new  int[offsets.length];
        int neighbourIndex = 0;
         for(int i = 0; i < offsets.length; i++) {
            int candidate = index + offsets[i];
            if (modulo(offsets[i]) == 1) {
                if (modulo(index % size - candidate % size ) != 1 || candidate < 0 || candidate > parents_grid.length) {
//                    ret[i] = -1;
                    continue;
                }
            } else if(candidate < 0 || candidate > parents_grid.length) {
//                ret[i] = -1;
                continue;
            }
            if(openness_grid[candidate] == 1) {
                ret[neighbourIndex] = candidate;
            } else {
//                ret[i] = -1;
            }
            neighbourIndex++;
         }
         for(int i = neighbourIndex; i < ret.length; i++) {
             ret[neighbourIndex] = -1;
         }
         return ret;
    }

    public static void main(String[] args) {  // test client (optional)
        Grid grid = new Grid(5);
        grid.open(1, 3);
        grid.open(5, 3);
        grid.open(4, 3);
        grid.open(2, 3);
        grid.open(3, 3);
    }
}
