import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class m_invIndex {
    LinkedList<String> stopWord;
    Index index1;
    InvertedIndex inverted;
    InvertedIndexBST inv_BST;
    int numOftokens=0;
    LinkedList<String> unique_word = new LinkedList<>();

    public m_invIndex () {
        stopWord = new LinkedList<>();
        index1 = new Index();
        inverted = new InvertedIndex();
        inv_BST = new InvertedIndexBST();
    }

    public void Load_stopWords(String fileName) {
        try {
            File f = new File(fileName);
            Scanner s = new Scanner(f);
            while (s.hasNextLine()) {
                String line = s.nextLine();
                stopWord.insert(line);
            }
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
    	          System.out.println("Empty line found skip =" + line);
    	            break;
    	        }

    	        String x = line.substring(0, line.indexOf(','));
    	        int id = Integer.parseInt(x.trim());
    	        String content = line.substring(line.indexOf(',') + 1).trim();

    	        LinkedList<String> words_in_doc =Linked_List_of_words_in_doc_index_inv_index(content, id);
    	        index1.add_Document(new Document(id, words_in_doc, content));
    	    }}catch (Exception e) {
            System.out.println("end of file");
        }
    }

    public LinkedList<String> Linked_List_of_words_in_doc_index_inv_index(String content, int id) {
        LinkedList<String> word_in_doc = new LinkedList<String>();
        index_and_inv_index(content, word_in_doc, id);
        return word_in_doc;
    }

    public void index_and_inv_index(String content, LinkedList<String> word_in_doc, int id) {
    	
    	    content = content.replaceAll("\'", " ");
    	    content = content.replaceAll("-", " ");
    	    content = content.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "");
    	    String[] tokens = content.split("\\s+");
    	    numOftokens += tokens.length;

    	    for (String w : tokens) {
    	        if (!unique_word.search(w)) {
    	            unique_word.insert(w);
    	            
    	        }

    	        if (!search_stop_word(w)) {
    	            word_in_doc.insert(w);
    	            inverted.add(w, id);
    	            inv_BST.add(w, id);
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
    public void display_doc(LinkedList<Integer> IDs) {
        if (IDs.empty()) {
            System.out.println("no docs exist");
            return;
        }

        IDs.findFirst();
        while (!IDs.last()) {
            Document d = index1.get_doc(IDs.retrieve());
            if (d != null) {
                System.out.println("Document " + d.id + ": " + d.content);
            }
            IDs.findNext();
        }

        Document d = index1.get_doc(IDs.retrieve());
        if (d != null) {
            System.out.println("Document " + d.id + ": " + d.content);
        }
        

        System.out.println("");
    }


    public void Load_all_file(String stop_file, String documents_file) {
        Load_stopWords(stop_file);
        Load_all_doc(documents_file);
    }
    public static void Test4() {
    	m_invIndex d = new m_invIndex();        // d.Load_all_files("stop.txt", "dataset2.csv");
        d.Load_all_file("stop.txt", "dataset.csv");
        // d.index1.displayDocuments();

        System.out.println("\n==============display_inverted_index==============");
        Query q = new Query(d.inverted);
        System.out.println("\n==============market AND sports==============");
        LinkedList<Integer> res = Query.MixedQuery("market AND sports");
        d.display_doc(res);

        System.out.println("\n==============weather AND warming==============");
        res = Query.MixedQuery("weather AND warming");
        d.display_doc(res);

        System.out.println("\n==============business AND world==============");
        res = Query.MixedQuery("business AND world");
        d.display_doc(res);

        System.out.println("\n==============weather OR warming==============");
        res = Query.MixedQuery("weather OR warming");
        d.display_doc(res);

        System.out.println("\n==============market OR sports==============");
        res = Query.MixedQuery("market OR sports");
        d.display_doc(res);
        System.out.println("=====market OR sports======");
        res = Query.MixedQuery("market OR sports AND warming");
        d.display_doc(res);

    }public static void Test1() {
    	m_invIndex d = new m_invIndex();        // d.Load_all_files("stop.txt", "dataset2.csv");

        //d.Load_all_files("stop.txt", "dataset2.csv");
        d.Load_all_file("stop.txt", "dataset.csv");
        d.index1.displayDocument();

        System.out.println("\n-------------------------");
        d.inv_BST.display_inverted_index();
               System.out.println("num of tokens=" + d.numOftokens);
        System.out.println("num of unique=" + d.unique_word.count);

        Query q = new Query(d.inverted);

        LinkedList res = Query.AndQuery("colorANDpole");
        d.display_doc(res);
        System.out.println("---------------union---------------");
        
        LinkedList res1 = Query.ORQuery("Arabia OR pole OR color");
        d.display_doc(res1);
        System.out.println("---------------not-----------------");
        
        LinkedList res6 = Query.notQuery("NOT athletes", d.index1);
        d.display_doc(res6);
    }



    public static void main(String[] args) {
    	Test4();


    }
}
