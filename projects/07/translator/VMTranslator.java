import java.io.*;

public class VMTranslator {

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 1 || !args[0].endsWith(".vm")) {
            System.out.println("Usage: java -jar vmtrans.jar filename.vm");
            return;
        }

        // convert file name to streams
        String inFile = args[0];
        String outFile = inFile.substring(0, inFile.length() - ".vm".length()) + ".asm";
        FileInputStream in = new FileInputStream(inFile);
        FileOutputStream out = new FileOutputStream(outFile);

        // translate
        Parser parser = new Parser(in);
        CodeWriter coder = new CodeWriter(out);
        while (parser.hasMoreCommands()) {
            parser.advance();
            switch (parser.commandType()) {
                case C_ARITHMETIC:
                    coder.writeArithmetic(parser.arg1());
                break;
                case C_PUSH: case C_POP:
                    coder.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
                break;
            }
        }

        // close the file
        coder.close();
    }
}
