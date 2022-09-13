import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    // top, bottom = virtual top and virtual bottom
    private int top = 0;
    private int bottom;
    private int size;
    private int openSiteCount = 0;
    private WeightedQuickUnionUF uf;

    // create n-n grid, all sites initially blocked (false)
    public Percolation(int n)
    {
        if (n<=0)
        {
            throw new IllegalArgumentException();
        }
        size = n;
        bottom = size*size +1;
        uf = new WeightedQuickUnionUF(size*size+2);
        grid = new boolean[size+1][size+1];
    }

    // get index of the site in Union Find based on row and col
    private int getUFIndex(int row, int col)
    {


        return size * (row-1) + col;
    }
    // open a site at (row, col)
    public void open(int row, int col) {
         if (row < 1 || row >size || col<1 || col> size) {
            throw new IllegalArgumentException();
        }
        // set value open true
        // row because grid array starts at 0, while UF[0] is top and start grid at 1
        grid[row][col] = true;
        openSiteCount += 1;

        // row  = 0 meaning top row, open by union with virtual top site
        if (row == 1) {

            uf.union(getUFIndex(row, col), top);
        }

        // row = size-1 (n-1) meaning bottom row, open by union with virtual bottom site
        if (row == size) {

            uf.union(getUFIndex(row,col), bottom);
        }

        // neither top or bottom, try to union with north, east, south, and west

        // north
        if (row > 1 && isOpen(row-1, col)) {

            uf.union(getUFIndex(row,col), getUFIndex(row-1, col));
        }
        // south
        if (row<size && isOpen(row+1, col)) {

            uf.union(getUFIndex(row,col), getUFIndex(row+1, col));
        }
        // west
        if (col > 1 && isOpen(row, col-1)) {
            uf.union(getUFIndex(row, col), getUFIndex(row,col-1));
        }
        // east
        if (col<size && isOpen(row, col+1)) {
            uf.union(getUFIndex(row,col), getUFIndex(row,col+1));
        }

    }

    // is this site open (row, col)
    public boolean isOpen(int row, int col) {
         if (row < 1 || row >size || col<1 || col> size) {
            throw new IllegalArgumentException();
        }
        return grid[row][col];
    }

    // is this site (row, col) full
    public boolean isFull(int row, int col) {
         if (row < 1 || row >size || col<1 || col> size) {
            throw new IllegalArgumentException();
        }
        return uf.find(top) == uf.find(getUFIndex(row,col));
    }

    // return number of open sites
    public int numberOfOpenSites()
    {
        return openSiteCount;
    }

    // does the system percolate
    public boolean percolates()
    {
        return uf.find(top) == uf.find(bottom);
    }

    // test client
    public static void main(String[] args) {
        int n = 30;
        Percolation percolation = new Percolation(n);

        int opened = 0;
        while (!percolation.percolates())
        {
            int randomRow = StdRandom.uniformInt(1, n+1);
            int randomCol = StdRandom.uniformInt(1, n+1);

            if (percolation.isOpen(randomRow,randomCol))
            {

                continue;
            }

            percolation.open(randomRow,randomCol);
            opened++;
        }
        boolean[][] ar = percolation.grid;
       /* for (int i = 0; i<n; i++)
        {
            for (int j = 0; j<n; j++)
            {
                System.out.print(ar[i][j] + " ");
            }
            System.out.println("");
        } */
        System.out.println("Size = " + n + " x " + n + " with " + percolation.numberOfOpenSites() + " with p = "
                + (double)percolation.numberOfOpenSites() / (n*n));
    }
}
