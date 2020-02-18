package at.matthew.dbexample;

import java.util.ArrayList;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        System.err.println("Welcome to dbexample!");

        DB db = new DB();

        // process commands forever...
        //noinspection InfiniteLoopStatement
        while (true) {
            String inputString = System.console().readLine();
            ArrayList<String> tokens = tokenizeOptionallyQuotedStrings(inputString);
            int argc = tokens.size();

            if (argc < 1) {
                System.err.println("CMD [key [value]]");
            }
            else {
                String cmd = tokens.get(0).toLowerCase();
                switch (cmd) {
                    case "get":
                        if (argc != 2) {
                            System.err.println("usage: GET <key>");
                        }
                        else {
                            String key = tokens.get(1);
                            String result = db.get(key);
                            if (result == null)
                                System.err.println("key \"" + key + "\" not found");
                            else
                                System.out.println(result);
                        }
                        break;
                    case "set":
                        if (argc != 3) {
                            System.err.println("usage: SET <key> <value>");
                        }
                        else {
                            String key = tokens.get(1);
                            String value = tokens.get(2);
                            db.set(key, value);
                        }
                        break;
                    case "unset":
                        if (argc != 2) {
                            System.err.println("usage: UNSET <key>");
                        }
                        else {
                            String key = tokens.get(1);
                            db.unset(key);
                        }
                        break;
                    case "exists":
                        if (argc != 2) {
                            System.err.println("usage: EXISTS <key>");
                        }
                        else {
                            String key = tokens.get(1);
                            boolean result = db.exists(key);
                            System.out.println(result ? "YES" : "NO");
                        }
                        break;
                    case "begin":
                        if (argc != 1) {
                            System.err.println("usage: BEGIN");
                        }
                        else {
                            db.begin();
                        }
                        break;
                    case "rollback":
                        if (argc != 1) {
                            System.err.println("usage: ROLLBACK");
                        }
                        else {
                            if (!db.rollback()) {
                                System.err.println("unable to roll back, not in transaction");
                            }
                        }
                        break;
                    case "commit":
                        if (argc != 1) {
                            System.err.println("usage: COMMIT");
                        }
                        else {
                            if (!db.commit())
                                System.err.println("unable to commit, not in transaction");
                        }
                        break;
                    default:
                        System.out.println("CMD [key [value]] where CMD is one of GET, SET, UNSET, EXISTS, BEGIN, ROLLBACK, COMMIT");
                        break;
                }
            }
        }
    }


    private static ArrayList<String> tokenizeOptionallyQuotedStrings(String input) {
        final String searchPattern = "[^\"\\s]+|\"(\\\\.|[^\\\\\"])*\"";   // find unquoted or quoted (including escaped) tokens
        final String stripPattern = "^\"|\"$";   // find leading or trailing quote

        String token;
        Scanner scanner = new Scanner(input);
        ArrayList<String> result = new ArrayList<>();

        while ((token = scanner.findInLine(searchPattern)) != null) {
            token = token.replaceAll(stripPattern, "");
            result.add(token);
        }

        return result;
    }
}
