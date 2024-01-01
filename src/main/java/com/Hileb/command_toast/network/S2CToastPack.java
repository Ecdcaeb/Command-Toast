package com.Hileb.command_toast.network;


import com.Hileb.command_toast.toast.ToastType;
import com.Hileb.command_toast.toast.ServerToast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @Project CommandToast
 * @Author Hileb
 * @Date 2023/12/30 20:37
 **/
public class S2CToastPack{
    public boolean isCorrect=false;
    public ServerToast toastServer;
    public S2CToastPack(PacketBuffer buf){
        try {
            CompoundNBT compound= buf.readNbt();
            ToastType.ServerToastFactory factory=ToastType.REGISTRY.get(compound.getString("type"));
            ServerToast serverToast=factory.createToast();
            serverToast.deserializeNBT(compound.getCompound("toast"));
            this.toastServer=serverToast;
            this.isCorrect=true;
        }catch (Exception ignored){
            isCorrect=false;
        }
    }
    public S2CToastPack(ServerToast serverToast){
        this.toastServer=serverToast;
        this.isCorrect=true;
    }

    public void toBytes(PacketBuffer buf) {
        CompoundNBT compound=new CompoundNBT();
        compound.putString("type",toastServer.name);
        compound.put("toast",toastServer.serializeNBT());
        buf.writeNbt(compound);
    }
    public static class Handler{

        public static void onMessage(S2CToastPack message, Supplier<NetworkEvent.Context> contextSupplier) {
            if (message.isCorrect){
                contextSupplier.get().enqueueWork(() -> {
                    IToast toast=message.toastServer.get();
                    if (toast!=null){
                       Minecraft.getInstance().getToasts().addToast(toast);
                    }
                });
            }
            contextSupplier.get().setPacketHandled(true);
        }
    }
}
