/**
 * @author StevenChen
 * Node class that will construct our BST
 */
public class Node {
    private Node left;
    private Node right;
    private Record record;
    public Node(Record record){
        this.record = record;
    }
    public Node() {
        return; // does not set any variables
    }
    public Record getRecord(){
        return record;
    }
    public void setRecord(Record record){
        this.record = record;
    }
    public Node getLeft(){
        return left;
    }
    public void setLeft(Node left){
        this.left = left;
    }
    public Node getRight(){
        return right;
    }
    public void setRight(Node right){
        this.right = right;
    }
    public boolean hasNoChildren(){
        return (left==null&&right==null);
    }
    public int compareTo(Node other){
         return getRecord().getKey().compareTo(other.getRecord().getKey());
    }
}
