
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class Graph {
    HashMap<Integer, Integer>[] adjacencyList;

    public Graph(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        adjacencyList = (HashMap<Integer, Integer>[]) new HashMap[n];
        for (int i = 0; i < n; i++) {
            adjacencyList[i] = new HashMap<Integer, Integer>();
        }
    }


    public int getSize() {
        return adjacencyList.length;
    }


    public boolean hasEdge(int u, int v) {
        if (u < 0 || u >= getSize()) {
            throw new IllegalArgumentException();
        }
        if (v < 0 || v >= getSize()) {
            throw new IllegalArgumentException();
        }
        return (adjacencyList[u].containsKey(v));
    }


    public int getWeight(int u, int v) {
        if (u < 0 || u >= getSize()) {
            throw new IllegalArgumentException();
        }
        if (v < 0 || v >= getSize()) {
            throw new IllegalArgumentException();
        }
        if (!hasEdge(u, v)) {
            throw new NoSuchElementException();
        }
        return (adjacencyList[u].get(v));
    }


    public boolean addEdge(int u, int v, int weight) {
        if (u < 0 || u >= getSize()) {
            throw new IllegalArgumentException();
        }
        if (v < 0 || v >= getSize()) {
            throw new IllegalArgumentException();
        }
        if (u == v) {
            throw new IllegalArgumentException();
        }
        if (hasEdge(u, v)) {
            return false;
        } else {
            adjacencyList[u].put(v, weight);
            return true;
        }
    }

    public Set<Integer> outNeighbors(int v) {
        if (v < 0 || v >= getSize()) {
            throw new IllegalArgumentException();
        }
        HashSet<Integer> returner = new HashSet<>();
        returner.addAll(adjacencyList[v].keySet());
        return returner;
    }
}