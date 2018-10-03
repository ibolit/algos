public class Percolation {
    private static final int OPEN = 1;
    private static final int FULL = 0;


    private int[] gridTop;
    private int[] gridBottom;
    private int[] gridOpen;
    private final int[] offsets;

    private int openSites = 0;

    private final int length;
    private final int rootBottom = -1;
    private final int rootTop = -2;
    private int n;
    private boolean percolates = false;


    public Percolation(int n) { //create n by n grid
        this.length = n*n;
        this.n = n;
        gridTop = new int[length];
        gridBottom = new int[length];
        gridOpen = new int[length];

        offsets = new int[]{-n, -1, 1, n};

        populateGrids();
    }


    private void populateGrids() {
        for (int i = 1; i < length; i++) {
            gridBottom[i] = i;
            gridTop[i] = i;
            gridOpen[i] = FULL;
        }
    }


    private int convertCoords(int row, int col) {
        if(row > 0 && col > 0) {
            int index = (row - 1) * n + (col - 1);
            if(index < length) {
                return index;
            }
        }
        throw new IllegalArgumentException();
    }


    public void open(int row, int col) {
        int index = convertCoords(row, col);

        if (_isOpen(index)) {
            return;
        }

        openSites++;
        gridOpen[index] = OPEN;

        if (row == 1) {
            gridTop[index] = rootTop;
            int lowestNeighbouringParent = lowestNeighbouringParent(gridBottom, index);
            gridBottom[index] = getParent(gridBottom, lowestNeighbouringParent);
        } else if (row == n) {
            gridBottom[index] = rootBottom;
            int lowestNeighbouringParent = lowestNeighbouringParent(gridTop, index);
            gridTop[index] = getParent(gridTop, lowestNeighbouringParent);
        } else {
            int lowestNeighbouringParent = lowestNeighbouringParent(gridTop, index);
            gridTop[index] = getParent(gridTop, lowestNeighbouringParent);

            lowestNeighbouringParent = lowestNeighbouringParent(gridBottom, index);
            gridBottom[index] = getParent(gridBottom, lowestNeighbouringParent);
        }
        if(
                getParent(gridBottom, index) == rootBottom &&
                getParent(gridTop, index) == rootTop
        ) {
            percolates = true;
        }
    }


    private int lowestNeighbouringParent(int[] grid, int index) {
        int lowestParent = this.length;
        int indexDivision = index / this.n;

        for(int offset : offsets) {
            int candidate = index + offset;
            if(offset == -1 || offset == 1) {
                int candidateDivision = candidate / this.n;
                if(indexDivision != candidateDivision) {
                    // The candidate must be in the same row, but isn't
                    continue;
                }
            }
            if(candidate >= 0 && candidate < length) {
                if(grid[candidate] < lowestParent && gridOpen[candidate] == OPEN) {
                    lowestParent = grid[candidate];
                }
            }
        }
        if(lowestParent == length) {
            return index;
        }
        return lowestParent;
    }


    private int getParent(int[] grid, int index) {
        if(index < 0) {
            return index;
        }
        int parent = grid[index];
        if(parent == index || parent < 0) {
            return parent;
        }
        parent = getParent(grid, parent);
        grid[index] = parent;
        return parent;
    }


    private boolean _isOpen(int index) {
        return gridOpen[index] == OPEN;
    }


    public boolean isOpen(int row, int col) {  // is site (row, col) open?
        int index = convertCoords(row, col);
        return _isOpen(index);
    }


    public boolean isFull(int row, int col) {  // is site (row, col) full?
        return !isOpen(row, col);
    }


    public int numberOfOpenSites() {      // number of open sites
        return openSites;
    }


    public boolean percolates() {             // does the system percolate?
        return percolates;
    }

    // TODO We do need calls to Union on connecting the candidates;
    public static void main(String[] args) {  // test client (optional)
        Percolation percolation = new Percolation(5);
        percolation.open(2, 3); // 7
        percolation.open(4, 3); // 17
        percolation.open(3, 3); // 12
        percolation.open(1, 3); // 2
        percolation.open(5, 3); // 22

//        percolation.open(6, 4);

        System.out.println();
//        percolation.open(0, 0);
//        percolation.open(11, 11);
//        percolation.print();
    }


}
