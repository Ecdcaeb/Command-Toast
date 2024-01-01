package com.Hileb.command_toast.toast;


import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @Project CommandToast
 * @Author Hileb
 * @Date 2023/12/30 20:32
 **/
public abstract class ServerToast implements INBTSerializable<CompoundNBT> {
    public final String name;
    public ServerToast(String name){
        this.name=name;
    }
    @OnlyIn(Dist.CLIENT)
    public abstract IToast get();
}
