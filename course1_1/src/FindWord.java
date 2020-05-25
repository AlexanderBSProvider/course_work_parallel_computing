import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;


class FindWord implements Runnable {
    private Map<String, List<Integer>> MapWord ;
    private String Word;
    private List<File> Files;

    public FindWord(String Word, List<File> Files, Map<String, List<Integer>> MapWord) {
        this.Word = Word;
        this.Files = Files;
        this.MapWord = MapWord;
    }

    public void run() {
        String Line;
        for (File File : Files) {
            String FileName = File.getName();
            try (FileReader FileReader = new FileReader(File)) {
                BufferedReader BufferedReader = new BufferedReader(FileReader);
                while ((Line = (BufferedReader.readLine())) != null) {
                    List<String> words = Arrays.asList(Line.replaceAll("\\W", " ").split(" "));
                    if (words.contains(Word)) {
                        if (this.MapWord.containsKey(FileName) == false) {
                            this.MapWord.put(FileName,new ArrayList<>());
                        }
                    }
                }
            } catch (IOException e) {
            }
        }
    }

}