package sll.plugin.helper.utils;

/**
 * Created by LSL on 2020/1/10 16:24
 */
public class WordConvertUtil {

    /**
     * 单词转驼峰命名
     *
     * @param inputString             转换单词
     * @param firstCharacterUppercase 首字母是否大写
     * @return
     */
    public static String getCamelCaseString(String inputString,
                                            boolean firstCharacterUppercase) {
        StringBuilder sb = new StringBuilder();

        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);

            switch (c) {
                case '_':
                case '-':
                case '@':
                case '$':
                case '#':
                case ' ':
                case '/':
                case '&':
                    if (sb.length() > 0) {
                        nextUpperCase = true;
                    }
                    break;

                default:
                    if (nextUpperCase) {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    } else {
                        sb.append(c);
//                        sb.append(Character.toLowerCase(c)); 原2020/1/15改
                    }
                    break;
            }
        }

        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        return sb.toString();
    }

    private WordConvertUtil() {
    }
}
