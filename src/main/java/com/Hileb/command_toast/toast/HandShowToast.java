package com.Hileb.command_toast.toast;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @Project Command-Toast
 * @Author Hileb
 * @Date 2024/1/2 22:50
 **/
@Mod.EventBusSubscriber
public class HandShowToast {
    public static final String NAME="hand";
    @SubscribeEvent
    public static void register(ToastType.RegisterEvent event){
        event.register(NAME,new Factory());
    }
    public static class Factory extends ToastType.ServerToastFactory{
        public Factory() {
            super(NAME);
        }

        @Nonnull
        @Override
        public LiteralArgumentBuilder<CommandSource> register() {
            return Commands.literal(NAME).requires((source)-> source.getEntity() instanceof ServerPlayerEntity)
                    .executes((context)->{
                        Collection<ServerPlayerEntity> targets = EntityArgument.getPlayers(context, "targets");
                        ServerPlayerEntity serverPlayer=context.getSource().getPlayerOrException();
                        ItemStack stack=serverPlayer.getItemInHand(Hand.MAIN_HAND);
                        ServerToast serverToast=new SimpleToast.SimpleServerToast(stack.getDisplayName(),serverPlayer.getDisplayName(),stack);
                        for(ServerPlayerEntity target:targets){
                            post(target,serverToast);
                        }
                        return targets.size();
                    });
        }

        @Override
        public ITextComponent help() {
            //TODO: i18n
            return new StringTextComponent("hand");
        }

        @Override
        public ServerToast createToast() {
            return new SimpleToast.SimpleServerToast();
        }
    }
}
