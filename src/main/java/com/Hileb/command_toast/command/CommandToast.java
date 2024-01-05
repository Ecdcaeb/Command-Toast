package com.Hileb.command_toast.command;

import com.Hileb.command_toast.CommandToastMod;
import com.Hileb.command_toast.config.CommandToastConfig;
import com.Hileb.command_toast.toast.HandShowToast;
import com.Hileb.command_toast.toast.SimpleToast;
import com.Hileb.command_toast.toast.ToastType;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Collection;

import static com.Hileb.command_toast.toast.ToastType.ServerToastFactory.post;

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

        MinecraftForge.EVENT_BUS.post(new ToastType.RegisterEvent());

        LiteralArgumentBuilder<CommandSource> builder=Commands.literal("toast").requires((commandSource) -> commandSource.hasPermission(CommandToastConfig.commandLevel.get()));
        RequiredArgumentBuilder<CommandSource,?> argumentBuilder=Commands.argument("targets",EntityArgument.players());
        LiteralArgumentBuilder<CommandSource> helpCommand=Commands.literal("help");
        for(ToastType.ServerToastFactory factory:ToastType.REGISTRY.values()){
            argumentBuilder=argumentBuilder.then(factory.register());
            CommandToastMod.LOGGER.debug("register a type of toast:  "+factory.name);
            helpCommand=helpCommand.then(Commands.literal(factory.name).executes(((context) -> {
                context.getSource().sendSuccess(factory.help(),true);
                return 0;
            })));
        }
        builder.then(argumentBuilder).then(helpCommand);
        dispatcher.register(builder);
    }

}
