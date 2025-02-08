package cardillan.mlogassertions.logic;

import arc.func.Cons;
import arc.func.Func;
import arc.func.Prov;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import mindustry.gen.LogicIO;
import mindustry.logic.LAssembler;
import mindustry.logic.LCategory;
import mindustry.logic.LExecutor;
import mindustry.logic.LStatement;
import mindustry.ui.Styles;

public class LogicStatements {
    private static final LogicStatementWriter writer = new LogicStatementWriter();

    public static void register() {
        register(AssertBoundsStatement::new, AssertBoundsStatement.opcode, AssertBoundsStatement::read);
    }

    private static void register(Prov<LStatement> prov, String opcode, Func<String[], LStatement> parser) {
        LogicIO.allStatements.add(prov);
        LAssembler.customParsers.put(opcode, parser);
    }

    private static abstract class AssertStatement extends LStatement {
        final String name;

        public AssertStatement(String name) {
            this.name = name;
        }

        @Override
        public String name() {
            return name;
        }
    }

    private static class AssertBoundsStatement extends AssertStatement {
        public static final String opcode = "assertBounds";
        public AssertionType type = AssertionType.integer;
        public String multiple = "2";
        public String min = "0";
        public AssertOp opMin = AssertOp.lessThanEq;
        public String value = "index";
        public AssertOp opMax = AssertOp.lessThanEq;
        public String max = "10";
        public String message = "\"Index out of bounds (0 to 10).\"";

        public AssertBoundsStatement() {
            super("Assert Bounds");
        }

        @Override
        public LCategory category() {
            return AssertLogic.assertsCategory;
        }

        @Override
        public void build(Table t) {
            t.clearChildren();
            t.left();
            t.add("Value type ").color(category().color).padLeft(4);
            t.table(table -> {
                table.left();
                table.color.set(category().color);
                table.add("value of ").padLeft(4);
                numField(table, value, str -> value = str);
                table.add(" is ").padLeft(4);
                table.button(b -> {
                    b.label(() -> type.name());
                    b.clicked(() -> showSelect(b, AssertionType.all, type, o -> {
                        type = o;
                        build(t);
                    }, 2, cell -> cell.size(110, 50)));
                }, Styles.logict, () -> {}).size(110, 40).left().pad(4f).color(table.color);
                if (type == AssertionType.multiple) {
                    table.add(" of ");
                    numField(table, multiple, str -> multiple = str);
                }
                table.add("").growX();
            });
            t.row();
            t.add("Bounds ").color(category().color).padLeft(4);;
            t.table(table -> {
                table.left();
                table.color.set(category().color);
                numField(table, min, str -> min = str);
                opButton(t, table, opMin, o -> opMin = o);
                table.add(" value ");
                opButton(t, table, opMax, o -> opMax = o);
                numField(table, max, str -> max = str);
                table.add("").growX();
            });
            t.row();
            t.add("Message").color(category().color).padLeft(4);
            field(t, message, str -> message = str).width(0f).maxTextLength(64).growX().padRight(3);
        }

        Cell<TextField> numField(Table table, String num, Cons<String> getter) {
            return field(table, num, getter).width(120).left();
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
            return new LogicInstructions.AssertI(type, builder.var(multiple),
                    builder.var(min), opMin, builder.var(value), opMax, builder.var(max),
                    builder.var(message));
        }

        @Override
        public void write(StringBuilder builder) {
            writer.start(builder);
            writer.write(opcode);
            writer.write(type.name());
            writer.write(multiple);
            writer.write(min);
            writer.write(opMin.name());
            writer.write(value);
            writer.write(opMax.name());
            writer.write(max);
            writer.write(message);
            writer.end();
        }

        public static LStatement read(String[] tokens) {
            AssertBoundsStatement stmt = new AssertBoundsStatement();
            int i = 1;
            if (tokens.length > i) stmt.type = AssertionType.valueOf(tokens[i++]);
            if (tokens.length > i) stmt.multiple = tokens[i++];
            if (tokens.length > i) stmt.min = tokens[i++];
            if (tokens.length > i) stmt.opMin = AssertOp.valueOf(tokens[i++]);
            if (tokens.length > i) stmt.value = tokens[i++];
            if (tokens.length > i) stmt.opMax = AssertOp.valueOf(tokens[i++]);
            if (tokens.length > i) stmt.max = tokens[i++];
            if (tokens.length > i) stmt.message = tokens[i++];
            return stmt;
        }
    }
}
