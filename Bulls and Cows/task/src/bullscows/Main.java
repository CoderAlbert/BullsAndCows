package bullscows;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    private final char BULL_SYMBOL = '*';
    private final char COW_SYMBOL = '?';
    private final String CODESET = "0123456789abcdefghijklmnopqrstuvwxyz";
    private final int AMOUNT_SYMBOLS;
    private final int LENGTH_SECRET_CODE;
    private String secretCode;

    public Main(int maxSymbols, int lenSecretNum) {
        this.AMOUNT_SYMBOLS = maxSymbols;
        this.LENGTH_SECRET_CODE = lenSecretNum;
    }

    private static boolean isUserLengthInputCorrect(String input) {
        return input.matches("\\d+") && Integer.parseInt(input) <= 36;
    }

    public void setSecretCode(String code) {
        this.secretCode = code;
    }
    public String getSecretCode() {
        return secretCode;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the length of the secret code:");
        String lenSecretNum = scanner.nextLine();

        if(!isUserLengthInputCorrect(lenSecretNum) || Integer.parseInt(lenSecretNum) <= 0) {
            System.out.println("Error: user input is not correct.");
            return;
        }

        System.out.println("Input the number of possible symbols in the code:");
        String maxSymbols = scanner.next();
        if(!isUserLengthInputCorrect(maxSymbols) || (Integer.parseInt(lenSecretNum) > Integer.parseInt(maxSymbols))) {
            System.out.println("Error: user input is not correct.");
            return;
        }
        Main main = new Main(Integer.parseInt(maxSymbols), Integer.parseInt(lenSecretNum));
        main.generateSecretCode();
        main.printCodeAsStarsAndPossibleChars();

        main.startGame();

    }

    private void startGame() {
        String guess = null;
        int turn = 1;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Okay, let's start a game!");

        while(!Objects.equals(this.secretCode, guess)) {
            System.out.println("Turn " + turn + ":");
            guess = scanner.nextLine();
            String codeWithBulls = addBullsToTmpCode(guess);
            String codeWithCowsAndBulls = addCowsToTmpCode(codeWithBulls, guess);
            int bulls = getBullsAndCows(BULL_SYMBOL, codeWithCowsAndBulls);
            int cows = getBullsAndCows(COW_SYMBOL, codeWithCowsAndBulls);
            printGrade(bulls, cows);
            turn++;
        }

        System.out.println("Congratulations! You guessed the secret code.");

    }

    private void printGrade(int bulls, int cows) {
        StringBuilder grade = new StringBuilder("Grade: ");

        if(bulls > 0) {
            grade.append(bulls).append(" bull(s)");
        }
        if(bulls > 0 && cows > 0) {
            grade.append(" and ").append(cows).append(" cow");
        }
        if(cows > 0 && bulls == 0) {
            grade.append(cows).append(" cow");
        }

        if(cows == 0 && bulls == 0) {
            grade.append("None.");
        }
        System.out.println(grade);
    }

    private int getBullsAndCows(char symbol, String codeWithBullsAndCows) {
        int result = 0;
        for(int i = 0; i < codeWithBullsAndCows.length(); i++) {
            if(codeWithBullsAndCows.charAt(i) == symbol) {
                result++;
            }
        }

        return result;
    }

    private String addBullsToTmpCode(String guess) {
        StringBuilder tmpSecretCode = new StringBuilder(secretCode);
        for(int i = 0; i < tmpSecretCode.length(); i++) {
            if(tmpSecretCode.charAt(i) == guess.charAt(i)) {
                tmpSecretCode.setCharAt(i, BULL_SYMBOL);
            }
        }
        return tmpSecretCode.toString();
    }

    private String addCowsToTmpCode(String codeWithBulls, String guess) {
        StringBuilder codeWithBullsAndCows = new StringBuilder(codeWithBulls);
        for(int i = 0; i < guess.length(); i++) {
            for(int j = 0; j < codeWithBullsAndCows.length(); j++) {
                if(guess.charAt(i) == codeWithBullsAndCows.charAt(j)) {
                    codeWithBullsAndCows.setCharAt(i, COW_SYMBOL);
                    break;
                }
            }
        }

        return codeWithBullsAndCows.toString();
    }

    private String getCharSetRange() {
        String range;
        if(AMOUNT_SYMBOLS <= 10) {
            range = "(0-" + CODESET.charAt(AMOUNT_SYMBOLS - 1) + ").";
        }
        else if(AMOUNT_SYMBOLS == 11) {
            range = "(0-9, a)";
        }
        else {
            range = "(0-9, a-" + CODESET.charAt(AMOUNT_SYMBOLS - 1) + ").";
        }

        return range;
    }

    private void printCodeAsStarsAndPossibleChars() {
        String secretCode = IntStream.range(0, LENGTH_SECRET_CODE).mapToObj(i -> "*").collect((Collectors.joining("")));
        System.out.println("The secret is prepared: " + secretCode + " " + getCharSetRange());
    }

    private void  generateSecretCode() {
        StringBuilder result = new StringBuilder();
        StringBuilder copyCodeSet = new StringBuilder(CODESET);
        Random rand = new Random();

        while(result.length() != LENGTH_SECRET_CODE) {
            int randomIdx = rand.nextInt(AMOUNT_SYMBOLS);
            if(copyCodeSet.charAt(randomIdx) != '-' &&
                    !result.toString().contains("" + copyCodeSet.charAt(randomIdx))) {
                result.append(CODESET.charAt(randomIdx));
                copyCodeSet.setCharAt(randomIdx, '-'); // do not add twice same character so replace added char with minus
            }

        }

        this.setSecretCode(result.toString());
    }


}
