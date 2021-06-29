package SBT;/*
 Modified code to fit SBT output
 https://github.com/javaparser/javaparser/blob/master/javaparser-core/src/main/java/com/github/javaparser/printer/YamlPrinter.java
 */

import static com.github.javaparser.utils.Utils.assertNotNull;
import static com.github.javaparser.utils.Utils.capitalize;
import static java.util.stream.Collectors.toList;

import java.util.LinkedList;
import java.util.List;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.metamodel.PropertyMetaModel;
import com.github.javaparser.printer.YamlPrinter;

public class ASTPrinter {
    String result = "";
    private static final int NUM_SPACES_FOR_INDENT = 4;
    private final boolean outputNodeType = true;

    public static void main(String[] args){
        ASTPrinter astp = new ASTPrinter();
        MethodDeclaration method = StaticJavaParser.parseMethodDeclaration("// This is the method header\n" +
                "    public String extractFor(Integer id){\n" +
                "        LOG.debug(\"Extracting method with ID:{}\", id);  // This is inline with LOG.debug\n" +
                "        \n" +
                "        // This is above return statement\n" +
                "        return requests.remove(id);\n" +
                "    }");
        System.out.println(astp.output(method));
    }


    // Getting all commments in method, header, inline, and orphaned
    public List<String> getComments(Node node){
        List<String> comments = new LinkedList<String>();

        String headerComment = node.getComment().toString();
        List<Comment> inlineComments = node.getAllContainedComments();

        // Getting comments between [ ... ]
        if(!headerComment.equals("Optional.empty")){
            headerComment = headerComment.substring(headerComment.indexOf("[") + 1);
            headerComment = headerComment.substring(0, headerComment.indexOf("]"));
            comments.add(headerComment);
        }


        for(Comment com: inlineComments)
            comments.add(com.toString());

        return comments;
    }

    public String output(Node node) {
        try{
            StringBuilder output = new StringBuilder();
            output.append("---");
            output(node, "root", 0, output);
            output.append(System.lineSeparator() + "...");
            return output.toString();
        }
        catch (com.github.javaparser.ParseProblemException e){
            return "error bitch";
        }
    }

    public void output(Node node, String name, int level, StringBuilder builder) {
        // System.out.println(" !!!!!" + name);
        try{
            assertNotNull(node);
            NodeMetaModel metaModel = node.getMetaModel();
            List<PropertyMetaModel> allPropertyMetaModels = metaModel.getAllPropertyMetaModels();
            List<PropertyMetaModel> attributes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isAttribute)
                    .filter(PropertyMetaModel::isSingular).collect(toList());
            List<PropertyMetaModel> subNodes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNode)
                    .filter(PropertyMetaModel::isSingular).collect(toList());
            List<PropertyMetaModel> subLists = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNodeList)
                    .collect(toList());

            if (outputNodeType)
                builder.append(System.lineSeparator() + indent(level) + "(" + metaModel.getTypeName());

            level++;
            for (PropertyMetaModel a : attributes) {
                builder.append("_" + escapeValue(a.getValue(node).toString()));
                builder.append(System.lineSeparator() +  indent(level) + "(" + metaModel.getTypeName() + "_" + escapeValue(a.getValue(node).toString()));
            }

            for (PropertyMetaModel sn : subNodes) {
                Node nd = (Node) sn.getValue(node);
                if (nd != null){
                    output(nd, sn.getName(), level, builder);
                    builder.append( System.lineSeparator() + indent(level) + ")" + metaModel.getTypeName());
                    level--;
                }
            }

            for (PropertyMetaModel sl : subLists) {
                NodeList<? extends Node> nl = (NodeList<? extends Node>) sl.getValue(node);
                if (nl != null && nl.isNonEmpty()) {
                    //builder.append(System.lineSeparator() + indent(level) + sl.getName() + ": ");
                    String slName = sl.getName();
                    slName = slName.endsWith("s") ? slName.substring(0, sl.getName().length() - 1) : slName;
                    for (Node nd : nl)
                        output(nd, "- " + slName, level , builder);
                        builder.append( ")" + metaModel.getTypeName() + "_" + slName );
                }
            }
        }
        catch (com.github.javaparser.ParseProblemException e){
            System.out.println("Error bitch");
        }
    }

    private String indent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++)
            for (int j = 0; j < NUM_SPACES_FOR_INDENT; j++)
                sb.append(" ");
        return sb.toString();
    }

    private String escapeValue(String value) {
        return "\"" + value
                .replace("\\", "\\\\")
                .replaceAll("\"", "\\\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\f", "\\f")
                .replace("\b", "\\b")
                .replace("\t", "\\t") + "\"";
    }

    public static void print(Node node) {
        System.out.println(new YamlPrinter(true).output(node));
    }
}
