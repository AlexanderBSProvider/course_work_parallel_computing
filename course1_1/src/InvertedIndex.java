import com.sun.source.tree.Tree;

import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Collections;


public class InvertedIndex {
    private static int NumberOfThreads = 32;
    private static String[] DirName = {"D:\\Paral\\aclImdb\\test\\neg", "D:\\Paral\\aclImdb\\test\\pos","D:\\Paral\\aclImdb\\train\\neg","D:\\Paral\\aclImdb\\train\\pos","D:\\Paral\\aclImdb\\train\\unsup"};
    public static void main(String[] args)  throws IOException {
        System.out.print("Введите слово: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String SearchedWord   = reader.readLine();
        long firsttime = System.currentTimeMillis();
        Map<String, List<String>> MapIndex = new HashMap<>();
        Map<String, List<String>> TreeMapIndex = new TreeMap<>();
        List<Thread> threads   = new ArrayList<>();
        List<File> ListOfFiles = getPaths();
        int koef = ListOfFiles.size() / NumberOfThreads;
        for (int i = 0; i < NumberOfThreads; i++){
            Thread thread;
            if (i == NumberOfThreads- 1) {
                thread = new Thread(new FindWord(ListOfFiles.stream().skip(i*koef).collect(Collectors.toList()), TreeMapIndex));
            } else {
                thread = new Thread(new FindWord(ListOfFiles.stream().skip(i*koef).limit(koef).collect(Collectors.toList()), TreeMapIndex));
            }
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Не удалось завершить поток");
            }
        }
        if (TreeMapIndex.get(SearchedWord) != null) {
            System.out.println("Слово найдено в файлах: " + TreeMapIndex.get(SearchedWord));
        } else {
            System.out.println("Слово не найдено (╯°□°）╯︵ ┻━┻");
        }
        System.out.println("Время выполнения: "+(System.currentTimeMillis()- firsttime));

    }

    public static List<File> getPaths() {
        int firstIndex;
        int lastIndex;
        List<File> files = new ArrayList<>();
        List<File> temp;
        for (String dir:DirName) {
            if (dir == "D:\\Paral\\aclImdb\\train\\unsup") {
                firstIndex = 500;
                lastIndex = 1000;
            }
            else {
                firstIndex = 500;
                lastIndex  = 250;
            }
            try {
                temp = Files.walk(Paths.get(dir)).map(Path::toFile).sorted(new Comparator<>(){

                    public int compare(File o1, File o2) {
                        int n1 = extractNumber(o1.getName());
                        int n2 = extractNumber(o2.getName());
                        return n1-n2;
                    }
                    private int extractNumber(String name) {
                        int i = 0;
                        try {
                            String[] strings = name.split("_");
                            i = Integer.parseInt(strings[0]);
                        } catch(Exception e) {
                            i = 0;
                        }
                        return i;
                    }
                }).skip(firstIndex).limit(lastIndex).collect(Collectors.toList());
                files.addAll(temp);
            } catch (IOException e) {
             System.out.println("Файл: "+dir+" не найден");
            }
        }
        return files;
    }
}
