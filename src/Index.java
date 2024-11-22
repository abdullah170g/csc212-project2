public class Index {
    LinkedList<Document> all_doc;

    public Index() {
        all_doc = new LinkedList<Document>();
    }

    public void add_Document(Document d) {
        all_doc.insert(d);
    }
    public Document get_doc(int id) {
        if (all_doc.empty()) {
            System.out.println("no docs exist");
            return null;
        }

        all_doc.findFirst();
        while (!all_doc.last()) {
            if (all_doc.retrieve().id == id)
                return all_doc.retrieve();
            all_doc.findNext();
        }

        if (all_doc.retrieve().id == id)
            return all_doc.retrieve();

        return null;
    }


    public void displayDocument() {
        if (all_doc == null) {
            System.out.println("null docs");
            return;
        } else if (all_doc.empty()) {
            System.out.println("empty docs");
            return;
        }
        all_doc.findFirst();
        while (!all_doc.last()) {
            Document doc = all_doc.retrieve();
            System.out.println("\n-----------------------");
            System.out.println("ID:" + doc.id);
            doc.words.display();
            all_doc.findNext();
        }

        Document doc = all_doc.retrieve();
        System.out.println("\n------------------------");
        System.out.println("ID: " + doc.id);
        doc.words.display();;
    }public LinkedList<Integer> get_all_docs(String term) {
        LinkedList<Integer> res = new LinkedList<>();
        
        if (all_doc.empty()) {
            System.out.println("No documents exist");
            return null;
        }
        
        all_doc.findFirst();
        while (!all_doc.last()) {
            if (all_doc.retrieve().words.search(term.toLowerCase().trim())) 
                res.insert(all_doc.retrieve().id);
            
            all_doc.findNext();
        }
        if (all_doc.retrieve().words.search(term.toLowerCase().trim())) 
            res.insert(all_doc.retrieve().id);

        
        return res;
    }
   

}
