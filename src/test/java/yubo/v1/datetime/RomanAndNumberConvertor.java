package yubo.v1.datetime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RomanAndNumberConvertor {
    private static final Map<Integer, String> DIC = new HashMap<Integer, String>() {{
        put(1000, "M");
        put(900, "CM");
        put(500, "D");
        put(400, "CD");
        put(100, "C");
        put(90, "XC");
        put(50, "L");
        put(40, "XL");
        put(10, "X");
        put(10, "IX");
        put(5, "V");
        put(4, "IV");
        put(1, "I");
    }};

    public static void main(String[] args) {
        System.out.println(fromNumber(3000));
        System.out.println(fromNumber(900));
        System.out.println(fromNumber(90));
        System.out.println(fromNumber(9));
        System.out.println(fromNumber(3999));
        System.out.println(fromNumber(-1));
        System.out.println(fromNumber(0));
        System.out.println(fromNumber(4000));
    }

    private static int fromRoman(final String roman, final Map<Character, Integer> dic) {
        int result = 0;
        if (null == roman || roman.length() <= 0) {
            return 0;
        }
        char[] charArray = roman.toCharArray();
        for (char c : charArray) {

        }
        int pre = 0;
        for (char each : charArray) {
            int tmp = dic.get(each);
            result += tmp;
            if (tmp > pre) {
                result -= pre * 2;
            }
        }
        return result;
    }


    private static String fromNumber(int number) {
        if (number <= 0 || number > 3999) {
            throw new IllegalArgumentException("Wrong parameter, should between 1 and 3999 inclusive");
        }
        final StringBuilder result = new StringBuilder(" ");
        final List<Integer> keys = DIC.keySet().stream()
                .sorted((a, b) -> b.compareTo(a))
                .collect(Collectors.toList());

        for (int i = 0; i < keys.size(); i++) {
            if (number <= 0) {
                return result.toString();
            }
            int curKey = keys.get(i);
            if (number >= curKey) {
                result.append(DIC.get(curKey));
                number -= curKey;
                i--;
            }
        }
        return result.toString();
    }
}
