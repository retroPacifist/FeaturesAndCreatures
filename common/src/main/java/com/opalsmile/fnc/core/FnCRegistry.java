package com.opalsmile.fnc.core;

public class FnCRegistry {

    public static void initialise() {
        FnCBlocks.init();
        FnCItems.init();
        FnCSounds.init();
        FnCEntities.init();
    }
}
