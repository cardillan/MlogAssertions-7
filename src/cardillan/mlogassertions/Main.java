package cardillan.mlogassertions;

import cardillan.mlogassertions.logic.AssertLogic;
import mindustry.mod.Mod;

public class Main extends Mod {

    @Override
    public void init(){
        super.init();

        AssertLogic.init();
    }
}
