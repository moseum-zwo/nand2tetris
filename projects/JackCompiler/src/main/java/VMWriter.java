import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VMWriter {

    private final BufferedWriter writer;

    public VMWriter(File file) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(file));
    }

    public void writePush(Segment segment, int index) {
        writeLine("push " + segment.toString() +" "+ index);
    }

    public void writePop(Segment segment, int index) {
        writeLine("pop " + segment.toString() +" "+ index);
    }

    public void writeArithmatic(String operation) {
        writeLine(operation);
    }

    public void writeLabel(String label) {
        //TODO
    }

    public void writeGoto(String label) {
        //TODO
    }

    public void writeIf(String condition) {
        //TODO
    }

    public void writeCall(String label) {
        //TODO
    }

    public void writeFunction(String label) {
        //TODO
    }

    public void writeReturn(String label) {
        //TODO
    }

    public void close() throws IOException {
        writer.close();
    }

    private void writeLine(String line) {
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
