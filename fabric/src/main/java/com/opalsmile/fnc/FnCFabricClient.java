package com.opalsmile.fnc;

import com.opalsmile.fnc.client.FnCEntityRenderers;
import com.opalsmile.fnc.mixin.client.access.AccessEntityRenderers;
import net.fabricmc.api.ClientModInitializer;

public class FnCFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FnCEntityRenderers.register(AccessEntityRenderers::register);
    }
}
