package JSONParsing;

import SBT.ASTPrinter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONWriter {
    public static void main(String[] args) throws IOException {
        JSONWriter jw = new JSONWriter();

        // Read the data file
        File data = new File("data/sample.txt");
        BufferedReader br = new BufferedReader(new FileReader(data));
        BufferedWriter bw = new BufferedWriter(new FileWriter("data/data.json", true));
        bw.write("[");
        bw.newLine();
        jw.createJSON(br, bw);
    }

    public void createJSON(BufferedReader br, BufferedWriter bw) throws IOException {
        ASTPrinter astp = new ASTPrinter();
        String line;
        int counter = 0;
        while ((line = br.readLine()) != null) {
            if (line.equals(""))
                continue;
            // Do SBT Stuff
            String code = line.substring(10, line.length() - 2).trim();
            code = code.replace("\\n", "").replace("\\t", "").replace("\\r", "").trim();

            try{
                MethodDeclaration method = StaticJavaParser.parseMethodDeclaration(code);
                String SBT = astp.output(method);
                List<String> comments = astp.getComments(method);
                writeToFile(code, SBT, comments, bw);
                counter++;
                System.out.println(counter + " objects completed.");
            }
            catch (com.github.javaparser.ParseProblemException ignored) {}
        }

        // finish JSON
        bw.write("]");
        bw.close();
    }

    private void writeToFile(String code, String sbt, List<String> comments, BufferedWriter bw) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode codeObject = mapper.createObjectNode();
        codeObject.put("code", code);
        codeObject.put("sbt", sbt);

        // Add comments to json if any
        if(comments.size() > 0){
            codeObject.put("header_comment", comments.get(0));
            if (comments.size() > 1) {
                ObjectNode commentAll = mapper.createObjectNode();
                for(int i = 1; i < comments.size(); i++)
                    commentAll.put("inline_comment" + i, comments.get(i));
                codeObject.set("inline_comments", commentAll);
            }
        }

        // Create json object
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(codeObject + ",");

        // Write to dataset
        bw.write(json);
        bw.newLine();
    }
}