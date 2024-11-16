import java.util.Scanner;
public class QueryProcessing {
	private m_invIndex inv_index;

	public QueryProcessing(m_invIndex inv_index) {
		this.inv_index = inv_index;
	}

	// Here AA and BB represents the two words given in query
	// O(m*n) where m is size of docListA and n is size of docListB
	public LinkedList<Integer> AND(String aa, String bb){
		//Searching using BST faster than the inverted index
		LinkedList<Integer> docListA = inv_index.search(aa);
		LinkedList<Integer> docListB = inv_index.search(bb);
		LinkedList<Integer> result = new LinkedList<Integer>();
		// As null && anything == null !! Domination Law in Math 151
		if(docListA == null || docListB == null)
			return new LinkedList<>();
		docListA.findFirst();
		docListB.findFirst();
		while(!docListA.last()) {
			int a = docListA.retrieve();
			docListB.findFirst();
			while(!docListB.last()) {
				int b = docListB.retrieve();
				if(a == b) {
					result.insert(a);
					break;
				}
			}
			docListA.findNext();
		}
		if(docListA.retrieve() == docListB.retrieve())
			result.insert(docListA.retrieve());
		return result;
	}
	// Also O(m*n) as the loops and nested loops depend on size
	// of docListA and docListB respectivly !
	public LinkedList<Integer> OR(String aa, String bb){
		LinkedList<Integer> docListA = inv_index.search(aa);
		LinkedList<Integer> docListB = inv_index.search(bb);
		LinkedList<Integer> result = new LinkedList<Integer>();
		// Null or Null == Null , Null or A == A , A or Null == A
		if(docListA == null && docListB == null) 
			return new LinkedList<>();
		else if(docListA == null)
			return docListB;
		else if(docListB == null)
			return docListA;
		docListA.findFirst();
		docListB.findFirst();
		while(!docListA.last()) {
			result.insert(docListA.retrieve());
			docListA.findNext();
		}
		result.insert(docListA.retrieve());
		boolean duplicate = false;
		while(!docListB.last()) {
			//Check for duplicates !!
			int a = docListB.retrieve();
			duplicate = false;
			result.findFirst();
			while(!result.last()) {
				if(result.retrieve() == a) {
					duplicate = true;
					break;
				}
				result.findNext();
			}
			if(!duplicate)
				result.insert(a);
			docListB.findNext();
		}
		
		result.findFirst();
		duplicate = false;
		while(!result.last()) {
			if(result.retrieve() == docListB.retrieve()) {
				duplicate = true;
				break;
			}
			result.findNext();
		}
		if(!duplicate)
			result.insert(docListB.retrieve());
		return result;
	}
	
	// We can implement a sorting algorithm to sort the resulting LinkedList !
	public static void main(String [] args) {
		Scanner inp = new Scanner(System.in);
		m_invIndex m = new m_invIndex();
		QueryProcessing qp = new QueryProcessing(m);
		m.Load_all_file("C:\\Users\\alz7\\Desktop\\stop.txt", "C:\\Users\\alz7\\Desktop\\dataset.csv");
		System.out.print("Please enter word 1 and word 2: ");
		String a = inp.next();
		String b = inp.next();
		LinkedList<Integer> andList = qp.AND(a, b);
		LinkedList<Integer> orList = qp.OR(a, b);
		System.out.print("AND: ");
		andList.display();
		System.out.print("\nOR: ");
		orList.display();
		inp.close();
	}
}
