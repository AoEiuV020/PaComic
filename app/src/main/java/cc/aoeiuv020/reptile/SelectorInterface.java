package cc.aoeiuv020.reptile;

import java.util.List;

/**
 * Created by AoEiuV020 on 2016/04/11 - 00:27:40
 */
public interface SelectorInterface {
    List<Object> selectElements(String query, String html);

    String selectString(String query, String html);

    List<Object> selectElements(String query, Object html);

    String selectString(String query, Object html);

    Object getDocument(String html);
}
