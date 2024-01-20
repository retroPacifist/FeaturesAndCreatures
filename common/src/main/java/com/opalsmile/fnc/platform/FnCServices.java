package com.opalsmile.fnc.platform;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.platform.services.FnCIConfigHelper;
import com.opalsmile.fnc.platform.services.FnCINetworkHelper;
import com.opalsmile.fnc.platform.services.FnCIPlatformHelper;

import java.util.ServiceLoader;

public class FnCServices {

    public static final FnCIPlatformHelper PLATFORM = load(FnCIPlatformHelper.class);
    public static final FnCIConfigHelper CONFIG = load(FnCIConfigHelper.class);

    public static final FnCINetworkHelper NETWORK = load(FnCINetworkHelper.class);

    public static <T> T load(Class<T> clazz){

        final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(
                () -> new NullPointerException("Failed to load service for " + clazz.getName()));
        FnCConstants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}