package com.opalsmile.fnc.util;

import net.minecraft.util.RandomSource;

import java.util.List;

public class FnCUtil {

    public static <T> T getRandomElement(RandomSource random, List<T> list){
        int index = random.nextInt(list.size());
        return list.get(index);
    }

}
