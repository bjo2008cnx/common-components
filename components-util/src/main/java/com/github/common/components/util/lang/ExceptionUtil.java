package com.github.common.components.util.lang;

public class ExceptionUtil {

    /**
     * 将异常或错误转换成运行时异常
     *
     * @param t
     * @return
     */
    public static RuntimeException transform(Throwable t) {
        RuntimeException re;
        if (t instanceof RuntimeException) {
            re = (RuntimeException) t;
        } else {
            re = new RuntimeException(t);
        }
        return re;
    }

    /**
     * 得到Throwable的堆栈信息
     *
     * @param t
     * @param rows 最长多少行
     * @return
     */
    public static String getStackTrace(Throwable t, int rows) {
        if (t == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        Throwable currentE = t;
        int count = 0;
        while (currentE != null) {
            if (currentE != t) {
                result.append("Caused by: ");
            }
            result.append(currentE.toString());
            result.append("\n");
            for (int i = 0; i < currentE.getStackTrace().length; i++) {
                count++;
                if (rows > 0 && count > rows) {
                    result.append("......\n");
                    break;
                }
                result.append(currentE.getStackTrace()[i]);
                result.append("\n");
            }
            currentE = currentE.getCause();
        }
        return result.toString();
    }

}
