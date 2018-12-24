/**
 * @author StevenChen
 * Class that stores key and associated data
 */
public class Record {
    private Pair key;
    private String data;
    public Record(Pair key, String data){
        this.key = key;
        this.data = data;
    }
    public Pair getKey(){
        return key;
    }
    public String getData(){
        return data;
    }
}
