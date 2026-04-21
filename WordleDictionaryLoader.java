import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordleDictionaryLoader {

    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public WordleDictionary load(String path) throws IOException {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                line = normalize(line);

                if (line.length() == 5 && line.matches("[а-я]+")) {
                    words.add(line);
                }
            }
        }

        if (words.isEmpty()) {
            throw new RuntimeException("Словарь пуст");
        }

        log.println("Загружено слов: " + words.size());
        log.flush();

        return new WordleDictionary(words);
    }

    private String normalize(String word) {
        return word.toLowerCase().replace('ё', 'е');
    }
}