import java.util.*;

public class Trie<V>{


    private static final int BRANCH_FACTOR = 26;


    private Node<V> root;


    public Node<V> getRoot() {
        return root;
    }


    private int size;


    public Trie() {
        root = new Node<>(null);
    }


    private static int convertToIndex(char c) {
        // we don't test for this, but I'm leaving it here
        if (c < 'a' || c > 'z') {
            throw new IllegalArgumentException("Character must be in the range [a..z]");
        }
        return c - 'a';
    }


    private static char convertToChar(int i) {
        if (i < 0 || i >= BRANCH_FACTOR) {
            throw new IllegalArgumentException("Index must be in the range [0..BRANCH_FACTOR]");
        }
        return (char) (i + 'a');
    }

    public int size() {
        return size;
    }


    public boolean isEmpty() {
        return size == 0;
    }

    public V put(CharSequence key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        //check not contain lowercase
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c < 'a' || c > 'z') {
                throw new IllegalArgumentException("Character must be in the range [a..z]");
            }
        }
        Node<V> currNode = getRoot();

        for (int i = 0; i < key.length(); i++) {
            char currChar = key.charAt(i);
            if (!currNode.hasChildren()) {
                currNode.initChildren();
            }
            if (currNode.getChild(currChar) == null) {
                currNode.setChild(currChar, new Node<V>(currNode));
            }
            currNode = currNode.getChild(currChar);
        }
        if (currNode.hasValue()) {
            V returner = currNode.getValue();
            currNode.setValue(value);
            return returner;
        }
        size++;
        currNode.setValue(value);
        return null;
    }


    public V get(CharSequence key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        //check not contain lowercase
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c < 'a' || c > 'z') {
                throw new IllegalArgumentException("Character must be in the range [a..z]");
            }
        }
        Node<V> currNode = root;

        for (int i = 0; i < key.length(); i++) {
            char currChar = key.charAt(i);
            if (!currNode.hasChildren()) {
                return null;
            }
            if (currNode.getChild(currChar) == null) {
                return null;
            }
            currNode = currNode.getChild(currChar);
        }
        return currNode.getValue();
    }

    public boolean containsKey(CharSequence key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        //check not contain lowercase
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c < 'a' || c > 'z') {
                throw new IllegalArgumentException("Character must be in the range [a..z]");
            }
        }
        Node<V> currNode = root;

        for (int i = 0; i < key.length(); i++) {
            char currChar = key.charAt(i);
            if (!currNode.hasChildren()) {
                return false;
            }
            if (currNode.getChild(currChar) == null) {
                return false;
            }
            currNode = currNode.getChild(currChar);
        }
        return currNode.hasValue();
    }
    boolean containsValueHelper(Node<V> node, Object value) {
        if (node == null) {
            return false;
        }
        if (node.getValue() != null) {
            if (node.getValue().equals(value)) {
                return true;
            }
        }
        if (node.getChildren() != null) {
            for (Node<V> child : node.getChildren()) {
                if (containsValueHelper(child, value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsValue(Object value) {
        if (value == null) {
            throw new IllegalArgumentException();
        } else {
            return containsValueHelper(root, value);
        }
    }
    public V remove(CharSequence key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        //check not contain lowercase
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c < 'a' || c > 'z') {
                throw new IllegalArgumentException("Character must be in the range [a..z]");
            }
        }
        if (key.equals("")) {
            if (containsKey("")) {
                V returner = root.value;
                root.value = null;
                size--;
                return returner;
            }
        }
        Node<V> currNode = root;

        for (int i = 0; i < key.length(); i++) {
            char currChar = key.charAt(i);
            if (!currNode.hasChildren()) {
                return null;
            }
            if (currNode.getChild(currChar) == null) {
                return null;
            }

            currNode = currNode.getChild(currChar);
        }
        if (currNode.getValue() == null) {
            return null;
        }
        V returner = currNode.getValue();
        size--;
        if (!currNode.hasChildren()) {
            //now we know that entry exists, want to traverse to the highest parent w/ only 1 child
            while (!currNode.parent.hasMultipleChildren() && currNode.parent.value == null) {
                currNode = currNode.parent;
                if (currNode == root) {
                    clear();
                    return returner;
                }
            }
            //now should be at the highest
            Node<V> parentNode = currNode.parent;
            //me when im at the most inefficient competition and my opponent is me:
            //still constant time tho
            for (int i = 0; i < 26; i++) {
                if (parentNode.getChildren()[i] != null) {
                    if (parentNode.getChildren()[i].equals(currNode)) {
                        parentNode.getChildren()[i] = null;
                        return returner;
                    }
                }
            }

        }
        currNode.setValue(null);

        return returner;

    }

    public void clear() {
        root = new Node<>(null);
        size = 0;
    }


    public int countPrefixes(CharSequence prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        //check not contain lowercase
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (c < 'a' || c > 'z') {
                throw new IllegalArgumentException("Character must be in the range [a..z]");
            }
        }
        Node<V> currNode = root;
        for (int i = 0; i < prefix.length(); i++) {
            char currChar = prefix.charAt(i);
            if (!currNode.hasChildren()) {
                return 0;
            }
            if (currNode.getChild(currChar) == null) {
                return 0;
            }
            currNode = currNode.getChild(currChar);
        }


        return countPrefixHelper(currNode);

    }
    int countPrefixHelper(Node<V> node) {
        int returner = 0;
        if (node.hasValue()) {
            returner++;
        }
        if (node.hasChildren()) {
            for (int i = 0; i < 26; i++) {
                if (node.getChildren()[i] != null) {
                    returner = returner + countPrefixHelper(node.getChildren()[i]);
                }
            }
        }
        return returner;
    }


    public List<V> allValuesWithPrefix(CharSequence prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        //check not contain lowercase
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (c < 'a' || c > 'z') {
                throw new IllegalArgumentException("Character must be in the range [a..z]");
            }
        }
        Node<V> currNode = root;
        LinkedList<V> returner = new LinkedList<>();
        for (int i = 0; i < prefix.length(); i++) {
            char currChar = prefix.charAt(i);
            if (!currNode.hasChildren()) {
                return returner;
            }
            if (currNode.getChild(currChar) == null) {
                return returner;
            }
            currNode = currNode.getChild(currChar);
        }


        getPrefixHelper(currNode, returner);
        return returner;
    }

    void getPrefixHelper(Node<V> currNode, LinkedList<V> list) {
        if (currNode.hasValue()) {
            list.addFirst(currNode.getValue());
        }
        if (currNode.hasChildren()) {
            for (int i = 0; i < 26; i++) {
                if (currNode.getChildren()[i] != null) {
                    getPrefixHelper(currNode.getChildren()[i], list);
                }
            }
        }
    }

    public Iterator<Map.Entry<CharSequence, V>> entryIterator() {
        throw new UnsupportedOperationException("TODO: implement");
    }


    static class Node<V> {

        private V value;
        private Node<V>[] children;
        private Node<V> parent;


        Node(Node<V> parent) {
            value = null;
            this.parent = parent;
        }
        boolean hasMultipleChildren() {
            if (hasChildren()) {
                for (int i = 0; i < 26; i++) {
                    if (getChildren()[i] != null) {
                        for (int j = i + 1; j < 26; j++) {
                            if (getChildren()[j] != null) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
            //top tier double forloop instead of returning a variable
            //also constant time technically bc im lazy in declaring another field :)
        }

        @SuppressWarnings("unchecked")
        public void initChildren() {
            this.children = new Node[26];
        }

        public Node<V>[] getChildren() {
            return children;
        }

        public boolean hasChildren() {
            return children != null && Arrays.stream(children).anyMatch(Objects::nonNull);
        }

        public Node<V> getChild(char c) {
            if (children == null) {
                return null;
            }
            return children[convertToIndex(c)];
        }


        public void setChild(char c, Node<V> node) {
            if (children == null) {
                initChildren();
            }
            children[convertToIndex(c)] = node;
        }


        public boolean hasValue() {
            return value != null;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}