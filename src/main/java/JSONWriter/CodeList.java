package JSONParsing;

import java.util.ArrayList;
import java.util.List;

public class CodeList {
    private List<Code> codeList;

    public CodeList(){
        this.codeList = new ArrayList<Code>();
    }

    public CodeList(List<Code> codeList){
        super();
        this.codeList = codeList;
    }

    public List<Code> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<Code> codeList) {
        this.codeList = codeList;
    }
}
