import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
public class m_invIndex {
	LinkedList<String> stopWord;
	Index index1;
	InvertedIndex inverted;
	BST<Word> inv_bst; // BST Implementation
	public m_invIndex () {
		stopWord = new LinkedList<>();
		index1 = new Index();
		inverted = new InvertedIndex();
		inv_bst = new BST<Word>(); // Initialization
	}
	public void Load_stopWords(String fileName) {
		try {
			File f = new File(fileName);
			Scanner s = new Scanner(f);
			while (s.hasNextLine()) {
				String line = s.nextLine();
				stopWord.insert(line);
			}
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void Load_all_doc(String fileName) {
		String line = null;
		try {
			File f = new File(fileName);
			Scanner s = new Scanner(f);
			s.nextLine(); 

			while (s.hasNextLine()) {
				line = s.nextLine();
				
				if (line.trim().length() < 3) {
					// System.out.println("Empty line skip this line: " + line); NOT REQUIRED
					break; 
				}

				String x = line.substring(0, line.indexOf(','));
				int id = Integer.parseInt(x.trim());
				String content = line.substring(line.indexOf(',') + 1).trim();

				LinkedList<String> word_in_doc =Linked_List_of_words_in_doc_index_inve_index(content,id);
				index1.add_Document(new Document(id, word_in_doc));

				// s.close(); يسبب مشاكل في القراءة
			}
			s.close(); // المكان الصحيح 
		} catch (Exception e) {
			System.out.println("Finished loading files...");
		}
		BST(); // Creating the BST after reading !!
	}
	public LinkedList<String> Linked_List_of_words_in_doc_index_inve_index(String content, int id) {
		LinkedList<String> word_in_doc = new LinkedList<String>();
		index_and_inv_index(content, word_in_doc, id);
		return word_in_doc;
	}
	public void index_and_inv_index(String content, LinkedList<String> word_in_doc, int id) {
		content = content.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
		String[] tokens = content.split("\\s+");

		for (String w : tokens) {
			if (!search_stop_word(w)) {
				word_in_doc.insert(w);
				inverted.add(w, id);
			}
		}
	}
	public boolean search_stop_word(String word) {
		if (stopWord == null || stopWord.empty()) 
			return false;


		stopWord.findFirst();
		while (!stopWord.last()) {
			if (stopWord.retrieve().equals(word)) {
				return true;
			}
			stopWord.findNext();
		}

		if (stopWord.retrieve().equals(word)) {
			return true;
		}

		return false;
	}
	public void Load_all_file(String stop_file, String documents_file) {
		Load_stopWords(stop_file);
		Load_all_doc(documents_file);
	}
	
	// Easiest way to implement BST
	public void BST() {
		inverted.inv_index.findFirst();
		while(!inverted.inv_index.last()) {
			Word w = inverted.inv_index.retrieve();
			inv_bst.insert(w.text, w);
			inverted.inv_index.findNext();
		}
		inv_bst.insert(inverted.inv_index.retrieve().text, inverted.inv_index.retrieve());
	}
	
	//Implementing search by term !!
	public LinkedList<Integer> search(String content){
		inv_bst.findkey(content.toLowerCase());
		Word w = inv_bst.retrieve();
		if(w != null)
			return w.doc_id;
		else
			return null;
	}
	
	public static void main(String[] args) {
		Scanner inp = new Scanner(System.in);
		m_invIndex m = new m_invIndex();
		m.Load_all_file("C:\\Users\\alz7\\Desktop\\stop.txt", "C:\\Users\\alz7\\Desktop\\dataset.csv");

		m.index1.displayDocument();
		System.out.println("\n-----------------");
		m.inverted.display_inv_index();
		
		System.out.println("\n-----------------");
		//Testing the BST
		System.out.print("Please enter the term you want search for: ");
		String x = inp.next();
		LinkedList<Integer> docIds = m.search(x);
		System.out.println("Documents that contain the term: ");
		docIds.display();
		inp.close();
	}
}

