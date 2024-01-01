package com.Hileb.command_toast.toast;

import com.Hileb.command_toast.command.CommandToast;
import com.Hileb.command_toast.toast.ServerToast;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Event;

import java.util.HashMap;

/**
 * @Project Command-Toast
 * @Author Hileb
 * @Date 2023/12/31 18:26
 **/
public class ToastType {
    public static final HashMap<String,ServerToastFactory> REGISTRY= new HashMap<>();
    public abstract static class ServerToastFactory{
        public String name;
        public CommandToast.ChildBuilder childCommand;
        public ServerToastFactory(String name, CommandToast.ChildBuilder childCommand){
            this.name=name;
            this.childCommand=childCommand;
        }
        public ArgumentBuilder<CommandSource, ?> registerChild(){
            return childCommand.register();
        }
        public static void post(ServerPlayerEntity player, ServerToast serverToast){

        }
        public abstract ServerToast createToast();
    }
    public final static class RegisterEvent extends Event {
        public RegisterEvent(){}
        public void register(String name,ServerToastFactory factory){
            REGISTRY.put(name,factory);
        }
    }
}
