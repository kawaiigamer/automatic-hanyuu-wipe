package utils;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Pattern;

public interface Constants {
    public static final String[] cirilic_vowel = new String[]{"у", "е", "ы", "а", "о", "э", "я", "и", "ю"};

    public static final String[] cirilic_consonant = new String[]{
            "й", "ц", "к", "н", "г", "ш", "щ", "з", "х", "ъ",
            "ф", "в", "п", "р", "л", "д", "ж", "ч", "с", "м",
            "т", "ь", "б"};

    public static final String[] ints = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    public static final String[] chars = new String[]{" ", "  ", "   "};

    public static final String CopyPastePath = "./ini/copypaste.txt";

    public static final Random random = new Random();

    public static final String encoding = "UTF-8";

    public static final Charset charset = Charset.forName("UTF-8");

    public static final Pattern rmchars = Pattern.compile("[^0-9]+");

    public static final Object lock = new Object();

    public static final String UserAgent = "Mozilla/5.0 (Windows; U; Windows NT 6.1; ru; rv:1.9.2) Gecko/20100115 Firefox/3.6";
}
