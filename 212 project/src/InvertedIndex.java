public class InvertedIndex {
    LinkedList<Word> inv_index;
    public InvertedIndex() {
        inv_index = new LinkedList<Word>();
    }

    public void add(String text, int id) {
        if (!search_word_in_inv(text)) {
            Word w = new Word(text);
            w.doc_id.insert(id);
            inv_index.insert(w);
        } else {
            Word exist_word = inv_index.retrieve();
            exist_word.add_Id(id);
        }
    }

    public boolean search_word_in_inv(String w) {
        if (inv_index == null || inv_index.empty()) {
            return false;
        }
        
        inv_index.findFirst();
        while (!inv_index.last()) {
            if (inv_index.retrieve().text.equals(w)) {
                return true;
            }
            inv_index.findNext();
        }
        
        if (inv_index.retrieve().equals(w)) {
            return true;
        }
        
        return false;
    }

    public void display_inv_index() {
        if (inv_index == null) {
            System.out.println("null inverted_index");
            return;
        } else if (inv_index.empty()) {
            System.out.println("empty inverted_index");
            return;
        } else {
            inv_index.findFirst();
            while (!inv_index.last()) {
                inv_index.retrieve().display();
                inv_index.findNext();
            }

            inv_index.retrieve().display();
        }
    }
}
