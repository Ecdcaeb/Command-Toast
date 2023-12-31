package com.Hileb.command_toast.toast;

import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @Project CommandToast
 * @Author Hileb
 * @Date 2023/12/30 20:32
 **/
public abstract class ServerToast implements INBTSerializable<NBTTagCompound> {
    public final String name;
    public ServerToast(String name){
        this.name=name;
    }
    @SideOnly(Side.CLIENT)
    public abstract IToast get();
}
