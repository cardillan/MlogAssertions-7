package cardillan.mlogassertions.logic;

import cardillan.mlogassertions.Assertions;
import mindustry.gen.Building;
import mindustry.logic.LExecutor;
import mindustry.logic.LExecutor.Var;
import mindustry.world.blocks.logic.LogicBlock;

public class LogicInstructions {

    public static class AssertI implements LExecutor.LInstruction {
        public AssertionType type = AssertionType.any;
        public int multiple = 1;
        public int min;
        public AssertOp opMin = AssertOp.lessThanEq;
        public int value;
        public AssertOp opMax = AssertOp.lessThanEq;
        public int max;
        public int message;

        public AssertI(AssertionType type, int multiple, int min, AssertOp opMin, int value, AssertOp opMax, int max, int message) {
            this.type = type;
            this.multiple = multiple;
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
            if ((vaVal.isobj ? type.objFunction.get(vaVal.objval) : type.function.get(vaVal.numval))
                    && (type != AssertionType.multiple || (vaVal.numval % var(exec, multiple).numval == 0))
                    && (opMin.function.get(num(exec,min), num(exec,value)))
                    && (opMax.function.get(num(exec,value), num(exec,max)))) {
                Assertions.remove((LogicBlock.LogicBuild) building);
            } else {
                Var message = var(exec, this.message);
                Assertions.add((LogicBlock.LogicBuild) building, LExecutor.PrintI.toString(message.objval));

                //skip back to self.
                exec.counter.numval--;
                //exec.yield = true;
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
