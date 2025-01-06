import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException{


        Trie<Integer> wordBank = new Trie<Integer>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/words_alpha.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                String word = parts[0].trim().replaceAll("\"", "");

                if (word.length() > 2 && word.length() < 17) {
                    wordBank.put(word, 1);
                }
            }
        }


        System.out.println("Enter map:");


            Scanner myObj = new Scanner(System.in);


            String input = myObj.nextLine();
            if (input.length() != 16) {
                System.out.println("Map must contain 16 letters!");
                return;
            }
            char[] inputArray = new char[16];
            for (int i = 0; i < inputArray.length; i++) {

                if (!Character.isLetter(input.charAt(i))) {
                   System.out.println("Map must contain only letters!");
                   continue;
                }

                inputArray[i] = Character.toLowerCase(input.charAt(i));
            }

            //create map
            Graph inputMap = new Graph(16);
            for (int u = 0; u < 16; u++) {
                for (int v = 0; v < 16; v++) {
                    if (u != v && isAdj(u, v)) {
                        inputMap.addEdge(u, v, 1);
                    }
                }
            }
            HashMap<String, Integer> answers = new HashMap<>();
            for (int i = 0; i < 16; i++) {
                boolean[] visited = new boolean[16];
                DFS(inputMap, i, visited, new StringBuilder(), wordBank, inputArray, answers);
            }
            ArrayList<String> list = new ArrayList<>();
            list.addAll(answers.keySet());
            Collections.sort(list, (string1, string2) -> answers.get(string2) - answers.get(string1));
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/found_words.txt"));
            for (String word:
                 list) {
                writer.write(word + "   " + answers.get(word));
                writer.newLine();
            }
            writer.close();
            System.out.println("Done!");





    }
    static boolean isAdj(int a, int b) {
        a++;
        b++;
        if (Math.abs(a - b) == 1 && (a - 1) / 4 == (b - 1) / 4) {
            return true;
        }

        if (Math.abs(a - b) == 4) {
            return true;
        }
        if ((a - b == 5) && ((a - 1) % 4 != 0)) {
            return true;
        }
        if ((b - a == 5) && (a % 4 != 0)) {
            return true;
        }
        if ((a - b == 3) && (a % 4 != 0)) {
            return true;
        }
        return (b - a == 3) && ((a - 1) % 4 != 0);
    }
    static void DFS(Graph graph, int u, boolean[] visited, StringBuilder string, Trie wordBank, char[] map, HashMap<String, Integer> answers) {
        visited[u] = true;
        if (wordBank.countPrefixes(string.toString()) == 0) {
            return;
        }
        if (wordBank.containsKey(string.toString())) {
            if (answers.put(string.toString(), calculateScore(string.toString())) == null) {
                System.out.println(string.toString());
            }
        }
        for (Integer outneighbors:
                graph.outNeighbors(u)) {
            if (visited[outneighbors]) {
                continue;
            }
            StringBuilder currString = new StringBuilder(string);

            DFS(graph, outneighbors, Arrays.copyOf(visited, 16), currString.append(map[outneighbors]), wordBank, map, answers);
        }


    }

    static int calculateScore(String string) {
        if (string.length() == 3) {
            return 100;
        }
        if (string.length() > 3 && string.length() < 6) {
            return (string.length() - 3) * 400;
        }
        return (string.length() - 3) * 400 + 200;
    }
}