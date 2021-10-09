package hanyuu.parser;

import hanyuu.net.wipe.AbstractWipe;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import utils.Constants;

import java.io.InputStream;
import java.util.ArrayList;

public class HornedParser implements Constants {
    private Parser parser = new Parser();

    private final TagNameFilter a = new TagNameFilter("a");

    private final TagNameFilter span = new TagNameFilter("span");

    private final TagNameFilter p = new TagNameFilter("p");

    private ArrayList<String> results = new ArrayList<>();

    private static void parse(InputStream in) {
        try {
            int i;
            while ((i = in.read()) != -1)
                System.out.print((char) i);
        } catch (Exception e) {
        }
    }

    public ArrayList<String> getThreads(InputStream in, AbstractWipe wipe) {
        try {
            this.parser.setLexer(new Lexer(new Page(in, "UTF-8")));
            Node[] al = this.parser.extractAllNodesThatMatch((NodeFilter) this.a).toNodeArray();
            this.results.clear();
            for (Node n : al) {
                String h = n.toHtml();
                if (h.contains((wipe.getChan()).BoardReplayTag))
                    this.results.add(rmchars.split(h)[1]);
            }
            return this.results;
        } catch (Exception e) {
            wipe.setException(e);
            wipe.getThreadManager().handleError(wipe);
            return null;
        }
    }

    public ArrayList<String> getPosts(InputStream in, AbstractWipe wipe) {
        try {
            this.parser.setLexer(new Lexer(new Page(in, "UTF-8")));
            Node[] al = this.parser.extractAllNodesThatMatch((NodeFilter) this.a).toNodeArray();
            this.results.clear();
            for (Node n : al)
                String h = n.toHtml().replaceAll("</a>", "");
            return this.results;
        } catch (Exception e) {
            wipe.setException(e);
            wipe.getThreadManager().handleError(wipe);
            return null;
        }
    }

    public String getMyPost(InputStream in, AbstractWipe wipe) {
        try {
            this.parser.setLexer(new Lexer(new Page(in, "UTF-8")));
            Node[] spans = this.parser.extractAllNodesThatMatch((NodeFilter) this.span).toNodeArray();
            boolean found = false;
            for (Node n : spans) {
                Tag tmp = (Tag) n;
                String attribute = tmp.getAttribute("class");
                String plain = tmp.toPlainTextString();
                if (attribute != null && attribute.contains("reflink")) {
                    String[] splits = rmchars.split(plain);
                    String id = splits[splits.length - 1];
                    for (Node nn : n.getParent().getChildren().toNodeArray()) {
                        if (nn instanceof Tag) {
                            int i;
                            tmp = (Tag) nn;
                            attribute = tmp.getAttribute("class");
                            plain = nn.toPlainTextString();
                            if (attribute != null && attribute.contains("filesize"))
                                i = found | ((plain.contains((wipe.getFile()).w_s) && plain.contains((wipe.getFile()).h_s)) ? 1 : 0);
                            if (i != 0 && wipe.getThreadManager().getMM().add(id))
                                return id;
                        }
                    }
                }
            }
        } catch (Exception e) {
            wipe.setException(e);
            wipe.getThreadManager().handleError(wipe);
        }
        return null;
    }

    private void p(Object o) {
        System.out.println(o);
    }
}
