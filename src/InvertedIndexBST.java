public class InvertedIndexBST {
    BST<Word> inv_index;

    public InvertedIndexBST() {
        inv_index = new BST<Word>();
    }

    public void add(String text, int id) {
        if (!inv_index.findKey(text)) {
            Word w = new Word(text);
            w.doc_id.insert(id);
            inv_index.insert(text, w);
        } else {
            Word exist_word = inv_index.retrieve();
            exist_word.add_Id(id);
        }
    }
    public boolean search_word_in_inv(String w) {
        return inv_index.findKey(w);
    }


    
    public void display_inverted_index() {
        if (inv_index == null) {
            System.out.println("null inv_index");
            return;
        } else if (inv_index.empty()) {
            System.out.println("empty inv_index");
            return;
        }
        inv_index.inOrder();
    }

}

