import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationStats {
    private int size;
    private int trials;
    private int[] openSiteNumbers;
    private double mean;
    private double stddev;
    private double confidenceFactor;


    public PercolationStats(int n, int trials) {
        size = n;
        this.trials = trials;
        openSiteNumbers = new int[trials];

        for(int i = 0; i < this.trials; i++) {
            Percolation percolation = new Percolation(size);
            while(!percolation.percolates()) {
                int row = StdRandom.uniform(size - 1) + 1;
                int col = StdRandom.uniform(size - 1) + 1;
                percolation.open(row, col);
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

    public double mean() {
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

    public double stddev() {
        return stddev;
    }

    private void setConfidenceFactor() {
        confidenceFactor = 1.96 * (stddev() / Math.sqrt(trials));
    }


    public double confidenceLo() {
        return mean() - confidenceFactor;
    }



    public double confidenceHi() {
        return mean() + confidenceFactor;
    }



    public static void main(String[] args) {
        if(args.length > 2) {
            throw new RuntimeException("Too many arguments");
        }
        int[] size_n_trials = new int[2];
        for(int i = 0; i < 2; i++) {
            size_n_trials[i] = Integer.parseInt(args[i]);
        }

        PercolationStats ps = new PercolationStats(size_n_trials[0], size_n_trials[1]);
        System.out.println(String.format("mean                    = %d", ps.mean()));
        System.out.println(String.format("stddev                  = %d", ps.stddev()));
        System.out.println(String.format("95% confidence interval = [%d, %d]", ps.confidenceLo(), ps.confidenceHi()));

        // test client (described below)
    }
}
