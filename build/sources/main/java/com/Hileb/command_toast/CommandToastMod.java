package com.Hileb.command_toast;

import com.Hileb.command_toast.command.CommandToast;
import com.Hileb.command_toast.command.ToastType;
import com.Hileb.command_toast.toast.SimpleToast;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Project CommandToast
 * @Author Hileb
 * @Date 2023/12/30 20:01
 **/
@Mod(modid = CommandToastMod.MODID,version = CommandToastMod.VERSION,name = CommandToastMod.NAME)
public class CommandToastMod {
    public static final String MODID="command_toast";
    public static final String NAME="Command Toast";
    public static final String VERSION="14.1.0.0";
    public static final Logger LOGGER= LogManager.getLogger(MODID);
    @Mod.Instance
    public static CommandToastMod INSTANCE;

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandToast());
    }

    @Mod.EventBusSubscriber
    public static class ForgeEventHandler{
        @SubscribeEvent
        public static void onRegister(ToastType.RegisterEvent event){
            event.register("simple",new SimpleToast.Factory());
        }
    }

}
