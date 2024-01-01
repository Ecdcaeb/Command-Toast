package com.Hileb.command_toast.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * @Project Command-Toast
 * @Author Hileb
 * @Date 2024/1/1 11:10
 **/
public class CommandToastConfig {
    public static final ForgeConfigSpec.Builder BUILDER=new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.IntValue commandLevel=
            BUILDER.comment("The level that toast command require")
                    .defineInRange("commandLevel",2,0,999);
    public static final ForgeConfigSpec SPEC=BUILDER.build();
}
