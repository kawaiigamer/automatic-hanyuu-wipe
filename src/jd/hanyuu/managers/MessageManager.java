package hanyuu.managers;

import java.util.TreeSet;

public class MessageManager extends TreeSet<String> {
    private ThreadManager tm;

    public MessageManager(ThreadManager tm) {
        this.tm = tm;
    }

    public String[] getAllMessages() {
        return (String[]) toArray((Object[]) new String[size()]);
    }

    public void deleteMessages() {
    }
}
