package main.java.com.streamlinq.Ex;

import java.math.BigDecimal;

/**
 * Created by yuany on 2018/1/15.
 */

public final class NumberEx
{
        public static boolean equals_Int(Integer integer, int compair)
        {
                return integer != null && integer.intValue() == compair;
        }

        public static BigDecimal getValueOrDefault(BigDecimal decimal)
        {
                return decimal == null ? BigDecimal.ZERO : decimal;
        }

        public static BigDecimal getValueOrDefault(BigDecimal decimal, BigDecimal defaultvalue)
        {
                return decimal == null ? defaultvalue : decimal;
        }

        public static Integer getValueOrDefault(Integer integer)
        {
                return integer == null ? Integer.valueOf(0) : integer;
        }

        public static Integer getValueOrDefault(Integer integer, Integer defaultvalue)
        {
                return integer == null ? defaultvalue : integer;
        }


//        public static boolean equals_Int(int integer, int compair)
//        {
//                return integer == compair;
//        }
}
