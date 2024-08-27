package ASM;

import ASM.section.data;
import ASM.section.rodata;
import ASM.section.text;

import java.util.ArrayList;

public class prog {
    public ArrayList<text> texts = new ArrayList<>();
    public ArrayList<data> datas = new ArrayList<>();
    public ArrayList<rodata> rodatas = new ArrayList<>();
    public void pushText(text text) {
        texts.add(text);
    }
    public void pushData(data data) {
        datas.add(data);
    }
    public void pushRodata(rodata rodata) {
        rodatas.add(rodata);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (rodata rodata : rodatas) {
            sb.append(rodata.toString());
        }
        for (data data : datas) {
            sb.append(data.toString());
        }
        for (text text : texts) {
            sb.append(text.toString());
        }
        return sb.toString();
    }
}
