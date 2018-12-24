/**
 * @author StevenChen
 * Class that stores key
 */
public class Pair {
    private String word, type;
    public Pair(String word, String type){
        this.word = word;
        this.type = type;
    }
    public String getWord(){
        return word;
    }
    public String getType(){
        return type;
    }
    public int compareTo(Pair k){
        int result = word.compareTo(k.getWord());
        if(result==0){
            result = type.compareTo(k.getType());
        }
        return result;
    }
}
