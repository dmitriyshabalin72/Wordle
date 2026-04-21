import java.util.*;

public class WordleDictionary {

    private final List<String> words;
    private final Set<String> wordSet;
    private final Random random = new Random();

    public WordleDictionary(List<String> words) {
        this.words = words;
        this.wordSet = new HashSet<>(words);
    }

    public boolean contains(String word) {
        return wordSet.contains(word);
    }

    public String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }

    public List<String> getWords() {
        return words;
    }
}