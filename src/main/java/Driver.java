import JSONWriter.JsonParser;

import SBT.ASTPrinter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import JSONWriter.JsonParser;

public class Driver {

    public static void main(String ... args) throws IOException, ParseException {

        String root = "D:\\Projects\\gitscraper\\resources\\ResultingJSON\\Java\\";
        String path_to_scraped_data = root + "comment_code_data1.json";
        String path_to_scraped_to_parsable_format_data = root + "luke_to_dan_conversion_test.json";
        String path_to_put_sbt_data = root + "test_to_sbt.json";


        JsonParser parser = new JsonParser(path_to_scraped_data, path_to_scraped_to_parsable_format_data);


        //after converting file to sbt parsable json format
        JSONParsing.JSONWriter jw = new JSONParsing.JSONWriter();
        File data = new File(path_to_scraped_to_parsable_format_data);
        BufferedReader br = new BufferedReader(new FileReader(data));
        BufferedWriter bw = new BufferedWriter(new FileWriter(path_to_put_sbt_data, true));

        bw.write("[");
        bw.newLine();
        jw.createJSON(br, bw);


    }
}
