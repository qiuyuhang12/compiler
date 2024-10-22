package ASM;

import ASM.section.data;
import ASM.section.rodata;
import ASM.section.text_new;

import java.util.ArrayList;

public class prog_new {
    public ArrayList<text_new> text_news = new ArrayList<>();
    public ArrayList<data> datas = new ArrayList<>();
    public ArrayList<rodata> rodatas = new ArrayList<>();
    public void pushText(text_new text_new) {
        text_news.add(text_new);
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
        for (int i = 0; i < text_news.size()-1; i++) {
            text_news.get(i).next_label=text_news.get(i+1).label;
        }
        for (text_new text_new : text_news) {
            sb.append(text_new.toString());
        }
        return sb.toString();
    }
}
