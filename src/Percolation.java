import java.util.ArrayList;
import java.util.List;

class Coordinates {
    public final int row;
    public final int col;

    Coordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }
}

public class Percolation {
    private int[][] parents_grid;
    private int[][] opennes_grid;
    private final int bottom_root = -1;
    private final int top_root = 0;
    private int n;
    private boolean percolates = false;


    public void print() {
        String[] patterns = new String[]{"| %d |", "< %d >"};
        for (int row = 1; row <= n; row++) {
            for (int col = 1; col <= n; col++) {
                System.out.print(
                        String.format(
                                patterns[opennes_grid[row-1][col-1]], getValueAt(row, col)
                        )
                );
            }
            System.out.println();
        }
    }


    public Percolation(int n) { //create n by n grid
        this.n = n;
        opennes_grid = new int[n][n];
        parents_grid = new int[n][n];
        populate_grid();
    }

    private void populate_grid() {
        for (int row = 1; row <= n; row++) {
            for (int col = 1; col <= n; col++) {
                setValueAt(row, col, row * n + col);
            }
        }
    }

    private void checkCoords(int x, int y) {
        if (x < 1 || x > n || y < 1 || y > n) {
            throw new RuntimeException(String.format("The value for coordinates should be between %d and %d", 1, n));
        }
    }


    private int getValueAt(int row, int col) {
        checkCoords(row, col);
        return this.parents_grid[row - 1][col - 1];
    }


    private void setValueAt(int row, int col, int value) {
        checkCoords(row, col);
        this.parents_grid[row - 1][col - 1] = value;
    }


    public void open(int row, int col) {
        checkCoords(row, col);
        if (isOpen(row, col)) {
            return;
        }
        if (row == 1) {
            setValueAt(row, col, top_root);
        }
        if (row == n) {
            setValueAt(row, col, bottom_root);
        }
        opennes_grid[row-1][col-1] = 1;

        List<Coordinates> neighbours = getNeighbours(row, col);
        for(Coordinates neighbour : neighbours){
            open(neighbour.row, neighbour.col);
        }

    }


    private List<Coordinates> getNeighbours(int row, int col) {
        List<Coordinates> coordinates = new ArrayList<>();
        for (int difference = -1; difference < 1; difference += 2) {
            for (int difference2 = -1; difference2 < 1; difference2 += 2) {
                row = row + difference;
                col = col + difference2;
                if (row > 0 && row <= n && col > 0 && col <= n) {
                    coordinates.add(new Coordinates(row, col));
                }
            }
        }
        return coordinates;
    }


    public boolean isOpen(int row, int col) {  // is site (row, col) open?
        return opennes_grid[row-1][ col-1] == 1;
    }

    public boolean isFull(int row, int col) {  // is site (row, col) full?
        return false;
    }

    public int numberOfOpenSites() {      // number of open sites
        return 0;
    }

    public boolean percolates() {             // does the system percolate?
        return false;
    }

    public static void main(String[] args) {  // test client (optional)
        Percolation percolation = new Percolation(6);
        percolation.open(1, 3);
        percolation.open(5, 5);
        percolation.open(6, 4);
//        percolation.open(0, 0);
//        percolation.open(11, 11);
        percolation.print();
    }


}
