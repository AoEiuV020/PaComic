package com.aoeiuv020.reptile;

import com.aoeiuv020.tool.Logger;
import com.aoeiuv020.tool.Tool;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by AoEiuV020 on 2016/04/11 - 00:52:48
 */
public class XPathSelector implements SelectorInterface {
    private static final String TAG = "aoeiuv020 XPathSelector";
    private XPath mXPath = null;
    private HtmlCleaner mCleaner = null;
    private DomSerializer mSerializer = null;

    public XPathSelector() {
        mXPath = XPathFactory.newInstance().newXPath();
        mCleaner = new HtmlCleaner();
        mSerializer = new DomSerializer(new CleanerProperties());
    }

    public Object getDocument(String html) {
        Document document = null;
        try {
            TagNode tag = mCleaner.clean(html);
            document = mSerializer.createDOM(tag);
        } catch (Exception e) {
        }
        return document;
    }

    public List<Object> selectElements(String query, String html) {
        return selectElements(query, getDocument(html));
    }

    public String selectString(String query, String html) {
        return selectString(query, getDocument(html));
    }

    public List<Object> selectElements(String query, Object html) {
        if (Tool.isEmpty(query) || Tool.isEmpty(html))
            return null;
        List<Object> list = null;
        try {
            NodeList nodes = (NodeList) mXPath.evaluate(query, html, XPathConstants.NODESET);
            list = new LinkedList<Object>();
            for (int i = 0; i < nodes.getLength(); ++i) {
                list.add(nodes.item(i));
            }
        } catch (XPathExpressionException e) {
        }
        return list;
    }

    public String selectString(String query, Object html) {
        if (Tool.isEmpty(query) || Tool.isEmpty(html))
            return null;
        String result = null;
        try {
            result = mXPath.evaluate(query, html, XPathConstants.STRING).toString();
        } catch (XPathExpressionException e) {
            Logger.e(e);
        }
        return result;
    }
}
