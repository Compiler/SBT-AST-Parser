package SBT;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithIdentifier;
import com.github.javaparser.metamodel.NodeMetaModel;

import java.util.ArrayList;
import java.util.List;

public class MethodParser {
    private static final int NUM_SPACES_FOR_INDENT = 4;
    StringBuilder result = new StringBuilder();
    String identifier = "";
    int counter = 0, level = 0;

    public static void main(String[] args) {
        MethodDeclaration mt = StaticJavaParser.parseMethodDeclaration("public String extractFor(Integer id){\nLOG.debug(\"Extracting method with ID:{}\", id);\nreturn requests.remove(id);\n}");
        MethodParser mtp = new MethodParser();
        List<Node> children = mt.getChildNodes();
        System.out.println(mtp.SBT(mt, children));
    }

    // SBT Traversal for AST
    public String SBT(Node n, List<Node> children){
        if(children.size() < 1)
            return result.toString();

        NodeMetaModel metaModel = n.getMetaModel();

        if(children.isEmpty()){
            if(n instanceof NodeWithIdentifier)
                identifier = ((NodeWithIdentifier<?>) n).getIdentifier();
            if (!identifier.equals(""))
                result.append("\t").append("(").append(metaModel.getTypeName()).append("_").append(identifier)
                        .append(")").append(metaModel.getTypeName()).append("_").append(identifier).append("\n");
            else
                result.append("\t").append("(").append(metaModel.getTypeName()).append(")").append(metaModel.getTypeName()).append("\n");

        }
        else{
            if(n instanceof NodeWithIdentifier)
                identifier = ((NodeWithIdentifier<?>) n).getIdentifier();

            if (!identifier.equals(""))
                result.append("\t").append("(").append(metaModel.getTypeName()).append("_").append(identifier).append("\n");
            else
                result.append("\t").append("(").append(metaModel.getTypeName()).append("\n");
            // Just get
            List<Node> newChildren = new ArrayList<Node>();
            for(int i = 1; i < children.size(); i++)
                newChildren.add(children.get(i));
            Node child = children.get(counter);
            result.append("\t").append(SBT(child, newChildren));

            if ((identifier != ""))
                result.append("\t").append(")").append(metaModel.getTypeName()).append("_").append(identifier).append("\n");
            else
                result.append("\t").append(")").append(metaModel.getTypeName()).append("\n");

        }

        return result.toString();
    }
}
