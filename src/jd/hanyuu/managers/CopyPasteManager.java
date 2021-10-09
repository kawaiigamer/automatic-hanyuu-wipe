package hanyuu.managers;

import config.Config;
import utils.Constants;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CopyPasteManager extends ArrayList<String> implements Constants {
    private String paste = "";

    public CopyPasteManager() {
        load("./ini/copypaste.txt");
    }

    public void load(String File) {
        try {
            clear();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(File), "UTF-8"));
            String s2 = "";
            if (Config.emptySeparator) {
                String s;
                while ((s = br.readLine()) != null) {
                    s2 = s2 + s + "\n";
                    if (s.isEmpty()) {
                        add(s2);
                        s2 = "";
                    }
                }
                add(s2);
            } else {
                String s;
                while ((s = br.readLine()) != null) {
                    s2 = s2 + s + "\n";
                    if (s.contains(Config.separator)) {
                        add(s2.substring(0, s2.length() - Config.separator.length() - 1));
                        s2 = "";
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getRandomXY(int min, int max) {
        return min + (int) Math.floor(random.nextDouble() * (max - min + 1));
    }

    public String getRandomCirilicString() {
        int count = getRandomXY(5, 256);
        String tmp = "";
        int m = getRandomXY(0, 3);
        for (int i = 0; i < count; i++) {
            if (m == 0)
                tmp = tmp + cirilic_vowel[random.nextInt(cirilic_vowel.length)];
            if (m == 1)
                tmp = tmp + cirilic_consonant[random.nextInt(cirilic_consonant.length)];
            if (m == 2)
                tmp = tmp + ints[random.nextInt(ints.length)];
            if (m == 3)
                tmp = tmp + chars[random.nextInt(chars.length)];
            m = getRandomXY(0, 3);
        }
        return tmp;
    }

    public String getCopyPaste() {
        String tmp = get(random.nextInt(size()));
        if (this.paste.contains(tmp)) {
            getCopyPaste();
        } else {
            this.paste = tmp;
        }
        return tmp;
    }

    public String randomize(String paste) {
        String result = "";
        String[] words = paste.split(" ");
        for (String s : words) {
            int length = s.length();
            if (length < 3) {
                result = result + " " + s;
            } else {
                for (int i = 0; i < Config.rndCount; i++) {
                    char c2;
                    String tmp = "";
                    char c1 = s.charAt(random.nextInt(length));
                    if (s.indexOf(c1) + 1 < s.length()) {
                        c2 = s.charAt(s.indexOf(c1) + 1);
                    } else {
                        c2 = s.charAt(s.indexOf(c1) - 1);
                    }
                    boolean r1 = false, r2 = false;
                    char[] array = null;
                    if (Config.rndCount > 1 && !tmp.isEmpty()) {
                        array = tmp.toCharArray();
                    } else {
                        array = s.toCharArray();
                    }
                    for (char c : array) {
                        if (s.toCharArray()[0] == c) {
                            tmp = tmp + c;
                        } else {
                            if (c == c1 && !r1) {
                                r1 = true;
                                c = c2;
                            }
                            if (c == c2 && !r2) {
                                r2 = true;
                                c = c1;
                            }
                            tmp = tmp + c;
                        }
                    }
                    s = tmp;
                }
                result = result + " " + s;
            }
        }
        return result;
    }
}
