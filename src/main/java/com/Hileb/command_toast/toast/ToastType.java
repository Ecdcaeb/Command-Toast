package com.Hileb.command_toast.toast;

import com.Hileb.command_toast.network.NetworkHandler;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnull;
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
        public ServerToastFactory(String name){
            this.name=name;
        }
        @Nonnull
        public abstract LiteralArgumentBuilder<CommandSource> register();
        public static void post(ServerPlayerEntity player, ServerToast serverToast){
            NetworkHandler.send(serverToast,player);
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
