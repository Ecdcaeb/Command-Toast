package com.Hileb.command_toast;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @Project CommandToast
 * @Author Hileb
 * @Date 2023/12/30 20:05
 **/
@Config(modid = CommandToastMod.MODID,category="")
public class CTMConfig {
    @Mod.EventBusSubscriber
    public static class EventHandler{
        @SubscribeEvent
        public static void onConfigChange(ConfigChangedEvent event){
            if (CommandToastMod.MODID.equals(event.getModID())){
                ConfigManager.sync(CommandToastMod.MODID,Config.Type.INSTANCE);
            }
        }
    }
    @Config.Comment("the general Config")
    public static final General general=new General();
    public static class General{
        @Config.Comment("The level that toast command require")
        @Config.RangeInt(min=0,max=999)
        public int commandLevel=2;
    }
}
