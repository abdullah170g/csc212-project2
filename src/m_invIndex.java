import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
public class m_invIndex {
	LinkedList<String> stopWord;
    Index index1;
    InvertedIndex inverted;
    public m_invIndex () {
        stopWord = new LinkedList<>();
        index1 = new Index();
        inverted = new InvertedIndex();
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
                    System.out.println("Empty line skip this line: " + line);
                    break; 
                }
                
                String x = line.substring(0, line.indexOf(','));
                int id = Integer.parseInt(x.trim());
                String content = line.substring(line.indexOf(',') + 1).trim();
                
                LinkedList<String> word_in_doc =Linked_List_of_words_in_doc_index_inve_index(content,id);
                index1.add_Document(new Document(id, word_in_doc));
            }
        } catch (Exception e) {
            System.out.println("end of file");
        }}
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

        public static void main(String[] args) {
        	m_invIndex m = new m_invIndex();
            m.Load_all_file("stop.txt", "dataset.csv");

            m.index1.displayDocument();
            System.out.println("\n-----------------");
            m.inverted.display_inv_index();
        }


        
        }


