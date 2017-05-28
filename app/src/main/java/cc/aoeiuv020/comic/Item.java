package cc.aoeiuv020.comic;

/**
 * Created by AoEiuV020 on 2016/04/06 - 05:18:47
 */
public class Item {
    public String image = null;
    public String title = null;
    public String content = null;
    public String url = null;

    public String toString() {
        return String.format("image=%s,title=%s,content=%s,url=%s,", image, title, content, url);
    }
}
