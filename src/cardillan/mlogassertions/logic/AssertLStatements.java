package cardillan.mlogassertions.logic;

import arc.func.Cons;
import arc.func.Prov;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.gen.LogicIO;
import mindustry.logic.LAssembler;
import mindustry.logic.LCategory;
import mindustry.logic.LExecutor;
import mindustry.logic.LStatement;
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
        public TypeAssertion type = TypeAssertion.integer;
        public String min = "0";
        public AssertOp opMin = AssertOp.lessThanEq;
        public String value = "index";
        public AssertOp opMax = AssertOp.lessThanEq;
        public String max = "10";
        public String message = "\"Array index out of bounds (0 to 10).\"";

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
                table.add("type ");
                table.button(b -> {
                    b.label(() -> type.name());
                    b.clicked(() -> showSelect(b, TypeAssertion.all, type, o -> {
                        type = o;
                        build(t);
                    }, 2, cell -> cell.size(110, 50)));
                }, Styles.logict, () -> {}).size(110, 40).left().pad(4f).color(table.color);
                table.add("bounds ");
                numField(table, min, str -> min = str);
                opButton(t, table, opMin, o -> opMin = o);
                numField(table, value, str -> value = str);
                opButton(t, table, opMax, o -> opMax = o);
                numField(table, max, str -> max = str);
                table.add("").growX();
            });
            t.row();
            t.add("message: ").padLeft(6);
            field(t, message, str -> message = str).width(0f).growX().padRight(3);
        }

        void numField(Table table, String num, Cons<String> getter) {
            field(table, num, getter).width(120).left();
        }

        void opButton(Table parent, Table table, AssertOp op, Cons<AssertOp> getter) {
            table.button(b -> {
                b.label(() -> op.symbol);
                b.clicked(() -> showSelect(b, AssertOp.all, op, o -> {
                    getter.get(o);
                    build(parent);
                }));
            }, Styles.logict, () -> {
            }).size(64f, 40f).left().pad(4f).color(table.color);
        }

        @Override
        public LExecutor.LInstruction build(LAssembler builder) {
            return new AssertsLInstructions.AssertI(type, builder.var(min), opMin, builder.var(value),
                    opMax, builder.var(max), builder.var(message));
        }

        @Override
        public final void write(StringBuilder builder) {
            writer.start(builder);
            writer.write(opcode);
            writer.write(type.name());
            writer.write(min);
            writer.write(opMin.name());
            writer.write(value);
            writer.write(opMax.name());
            writer.write(max);
            writer.write(message);
            writer.end();
        }

        public LStatement read(String[] tokens) {
            int i = 1;
            if (tokens.length > i) type = TypeAssertion.valueOf(tokens[i++]);
            if (tokens.length > i) min = tokens[i++];
            if (tokens.length > i) opMin = AssertOp.valueOf(tokens[i++]);
            if (tokens.length > i) value = tokens[i++];
            if (tokens.length > i) opMax = AssertOp.valueOf(tokens[i++]);
            if (tokens.length > i) max = tokens[i++];
            if (tokens.length > i) message = tokens[i++];
            return this;
        }

        @Override
        public String name() {
            return "Assert";
        }
    }
}
