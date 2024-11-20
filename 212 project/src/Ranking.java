class Doc_Rank {
	int id ;
	int rank;


	public Doc_Rank(int id , int rank) {
		this.id = id;
		this.rank = rank;
	}
	
	public void display(){

		System.out.printf("%-8d%-8d\n", id , rank);
		
	}
}

public class Ranking {
	static String Query;
	static InvertedIndexBST inverted;
	static Index index1;
	static LinkedList<Integer> allDocInQuery;
	static LinkedList<Doc_Rank> allDocRanked;

	// Constractor 
	public Ranking(InvertedIndexBST inverted, Index index1, String Query) {
		this.inverted = inverted;
		this.index1 = index1;
		this.Query = Query;
		allDocInQuery = new LinkedList<Integer>();
		allDocRanked = new LinkedList<Doc_Rank>();
	}
	
	// Display all ranked document 
	public static void display_all_doc_with_score_usingList() {
		if (allDocRanked.empty()) {
			System.out.println("empty");
			return;
		}
		System.out.printf("%-8s%-8s\n", "DocID", "Score");
		allDocRanked.findFirst();
		while (!allDocRanked.last()) {
			allDocRanked.retrieve().display();
			allDocRanked.findNext();
		}
		allDocRanked.retrieve().display();
	}
	
	public static Document get_doc_by_id(int id) {
		return index1.get_doc(id);
	}
	
	// get number of term in one document
	public static int term_freq(Document d, String term) {
		int freq = 0;
		LinkedList<String> words = d.words;
		if (words.empty()) return 0;
		words.findFirst();
		while (!words.last()) {
			if (words.retrieve().equalsIgnoreCase(term))
				freq++;
			words.findNext();
		}
		if (words.retrieve().equalsIgnoreCase(term))
			freq++;
		return freq;
	}
	
	//get rank score for document
	public static int getDocRank(Document d, String Query) {
		if (Query.length() == 0) return 0;
	
		String terms[] = Query.split(" ");
		int sum_freq = 0;
	
		for (int i = 0; i < terms.length; i++) {
			sum_freq += term_freq(d, terms[i].trim().toLowerCase());
		}
	
		return sum_freq;
	}
	
	// 
	public static void RankQuery(String Query) {
		LinkedList<Integer> docs_ids = new LinkedList<Integer>();
		if (Query.length() == 0) return;

		// \\s++ it's mean space (" ")
		String terms[] = Query.split("\\s+");
		boolean found = false;
	
		for (int i = 0; i < terms.length; i++) {
			found = inverted.search_word_in_inv(terms[i].trim().toLowerCase());
			if (found) {
				docs_ids = inverted.inv_index.retrieve().doc_id;
				Adding_in_1_List_sorted(docs_ids);
			}
		}
	}
	
	//
	public static void Adding_in_1_List_sorted(LinkedList<Integer> A) {
		if (A.empty()) return;
	
		A.findFirst();
		while (!A.empty()) {
			boolean found = existsIn_result(allDocInQuery, A.retrieve());
			if (!found) {
				// Not found in result
				insert_sorted_Id_list(A.retrieve());
			} // end if
	
			if (!A.last()) {
				A.findNext();
			} else {
				break;
			}
		}
	}

	// to check if id is in a result linkedlist or not
	public static boolean existsIn_result(LinkedList<Integer> result, Integer id) {
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


	public static void insert_sorted_Id_list(Integer id) {
		if (allDocInQuery.empty()) {
			allDocInQuery.insert(id);
			return;
		}
	
		allDocInQuery.findFirst();
	
		while (!allDocInQuery.last()) {
			if (id < allDocInQuery.retrieve()) {
				Integer id1 = allDocInQuery.retrieve();
				allDocInQuery.update(id);
				allDocInQuery.insert(id1);
				return;
			} else {
				allDocInQuery.findNext();
			}
		}
	
		if (id < allDocInQuery.retrieve()) {
			Integer id1 = allDocInQuery.retrieve();
			allDocInQuery.update(id);
			allDocInQuery.insert(id1);
		} else {
			allDocInQuery.insert(id);
		}
	}
	
	public static void insert_sorted_in_list() {
		if (allDocInQuery.empty()) {
			System.out.println("empty query");
			return;
		}

		RankQuery(Query);

		allDocInQuery.findFirst();
		while (!allDocInQuery.last()) {
			Document d = get_doc_by_id(allDocInQuery.retrieve());
			int Rank = getDocRank(d, Query);
			insert_ranked_list(new Doc_Rank(allDocInQuery.retrieve(), Rank));
			allDocInQuery.findNext();
		}
	
		Document d = get_doc_by_id(allDocInQuery.retrieve());
		int Rank = getDocRank(d, Query);
		insert_ranked_list(new Doc_Rank(allDocInQuery.retrieve(), Rank));
	}



	public static void insert_ranked_list(Doc_Rank dr) {
		if (allDocRanked.empty()) {
			allDocRanked.insert(dr);
			return;
		}
	
		allDocRanked.findFirst();
	
		while (!allDocRanked.last()) {
			if (dr.rank > allDocRanked.retrieve().rank) {
				Doc_Rank dr1 = allDocRanked.retrieve();
				allDocRanked.update(dr);
				allDocRanked.insert(dr1);
				return;
			} else {
				allDocRanked.findNext();
			}
		}
	
		if (dr.rank > allDocRanked.retrieve().rank) {
			Doc_Rank dr1 = allDocRanked.retrieve();
			allDocRanked.update(dr);
			allDocRanked.insert(dr1);
		}
	}
}

