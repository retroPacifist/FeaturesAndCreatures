package com.opalsmile.fnc.registries;

public class FnCRegistry {

    public static void initialise(){
        FnCEntities.init();
        FnCBlocks.init();
        FnCItems.init();
        FnCSounds.init();
    }
}
