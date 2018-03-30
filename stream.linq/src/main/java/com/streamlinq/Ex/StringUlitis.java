package main.java.com.streamlinq.Ex;

/**
 * Created by yuany on 2017/12/15.
 */

public class StringUlitis
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
                return StringUlitis.padLeft(obj + "", ch, length);
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



        public static int LevenshteinDistance(String source, String target)
        {
                if(StringUlitis.isNullOrEmpty(source))
                {
                        if(StringUlitis.isNullOrEmpty(target))
                        {
                                return 0;
                        }
                        return target.length();
                }
                if(StringUlitis.isNullOrEmpty(target))
                {
                        return source.length();
                }

                if(source.length() > target.length())
                {
                        String temp = target;
                        target = source;
                        source = temp;
                }

                int m = target.length();
                int n = source.length();
                int[][] distance = new int[2][m + 1];

                // Initialize the distance 'matrix'
                for(int j = 1; j <= m; j++)
                {
                        distance[0][j] = j;
                }

                int currentRow = 0;
                for(int i = 1; i <= n; ++i)
                {
                        currentRow = i & 1;
                        distance[currentRow][0] = i;
                        int previousRow = currentRow ^ 1;
                        for(int j = 1; j <= m; j++)
                        {
                                int cost = (target.charAt(j - 1) == source.charAt(i - 1) ? 0 : 1);

                                distance[currentRow][j] = Math.min
                                        (
                                                Math.min
                                                        (
                                                                distance[previousRow][j] + 1,
                                                                distance[currentRow][j - 1] + 1
                                                        ),
                                                distance[previousRow][j - 1] + cost
                                        );
                        }
                }
                return distance[currentRow][m];
        }

        public static double LevenshteinDistancePercent(String str1, String str2)
        {
                int maxLenth = Math.max(str1.length(), str2.length());
                int val = LevenshteinDistance(str1, str2);
                return 1 - (double) val / maxLenth;
        }
}
