package com.Hileb.command_toast.command;

import com.Hileb.command_toast.toast.ServerToast;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @Project CommandToast
 * @Author Hileb
 * @Date 2023/12/30 20:27
 **/
public class ToastType {
    public static final HashMap<String,ServerToastFactory> REGISTRY=new HashMap<>();
    public static void onRegister(){

        MinecraftForge.EVENT_BUS.post(new RegisterEvent());
    }
    public abstract static class ServerToastFactory{

        public abstract ServerToast product(String[] args) throws CommandException;
        public abstract String getUsage(ICommandSender sender);
        public abstract ServerToast createToast();
        public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
            return Collections.emptyList();
        }
    }
    public final static class RegisterEvent extends Event {
        public void register(String name,ServerToastFactory factory){
            REGISTRY.put(name,factory);
        }
    }
}
