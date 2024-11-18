class BSTNode<T> {
    public String key;
    public T data;
    public BSTNode<T> left, right;

    public BSTNode(String key, T data) {
        this.key = key;
        this.data = data;
        left = right = null;
    }
}

public class BST<T> {
    private BSTNode<T> root, current;

    public BST() {
        current = root = null;
    }
    public boolean empty() {
        return root == null;
    }

    public boolean full() {
        return false;
    }

    public T retrieve() {
        return current.data;
    }
    public boolean findKey(String A) {
        BSTNode<T> p = root;
        while (p != null) {
            current = p;
            if (A.compareToIgnoreCase(p.key) == 0) {
                return true;
            } else if (A.compareToIgnoreCase(p.key) < 0) {
                p = p.left;
            } else {
                p = p.right;
            }
        }
        return false;
    }
    public boolean insert(String k, T val) {
        if (root == null) {
            current = root = new BSTNode<T>(k, val);
            return true;
        }

        BSTNode<T> p = current;
        if (findKey(k)) {
            current = p;
            return false;
        }

        BSTNode<T> tmp = new BSTNode<T>(k, val);
        if (k.compareToIgnoreCase(current.key) < 0) {
            current.left = tmp;
        } else {
            current.right = tmp;
        }
        current = tmp;
        return true;
    }


    public void inOrder() {
        if (root == null)
            System.out.println("empty tree");
        else
            inOrder(root);
    }

    private void inOrder(BSTNode<T> p) {
        if (p == null) return;
        inOrder(p.left);
        ((Word) p.data).display();
        inOrder(p.right);
    }

    public void preOrder() {
        if (root == null)
            System.out.println("empty tree");
        else
            preOrder(root);
    }
    private void preOrder(BSTNode<T> p) {
        if (p == null) return;
        ((Word) p.data).display();

        preOrder(p.left);
        preOrder(p.right);
    }
   
}