package az.dev.smallbankingapp.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public final class MessageUtil {

    private MessageUtil() {
        throw new UnsupportedOperationException("Can't instantiate a util class");
    }

    public static String resolveMessage(String message, Object... args) {
        boolean isBlankMessage = StringUtils.isBlank(message);
        boolean isEmptyArgs = (ArrayUtils.isEmpty(args) || args[0] == null);

        if (isBlankMessage && isEmptyArgs) {
            return null;
        }

        if (isBlankMessage) {
            message = String.valueOf(args[0]);

            if (args.length == 1) {
                return message;
            }

            args = ArrayUtils.subarray(args, 1, args.length);
        } else if (isEmptyArgs) {
            return message;
        }

        return bindArguments(message, args);
    }

    private static String bindArguments(String message, Object... args) {
        StringBuilder sb = new StringBuilder(message);
        String placeHolder = "{}";

        for (Object arg : args) {
            int index = sb.indexOf(placeHolder);

            if (index == -1) {
                break;
            }

            sb.replace(index, index + placeHolder.length(), String.valueOf(arg));
        }

        return sb.toString();
    }

}