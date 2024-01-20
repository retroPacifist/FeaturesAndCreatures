package com.opalsmile.fnc.registries;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.advancements.AntlerHeaddressTrigger;
import com.opalsmile.fnc.advancements.JockeyTradeTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class FnCTriggers {
    public static final AntlerHeaddressTrigger ANTLER_HEADDRESS = new AntlerHeaddressTrigger();
    public static final JockeyTradeTrigger JOCKEY_TRADE = new JockeyTradeTrigger();

    public static void register(){
        CriteriaTriggers.register(FnCConstants.resourceLocation("used_headdress").toString(), ANTLER_HEADDRESS);
        CriteriaTriggers.register(FnCConstants.resourceLocation("jockey_trade").toString(), JOCKEY_TRADE);
    }
}
