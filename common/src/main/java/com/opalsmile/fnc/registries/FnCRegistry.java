package com.opalsmile.fnc.registries;

public class FnCRegistry {

    public static void initialise(){
        FnCBlocks.init();
        FnCItems.init();
        FnCSounds.init();
        FnCEntities.init();
    }
}
