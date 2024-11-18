import java.util.Scanner;
public class QueryProcessing {
	private m_invIndex inv_index;
	// AND Has a Logic Mistake !! To Be Fixed Later ...
	public QueryProcessing(m_invIndex inv_index) {
		this.inv_index = inv_index;
	}
	// NOTE: I made the AND(String,String) .. to have an easier
	// grasp of the method, and easier testing, and even easier
	// Performance Analysis and finding the Big-Oh of the methods !
	// Here AA and BB represents the two words given in query
	// O(m*n) where m is size of docListA and n is size of docListB
	public LinkedList<Integer> AND(String aa, String bb){
		//Searching using BST faster than the inverted index
		LinkedList<Integer> docListA = inv_index.search(aa);
		LinkedList<Integer> docListB = inv_index.search(bb);
		LinkedList<Integer> result = new LinkedList<Integer>();
		// As null && anything == null !! Domination Law in Math 151
		if(docListA == null || docListB == null)
			return new LinkedList<Integer>();
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
			return new LinkedList<Integer>();
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

	//Here we also have the same complexity as the previous methods
	//O(m*n) where m denotes size of docListAll and n for docListInput
	//For these three methods the Big-Oh is considering the worst case only!!
	public LinkedList<Integer> NOT(String aa){
		LinkedList<Integer> result = new LinkedList<Integer>();
		LinkedList<Integer> docListInput = inv_index.search(aa);
		LinkedList<Document> docListAll = inv_index.index1.all_doc;
		docListAll.findFirst();
		if(docListInput == null) {
			while(!docListAll.last()) {
				result.insert(docListAll.retrieve().id);
				docListAll.findNext();
			}
			result.insert(docListAll.retrieve().id);
			return result;
		}
		else {
			if(docListAll.last())
				return result;
			while(!docListAll.last()) {
				boolean duplicate = false;
				docListInput.findFirst();
				while(!docListInput.last()) {
					if(docListInput.retrieve() == docListAll.retrieve().id) {
						duplicate = true;
						break;
					}
					docListInput.findNext();
				}
				if(!duplicate)
					result.insert(docListAll.retrieve().id);
				docListAll.findNext();
			}
			if(docListInput.retrieve() != docListAll.retrieve().id)
				result.insert(docListAll.retrieve().id);
		}
		return result;
	}

	//For Processing a query with compound statments we implement the previous
	//in this method which will tokenize the string and evaluate them according
	//to rules of precedence (MATH 151) + We will use Stack !!
	public LinkedList<Integer> eval(String query){
		//Starting with tokenization process
		query = query.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
		String[] tokens = query.split("\\s+");
		//Stacks to evaluate the logical operations
		Stack<LinkedList<Integer>> operands = new Stack<LinkedList<Integer>>();
		Stack<String> opStack = new Stack<String>();
		for(String token : tokens) {
			if(token.equals("and") || token.equals("or") || token.equals("not")) {
				while(!opStack.empty() && precedence(token, opStack.retrieve())) {
					String op = opStack.pop();
					LinkedList<Integer> op2 = operands.pop();
					LinkedList<Integer> op1 = operands.pop();
					LinkedList<Integer> result = applyOp(op,op1,op2);
					operands.push(result);
				}
				opStack.push(token);
			}
			else {
				LinkedList<Integer> docList = inv_index.search(token);
				if(docList == null)
					operands.push(new LinkedList<Integer>());
				else
					operands.push(docList);
			}
		}
		while(!opStack.empty()) {
			String op = opStack.pop();
			LinkedList<Integer> op2 = operands.pop();
			LinkedList<Integer> op1 = null;
			if(operands.empty())
				op1 = new LinkedList<Integer>();
			else
				op1 = operands.pop();
			LinkedList<Integer> result = applyOp(op,op1,op2);
			operands.push(result);
		}
		return operands.pop(); // Final Result !!!

	}
	//To avoid repition in code we implement these five methods
	private boolean precedence(String current,String previous) {
		//Knowing that NOT>AND>OR in the precedence rules
		switch(current) {
		case "not" : return true; 
		case "and" : if(previous.equals("and") || previous.equals("or")) {return true;} else { return false;}
		case "or" : return false;
		default : return false; 
		}
	}

	private LinkedList<Integer> applyOp(String operator, LinkedList<Integer> op1, LinkedList<Integer> op2){
		switch(operator) {
		case "and" : return AND(op1,op2);
		case "or" : return OR(op1,op2);
		case "not" : return NOT(op1);
		default : return new LinkedList<Integer>();
		}
	}
	// precedence and applyOp clearly have a O(1) complexity
	//Now we overload the AND , OR , NOT to accept LinkedList<Integer> 
	//They have same complexity as the String parameter ! O(m*n)
	//Now we are dealing with pure sets so we can implement mathematical methods
	private LinkedList<Integer> AND(LinkedList<Integer> aa, LinkedList<Integer> bb){
		LinkedList<Integer> result = new LinkedList<Integer>();
		aa.findFirst();
		while(!aa.last()) {
			bb.findFirst();
			while(!bb.last()) {
				if(aa.retrieve() == bb.retrieve()) {
					result.insert(aa.retrieve());
					break;
				}
				bb.findNext();
			}
			aa.findNext();
		}
		if(aa.retrieve() == bb.retrieve())
			result.insert(aa.retrieve());
		return result;
	}

	private LinkedList<Integer> OR(LinkedList<Integer> aa, LinkedList<Integer> bb){
		LinkedList<Integer> result = new LinkedList<Integer>();
		aa.findFirst();
		while(!aa.last()) {
			result.insert(aa.retrieve());
			aa.findNext();
		}
		result.insert(aa.retrieve());
		bb.findFirst();
		while(!bb.last()) {
			boolean duplicate = false;
			result.findFirst();
			while(!result.last()) {
				if(result.retrieve() == bb.retrieve()) {
					duplicate = true;
					break;
				}
				result.findNext();
			}
			if(!duplicate)
				result.insert(bb.retrieve());
			bb.findNext();
		}
		boolean duplicate = false;
		result.findFirst();
		while(!result.last()) {
			if(result.retrieve() == bb.retrieve()) {
				duplicate = true;
				break;
			}
			result.findNext();
		}
		if(!duplicate)
			result.insert(bb.retrieve());
		return result;
	}

	private LinkedList<Integer> NOT(LinkedList<Integer> aa){
		LinkedList<Integer> result = new LinkedList<Integer>();
		LinkedList<Document> docListAll = inv_index.index1.all_doc;

		docListAll.findFirst();
		while (!docListAll.last()) {
			boolean found = false;
			aa.findFirst();
			while (!aa.last()) {
				if (aa.retrieve() == docListAll.retrieve().id) {
					found = true;
					break;
				}
				aa.findNext();
			}
			if (!found) {
				result.insert(docListAll.retrieve().id);
			}
			docListAll.findNext();
		}
		boolean found = false;
		aa.findFirst();
		while (!aa.last()) {
			if (aa.retrieve() == docListAll.retrieve().id) {
				found = true;
				break;
			}
			aa.findNext();
		}
		if (!found) {
			result.insert(docListAll.retrieve().id);
		}

		return result;
	}

	// We could implement a sorting algorithm to sort the resulting LinkedList's !
	// But considering the sorting alone could harm the time complexity.
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
		LinkedList<Integer> notList = qp.NOT(a);
		System.out.print("AND: ");
		andList.display();
		System.out.print("\nOR: ");
		orList.display();
		System.out.print("\nNOT (Word 1): ");
		notList.display();
		System.out.println("\n----------------");
		//Testing the Query Search !!
		System.out.print("Please enter your query: ");
		String input = inp.nextLine();
		input = inp.nextLine();
		LinkedList<Integer> queryResult = qp.eval(input);
		System.out.print("Result: ");
		queryResult.display();
		inp.close();
	}
}
