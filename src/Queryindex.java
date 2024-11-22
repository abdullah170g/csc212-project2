
public class Queryindex {

	static Index inv;

	public Queryindex (Index inv) {
		this.inv = inv;
	}
	public static LinkedList<Integer> BooleanQuery(String Query) {
		if (!Query.contains("AND") && !Query.contains("OR"))
			return AndQuery(Query);
		else if (Query.contains("AND") && !Query.contains("OR"))
			return AndQuery(Query);
		else if (!Query.contains("AND") && Query.contains("OR"))
			return ORQuery(Query);
		else
			return MixedQuery(Query);
	}
	public static LinkedList<Integer> AndQuery(String Query) {
		LinkedList<Integer> one = new LinkedList<Integer>();
		LinkedList<Integer> two = new LinkedList<Integer>();
		String terms[] = Query.split("AND");

		if (terms.length == 0) return one;

		one = inv.get_all_docs(terms[0].trim().toLowerCase());
		for (int i = 1; i < terms.length; i++) {
			two = inv.get_all_docs(terms[i].trim().toLowerCase());
			one =AndQuery(one, two);
		}




		return one;
	}
	public static LinkedList<Integer> AndQuery(LinkedList<Integer> one, LinkedList<Integer> two) {
		LinkedList<Integer> result = new LinkedList<Integer>();

		if (one.empty() || two.empty()) {
			return result;
		}

		one.findFirst();
		while (true) {
			boolean found = search_result(result, one.retrieve());
			if (!found) { // not found in result
				two.findFirst();
				while (true) {
					if (two.retrieve().equals(one.retrieve())) {
						result.insert(one.retrieve());
						break;
					}
					if (!two.last()) {
						two.findNext();
					} else {
						break;
					}

				}// end inner while for two

			}	// end if not found
			if (!one.last()) {
				one.findNext();
			} else {
				break;
			}}

		return result;

	}
	public static LinkedList<Integer> ORQuery(String Query) {
		LinkedList<Integer> A = new LinkedList<Integer>();
		LinkedList<Integer> B = new LinkedList<Integer>();
		String terms[] = Query.split("OR");

		if (terms.length == 0) return A;
		A= inv.get_all_docs(terms[0].trim().toLowerCase());
		for (int i = 1; i < terms.length; i++) {
			B = inv.get_all_docs(terms[i].trim().toLowerCase());

			A = ORQuery(A, B);}



		return A;
	}

	public static LinkedList<Integer> ORQuery(LinkedList<Integer> A, LinkedList<Integer> B) {
		LinkedList<Integer> result = new LinkedList<Integer>();

		if (A.empty() && B.empty()) {
			return result;
		}

		A.findFirst();
		while (!A.empty()) {
			boolean found = search_result(result, A.retrieve());
			if (!found) { // not found in result
				result.insert(A.retrieve());
			}

			// end if
			if (!A.last()) {
				A.findNext();
			} else {
				break;
			}
		}

		B.findFirst();
		while (!B.empty()) {
			boolean found = search_result(result, B.retrieve());
			if (!found) { // not found in result
				result.insert(B.retrieve());
			}

			if (!B.last()) {
				B.findNext();
			} else {
				break;
			}
		}

		return result;
	}

	public static boolean search_result(LinkedList<Integer> result, Integer id) {
		if (result.empty()) return false;

		result.findFirst();
		while (!result.last()) {
			if (result.retrieve().equals(id)) {
				return true;
			}
			result.findNext();
		}

		if (result.retrieve().equals(id)) {
			return true;
		}

		return false;
	}
	public static LinkedList<Integer> MixedQuery(String Query) {
		LinkedList<Integer> A = new LinkedList<Integer>();
		LinkedList<Integer> B = new LinkedList<Integer>();

		if (Query.length() == 0) return A;

		String s[] = Query.split("OR");

		A = AndQuery(s[0]);
		for (int i = 1; i < s.length; i++) {
			B = AndQuery(s[i]);
			A = ORQuery(A, B);
		}

		return A;
	}
	public static LinkedList<Integer> notQuery(String Query, Index index1) {
		LinkedList<Integer> A = new LinkedList<Integer>();
		LinkedList<Integer> B = new LinkedList<Integer>();

		if (Query == null || Query.isEmpty()) return A;
		if (!Query.contains("NOT")) return A;

		String term = Query.replaceFirst("NOT", "").trim().toLowerCase();
		A= inv.get_all_docs(term.trim().toLowerCase());


		if (index1.all_doc.empty()) return A;

		index1.all_doc.findFirst();
		while (!index1.all_doc.last()) {
			if (!A.search(index1.all_doc.retrieve().id)) {
				B.insert(index1.all_doc.retrieve().id);
			}
			index1.all_doc.findNext();
		}

		if (!A.search(index1.all_doc.retrieve().id)) {
			B.insert(index1.all_doc.retrieve().id);
		}

		return B;
	}


}

