package com.Hileb.command_toast.network;

import com.Hileb.command_toast.CommandToastMod;
import com.Hileb.command_toast.toast.ServerToast;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * @Project Command-Toast
 * @Author Hileb
 * @Date 2023/12/31 20:40
 **/
public class NetworkHandler {
    public static final SimpleChannel CHANNEL=
            NetworkRegistry.newSimpleChannel(
                    new ResourceLocation(CommandToastMod.MODID, "main"),
                    () -> CommandToastMod.VERSION,//version
                    CommandToastMod.VERSION::equals,//client
                    CommandToastMod.VERSION::equals//server
            );
    static {
         CHANNEL.messageBuilder(S2CToastPack.class,1, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CToastPack::toBytes)
                .decoder(S2CToastPack::new)
                .consumer(S2CToastPack.Handler::onMessage).add();
    }
    public static void send(ServerToast serverToast, ServerPlayerEntity serverPlayer){
        S2CToastPack pack=new S2CToastPack(serverToast);
        CHANNEL.send(PacketDistributor.PLAYER.with(()->serverPlayer),pack);
    }
    public static void forceToClassLoad(){}


}
