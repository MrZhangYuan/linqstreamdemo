package main.java.com.streamlinq.Ex;

/**
 * Created by yuany on 2017/12/15.
 */

public class StringEx
{
        public static boolean isNullOrEmpty(String string)
        {
                if(string == null
                        || string.length() == 0)
                {
                        return true;
                }
                return false;
        }

        public static String padLeft(String string, char ch, int length)
        {
                if(length < 0)
                {
                        return string;
                }

                int count = length - (string + "").length();
                if(count > 0)
                {
                        for(int i = 0; i < count; i++)
                        {
                                string = ch + string;
                        }
                }
                return string;
        }

        public static String padLeft(Object obj, char ch, int length)
        {
                return StringEx.padLeft(obj + "", ch, length);
        }

        /**
         * 此方法用来替换String.formate的
         * 保持和C#的查询语句兼容
         *
         * @param template
         * @param params
         * @return
         * @throws Exception
         */
        public static String relpace(String template, Object... params)
        {
                for(int i = 0; i < params.length; i++)
                {
                        template = template.replace("{" + i + "}", params[i] == null ? "" : params[i].toString());
                }
                return template;
        }
}
