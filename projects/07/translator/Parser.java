import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    /** All command types */
    public enum CommandType {
        C_ARITHMETIC,
        C_PUSH, C_POP,
        C_LABEL, C_GOTO,
        C_IF, C_FUNCTION,
        C_RETURN, C_CALL
    }

    /** Exception of parser processing */
    public static class ParseException extends RuntimeException {
        public ParseException(int lineNumber, String msg) {
            super(String.format("Line %d: %s", lineNumber, msg));
        }
    }

    /** Represents a line */
    private class Line {
        int lineNumber;
        String command;
        public Line(int lineNumber, String command) {
            this.lineNumber = lineNumber;
            this.command = command;
        }
    }

    private List<Line> lines;
    private int idx;

    public Parser(InputStream in) {
        this.idx = -1;

        // read all lines to a list
        Scanner scanner = new Scanner(in);
        int lineCount = 1;
        this.lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().strip();

            // filter comments and white lines
            if (!command.startsWith("//") && !command.equals("")) {
                lines.add(new Line(
                        lineCount,
                        command
                ));
            }

            lineCount += 1;
        }
    }

    /** Check if there are more commands to parse */
    public boolean hasMoreCommands() {
        return this.idx != lines.size() - 1;
    }

    /** Advanced to the next command */
    public void advance() {
        this.idx += 1;
    }

    /** Return the type of current command */
    public CommandType commandType() {
        String[] pieces = command().split(" ");

        switch (pieces[0]) {
            // arithmetic & logical command
            case "add": case "sub": case "neg": case "eq": case "gt": case "lt": case "and": case "or": case "not":
                if (pieces.length == 1) {
                    return CommandType.C_ARITHMETIC;
                }
            break;

            // push command
            case "push": case "pop":
                if (pieces.length == 3) {
                    return pieces[0].equals("push") ? CommandType.C_PUSH : CommandType.C_POP;
                }
            break;

            // TODO check the braching and function command type
        }

        throw new ParseException(lineNumber(), String.format("unrecognized command: \"%s\".", command()));
    }

    /** Return the first argument */
    public String arg1() {
        if (this.commandType() == CommandType.C_ARITHMETIC) {
            return this.command();
        } else {
            String[] pieces = this.command().split(" ");
            return pieces[1];
        }
    }

    /** Return the second argument; only should be called if the command is C_PUSH, C_POP, C_FUNCTION, C_CALL */
    public int arg2() {
        String argStr = this.command().split(" ")[2];
        try {
            int arg = Integer.parseInt(argStr);

            // the argument cannot be negative
            if (arg < 0) {
                throw new Exception();
            }

            return arg;
        } catch (Exception e) {
            throw new ParseException(lineNumber(), String.format("the second argument of \"%s\" command should be a non-negative integer.", commandType()));
        }
    }

    /** Return the current command */
    private String command() {
        return this.lines.get(idx).command;
    }

    /** Return the current line number */
    public int lineNumber() {
        return this.lines.get(idx).lineNumber;
    }
}
