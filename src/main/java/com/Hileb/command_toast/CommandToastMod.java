package com.Hileb.command_toast;

import com.Hileb.command_toast.config.CommandToastConfig;
import com.Hileb.command_toast.network.NetworkHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.MavenVersionStringHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Project Command-Toast
 * @Author Hileb
 * @Date 2023/12/31 17:51
 **/
@Mod(CommandToastMod.MODID)
public class CommandToastMod {
    public static final String MODID="command_toast";
    public static final Log LOGGER= LogFactory.getLog(MODID);
    public static String VERSION="36.1.0.0";
    public CommandToastMod(){
        IEventBus modBus= FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,CommandToastConfig.SPEC);
        NetworkHandler.forceToClassLoad();
    }

}
