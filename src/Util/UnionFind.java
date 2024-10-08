package Util;

import java.util.HashMap;

public class UnionFind<T> {
    private HashMap<T, T> parent;
    private HashMap<T, Integer> rank;
    
    public UnionFind() {
        parent = new HashMap<>();
        rank = new HashMap<>();
    }
    
    public void add(T item) {
        if (!parent.containsKey(item)) {
            parent.put(item, item);
            rank.put(item, 0);
        }
    }
    
    public T find(T item) {
        if (!parent.containsKey(item)) {
            throw new IllegalArgumentException("Item not found");
        }
        if (!parent.get(item).equals(item)) {
            parent.put(item, find(parent.get(item))); // Path compression
        }
        return parent.get(item);
    }
    
    public void union(T item1, T item2) {
        T root1 = find(item1);
        T root2 = find(item2);
        
        if (!root1.equals(root2)) {
            int rank1 = rank.get(root1);
            int rank2 = rank.get(root2);
            
            if (rank1 > rank2) {
                parent.put(root2, root1);
            } else if (rank1 < rank2) {
                parent.put(root1, root2);
            } else {
                parent.put(root2, root1);
                rank.put(root1, rank1 + 1);
            }
        }
    }
    
    public boolean connected(T item1, T item2) {
        return find(item1).equals(find(item2));
    }
}
