package com.chocohead.advsolar.gametest;

import net.minecraft.gametest.framework.GameTestHelper;

final class AdvSolarGameTestAssertions {
    private AdvSolarGameTestAssertions() { }

    static void assertNear(GameTestHelper helper, double actual, double expected, double tolerance, String name) {
        helper.assertTrue(Math.abs(actual - expected) <= tolerance,
                name + ": expected " + expected + " +/- " + tolerance + ", got " + actual);
    }
}
