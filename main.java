import java.util.*;

// Class to represent an edge in the graph
class Edge implements Comparable<Edge> {
    int source, destination, weight;

    public Edge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge compareEdge) {
        return this.weight - compareEdge.weight;
    }
}

// Class to represent a subset for Union-Find
class Subset {
    int parent, rank;
}

public class MinimumCostSpanningTree {
    int vertices, edges;
    Edge[] edge;

    // Constructor
    MinimumCostSpanningTree(int vertices, int edges) {
        this.vertices = vertices;
        this.edges = edges;
        edge = new Edge[edges];
        for (int i = 0; i < edges; ++i) {
            edge[i] = new Edge(0, 0, 0);
        }
    }

    // Find set of an element i (uses path compression)
    int find(Subset[] subsets, int i) {
        if (subsets[i].parent != i) {
            subsets[i].parent = find(subsets, subsets[i].parent);
        }
        return subsets[i].parent;
    }

    // Union of two sets x and y (uses union by rank)
    void union(Subset[] subsets, int x, int y) {
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);

        if (subsets[xroot].rank < subsets[yroot].rank) {
            subsets[xroot].parent = yroot;
        } else if (subsets[xroot].rank > subsets[yroot].rank) {
            subsets[yroot].parent = xroot;
        } else {
            subsets[yroot].parent = xroot;
            subsets[xroot].rank++;
        }
    }

    // Main function to construct MST using Kruskal's algorithm
    void kruskalMST() {
        Edge[] result = new Edge[vertices];
        int e = 0; // Result edges
        int i = 0; // Sorted edges
        for (i = 0; i < vertices; ++i) {
            result[i] = new Edge(0, 0, 0);
        }

        // Step 1: Sort all edges by weight
        Arrays.sort(edge);

        // Allocate memory for creating V subsets
        Subset[] subsets = new Subset[vertices];
        for (i = 0; i < vertices; ++i) {
            subsets[i] = new Subset();
            subsets[i].parent = i;
            subsets[i].rank = 0;
        }

        i = 0; // Initial index of sorted edges

        // Number of edges to be taken is V-1
        while (e < vertices - 1) {
            Edge nextEdge = edge[i++];
            int x = find(subsets, nextEdge.source);
            int y = find(subsets, nextEdge.destination);

            // If including this edge doesn't cause cycle, include it
            if (x != y) {
                result[e++] = nextEdge;
                union(subsets, x, y);
            }
        }

        // Print the result
        System.out.println("Edges in Minimum Cost Spanning Tree:");
        int minimumCost = 0;
        for (i = 0; i < e; ++i) {
            System.out.println(result[i].source + " -- " + result[i].destination + " == " + result[i].weight);
            minimumCost += result[i].weight;
        }
        System.out.println("Minimum Wiring Cost = " + minimumCost);
    }

    // Main Method
    public static void main(String[] args) {
        int vertices = 4; // Number of cities
        int edges = 5;    // Number of possible connections
        MinimumCostSpanningTree graph = new MinimumCostSpanningTree(vertices, edges);

        // Add edges (source, destination, weight)
        graph.edge[0] = new Edge(0, 1, 10);
        graph.edge[1] = new Edge(0, 2, 6);
        graph.edge[2] = new Edge(0, 3, 5);
        graph.edge[3] = new Edge(1, 3, 15);
        graph.edge[4] = new Edge(2, 3, 4);

        graph.kruskalMST();
    }
}