import java.util.ArrayList;
import java.util.List;

public class Grid {
    private int[][] parents_grid;
    private int[][] openness_grid;

    private final int top_root = -1;
    private final int bottom_root = -2;
    private final int size;
    private boolean percolates = false;


    public Grid(int size) {
        this.size = size;
        parents_grid = new int[size][size];
        openness_grid = new int[size][size];

        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                parents_grid[row][col] = row * this.size + col;
                openness_grid[row][col] = 0;
            }
        }
    }


    private void checkCoords(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new RuntimeException(
                    String.format("The value for coordinates should be between %d and %d", 1, size)
            );
        }
    }

    private int valueAt(int row, int col, int[][] grid) {
        checkCoords(row, col);
        return grid[row-1][col-1];
    }


    private void setValueAt(int row, int col, int value, int[][] grid) {
        checkCoords(row, col);
        grid[row-1][col-1] = value;
    }


    public boolean isOpen(int row, int col) {
        return valueAt(row, col, openness_grid) == 1;
    }


    public void open(int row, int col) {
        if(isOpen(row, col)) {
            return;
        }
        int r = row - 1;
        int c =col -1;

        if (row == 1) {
            parents_grid[r][c] = top_root;
        }
        if (row == size) {
            parents_grid[r][c] = bottom_root;
        }

        openness_grid[r][c] = 1;
        List<Coordinates> neighbours = getNeighbours(row, col);
        int min_parent = size * size + 2;
        int[] parents = new int[neighbours.size()];
        int i = 0;
        for(Coordinates neighbour : neighbours) {
            int parent = valueAt(neighbour.row, neighbour.col, parents_grid);
            if(
                    (parent == top_root && min_parent == bottom_root)
                    || (parent == bottom_root && min_parent == top_root)
            ) {
                percolates = true;
            }
            if(parent < min_parent) {
                min_parent = parent;
            }
            parents[i] = parent;
            i++;
        }
        parents_grid[r][c] = min_parent;
        for (int _row = 0; _row < this.size; _row++) {
            for (int _col = 0; _col < this.size; _col++) {
                for (int a_parent : parents) {
                    if (parents_grid[_row][_col] == a_parent) {
                        parents_grid[_row][_col] = min_parent;
                    }
                }
            }
        }
    }


    private List<Coordinates> getNeighbours(int row, int col) {
        List<Coordinates> coordinates = new ArrayList<>();
        for (int difference = -1; difference < 1; difference += 2) {
            for (int difference2 = -1; difference2 < 1; difference2 += 2) {
                row = row + difference;
                col = col + difference2;
                if (row > 0 && row <= size && col > 0 && col <= size) {
                    if(isOpen(row, col)) {
                        coordinates.add(new Coordinates(row, col));
                    }
                }
            }
        }
        return coordinates;
    }


}
