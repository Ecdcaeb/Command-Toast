package com.Hileb.command_toast.command;

import com.Hileb.command_toast.config.CommandToastConfig;
import com.Hileb.command_toast.toast.ToastType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @Project Command-Toast
 * @Author Hileb
 * @Date 2023/12/31 17:55
 **/
@Mod.EventBusSubscriber
public class CommandToast {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event){
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        MinecraftForge.EVENT_BUS.register(new ToastType.RegisterEvent());

        LiteralArgumentBuilder<CommandSource> builder=LiteralArgumentBuilder.<CommandSource>literal("toast");
        builder=builder.requires((commandSource) -> commandSource.hasPermission(CommandToastConfig.commandLevel.get()));
        RequiredArgumentBuilder<CommandSource, EntitySelector> requiredArgumentBuilder= Commands.argument("target", EntityArgument.players());
        for(ToastType.ServerToastFactory childBuilder:ToastType.REGISTRY.values()){
            requiredArgumentBuilder=requiredArgumentBuilder.then(childBuilder.registerChild());
        }
        builder=builder.then(requiredArgumentBuilder);
        dispatcher.register(builder);
    }
    public interface ChildBuilder{
        ArgumentBuilder<CommandSource, ?> register();
    }
}
