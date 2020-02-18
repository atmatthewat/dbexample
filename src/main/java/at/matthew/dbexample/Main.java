package at.matthew.dbexample;

import java.util.Scanner;

class Main {
    private static final String searchPattern = "[^\"\\s]+|\"(\\\\.|[^\\\\\"])*\"";   // find unquoted or quoted (including escaped) tokens
    private static final String stripPattern = "^\"|\"$";   // find leading or trailing quote

    public static void main(String[] args) {
        System.err.println("Welcome to dbexample!");

        DB db = new DB();

        // process commands forever...
        //noinspection InfiniteLoopStatement
        while (true) {
            String inputString = System.console().readLine();
            Scanner scanner = new Scanner(inputString);
            String cmd = scanner.findInLine(searchPattern);
            String key = scanner.findInLine(searchPattern);
            if (key != null)
                key = key.replaceAll(stripPattern, "");
            String value = scanner.findInLine(searchPattern);
            if (value != null)
                value = value.replaceAll(stripPattern, "");


            if (cmd == null) {
                System.err.println("CMD [key [value]]");
            }
            else {
                switch (cmd.toLowerCase()) {
                    case "get":
                        if (key == null) {
                            System.err.println("usage: GET <key>");
                        }
                        else {
                            String result = db.get(key);
                            if (result == null)
                                System.err.println("key \"" + key + "\" not found");
                            else
                                System.out.println(result);
                        }
                        break;
                    case "set":
                        if (key == null || value == null) {
                            System.err.println("usage: SET <key> <value>");
                        }
                        else {
                            db.set(key, value);
                        }
                        break;
                    case "unset":
                        if (key == null) {
                            System.err.println("usage: UNSET <key>");
                        }
                        else {
                            db.unset(key);
                        }
                        break;
                    case "exists":
                        if (key == null) {
                            System.err.println("usage: EXISTS <key>");
                        }
                        else {
                            boolean result = db.exists(key);
                            System.out.println(result ? "YES" : "NO");
                        }
                        break;
                    case "begin":
                        db.begin();
                        break;
                    case "rollback":
                        if (!db.rollback()) {
                            System.err.println("unable to roll back, not in transaction");
                        }
                        break;
                    case "commit":
                        if (!db.commit())
                            System.err.println("unable to commit, not in transaction");
                        break;
                    default:
                        System.out.println("CMD [key [value]] where CMD is one of GET, SET, UNSET, EXISTS, BEGIN, ROLLBACK, COMMIT");
                        break;
                }
            }
        }
    }
}
