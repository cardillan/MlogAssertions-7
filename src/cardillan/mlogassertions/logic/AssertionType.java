package cardillan.mlogassertions.logic;

import java.util.Objects;

public enum AssertionType {
    any(num -> true, obj -> true),
    notNull(num -> true, Objects::nonNull),
    decimal(num -> true),
    integer(num -> num == (long) num),
    multiple(num -> num == (long) num),
    ;

    public static final AssertionType[] all = values();
    public final TypeAssertionObjLambda objFunction;
    public final TypeAssertionLambda function;

    AssertionType(TypeAssertionLambda function) {
        this(function, obj -> false);
    }

    AssertionType(TypeAssertionLambda function, TypeAssertionObjLambda objFunction) {
        this.function = function;
        this.objFunction = objFunction;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public interface TypeAssertionObjLambda {
        boolean get(Object obj);
    }

    public interface TypeAssertionLambda {
        boolean get(double val);
    }
}
