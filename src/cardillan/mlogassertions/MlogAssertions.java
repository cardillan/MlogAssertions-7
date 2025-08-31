package cardillan.mlogassertions;

import arc.Events;
import cardillan.mlogassertions.logic.AssertLogic;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class MlogAssertions extends Mod {

    public MlogAssertions() {
        Events.on(EventType.ClientLoadEvent.class, e -> {
            LogicDialogAddon.init();

            AssertLogic.init();
            Assertions.init();
        });
    }
}
