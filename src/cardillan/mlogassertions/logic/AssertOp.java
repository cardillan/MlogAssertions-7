package cardillan.mlogassertions.logic;

public enum AssertOp {
    lessThan("<", (a, b) -> a < b),
    lessThanEq("<=", (a, b) -> a <= b),
    ;

    public static final AssertOp[] all = values();

    public final AssertOpLambda function;
    public final String symbol;

    AssertOp(String symbol, AssertOpLambda function){
        this.symbol = symbol;
        this.function = function;
    }

    @Override
    public String toString(){
        return symbol;
    }

    public interface AssertOpLambda {
        boolean get(double a, double b);
    }
}
