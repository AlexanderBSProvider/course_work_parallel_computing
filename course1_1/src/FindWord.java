import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;


class FindWord implements Runnable {
    private Map<String, List<String>> MapWord ;
    private List<File> Files;

    public FindWord(List<File> Files, Map<String, List<String>> MapWord) {
        this.Files = Files;
        this.MapWord = MapWord;
    }

    public void run() {
        String Line;
        List<String> GetWord;
        List<String> words = new ArrayList<>();
        for (File File : Files) {
            String FileName = File.getName();
            try (FileReader FileReader = new FileReader(File)) {
                BufferedReader BufferedReader = new BufferedReader(FileReader);
                while ((Line = (BufferedReader.readLine())) != null) {
                    words = Arrays.asList(Line.replaceAll("\\W", " ").split(" "));
                }
                    for (String word: words) {
                        //GetWord = this.MapWord.get(word);
                            this.MapWord.putIfAbsent(word, new ArrayList<>());
                            GetWord = this.MapWord.get(word);
                            if (GetWord != null) {
                                if (GetWord.contains(FileName) == false) {
                                    GetWord.add(FileName);
                                }
                        }
                    }
            } catch (IOException e) {
            }
        }
    }

}