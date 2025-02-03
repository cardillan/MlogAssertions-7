package cardillan.mlogassertions.logic;

import mindustry.ctype.Content;
import mindustry.ctype.MappableContent;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.logic.ConditionOp;
import mindustry.logic.LExecutor;
import mindustry.logic.LVar;

public class AssertsLInstructions {

    public static class AssertI implements LExecutor.LInstruction {
        public LVar min;
        public ConditionOp opMin = ConditionOp.lessThanEq;
        public LVar value;
        public ConditionOp opMax = ConditionOp.lessThanEq;
        public LVar max;
        public LVar message;

        public AssertI(LVar min, ConditionOp opMin, LVar value, ConditionOp opMax, LVar max, LVar message) {
            this.min = min;
            this.opMin = opMin;
            this.value = value;
            this.opMax = opMax;
            this.max = max;
            this.message = message;
        }

        @Override
        public final void run(LExecutor exec) {
            if (exec.textBuffer.length() >= LExecutor.maxTextBuffer) return;

            if (value.isobj) {
                String strValue = toString(value.objval);
                exec.textBuffer.append(strValue);
            } else {
                exec.textBuffer.append(value.numval);
            }
        }

        public static String toString(Object obj) {
            return
                obj == null ? "null" :
                obj instanceof String s ? s :
                obj instanceof MappableContent content ? content.name :
                obj instanceof Content ? "[content]" :
                obj instanceof Building build ? build.block.name :
                obj instanceof Unit unit ? unit.type.name :
                obj instanceof Enum<?> e ? e.name() :
                obj instanceof Team team ? team.name :
                "[object]";
        }
    }
}
