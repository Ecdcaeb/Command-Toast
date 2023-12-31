package com.Hileb.command_toast.toast;

import com.Hileb.command_toast.command.ToastType;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @Project CommandToast
 * @Author Hileb
 * @Date 2023/12/30 20:46
 **/
public class SimpleToast{

    /*

    /toast @a simple {"text":"Toast_Title","color":"yellow","italic":true,"bold":true} {"text":"send_more_toasts!","strikethrough":true} minecraft:iron_sword 1 0 {ench:[{id:1,lvl:2}]}


    * */
    public static class Factory extends ToastType.ServerToastFactory{
        @Override
        public com.Hileb.command_toast.toast.ServerToast product(String[] args) throws CommandException {
            if (args.length<3) throw new CommandException("Not enough args!");
            return new ServerToast("simple",args);
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "simple <title> <text> <icon>";
        }

        @Override
        public com.Hileb.command_toast.toast.ServerToast createToast() {
            return new ServerToast("simple");
        }

        @Override
        public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
            if (args.length==3){
                return CommandBase.getListOfStringsMatchingLastWord(args, Item.REGISTRY.getKeys());
            }else return Collections.emptyList();
        }
    }

    public static class ServerToast extends com.Hileb.command_toast.toast.ServerToast{
        public String[] args=null;

        public ITextComponent title=null;
        public ITextComponent text=null;
        public ItemStack icon=null;
        public UUID target=null;
        public ServerToast(String name,String[] args) throws CommandException {
            super(name);
            this.args=args;
            title= ITextComponent.Serializer.jsonToComponent(args[0]);
            text= ITextComponent.Serializer.jsonToComponent(args[1]);
            icon=getItem(args,2);

        }
        public ServerToast(String name) {
            super(name);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IToast get() {
            if (icon!=null && title!=null && text!=null){
                return new SimpleIconToast(title,text,icon);
            }else return null;
        }


        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbtTagCompound=new NBTTagCompound();
            NBTTagList tagList=new NBTTagList();
            for(String s:args){
                tagList.appendTag(new NBTTagString(s));
            }
            nbtTagCompound.setTag("args",tagList);
            return nbtTagCompound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            NBTTagList tagList=nbt.getTagList("args",8);
            ArrayList<String> strings=new ArrayList<>();
            for(int i=0,size=tagList.tagCount();i<size;i++){
                strings.add(((NBTTagString)tagList.get(i)).getString());
            }
            args=strings.toArray(new String[0]);

            try {
                title= ITextComponent.Serializer.jsonToComponent(args[0]);
                text= ITextComponent.Serializer.jsonToComponent(args[1]);
                icon=getItem(args,2);
            } catch (CommandException e) {
                title=null;
                text=null;
                icon=null;
            }
        }
        public static ItemStack getItem(String[] args,int sti) throws CommandException {
            Item item = CommandBase.getItemByText(null, args[sti]);
            int i = args.length >= (sti+2) ? CommandBase.parseInt(args[sti+1], 1, item.getItemStackLimit()) : 1;
            int j = args.length >= (sti+3) ? CommandBase.parseInt(args[sti+2]) : 0;
            ItemStack itemstack = new ItemStack(item, i, j);

            if (args.length >= (sti+4))
            {
                String s = CommandBase.buildString(args, sti+3);

                try
                {
                    itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
                }
                catch (NBTException nbtexception)
                {
                    throw new CommandException("commands.give.tagError", nbtexception.getMessage());
                }
            }
            return itemstack;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class SimpleIconToast implements IToast {
        public final ItemStack icon;
        public final ITextComponent title;
        public final ITextComponent text;
        public SimpleIconToast(ITextComponent title,ITextComponent text,ItemStack icon){
            this.icon=icon;
            this.text=text;
            this.title=title;
        }
        public Visibility draw(GuiToast toastGui, long delta)
        {
            toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            toastGui.drawTexturedModalRect(0, 0, 0, 0, 160, 32);

            List<String> list = toastGui.getMinecraft().fontRenderer.listFormattedStringToWidth(text.getFormattedText().trim(), 125);
            int i = 16776960;

            if (list.size() == 1)
            {
                toastGui.getMinecraft().fontRenderer.drawString(title.getFormattedText().trim() ,30, 7, i | -16777216);
                toastGui.getMinecraft().fontRenderer.drawString(text.getFormattedText().trim(), 30, 18, -1);
            }
            else
            {
                int j = 1500;
                float f = 300.0F;

                if (delta < 1500L)
                {
                    int k = MathHelper.floor(MathHelper.clamp((float)(1500L - delta) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                    toastGui.getMinecraft().fontRenderer.drawString(title.getFormattedText().trim(), 30, 11, i | k);
                }
                else
                {
                    int i1 = MathHelper.floor(MathHelper.clamp((float)(delta - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                    int l = 16 - list.size() * toastGui.getMinecraft().fontRenderer.FONT_HEIGHT / 2;

                    for (String s : list)
                    {
                        toastGui.getMinecraft().fontRenderer.drawString(s, 30, l, 16777215 | i1);
                        l += toastGui.getMinecraft().fontRenderer.FONT_HEIGHT;
                    }
                }
            }

            RenderHelper.enableGUIStandardItemLighting();
            toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(null,icon, 8, 8);
            return delta >= 5000L ? Visibility.HIDE : Visibility.SHOW;
        }
    }
}
