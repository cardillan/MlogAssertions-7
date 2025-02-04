package cardillan.mlogassertions.logic;

import mindustry.logic.ConditionOp;

import java.util.Objects;

public enum TypeAssertion {
    any(num -> true, obj -> true),
    notNull(num -> true, Objects::nonNull),
    decimal(num -> true),
    integer(num -> num == (long) num),
    even(num -> num == (long) num && num % 2 == 0),
    odd(num -> num == (long) num && num % 2 != 0),
    ;

    public static final TypeAssertion[] all = values();
    public final TypeAssertionObjLambda objFunction;
    public final TypeAssertionLambda function;

    TypeAssertion(TypeAssertionLambda function) {
        this(function, obj -> false);
    }

    TypeAssertion(TypeAssertionLambda function, TypeAssertionObjLambda objFunction) {
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
