package Frontend.IR.util;

import java.util.HashMap;
import java.util.Stack;

public class Renamer {
    private boolean inClass=false;
    private String className=null;
    private Stack<HashMap<String, Integer>> nameMapStack=new Stack<>();
    private HashMap<String, Integer> nameMap;
    private int cnt=0;
    public Renamer(){
        nameMap=new HashMap<>();
        nameMapStack.push(nameMap);
    }
    public String getAnonymousName(){
        return "%anon."+cnt++;
    }
    public String rename(String name) {
        if(inClass&&nameMapStack.size()==2){
            nameMapStack.peek().put(name, -1);
            return className+"."+name;
        }
        if (!nameMap.containsKey(name)) {
            nameMap.put(name, 0);
            nameMapStack.peek().put(name, 0);
            return name;
        }
        int cnt = nameMap.get(name);
        nameMap.put(name, cnt + 1);
        nameMapStack.peek().put(name, cnt + 1);
        return name + "." + cnt;
    }

    public String getRenamed(String name) {
        for (int i = nameMapStack.size() - 1; i >= 0; i--) {
            if (nameMapStack.get(i).containsKey(name)) {
                if(inClass&&nameMapStack.size()==2){
                    assert nameMapStack.get(i).get(name)==-1;
                    return "%"+className+"."+name;
                }
                return (i == 0 ? "@" : "%") + name + "." + nameMapStack.get(i).get(name);
            }
        }
        //名字不存在
        assert false;
        return null;
    }

    public void in() {
        HashMap<String, Integer> newMap = new HashMap<>();
        nameMapStack.push(newMap);
    }

    public void in(String className){
        in();
        assert !this.inClass;
        assert this.className==null;
        this.inClass=true;
        this.className=className;
    }

    public void out() {
        nameMapStack.pop();
    }

    public void out(String className){
        out();
        assert this.className.equals(className);
        this.inClass=false;
        this.className=null;
    }
}
