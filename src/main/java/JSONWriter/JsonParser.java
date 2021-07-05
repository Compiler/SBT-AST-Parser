package JSONWriter;
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


public class JsonParser {

    private String file_path;
    public static String unEscapeString(String s){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<s.length(); i++)
            switch (s.charAt(i)){
                case '\n': sb.append("\\n"); break;
                case '\t': sb.append("\\t"); break;
                // ... rest of escape characters
                default: sb.append(s.charAt(i));
            }
        return sb.toString();
    }
    public String parse_line(String line) throws ParseException {
        //System.out.println("Parsing:\n" + line);
        String saved_val = "";
        try {
            Object obj = new JSONParser().parse(line);
            JSONObject jo = (JSONObject) obj;

            Map code = ((Map) jo.get("code"));
            Iterator<Map.Entry> itr1 = code.entrySet().iterator();
            String final_value = "";
            while (itr1.hasNext()) {
                Map.Entry pair = itr1.next();
                pair.getValue();
                saved_val = pair.getValue().toString();
                final_value = "{\"code\": \"" + unEscapeString(pair.getValue().toString()) + "\"}\n";
            }
            return final_value;
        }catch(Exception e){
            System.out.println("EOF:\n" + line);
            return "";
        }
    }
    public JsonParser(String file_path, String conversion_file_path) throws IOException, ParseException {
        this.file_path = file_path;

        /*try (Stream<String> stream = Files.lines(Paths.get(file_path), Charset.defaultCharset())) {
            stream.forEach(this::print);
        }catch(ParseException | IOException e){}*/
        FileWriter fw = new FileWriter(conversion_file_path, true);
        BufferedWriter bw = new BufferedWriter(fw);

        FileInputStream fstream = new FileInputStream(file_path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        while ((strLine = br.readLine()) != null)   {
            if(strLine == "") continue;
            String parsed_line = parse_line(strLine);
            bw.write(parsed_line);

        }
        bw.close();
        fstream.close();




    }




    public static void main(String... args) throws IOException, ParseException {
        String root = "D:\\Projects\\gitscraper\\resources\\ResultingJSON\\Java\\";
        String path_to_data = root + "comment_code_data1.json";
        String path_to_output_data = root + "luke_to_dan_conversion.json";


        JsonParser parser = new JsonParser(path_to_data, path_to_output_data);
       // JsonParser parser = new JsonParser("data/luke_template", "data/luke_template_parsed.json");


    }
}
