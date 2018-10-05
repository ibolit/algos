public class Percolation {
    private int[] parentsGrid;
    private int[] opennessGrid;

    private final int topRoot = -1;
    private final int bottomRoot = -2;
    private final int size;
    private boolean percolates = false;
    private final int[] offsets;
    private int numOfOpenSites = 0;


    public Percolation(int size) {
        this.size = size;
        int length = size * size;

        parentsGrid = new int[length];
        opennessGrid = new int[length];

        for (int i = 0; i < length; i++) {
            parentsGrid[i] = i;
            opennessGrid[i] = 0;
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
        return valueAt(row, col, opennessGrid) == 1;
    }


    public boolean isFull(int row, int col) {
        return !isOpen(row, col);
    }


    public int numberOfOpenSites(){
        return numOfOpenSites;
    }


    public boolean percolates() {
        return this.percolates;
    }

    public void open(int row, int col) {
        if(isOpen(row, col)) {
            return;
        }
        int index = translateCoords(row, col);

        if (row == 1) {
            parentsGrid[index] = topRoot;
        }
        if (row == size) {
            parentsGrid[index] = bottomRoot;
        }

        opennessGrid[index] = 1;
        numOfOpenSites++;

        int[] neighbours = getNeighbours(index);
        checkPercolates(neighbours);
        int minParent = min(parentsGrid[index], minParent(neighbours));
        parentsGrid[index] = minParent;

        for(int i = 0; i < parentsGrid.length; i++) {
            for(int parent : neighbours) {
                if(parent < 0) {
                    break;
                }
                if(parentsGrid[i] == parent) {
                    parentsGrid[i] = minParent;
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
            if(i >= 0 && parentsGrid[i] < 0) {
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
        int minParent = parentsGrid.length;
        for (int i : indexes) {
            if (i >= 0) {
                minParent = min(parentsGrid[i], minParent);
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
                if (modulo(index % size - candidate % size ) != 1 || candidate < 0 || candidate > parentsGrid.length) {
                    continue;
                }
            } else if(candidate < 0 || candidate > parentsGrid.length) {
                continue;
            }
            if(opennessGrid[candidate] == 1) {
                ret[neighbourIndex] = candidate;
            }
            neighbourIndex++;
         }
         for(int i = neighbourIndex; i < ret.length; i++) {
             ret[neighbourIndex] = -1;
         }
         return ret;
    }

    public static void main(String[] args) {  // test client (optional)
        Percolation percolation = new Percolation(5);
        percolation.open(1, 3);
        percolation.open(5, 3);
        percolation.open(4, 3);
        percolation.open(2, 3);
        percolation.open(3, 3);
    }
}
