package com.Hileb.command_toast.toast;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @Project Command-Toast
 * @Author Hileb
 * @Date 2024/1/2 22:50
 **/
public class HandShowToast {
    public static final String HAND="hand";
    public static class Factory extends ToastType.ServerToastFactory{
        public Factory() {
            super(HAND);
        }

        @Nonnull
        @Override
        public LiteralArgumentBuilder<CommandSource> register() {
            return Commands.literal(HAND).requires((source)-> source.getEntity() instanceof ServerPlayerEntity)
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
        public String help() {
            return "hand (Void)";
        }

        @Override
        public ServerToast createToast() {
            return new SimpleToast.SimpleServerToast();
        }
    }
}
