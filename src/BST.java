public class BST<T> {
	BSTNode<T> root,current;
	
	public BST() {
		root = current = null;
	}
	
	public boolean empty() {
		return root == null;
	}
	
	public boolean full() {
		return false;
	}
	
	public T retrieve () {
		return current.data;
	}
	//Findkey modified to support String Key's
	public boolean findkey(String tkey) {
		BSTNode<T> p = root, q = root;
				
		if(empty())
			return false;
		
		while(p != null) {
			q = p;
			int compare = tkey.compareTo(p.key);
			if(compare == 0) {
				current = p;
				return true;
			}
			else if(compare < 0)
				p = p.left;
			else
				p = p.right;
		}
		
		current = q;
		return false;
	}
	//Insert modified to support String Key's
	public boolean insert(String k, T val) {
		BSTNode<T> p, q = current;
		
		if(findkey(k)) {
			current = q;  
			return false; 
		}
		p = new BSTNode<T>(k, val);
		if (empty()) {
			root = current = p;
			return true;
		}
		else {
			int compare = k.compareTo(current.key);
			if (compare < 0)
				current.left = p;
			else
				current.right = p;
			current = p;
			return true;
		}
	}
	
	// We will not need the remove function !
//	public boolean remove_key (int tkey){
//		boolean removed = false;
//		BSTNode<T> p;
//		p = remove_aux(tkey, root, removed);
//		current = root = p;
//		return removed;
//	}
//	
//	private BSTNode<T> remove_aux(int key, BSTNode<T> p, boolean flag) {
//		BSTNode<T> q, child = null;
//		if(p == null)
//			return null;
//		if(key < p.key)
//			p.left = remove_aux(key, p.left, flag); 
//		else if(key > p.key)
//			p.right = remove_aux(key, p.right, flag); 
//		else { 
//			flag = true;
//			if (p.left != null && p.right != null){ 
//				q = find_min(p.right);
//				p.key = q.key;
//				p.data = q.data;
//				p.right = remove_aux(q.key, p.right, flag);
//			}
//			else {
//				if (p.right == null) //one child
//					child = p.left;
//				else if (p.left == null) //one child
//					child = p.right;
//				return child;
//			}
//		}
//		return p;
//	}
//	
//	private BSTNode<T> find_min(BSTNode<T> p){
//		if(p == null)
//			return null;
//		
//		while(p.left != null){
//			p = p.left;
//		}
//		
//		return p;
//	}
	
	// We will not need the Update Function
//	public boolean update(String key, T data){
//		remove_key(current.key);
//		return insert(key, data);
//	}
	
	// Using in-order display as it is sorted
	public void display() {
		displayrec(root);
	}
	private void displayrec(BSTNode<T> p) {
		if(p != null) {
			displayrec(p.left);
			System.out.print(p.key +", ");
			System.out.println(p.data);
			displayrec(p.right);
		}
	}
}
