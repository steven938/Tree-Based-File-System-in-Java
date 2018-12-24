/**
 * @author StevenChen
 * Ordered Dictionary Class
 */
public class OrderedDictionary implements OrderedDictionaryADT{
    /* Ordered Dictionary ADT */
    private Node root;

    /**
     * Constructor creates new Ordered Dictionary with a empty node as the root
     */
    public OrderedDictionary(){
        root = new Node();
    }


    /**
     * Returns the Record object with key k, or it returns null if such
     * a record is not in the dictionary.
     * @param k key
     * @return Record or null
     */
    public Record get (Pair k){
        Node target = getNode(k);
        if(target.hasNoChildren())
            return null;
        return target.getRecord();
    }


    /**
     * Inserts r into the ordered dictionary. It throws a DictionaryException
     *        if a record with the same key as r is already in the dictionary.
     * @param r record
     * @throws DictionaryException
     */
    public void put (Record r) throws DictionaryException {
        Node target = getNode(r.getKey());
        if(!target.hasNoChildren())
            throw new DictionaryException("Trying to insert key that is already in Dictionary");
        target.setRecord(r);
        target.setRight(new Node()); // setting children as empty nodes
        target.setLeft(new Node());
    }

    /**
     * Removes the record with key k from the dictionary. It throws a
     *         DictionaryException if the record is not in the dictionary.
     * @param k key
     * @throws DictionaryException
     */
    public void remove (Pair k) throws DictionaryException {
        Node[] nodePair = getNodeAndParent(k);
        Node target = nodePair[0];
        Node parent = nodePair[1];
        if(target.hasNoChildren()&&target.getRecord()!=null)
            root = new Node();
        else if(target.hasNoChildren())
            throw new DictionaryException("Trying to remove key that is not in Dictionary");
        else if(target.getRight().hasNoChildren()&&target.getLeft().hasNoChildren()){
            if(parent==null)
                root = new Node();
            else if(parent.getRight()==target)
                parent.setRight(new Node());
            else
                parent.setLeft(new Node());
        }else {
            Node nonleafChild = null;
            if (target.getLeft().hasNoChildren() && !target.getRight().hasNoChildren())
                nonleafChild = target.getRight();
            else if (!target.getLeft().hasNoChildren() && target.getRight().hasNoChildren())
                nonleafChild = target.getLeft();
            if (nonleafChild != null) { // this means one of the children is a leaf
                if (target == root) {
                    root = nonleafChild;
                }else {
                    if(parent.getLeft()==target)
                        parent.setLeft(nonleafChild);
                    else
                        parent.setRight(nonleafChild);
                }
            } else { // this means neither of the children are leafs
                Node smallestRight = smallestNode(target.getRight());
                Node parentOfSmallest = getNodeAndParent(smallestRight.getRecord().getKey())[1];
                target.setRecord(smallestRight.getRecord());
                if(parentOfSmallest.getLeft() == smallestRight)
                    parentOfSmallest.setLeft(new Node());
                else
                    parentOfSmallest.setRight(new Node());
            }
        }
    }


    /**
     * Returns the successor of k (the record from the ordered dictionary
     *        with smallest key larger than k); it returns null if the given key has
     *        no successor. The given key DOES NOT need to be in the dictionary.
     * @param k key
     * @return successor key or null
     */
    public Record successor (Pair k) {
        Record result = null;
        Node[] nodePair = getNodeAndParent(k);
        Node target = nodePair[0];
        Node parent = nodePair[1];
        if(target==root){ // if the target node found is the root
            if(root.getRight()==null)
                return null;
            return root.getRight().getRecord();
        }
        if(target.hasNoChildren()){ // in the case the key doesn't exist
            if(parent.getLeft()==target) // if target node is the left child
                return parent.getRecord();
            else
                target.setRecord(new Record(new Pair(parent.getRecord().getKey().getWord()+ "z", ""), ""));
        }
        else if(!target.getRight().hasNoChildren()){
            return smallestNode(target.getRight()).getRecord();
        }

        Node tempRoot = root;
        Node succ = null;
        while(!tempRoot.hasNoChildren()){
            if(target.compareTo(tempRoot)<0){
                succ = tempRoot;
                tempRoot = tempRoot.getLeft();
            }
            else if(target.compareTo(tempRoot)>0){
                tempRoot = tempRoot.getRight();
            }
            else
                break;
        }
        if(succ!=null)
            result = succ.getRecord();
        if(target.getRecord().getData()==""){
            target.setRecord(null);
        }
        return result;
    }


