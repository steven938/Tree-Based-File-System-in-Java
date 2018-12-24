import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author StevenChen
 * UserInterface class
 */
public class UserInterface {
    /**
     * Main class that the user interacts with
     * @param args command line arguments which should be the filename containing elements for the BST
     */
    public static void main(String[] args){
        String inputFile = args[0];
        OrderedDictionary dict = new OrderedDictionary();
        // code below is used to read the input textfile and construct the BST
        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) { // loop ensures every line is read
                String name = line.toLowerCase();
                String data = bufferedReader.readLine();
                String type;
                String[] parts = data.split("\\.");
                if (parts.length == 2) {
                    String end = parts[1];
                    if (end.equals("wav") || end.equals("mid"))
                        type = "audio";
                    else if (end.equals("jpg") || end.equals("gif")) {
                        type = "image";
                    } else
                        type = "text";
                } else
                    type = "text";
                try {
                    dict.put(new Record(new Pair(name, type), data));
                } catch (DictionaryException e) {
                    System.out.println(e.getMessage());
                }
            }
        }catch (IOException e){
            System.out.println("Error reading input file");
            System.exit(1);
        }

        // the code below is used to read user input options like get, delete, etc.
        StringReader reader = new StringReader();
        String option = "";
        while(!option.toLowerCase().equals("finish")){
            option = reader.read("Enter next command:   ");
            String[] section = option.toLowerCase().split(" ");
            if(option.startsWith("get")){
                get(section[1], dict);
            }else if(option.startsWith("delete")){
                delete(section[1], section[2], dict);
            }else if(option.startsWith("put")){
                String beginning = "";
                for(int i=0; i<3; i++){
                    beginning += section[i] + " "; // recreating the first portion of the substring
                }
                String inputData = option.substring(beginning.length(), option.length()); // latter substring containing the data
                putWord(section[1], section[2], inputData, dict);
            }else if(option.startsWith("list")){
                list(section[1], dict);
            }
            else if(option.startsWith("smallest")){
                smallest(dict);
            }else if(option.startsWith("largest")){
                largest(dict);
            }
        }
    }

    /**
     * get method outputs items stored in the dictionary with given 'word'
     * @param word word to search for
     * @param dict dictionary to search in
     */
    private static void get(String word, OrderedDictionary dict){
        PictureViewer pic = new PictureViewer();
        SoundPlayer sound = new SoundPlayer();
        // below checks if audio, image, text exists with word
        Record audio = dict.get(new Pair(word, "audio"));
        if(audio!=null) {
            try {
                sound.play(audio.getData());
            } catch (MultimediaException e) {
                System.out.println("There was an error with playing the audio file");
            }
        }
        Record image = dict.get(new Pair(word, "image"));
        if(image!=null) {
            try {
                pic.show(image.getData());
            } catch (MultimediaException e) {
                System.out.println("There was an error with opening the image");
            }
        }
        Record text = dict.get(new Pair(word, "text"));
        if(text!=null){
            System.out.println(text.getData());
        }
        // if no records were found then we show predecessor and successor
        if(audio==null&&image==null&&text==null){
            Record predRecord = dict.predecessor(new Pair(word, ""));
            String pred = "";
            if(predRecord!=null)
                pred = predRecord.getKey().getWord();
            Record succRecord = dict.successor(new Pair(word, ""));
            String succ = "";
            if(succRecord!=null)
                succ = succRecord.getKey().getWord();
            System.out.println("Preceding word:   "  + pred);
            System.out.println("Following word:   "  + succ);
        }
    }

    /**
     * Deletes given record from dictionary
     * @param word word associated with record
     * @param type type associated with record
     * @param dict dictionary to delete from
     */
    private static void delete(String word, String type, OrderedDictionary dict){
        try {dict.remove(new Pair(word, type));}
        catch(DictionaryException e){
            System.out.println("No record in the ordered dictionary has key (" + word + ", " + type + ").");
        }
    }

    /**
     * puts the given record in the dictionary
     * @param word word associated with record
     * @param type type associated with record
     * @param data data associated with record
     * @param dict dict associated with record
     */
    private static void putWord(String word, String type, String data, OrderedDictionary dict){
        try {dict.put(new Record(new Pair(word, type), data));}
        catch (DictionaryException e){
            System.out.println("A record with the given key (" + word + ", " + type + ") is already in the ordered dictionary.");
        }
    }

    /**
     * lists words in dictionary with given prefix
     * @param prefix prefix to search for
     * @param dict dictionary to search in
     */
    private static void list(String prefix, OrderedDictionary dict){
        String[] output = new String[1000];
        int index = 0;
        // Finds items in dictionary that has word = prefix
        Record text = dict.get(new Pair(prefix, "text"));
        Record audio = dict.get(new Pair(prefix, "audio"));
        Record image = dict.get(new Pair(prefix, "image"));
        if(audio!=null){
            output[index] = audio.getKey().getWord();
            index++;
        }
        if(image!=null){
            output[index] = image.getKey().getWord();
            index++;
        }
        if(text!=null){
            output[index] = text.getKey().getWord();
            index++;
        }
        Record succ = dict.successor(new Pair(prefix, "zzzzzz"));
        if(text==null&&audio==null&&image==null)
            succ = dict.successor(new Pair(prefix, ""));
        Pair currentKey = succ.getKey();
        while(succ!=null&&succ.getKey().getWord().startsWith(prefix)){
            output[index] = succ.getKey().getWord();
            index++;
            succ = dict.successor(currentKey);
            currentKey = succ.getKey();
        }
        String[] actualOutput = new String[index];
        for(int i=0; i<index; i++){
            actualOutput[i] = output[i];
        }
        if(index==0)
            System.out.println("No words in the ordered dictionary start with prefix " + prefix);
        else
            System.out.println(String.join(",", actualOutput));
    }

    /**
     * Finds smallest record in dictionary and displays it
     * @param dict dict to search in
     */
    private static void smallest(OrderedDictionary dict){
        Record result = dict.smallest();
        String output = "";
        if(result!=null) {
            output += result.getKey().getWord();
            output += ", " + result.getKey().getType();
            output += ", " + result.getData();
        }
        System.out.println(output);
    }

    /**
     * Displays largest record in dictionary
     * @param dict dict to search in
     */
    private static void largest(OrderedDictionary dict){
        Record result = dict.largest();
        String output = "";
        if(result!=null) {
            output += result.getKey().getWord();
            output += ", " + result.getKey().getType();
            output += ", " + result.getData();
        }
        System.out.println(output);
    }
}
