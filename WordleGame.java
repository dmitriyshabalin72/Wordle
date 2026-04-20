import java.io.PrintWriter;
import java.util.*;

public class WordleGame {

    private final WordleDictionary dictionary;
    private final String answer;
    private int attemptsLeft = 6;

    private final List<String> history = new ArrayList<>();
    private final Map<String, String> patterns = new HashMap<>();

    private final PrintWriter log;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();
        this.log = log;

        log.println("Ответ: " + answer);
        log.flush();
    }

    public String makeGuess(String word) throws WordNotFoundException {
        word = normalize(word);

        validate(word);

        attemptsLeft--;
        history.add(word);

        String pattern = check(word, answer);
        patterns.put(word, pattern);

        return pattern;
    }

    private void validate(String word) throws WordNotFoundException {
        if (word.length() != 5) {
            throw new IllegalArgumentException("Слово должно быть из 5 букв");
        }

        if (!word.matches("[а-я]+")) {
            throw new IllegalArgumentException("Только русские буквы");
        }

        if (!dictionary.contains(word)) {
            throw new WordNotFoundException();
        }
    }

    // ✅ корректный Wordle-алгоритм с учётом повторов
    private String check(String guess, String answer) {
        char[] result = {'-', '-', '-', '-', '-'};
        Map<Character, Integer> freq = new HashMap<>();

        for (char c : answer.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        // +
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                result[i] = '+';
                freq.put(guess.charAt(i), freq.get(guess.charAt(i)) - 1);
            }
        }

        // ^
        for (int i = 0; i < 5; i++) {
            if (result[i] == '+') continue;

            char c = guess.charAt(i);

            if (freq.getOrDefault(c, 0) > 0) {
                result[i] = '^';
                freq.put(c, freq.get(c) - 1);
            }
        }

        return new String(result);
    }

    public boolean isGameOver() {
        return attemptsLeft <= 0 || history.contains(answer);
    }

    public String getAnswer() {
        return answer;
    }

    public String getHint() {
        List<String> candidates = new ArrayList<>(dictionary.getWords());

        for (String guess : history) {
            String expectedPattern = patterns.get(guess);

            List<String> filtered = new ArrayList<>();

            for (String word : candidates) {
                if (check(guess, word).equals(expectedPattern)) {
                    filtered.add(word);
                }
            }

            candidates = filtered;
        }

        if (candidates.isEmpty()) {
            return dictionary.getRandomWord();
        }

        return candidates.get(new Random().nextInt(candidates.size()));
    }

    private String normalize(String word) {
        return word.toLowerCase().replace('ё', 'е');
    }
}