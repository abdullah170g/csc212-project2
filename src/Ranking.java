class Doc_Rank {
	int id;
	int rank;

	public Doc_Rank(int i, int r) {
		id = i;
		rank = r;
	}

	public void display() {
		System.out.printf("%-8d%-8d%n", id, rank);
	}
}

public class Ranking {
	static String Query;
	static InvertedIndexBST inv;
	static Index index1;
	static LinkedList<Integer> alldocQuery;
	static LinkedList<Doc_Rank> alldocrank;

	public Ranking(InvertedIndexBST inv, Index index1, String Query) {
		this.inv = inv;
		this.index1 = index1;
		this.Query = Query;
		alldocQuery = new LinkedList<Integer>();
		alldocrank = new LinkedList<Doc_Rank>();
	}

	public static void display_all_doc_with_score() {
		if (alldocrank.empty()) {
			System.out.println("empty");
			return;
		}

		System.out.printf("%-8s%-8s%n", "DocID", "Score");
		alldocrank.findFirst();
		while (!alldocrank.last()) {
			alldocrank.retrieve().display();
			alldocrank.findNext();
		}
		alldocrank.retrieve().display();
	}

	public static Document get_doc_given_id(int id) {
		return index1.get_doc(id);
	}

	public static int term_frequency_in_doc(Document d, String term) {
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

	public static int get_doc_rank_score(Document d, String Query) {
		if (Query.length() == 0) return 0;

		String terms[] = Query.split(" ");
		int sum_freq = 0;

		for (int i = 0; i < terms.length; i++) {
			sum_freq += term_frequency_in_doc(d, terms[i].trim().toLowerCase());
		}

		return sum_freq;
	}

	public static void RankQuery(String Query) {
		LinkedList<Integer> A = new LinkedList<Integer>();
		if (Query.length() == 0) return;

		String terms[] = Query.split(" ");
		boolean found = false;

		for (int i = 0; i < terms.length; i++) {
			found = inv.search_word_in_inv(terms[i].trim().toLowerCase());
			if (found) {
				A = inv.inv_index.retrieve().doc_id;
				Adding_in_List_sorted(A);
			}
		}
	}

	public static void Adding_in_List_sorted(LinkedList<Integer> A) {
		if (A.empty())
			return;

		A.findFirst();
		while (!A.empty()) {
			boolean found = existsIn_result(alldocQuery, A.retrieve());
			if (!found) { // not found in result
				insert_sorted_Id_list(A.retrieve());
			}
			if (!A.last()) {
				A.findNext();
			} else {
				break;
			}
		}
	}

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
		if (alldocQuery.empty()) {
			alldocQuery.insert(id);
			return;
		}

		alldocQuery.findFirst();
		while (!alldocQuery.last()) {
			if (id < alldocQuery.retrieve()) {
				Integer ID = alldocQuery.retrieve();
				alldocQuery.update(id);
				alldocQuery.insert(ID);
				return;
			} else
				alldocQuery.findNext();
		}

		if (id < alldocQuery.retrieve()) {
			Integer ID = alldocQuery.retrieve();
			alldocQuery.update(id);
			alldocQuery.insert(ID);
			return;
		} else
			alldocQuery.insert(id);
	}

	public static void insert_sorted() {
		RankQuery(Query); 
		if (alldocQuery.empty()) {
			System.out.println("empty query");
			return;
		}

		alldocQuery.findFirst();
		while (!alldocQuery.last()) {
			Document d = get_doc_given_id(alldocQuery.retrieve());
			int Rank = get_doc_rank_score(d, Query);
			insert_sorted_list(new Doc_Rank(alldocQuery.retrieve(), Rank)); // Insert Doc_Rank
			alldocQuery.findNext();
		}

		Document d = get_doc_given_id(alldocQuery.retrieve());
		int Rank = get_doc_rank_score(d, Query);
		insert_sorted_list(new Doc_Rank(alldocQuery.retrieve(), Rank)); // Insert Doc_Rank
	}

	public static void insert_sorted_list(Doc_Rank r) {
		if (alldocrank.empty()) {
			alldocrank.insert(r);
			return;
		}

		alldocrank.findFirst();
		while (!alldocrank.last()) {
			if (r.rank > alldocrank.retrieve().rank) {
				Doc_Rank r1 = alldocrank.retrieve();
				alldocrank.update(r);
				alldocrank.insert(r1);
				return;
			} else
				alldocrank.findNext();
		}
		if (r.rank > alldocrank.retrieve().rank) {
			Doc_Rank r1 = alldocrank.retrieve();
			alldocrank.update(r);
			alldocrank.insert(r1);
			return;
		} else
			alldocrank.insert(r);
	}
}
