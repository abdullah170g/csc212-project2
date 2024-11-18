public class Index {
	LinkedList<Document> all_doc;

	public Index() {
	    all_doc = new LinkedList<Document>();
	}

	public void add_Document(Document d) {
	    all_doc.insert(d);
	}

	public void displayDocument() {
	    if (all_doc == null) {
	        System.out.println("Null document.");
	        return;
	    } else if (all_doc.empty()) {
	        System.out.println("Empty document.");
	        return;
	    }
	    all_doc.findFirst();
	    while (!all_doc.last()) {
	        Document doc = all_doc.retrieve();
	        System.out.println("\n-----------------------");
	        System.out.println("ID:" + doc.id);
	        doc.word.display();
	        all_doc.findNext();
	    }

	    Document doc = all_doc.retrieve();
	    System.out.println("\n------------------------");
	    System.out.println("ID: " + doc.id);
	    doc.word.display();

	}
	
}
