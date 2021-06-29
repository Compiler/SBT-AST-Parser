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
    public void parse_line(String line) throws ParseException {
        System.out.println("Parsing:\n" + line);
        try {
            Object obj = new JSONParser().parse(line);
            JSONObject jo = (JSONObject) obj;

            Map code = ((Map) jo.get("code"));
            Iterator<Map.Entry> itr1 = code.entrySet().iterator();
            String final_value = "";
            while (itr1.hasNext()) {
                Map.Entry pair = itr1.next();
                pair.getValue();
                final_value = "{\"code\": \"" + unEscapeString(pair.getValue().toString()) + "\"}\n";
            }
            System.out.println(final_value);
        }catch(Exception e){
            System.out.println("EOF");
        }
    }
    public JsonParser(String file_path) throws IOException, ParseException {
        this.file_path = file_path;

        /*try (Stream<String> stream = Files.lines(Paths.get(file_path), Charset.defaultCharset())) {
            stream.forEach(this::print);
        }catch(ParseException | IOException e){}*/

        FileInputStream fstream = new FileInputStream(file_path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        while ((strLine = br.readLine()) != null)   {
            if(strLine == "") continue;
            parse_line(strLine);
        }
        fstream.close();


    }




    public static void main(String... args) throws IOException, ParseException {

        JsonParser parser = new JsonParser("data/luke_template");

    }
}
