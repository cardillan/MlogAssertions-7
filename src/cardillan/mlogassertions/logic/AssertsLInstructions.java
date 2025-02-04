package cardillan.mlogassertions.logic;

import mindustry.logic.LExecutor;
import mindustry.logic.LVar;

public class AssertsLInstructions {

    public static class AssertI implements LExecutor.LInstruction {
        public TypeAssertion type;
        public LVar min;
        public AssertOp opMin;
        public LVar value;
        public AssertOp opMax;
        public LVar max;
        public LVar message;

        public AssertI(TypeAssertion type, LVar min, AssertOp opMin, LVar value, AssertOp opMax, LVar max, LVar message) {
            this.type = type;
            this.min = min;
            this.opMin = opMin;
            this.value = value;
            this.opMax = opMax;
            this.max = max;
            this.message = message;
        }

        @Override
        public final void run(LExecutor exec) {
            boolean typeAssert = value.isobj ? type.objFunction.get(value.objval) : type.function.get(value.numval);
            boolean minAssert = opMin.function.get(min.num(), value.num());
            boolean maxAssert = opMax.function.get(value.num(), max.num());

            if (!typeAssert || !minAssert || !maxAssert) {
                //skip back to self.
                // TODO enter broken assertion state
                exec.counter.numval--;
                exec.yield = true;
            }
        }
    }
}
