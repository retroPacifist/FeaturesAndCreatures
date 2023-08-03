package com.opalsmile.fnc.core;

import com.opalsmile.fnc.advancements.AntlerHeaddressTrigger;
import com.opalsmile.fnc.advancements.JockeyTradeTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class FnCTriggers {
    public static final AntlerHeaddressTrigger ANTLER_HEADDRESS = new AntlerHeaddressTrigger();
    public static final JockeyTradeTrigger JOCKEY_TRADE = new JockeyTradeTrigger();

    public static void register() {
        CriteriaTriggers.register(ANTLER_HEADDRESS);
        CriteriaTriggers.register(JOCKEY_TRADE);
    }
}
