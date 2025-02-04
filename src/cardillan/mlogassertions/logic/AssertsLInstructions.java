package cardillan.mlogassertions.logic;

import cardillan.mlogassertions.Assertions;
import mindustry.gen.Building;
import mindustry.logic.LExecutor;
import mindustry.logic.LExecutor.Var;
import mindustry.world.blocks.logic.LogicBlock;

public class AssertsLInstructions {

    public static class AssertI implements LExecutor.LInstruction {
        public TypeAssertion type = TypeAssertion.any;
        public int min;
        public AssertOp opMin = AssertOp.lessThanEq;
        public int value;
        public AssertOp opMax = AssertOp.lessThanEq;
        public int max;
        public int message;

        public AssertI(TypeAssertion type, int min, AssertOp opMin, int value, AssertOp opMax, int max, int message) {
            this.type = type;
            this.min = min;
            this.opMin = opMin;
            this.value = value;
            this.opMax = opMax;
            this.max = max;
            this.message = message;
        }

        public AssertI() {
        }

        @Override
        public final void run(LExecutor exec) {
            Building building = exec.building( LExecutor.varThis);

            Var vaVal = var(exec, value);
            boolean typeAssert = vaVal.isobj ? type.objFunction.get(vaVal.objval) : type.function.get(vaVal.numval);
            boolean minAssert = opMin.function.get(num(exec,min), num(exec,value));
            boolean maxAssert = opMax.function.get(num(exec,value), num(exec,max));

            if (!typeAssert || !minAssert || !maxAssert) {
                Var message = var(exec, this.message);
                Assertions.add((LogicBlock.LogicBuild) building, LExecutor.PrintI.toString(message.objval));

                //skip back to self.
                exec.counter.numval--;
                //exec.yield = true;
            } else {
                Assertions.remove((LogicBlock.LogicBuild) building);
            }
        }

        // For easier switching between v7 and v8 logic

        private Var var(LExecutor exec, int var) {
            return exec.var(var);
        }

        private double num(LExecutor exec, int var) {
            return exec.num(var);
        }
    }
}
