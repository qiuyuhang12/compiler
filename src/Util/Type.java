package Util;

import java.util.ArrayList;
import java.util.HashMap;

public class Type {
    public enum TypeEnum {
        VOID,
        BOOL,
        INT,
        STRING,
        NULL,
//        ARRAY,
        CLASS
    }
    public boolean isfString=false;
    public TypeEnum type = null;
//    public boolean
//            isVoid = false,
//            isBool = false,
//            isInt = false,
//            isString = false;
    public boolean isArray = false;
    public boolean isFun = false;
    public int dim=0;
//    public boolean isClass = false;
    public String name = null;
//    public HashMap<String, Type> members = null;
}
