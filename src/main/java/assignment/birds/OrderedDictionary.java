package assignment.birds;

public class OrderedDictionary implements OrderedDictionaryADT {

    Node root;

    OrderedDictionary() {
        root = new Node();
    }

    /**
     * Returns the Record object with key k, or it returns null if such a record
     * is not in the dictionary.
     *
     * @param k
     * @return
     * @throws assignment/birds/DictionaryException.java
     */
    @Override
    public BirdRecord find(DataKey k) throws DictionaryException {
        Node current = root;
        int comparison;
        if (root.isEmpty()) {         
            throw new DictionaryException("There is no record matches the given key");
        }

        while (true) {
            comparison = current.getData().getDataKey().compareTo(k);
            if (comparison == 0) { // key found
                return current.getData();
            }
            if (comparison == 1) {
                if (current.getLeftChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getLeftChild();
            } else if (comparison == -1) {
                if (current.getRightChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getRightChild();
            }
        }

    }

    /**
     * Inserts r into the ordered dictionary. It throws a DictionaryException if
     * a record with the same key as r is already in the dictionary.
     *
     * @param r
     * @throws DictionaryException
     */
    @Override
    public void insert(BirdRecord r) throws DictionaryException {

        //Node newNode = new Node(r);
        int comparison;

        if (r==null){
            throw new DictionaryException("invalid record");
        }
        if(root==null){
            root=new Node(r);
        }
        else{
            Node parent = null;
            Node current = root;
            while(current != null){
                comparison = r.getDataKey().compareTo(current.getData().getDataKey());
                if (comparison== -1){
                    parent = current;
                    current = current.getLeftChild();
                } else if (comparison==1) {
                    parent = current;
                    current = current.getRightChild();
                }
                else{
                    throw new DictionaryException("Cannot add duplicate record");
                }
            }
            if(r.getDataKey().compareTo(parent.getData().getDataKey())==-1){
                parent.setLeftChild(new Node(r));
            }
            else{
                parent.setRightChild(new Node(r));
            }
        }

    }

    /**
     * Removes the record with Key k from the dictionary. It throws a
     * DictionaryException if the record is not in the dictionary.
     *
     * @param k
     * @throws DictionaryException
     */
    @Override
    public void remove(DataKey k) throws DictionaryException {
        if(isEmpty()){
            throw new DictionaryException("Cant remove from empty tree");
        }
        if(k== null){
            throw new DictionaryException("Invalid record ");
        }
        int comparison;
        Node current = root;
        Node parent = new Node();
        while(current != null){
            comparison = k.compareTo(current.getData().getDataKey());
            if(comparison==0){  //matching key found
                if(!current.hasLeftChild()){ //If left child empty
                    if(parent.isEmpty()){ //If at root 
                        root=current.getRightChild();
                    } else if (current== parent.getLeftChild()) {
                        parent.setLeftChild(current.getRightChild()); //Set parent leftChild = current right
                    }
                    else{
                        parent.setRightChild(current.getRightChild());
                    }
                } else if (!current.hasRightChild()) {
                    if(parent.isEmpty()){ //If at root
                        root=current.getLeftChild(); //if right child empty
                    } else if (current== parent.getLeftChild()) {
                        parent.setLeftChild(current.getLeftChild());
                    }
                    else{
                        parent.setRightChild(current.getLeftChild());
                    }
                }else {
                    current=removeNodeWithTwoChildren(current); //Calls function to deal with 2 child nodes
                }
                return;
            } else if (comparison==-1) {
                parent=current;
                current=current.getLeftChild();
            }
            else{
                parent=current;
                current=current.getRightChild();
            }
        }
    }
    private Node removeNodeWithTwoChildren(Node current){
        Node succesor = current.getRightChild();
        while(succesor.getLeftChild() != null){
            succesor=succesor.getLeftChild(); //Finds Successor
        }
        current.setData(succesor.getData()); //Overides current data
        current.setRightChild(removeMin(current.getRightChild())); //Sets new right child to subtree with deleted duplicate
        return current;

    }
    private Node removeMin(Node current){ //method for removing the duplicate
        if(current.hasLeftChild()){
            return current.getRightChild();
        }
        current.setLeftChild(removeMin(current.getLeftChild()));
        return current;
    }
    /**
     * Returns the successor of k (the record from the ordered dictionary with
     * smallest key larger than k); it returns null if the given key has no
     * successor. The given key DOES NOT need to be in the dictionary.
     *
     * @param k
     * @return
     * @throws DictionaryException
     */
    @Override
    public BirdRecord successor(DataKey k) throws DictionaryException{
        if(isEmpty()){
            throw new DictionaryException("Tree is empty");
        }
        if(k== null){
            throw new DictionaryException("Invalid record ");
        }
        Node current =root;
        int comparison;
        BirdRecord succesor = null;
        while(current != null){
            comparison = k.compareTo(current.getData().getDataKey());
            if(comparison==1){
                succesor= new BirdRecord(current.getData().getDataKey());
                current=current.getLeftChild();
            }else{
                current=current.getRightChild();
            }
        }
        return succesor;
    }

   
    /**
     * Returns the predecessor of k (the record from the ordered dictionary with
     * largest key smaller than k; it returns null if the given key has no
     * predecessor. The given key DOES NOT need to be in the dictionary.
     *
     * @param k
     * @return
     * @throws DictionaryException
     */
    @Override
    public BirdRecord predecessor(DataKey k) throws DictionaryException{
        if(isEmpty()){
            throw new DictionaryException("Tree is empty");
        }
        if(k== null){
            throw new DictionaryException("Invalid record ");
        }
        Node current =root;
        int comparison;
        BirdRecord predecessor = null;
        while(current != null){
            comparison = k.compareTo(current.getData().getDataKey());
            if(comparison==-1){
                predecessor= new BirdRecord(current.getData().getDataKey());
                current=current.getRightChild();
            }else{
                current=current.getLeftChild();
            }
        }
        return predecessor;
    }

    /**
     * Returns the record with smallest key in the ordered dictionary. Returns
     * null if the dictionary is empty.
     *
     * @return
     */
    @Override
    public BirdRecord smallest() throws DictionaryException{
        if(isEmpty()){
            throw new DictionaryException("the dictionary does not have a smallest.");
        }
        Node current = root;
        while(current.getLeftChild() != null){
            current = current.getLeftChild();
        }

        return current.getData(); // change this statement

    }

    /*
	 * Returns the record with largest key in the ordered dictionary. Returns
	 * null if the dictionary is empty.
     */
    @Override
    public BirdRecord largest() throws DictionaryException{
        if(isEmpty()){
            throw new DictionaryException("the dictionary does not have a smallest.");
        }
        Node current = root;
        while(current.getRightChild() != null){
            current = current.getRightChild();
        }

        return current.getData(); // change this statement
    }
      
    /* Returns true if the dictionary is empty, and true otherwise. */
    @Override
    public boolean isEmpty (){
        return root.isEmpty();
    }
}