    /**
     * Returns the predecessor of k (the record from the ordered dictionary
     *        with largest key smaller than k; it returns null if the given key has
     *        no predecessor. The given key DOES NOT need to be in the dictionary.
     * @param k key
     * @return predecessor or null
     */
    public Record predecessor (Pair k) {
        Record result = null;
        Node[] nodePair = getNodeAndParent(k);
        Node target = nodePair[0];
        Node parent = nodePair[1];
        if(target==root) {
            if (root.getLeft() == null)
                return null;
            return root.getLeft().getRecord();
        }
        if(target.hasNoChildren()){
            if(parent.getRight()==target) // if the target node is the right child of its parent
                return parent.getRecord();
            else
                target.setRecord(new Record(new Pair(parent.getRecord().getKey().getWord(), parent.getRecord().getKey().getWord().substring(0,1)), ""));
        }
        else if(!target.getLeft().hasNoChildren()){
            return largestNode(target.getLeft()).getRecord();
        }

        Node tempRoot = root;
        Node pred = null;
        while(!tempRoot.hasNoChildren()){
            if(target.compareTo(tempRoot)<0){
                tempRoot = tempRoot.getLeft();
            }
            else if(target.compareTo(tempRoot)>0){
                pred = tempRoot;
                tempRoot = tempRoot.getRight();
            }
            else
                break;
        }
        if(pred!=null)
            result = pred.getRecord();
        if(target.getRecord().getData()==""){
            target.setRecord(null);
        }
        return result;
    }


    /**
     * Returns the record with smallest key in the ordered dictionary.
     *        Returns null if the dictionary is empty.
     * @return smallest item or null if empty
     */
    public Record smallest (){
        Node smallest = smallestNode(root);
        if(smallest==null)
            return null;
        return smallest.getRecord();
    }


    /**
     * Returns the record with largest key in the ordered dictionary.
     *        Returns null if the dictionary is empty.
     * @return largest item or null
     */
    public Record largest (){
        Node largest = largestNode(root);
        if(largest==null)
            return null;
        return largest.getRecord();
    }

    /**
     * Function finds and returns the node with Pair k or an empty leaf node if not found
     * @param k key to search for
     * @return node storing key or null
     */
    private Node getNode(Pair k){
        Node target = new Node(new Record(k, ""));
        Node current = root;
        while(!current.hasNoChildren()){
            if(current.compareTo(target)>0)
                current = current.getLeft();
            else if(current.compareTo(target)<0)
                current = current.getRight();
            else // means current.compareTo(target) = 0 which means they match
                break;
        }
        return current;
    }

    /**
     * This method is similar to the getNode function except that it also returns the parent of the that node.
     * @param k Given key to search for
     * @return Index 0: The node with the the key or a leaf node, Index 1: the node's parent
     */
    private Node[] getNodeAndParent(Pair k){
        Node[] output = new Node[2];
        Node target = new Node(new Record(k, ""));
        Node current = root;
        Node parent = null;
        while(!current.hasNoChildren()){
            if(current.compareTo(target)>0) {
                parent = current;
                current = current.getLeft();
            }
            else if(current.compareTo(target)<0) {
                parent = current;
                current = current.getRight();
            }
            else // means current.compareTo(target) = 0 which means they match
                break;
        }
        output[0] = current;
        output[1] = parent;
        return output;
    }

    /**
     * Searches and returns the smallest node in our tree
     * @param startingNode the node treated as the root. algorithm starts search from this node
     * @return smallest node or null
     */
    private Node smallestNode(Node startingNode){
        if(startingNode.hasNoChildren())
            return startingNode;
        Node parent = null;
        Node current = startingNode;
        while(!current.hasNoChildren()) {
            parent = current;
            current = current.getLeft();
        }
        return parent;
    }

    /**
     * Searches and returns largest node in our tree
     * @param startingNode the node treated as the root. algorithm starts search form this node
     * @return largest node or null
     */
    private Node largestNode(Node startingNode){
        if(startingNode.hasNoChildren())
            return startingNode;
        Node parent = null;
        Node current = startingNode;
        while(!current.hasNoChildren()){
            parent = current;
            current = current.getRight();
        }
        return parent;
    }

}
