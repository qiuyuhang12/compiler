package Frontend.IR.util;

import java.util.ArrayList;

public class listToString {
    public static <T> String listToString(ArrayList<T> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) sb.append(", ");
            sb.append(list.get(i).toString());
        }
        return sb.toString();
    }
}
