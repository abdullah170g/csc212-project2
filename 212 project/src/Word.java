public class Word {
    String text;
    LinkedList<Integer> doc_id;

    public Word(String w) {
        text = w;
        doc_id = new LinkedList<Integer>();
    }

    public void add_Id(int id) {
        if (!search_doc_id(id)) {
            doc_id.insert(id);
        }
    }
    public boolean search_doc_id(Integer id) {
        if (doc_id.empty()) 
            return false;
        
        doc_id.findFirst();
        while (!doc_id.last()) {
            if (doc_id.retrieve().equals(id)) {
                return true;
            }
            doc_id.findNext();
        }
        
        if (doc_id.retrieve().equals(id)) {
            return true;
        }
        
        return false;
    }
    public void display() {
        System.out.println("\n--------------------");
        System.out.print("word: " + text);
        System.out.print("[");
        doc_id.display();
        System.out.println("]");
    }
}
