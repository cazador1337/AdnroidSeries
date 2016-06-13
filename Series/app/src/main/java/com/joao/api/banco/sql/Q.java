package com.joao.api.banco.sql;

/**
 * Created by joao on 11/06/2016.
 */
public class Q {
    public static final Option AND = new Option(" AND "), OR = new Option(" OR ");

    public static String equal(String col) {
        return col + "=?";
    }

    public static Option equal(String col, Object value) {
        return new Option(col + "=?", value.toString());
    }

    public static Option like(String col, Object value) {
        return new Option(col + " like ? ", "%" + value.toString() + "%");
    }

    public static class Option {
        private String expression = null, value = null;

        Option() {
        }

        Option(String expression) {
            this.expression = expression;
        }

        Option(String expression, String value) {
            this.expression = expression;
            this.value = value;
        }

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
