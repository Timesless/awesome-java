package com.leetcode;

import java.util.Map;
import java.util.TreeMap;

/**
 * @Date: 2020/2/14
 * @Desc: 
 * insert(" apple ", 3), 输出: Null
 * 输入: sum("ap"), 输出: 3
 * 输入: insert("app", 2), 输出: Null
 * 输入: sum("ap"), 输出: 5
 **/
public class MapSum {
    
    static class Node {
        int val;
        Map<Character, Node> next;
        public Node() { this.next = new TreeMap<>(); }
    }

    private Node root;
    public MapSum() { this.root = new Node(); }
    
    public void insert(String key, int val) {
        Node p = root;
        for (int i = 0; i < key.length(); ++i) {
            char c = key.charAt(i);
            if (p.next.get(c) == null) 
                p.next.put(c, new Node());
            p = p.next.get(c);
        }
        p.val = val;
    }
    
    public int sum(String prefix) {
        Node p = root;
        for (int i = 0; i < prefix.length(); ++i) {
            char c = prefix.charAt(i);
            if (p.next.get(c) == null)
                return 0;
            p = p.next.get(c);
        }
        return sum(p);
    }

    private int sum(Node node) {
        int result = node.val;
        // 
        for (Character c : node.next.keySet()) 
            result += sum(node.next.get(c));
        return result;
    }
}