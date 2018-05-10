/**
 * Homework Assignment #6: "8-Puzzle"
 *
 *  - Board class for solving "8-Puzzle" Programming Assignment
 *
 *  Compilation:  javac Board.java Queue.java
 *
 * @ Student ID : 20163228
 * @ Name       : Yusoon Nam
 **/

import java.io.File;
import java.util.Scanner;

public class Board {

    private int[][] tiles;
    private int N;
    /*********************************
     * YOU CAN ADD MORE HERE
     *********************************/

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null)
            throw new java.lang.NullPointerException();

        N = blocks.length;
        if (N < 2 || N > 128)
            throw new IllegalArgumentException("N must be <= 128");

        tiles = new int[N][N];
        for (int i = 0; i < N; i++)
            System.arraycopy(blocks[i], 0, tiles[i], 0, blocks[i].length);
    }

    // board dimension N
    public int dimension() {
        return N;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattan = 0;
        
        int row = 0, col = 0;
        int num;
        
        // my added code
        for (int i = 0; i < N; i++) {
        	for (int j = 0; j < N; j++) {
        		if (tiles[i][j] != 0) {
        			num = tiles[i][j];
        			
        			if ((num % N) != 0) {
        				row = Math.abs((num / N) - i);
        				col = Math.abs((num % N) - (j + 1));
        			}
        			else {	// if num % N == 0
        				row = Math.abs((num / N) - 1 - i);
        				col = Math.abs( N - 1 - j);
        			}
        			
        			manhattan = manhattan + row + col;
        			//System.out.println(row + " " + col + " " + manhattan);
        		}
        	}
        }
        
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {

    // my added code
    	
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < N; j++) {
    			if ((i != N-1) || (j != N-1)) {
    				if (tiles[i][j] != i*N+j+1)
    					return false;
    			}
    		}
    	}
    	
        return true;
    }

    private void swap(int[][] blocks, int r1, int c1, int r2, int c2) {
        if (r1 < 0 || c1 < 0 || r2 < 0 || c2 < 0)
            throw new IndexOutOfBoundsException("row/col index < 0");
        if (r1 >= N || c1 >= N || r2 >= N || c2 >= N)
            throw new IndexOutOfBoundsException("row/col index >= N");

        // swap blocks
        
        // my added code
        int temp;
        temp = blocks[r1][c1];
        blocks[r1][c1] = blocks[r2][c2];
        blocks[r2][c2] = temp;
     
    }

    // a board that is obtained by exchanging two adjacent blocks in the same row
    public Board twin() {
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            System.arraycopy(tiles[i], 0, blocks[i], 0, tiles[i].length);
        
        // my added code
        if ((blocks[0][0] != 0) && (blocks[0][1] != 0))
        	swap(blocks, 0, 0, 0, 1);
        else
        	swap(blocks, 1, 0, 1, 1);
      
        return new Board(blocks);
    }

    // does this board equal y?
    public boolean equals(Object y) {

    	// my added code
    	Board yBoard = (Board)y;
    	
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < N; j++) {
    				if (this.tiles[i][j] != yBoard.tiles[i][j])
    					return false;
    		}
    	}
    	return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        Queue<Board> nbrs = new Queue<Board>();

        // put all neighbor boards into the queue

        // my added code
        int[][] nbrBlocks = new int[N][N];
        int r = 0, c = 0;
        int flag = 0;
        
        // find coordination of 0
        for (int i = 0; i < N; i++) {
        	for (int j = 0; j < N; j++) {
        		if (tiles[i][j] == 0) {
        			r = i;
        			c = j;
        			flag = 1;
        			break;
        		}
        	}
        	if (flag == 1)
        		break;
        }
        
        // check boundary and make neighbor
        if (r - 1 >= 0) {	// up
        	for (int i = 0; i < N; i++)
                System.arraycopy(tiles[i], 0, nbrBlocks[i], 0, tiles[i].length);
        	swap(nbrBlocks, r, c, r-1, c);
        	nbrs.enqueue(new Board(nbrBlocks));
        }
        if (r + 1 < N) {	// down
        	for (int i = 0; i < N; i++)
                System.arraycopy(tiles[i], 0, nbrBlocks[i], 0, tiles[i].length);
        	swap(nbrBlocks, r, c, r+1, c);
        	nbrs.enqueue(new Board(nbrBlocks));
        }
        
        if (c - 1 >= 0) {	// left
        	for (int i = 0; i < N; i++)
                System.arraycopy(tiles[i], 0, nbrBlocks[i], 0, tiles[i].length);
        	swap(nbrBlocks, r, c, r, c-1);
        	nbrs.enqueue(new Board(nbrBlocks));
        }
        if (c + 1 < N) {	// right
        	for (int i = 0; i < N; i++)
                System.arraycopy(tiles[i], 0, nbrBlocks[i], 0, tiles[i].length);
        	swap(nbrBlocks, r, c, r, c+1);
        	nbrs.enqueue(new Board(nbrBlocks));
        }
        

        return nbrs;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (DO NOT MODIFY)
    public static void main(String[] args) {
        // read the input file
        Scanner in;
        try {
            in = new Scanner(new File(args[0]), "UTF-8");
        } catch (Exception e) {
            System.out.println("invalid or no input file ");
            return;
        }

        // create initial board from file
        int N = in.nextInt();
        int[][] blocks = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = (int)in.nextInt();
                if (blocks[i][j] >= N*N)
                    throw new IllegalArgumentException("value must be < N^2");
                if (blocks[i][j] < 0)
                    throw new IllegalArgumentException("value must be >= 0");
            }
        }

        Board initial = new Board(blocks);

        System.out.println("\n=== Initial board ===");
        System.out.println(" - manhattan = " + initial.manhattan());
        System.out.println(initial.toString());

        Board twin = initial.twin();

        System.out.println("\n=== Twin board ===");
        System.out.println(" - manhattan = " + twin.manhattan());
        System.out.println(" - equals = " + initial.equals(twin));
        System.out.println(twin.toString());

        System.out.println("\n=== Neighbors ===");
        for (Board board : initial.neighbors())
            System.out.println(board);
        
        in.close();
    }
}

