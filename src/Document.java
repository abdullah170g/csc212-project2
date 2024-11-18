public class Document {
	LinkedList<String> word = new LinkedList<>();
	int id;

	Document(int id, LinkedList<String> word) {
	    this.id = id;
	    this.word = word;
	}

}
