public class PercolationStats {
    private int size;
    private int trials;
    private int[] openSiteNumbers;
    private double mean;
    private double stddev;
//    private double confidenceLo;
//    private double confidenceHi;
    private double confidenceFactor;


    public PercolationStats(int n, int trials) {
        size = n;
        this.trials = trials;
        openSiteNumbers = new int[trials];

        for(int i = 0; i < this.trials; i++) {
            Percolation percolation = new Percolation(size);
            while(!percolation.percolates()) {
                percolation.open(1, 1);
            }
            openSiteNumbers[i] = percolation.numberOfOpenSites();
        }

        mean = _mean();
        stddev = _stddev();
        setConfidenceFactor();
    }


    private double _mean() {
        int acc = 0;
        for (int num : openSiteNumbers) {
            acc += num;
        }
        return acc / trials;
    }

    private double mean() {
        return mean;
    }


    private double _stddev() {
        double mean = mean();
        double acc = 0.0;
        for (int num : openSiteNumbers) {
            acc += Math.pow(num - mean, 2);
        }
        return Math.sqrt(acc / (trials - 1));
    }

    private double stddev() {
        return stddev;
    }

    private void setConfidenceFactor() {
        confidenceFactor = 1.96 * (stddev() / Math.sqrt(trials));
    }


    private double confidenceLo() {
        return mean() - confidenceFactor;
    }



    public double confidenceHi() {
        return mean() + confidenceFactor;
    }



    public static void main(String[] args) {
        // test client (described below)
    }
}