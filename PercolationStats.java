import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationStats {
    private double[] prob;
    private int size;
    private int T;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        size = n;
        T = trials;
        prob = new double[trials];
        if (n<=0 || trials<=0) {
            throw new IllegalArgumentException("<=0");
        }
        // run trials
        for (int countExp = 0; countExp<trials; countExp+=1){
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()){
                int randomRow = StdRandom.uniformInt(1,n+1);
                int randomCol = StdRandom.uniformInt(1,n+1);

                // if not open, then open
                if (!percolation.isOpen(randomRow,randomCol)) {
                    percolation.open(randomRow,randomCol);
                }
            }
            double probability = (double) percolation.numberOfOpenSites()/(n*n);
            prob[countExp] = probability;
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(prob);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(prob);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean() + 1.96* stddev() / Math.sqrt(T);
    }

    // test client (see below)
    public static void main(String[] args){
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(N, T);
        StdOut.println("mean                = " + percolationStats.mean());
        StdOut.println("stddev              = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }

}