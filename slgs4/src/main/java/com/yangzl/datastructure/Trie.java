package com.yangzl.datastructure;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangzl
 * @date 2020/2/15
 * 
 * 前缀树，字典树。 Trie 念 try
 */
public class Trie {
    /**
     * @date 2020/2/14
     * 这个节点类，真的是太沙雕了
     */
    static class Node {
        boolean isWord;
        char c;
        List<Node> next;
        public Node(char c) { this(false, c); }
        public Node(boolean isWord, char c) {
            this.isWord = isWord;
            this.c = c;
            this.next = new ArrayList<>(26);
        }
    }

    private final Node root;
    public Trie() { this.root = new Node((char) -1); }
    
    public void insert(String word) {
        Node p = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (p.next.stream().noneMatch(n -> n.c == c))
                p.next.add(new Node(c));
            p = p.next.stream().filter(n -> n.c == c).findFirst().get();
        }
        p.isWord = true;
    }
    
    public boolean search(String word) {
        Node p = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (p.next.stream().noneMatch(n -> n.c == c))
                return false;
            p = p.next.stream().filter(n -> n.c == c).findFirst().get();
        }
        return p.isWord;
    }

    public boolean startsWith(String prefix) {
        Node p = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (p.next.stream().noneMatch(n -> n.c == c))
                return false;
            p = p.next.stream().filter(n -> n.c == c).findFirst().get();
        }
        return true;
    }
    
    /*static class Node {
        boolean isWord;
        Map<Character, Node> next;
        public Node() { this(false); }
        public Node(boolean isWord) {
            this.isWord = isWord;
            this.next = new TreeMap<>();
        }
    }
    
    private Node root;
    public Trie() { this.root = new Node(); }

    public void insert(String word) {
        Node p = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!p.next.containsKey(c)) {
                p.next.put(c, new Node());
            }
            p = p.next.get(c);
        }
        p.isWord = true;
    }

    public boolean search(String word) {
        Node p = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (p.next.get(c) == null) 
                return false;
            p = p.next.get(c);
        }
        return p.isWord;
    }

    public boolean startsWith(String prefix) {
        Node p = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (p.next.get(c) == null)
                return false;
            p = p.next.get(c);
        }
        return true;
    }*/

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        trie.insert("jam");
        System.out.println(trie.search("apple"));
        System.out.println(trie.search("app"));
        System.out.println(trie.startsWith("app"));
        System.out.println(trie.search("jam"));
    }
}
