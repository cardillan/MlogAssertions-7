package cardillan.mlogassertions.logic;

import arc.func.Cons;
import arc.func.Prov;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.gen.LogicIO;
import mindustry.logic.ConditionOp;
import mindustry.logic.LAssembler;
import mindustry.logic.LCategory;
import mindustry.logic.LExecutor;
import mindustry.logic.LStatement;
import mindustry.logic.LVar;
import mindustry.ui.Styles;

public class AssertLStatements {
    private static final String opcode = "assert";
    private static final AssertLStatementWriter writer = new AssertLStatementWriter();

    @SuppressWarnings("unchecked")
    private static final Seq<Prov<AssertStatement>> statements = Seq.with(AssertStatement::new);

    public static void register() {
        Seq<Prov<LStatement>> seq = statements.map(prov -> prov::get);
        LogicIO.allStatements.addAll(seq);

        // Customized LStatements will register customized reader automatically.
        for (Prov<AssertStatement> prov : statements) {
            AssertStatement statement = prov.get();
            LAssembler.customParsers.put(opcode, statement::read);
        }
    }

    private static class AssertStatement extends LStatement {
        private static final ConditionOp[] conditions = {ConditionOp.lessThan, ConditionOp.lessThanEq};

        public String min = "0";
        public ConditionOp opMin = ConditionOp.lessThanEq;
        public String value = "index";
        public ConditionOp opMax = ConditionOp.lessThanEq;
        public String max = "length";
        public String message = "\"Array index '%s' out of bounds (%d to %d)\"";

        @Override
        public LCategory category() {
            return AssertLogic.assertsCategory;
        }

        @Override
        public void build(Table t) {
            t.clearChildren();
            t.left();
            t.add("invariant: ").padLeft(6);
            t.table(table -> {
                table.color.set(category().color);
                field(table, min, str -> min = str).left();
                opButton(t, table, opMin, o -> opMin = o);
                field(table, value, str -> value = str).left();
                opButton(t, table, opMax, o -> opMax = o);
                field(table, max, str -> max = str).left();
                table.add("").growX();
            });
            t.row();
            t.add("message: ").padLeft(6);
            field(t, message, str -> message = str).width(0f).growX().padRight(3);
        }

        void opButton(Table parent, Table table, ConditionOp op, Cons<ConditionOp> getter) {
            table.button(b -> {
                b.label(() -> op.symbol);
                b.clicked(() -> showSelect(b, conditions, op, o -> {
                    getter.get(o);
                    build(parent);
                }));
            }, Styles.logict, () -> {
            }).size(64f, 40f).left().pad(4f).color(table.color);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder) {
            return new AssertsLInstructions.AssertI(builder.var(min), opMin, builder.var(value),
                    opMax, builder.var(max), builder.var(message));
        }

        @Override
        public final void write(StringBuilder builder) {
            writer.start(builder);
            writer.write(opcode);
            writer.write(min);
            writer.write(opMin.name());
            writer.write(value);
            writer.write(opMax.name());
            writer.write(max);
            writer.write(message);
            writer.end();
        }

        public LStatement read(String[] tokens) {
            if (tokens.length > 1) min = tokens[1];
            if (tokens.length > 2) opMin = mindustry.logic.ConditionOp.valueOf(tokens[2]);
            if (tokens.length > 3) value = tokens[3];
            if (tokens.length > 4) opMax = mindustry.logic.ConditionOp.valueOf(tokens[4]);
            if (tokens.length > 5) max = tokens[5];
            if (tokens.length > 6) message = tokens[6];
            return this;
        }

        @Override
        public String name() {
            return "Assert";
        }
    }
}
