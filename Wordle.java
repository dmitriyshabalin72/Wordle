import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter("log.txt")) {

            WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
            WordleDictionary dictionary =
                    loader.load("/Users/dmitrijsabalin/IdeaProjects/Wordle/src/dictionary.txt");

            WordleGame game = new WordleGame(dictionary, log);

            Scanner scanner = new Scanner(System.in);

            while (!game.isGameOver()) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Подсказка: " + game.getHint());
                    continue;
                }

                try {
                    String result = game.makeGuess(input);
                    System.out.println(result);
                } catch (WordNotFoundException e) {
                    System.out.println("Слово не найдено в словаре");
                } catch (IllegalArgumentException e) {
                    System.out.println("Некорректный ввод: " + e.getMessage());
                }
            }

            System.out.println("Игра окончена!");
            System.out.println("Слово было: " + game.getAnswer());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}