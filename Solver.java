/**
 * Homework Assignment #6: "8-Puzzle"
 *
 *  - Solver class for solving "8-Puzzle" Programming Assignment
 *
 *  Compilation:  javac Solver.java Board.java
 *  Execution:    java Solver inputfile.txt
 *  Dependencies: MinPQ
 *
 * @ Student ID : 20163228
 * @ Name       : Yusoon Nam
 **/

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Solver {

    private boolean solved;
    private Node solution;
    
    /*********************************
     * YOU CAN ADD MORE HERE
     *********************************/    
    private MinPQ<Node> solPQ;
    private MinPQ<Node> solTwinPQ;
    private Node minNode;
    private Node minNodeTwin;

    //private LinkedList<Board> history = new LinkedList<Board>();
    
    // search node
    private class Node implements Comparable<Node> {
        private Board board;
        private int moves;
        private Node prev;

        public Node(Board board, int moves, Node prev) {
            if (board == null)
                throw new java.lang.NullPointerException();
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }

        // calculate distance of this search node
        public int distance() {

        	// my added code 
        	return board.manhattan();
        }

        // calculate priority of this search node
        public int priority() {

        	// my added code
            return distance() + moves;
        }

        // compare node by priority (implements Comparable<Node>)
        public int compareTo(Node that) {

        	// my added code
            return (this.priority() - that.priority());
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new java.lang.NullPointerException();

        solved = false;
        solution = null;

        // added code	
        // create initial search node (and it's twin)
        minNode = new Node(initial, 0, null);
        minNodeTwin = new Node(initial.twin(), 0, null);
     
        
        // priority queue
        solPQ = new MinPQ<Node>();
        solTwinPQ = new MinPQ<Node>();
        
        // insert the initial search node into a priority queue
        solPQ.insert(minNode);
        solTwinPQ.insert(minNodeTwin);
        
        System.out.println("\n===Solving===\n");
        
        
        // solve the puzzle   
        int it = 1;
        // Iterate until either of the solution reaches the Goal
        while (!(solPQ.min().board.isGoal()) && !(solTwinPQ.min().board.isGoal())) {
        	System.out.println(it++);
        	// dequeue the minimum priority Node
        	minNode = solPQ.delMin();
        	minNodeTwin = solTwinPQ.delMin();
        	
        	//System.out.println("++Min Board\n"+minNode.priority()+"\n"+minNode.board);
        	//System.out.println("+Enqueued Board");
        	
        	// insert if neighbor board is not same with MinNode's previous board
        	for (Board board : minNode.board.neighbors()) {
        		//int priority = board.manhattan()+minNode.moves+1;
        		if (minNode.prev == null) {
        			solPQ.insert(new Node(board, minNode.moves+1, minNode));
        			//System.out.println(priority+"\n"+board);
        		}
        		else if (!board.equals(minNode.prev.board)) {
        			//if (isNotDuplicated(board)) {
    					solPQ.insert(new Node(board, minNode.moves+1, minNode));
            			//System.out.println(priority+"\n"+board);
    				//}
        		}
        		//System.out.println(board);
        	}

        	for (Board boardTwin : minNodeTwin.board.neighbors()) {	// for Twin
        		if (minNodeTwin.prev == null) {
        			solTwinPQ.insert(new Node(boardTwin, minNodeTwin.moves+1, minNodeTwin));
        		}
        		else if (!boardTwin.equals(minNodeTwin.prev.board)) {
        			solTwinPQ.insert(new Node(boardTwin, minNodeTwin.moves+1, minNodeTwin));
        		}
        	}
        }
        
        
        
        // determine solved
        if ((solPQ.min().board.isGoal()) && !(solTwinPQ.min().board.isGoal())) {
        	solved = true;
        	solution = solPQ.min();
        }
        else {
        	solved = false;
        	solution = null;
        }
        System.out.println("\n===Finished Solving===\n");
    }
    
    private boolean isNotDuplicated(Board rboard) {
    	
    	Board hisBoard;
    	for (Node hisNode : boardIter()) {
    		hisBoard = hisNode.board;
    		if (rboard.equals(hisBoard)) {
    			System.out.println("Duplicated");
    			return false;
    		}
    	}
    	return true;
    }
    
    private Iterable<Node> boardIter() {
    	return solPQ;
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
    	
    	// my added code
    	return solved;
    }
    
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
    	// my added code
    	
    	if (isSolvable())
    		return solution.moves;
    	else
    		return -1;
    	
    }

    // sequence of boards in a intest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solution == null)
            return null;
        Stack<Board> sol = new Stack<Board>();
        Node searchNode = solution;
        while (searchNode != null) {
            sol.push(searchNode.board);
            searchNode = searchNode.prev;
        }
        return sol;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // to calculate running time
        long start = System.currentTimeMillis();
        double time;

        // read the input file
        Scanner in;
        String filename = args[0];
        try {
            in = new Scanner(new File(filename), "UTF-8");
        } catch (Exception e) {
            System.out.println("invalid or no input file ");
            return;
        }

        // create initial board from file
        int N = in.nextInt();
        int[][] blocks = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = in.nextInt();
                if (blocks[i][j] >= N*N)
                    throw new IllegalArgumentException("value must be < N^2");
                if (blocks[i][j] < 0)
                    throw new IllegalArgumentException("value must be >= 0");
            }
        }

        // initial board
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            System.out.println("No solution possible");
        else {
            System.out.println("Minimum number of moves = " + solver.moves() + "\n");
            for (Board board : solver.solution())
                System.out.println(board);
        }
        //System.out.println(solver.solPQ.size());

        // calculate running time
        time = (System.currentTimeMillis() - start) / 1000.0;
        System.out.println("time = "+ time + "sec");
        System.out.println("Minimum number of moves = " + solver.moves() + "\n");
        
        in.close();
    }
}

