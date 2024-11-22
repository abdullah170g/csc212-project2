import java.util.Scanner;

public class Main {
	public static void display_Menu() {
		System.out.println("1 - Retrieve a term | there are choices"
				+ " -using index with lists"
				+ " -inverted index with lists"
				+ " -inverted index with BST.");
		System.out.println("2 - Boolean Retrieval.");
		System.out.println("3 - Ranked Retrieval.");
		System.out.println("4 - Indexed Documents: print all docs");
		System.out.println("5 - Number of documents in the index.");
		System.out.println("6 - Number of unique words without stop word in the index.");
		System.out.println("7 - Indexed Tokens: to show number of vocabulary and tokens in the index");
		System.out.println("8 - Exit");
	}
	public static void TestMenu() {
		m_invIndex d = new m_invIndex(); 	
		d.Load_all_file("stop.txt", "dataset.csv");
		Scanner s = new Scanner(System.in);
		int ch = 0;
		do {
			display_Menu();
			System.out.print("Please enter your choice: ");
			ch = s.nextInt();
			switch (ch) {
			case 1:
				System.out.print("Enter a term to retrieve: ");
				String term = s.next();
				term = term.toLowerCase().trim();
				System.out.println("- using index with lists -");
				LinkedList<Integer> res = d.index1.get_all_docs(term);
				System.out.print("Word: " + term + "[");
				res.display();
				System.out.println("]");
				System.out.println("-------------------------");
				System.out.println("- inverted index with lists -");
				boolean found = d.inverted.search_word_in_inv(term);
				if (found)
					d.inverted.inv_index.retrieve().display();

				else
					System.out.println("Not found in the inverted index with lists !");

				System.out.println("- inverted index with BST -");
				boolean found2 = d.inv_BST.search_word_in_inv(term);
				if (found2)
					d.inverted.inv_index.retrieve().display();
				else
					System.out.println("Not found in inverted index with BST !!");
				break;

			case 2:
				s.nextLine();
				System.out.print("Enter a query to retrieve: ");
				String query = s.nextLine();
				query = query.toLowerCase();
				query = query.replaceAll(" and ", " AND ");
				query = query.replaceAll(" or ", " OR ");
				System.out.println("\nWhich method you want to make query enter:\n"
						+ "1 - For using index \n"
						+ "2 - For using inverted index list of lists \n"
						+ "3 - For using BST\n");
				System.out.print("Choice: ");
				int x = s.nextInt();
				do {
					if (x == 1) {
						Queryindex q = new Queryindex(d.index1);
						System.out.println("================" + query + "================");
						LinkedList res1 = Queryindex.MixedQuery(query);
						d.display_doc(res1);
					} else if (x == 2) {
						Query q = new Query(d.inverted);
						System.out.println("================" + query + "================");
						LinkedList res1 = Query.MixedQuery(query);
						d.display_doc(res1);
					} else if (x == 3) {
						QueryBST q = new QueryBST(d.inv_BST);
						System.out.println("================" + query + "================");
						LinkedList res1 = QueryBST.MixedQuery(query);
						d.display_doc(res1);
					} else if (x > 3 || x < 1) 
						break;
					else 
						System.out.println("Wrong query!!");


					System.out.println("\nWhich method you want to make query enter:\n"
							+ "1 - For using index \n"
							+ "2 - For using inverted index list of lists \n"
							+ "3 - For using BST\n");
					System.out.print("Choice: ");
					x = s.nextInt();
				} while (x != 4);
				break;
			case 3:
				s.nextLine();
				System.out.print("Enter a query to Rank: ");
				String query2 = s.nextLine();
				query2 = query2.toLowerCase();
				Ranking R5 = new Ranking(d.inv_BST, d.index1 , query2);
				R5.insert_sorted();
				R5.display_all_doc_with_score();
				break;

			case 4:
				d.index1.displayDocument();
				System.out.println("----------------");
				break;

			case 5:
				System.out.println("Number of documents = " + d.index1.all_doc.count);
				System.out.println("----------------");
				break;

			case 6:
				System.out.println("Number of unique words without stop word  = " + d.inverted.inv_index.count);
				System.out.println("----------------");

				break;
			case 7:
				System.out.println("Number of tokens = " + d.numOftokens);
				System.out.println("Number of unique word = " + d.unique_word.count);
				break;




			case 8:
				System.out.println("Goodbye!!");
				break;

			default:
				System.out.println("Error input please try again!");
				break;
			}
		} while (ch != 8);


	}
	public static void main(String[] args) {  
		System.out.println("#### SEARCH ENGINE ####");
		TestMenu();
	}
}

