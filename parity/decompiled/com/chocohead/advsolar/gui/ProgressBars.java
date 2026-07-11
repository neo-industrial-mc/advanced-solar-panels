/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ic2.core.gui.Gauge$GaugeProperties
 *  ic2.core.gui.Gauge$GaugePropertyBuilder
 *  ic2.core.gui.Gauge$GaugePropertyBuilder$GaugeOrientation
 *  ic2.core.gui.Gauge$GaugeStyle
 *  ic2.core.gui.Gauge$IGaugeStyle
 *  net.minecraft.util.ResourceLocation
 */
package com.chocohead.advsolar.gui;

import ic2.core.gui.Gauge;
import java.util.Locale;
import net.minecraft.util.ResourceLocation;

public enum ProgressBars implements Gauge.IGaugeStyle
{
    PROGRESS_MOLECULAR_TRANSFORMER(new Gauge.GaugePropertyBuilder(221, 7, 10, 15, Gauge.GaugePropertyBuilder.GaugeOrientation.Down).withTexture(new ResourceLocation("advanced_solar_panels", "textures/gui/MolecularTransformer.png"))),
    PROGRESS_JEI_MOLECULAR_TRANSFORMER(new Gauge.GaugePropertyBuilder(176, 2, 12, 11, Gauge.GaugePropertyBuilder.GaugeOrientation.Down).withTexture(new ResourceLocation("advanced_solar_panels", "textures/gui/MolecularTransformer JEI.png"))),
    ENERGY_ADVANCED_SOLAR(new Gauge.GaugePropertyBuilder(195, 0, 24, 14, Gauge.GaugePropertyBuilder.GaugeOrientation.Right).withTexture(new ResourceLocation("advanced_solar_panels", "textures/gui/AdvancedSolarPanel.png")));

    private final String name = this.name().toLowerCase(Locale.ENGLISH);
    private final Gauge.GaugeProperties properties;

    private ProgressBars(Gauge.GaugePropertyBuilder properties) {
        this.properties = properties.build();
    }

    public Gauge.GaugeProperties getProperties() {
        return this.properties;
    }

    public static void addStyles() {
        for (ProgressBars bar : ProgressBars.values()) {
            Gauge.GaugeStyle.addStyle((String)bar.name, (Gauge.IGaugeStyle)bar);
        }
    }
}

