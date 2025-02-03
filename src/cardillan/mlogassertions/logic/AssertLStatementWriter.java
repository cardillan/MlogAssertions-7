package cardillan.mlogassertions.logic;

public class AssertLStatementWriter {
    public static final String SEPARATOR = " ";

    private StringBuilder builder;

    public void start(StringBuilder builder){
        this.builder = builder;
    }

    public void end(){
        // remove the last space
        builder.deleteCharAt(builder.length() - 1);
    }

    public AssertLStatementWriter write(Object obj) {
        return write(String.valueOf(obj)).write(SEPARATOR);
    }

    public AssertLStatementWriter write(String str) {
        builder.append(str).append(SEPARATOR);
        return this;
    }

    public AssertLStatementWriter write(StringBuffer sb) {
        builder.append(sb).append(SEPARATOR);
        return this;
    }

    public AssertLStatementWriter write(CharSequence s) {
        builder.append(s).append(SEPARATOR);
        return this;
    }

    public AssertLStatementWriter write(CharSequence s, int start, int end) {
        builder.append(s, start, end).append(SEPARATOR);
        return this;
    }

    public AssertLStatementWriter write(char[] str) {
        builder.append(str).append(SEPARATOR);
        return this;
    }

    public AssertLStatementWriter write(char[] str, int offset, int len) {
        builder.append(str, offset, len).append(SEPARATOR);
        return this;
    }

    public AssertLStatementWriter write(boolean b) {
        builder.append(b).append(SEPARATOR);
        return this;
    }

    public AssertLStatementWriter write(char c) {
        builder.append(c).append(SEPARATOR);
        return this;
    }

    public AssertLStatementWriter write(int i) {
        builder.append(i).append(SEPARATOR);
        return this;
    }

    public AssertLStatementWriter write(long lng) {
        builder.append(lng).append(SEPARATOR);
        return this;
    }

    public AssertLStatementWriter write(float f) {
        builder.append(f).append(SEPARATOR);
        return this;
    }

    public AssertLStatementWriter write(double d) {
        builder.append(d).append(SEPARATOR);
        return this;
    }
}
