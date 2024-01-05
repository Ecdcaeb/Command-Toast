package com.Hileb.command_toast.toast;

import com.Hileb.command_toast.CommandToastMod;
import com.Hileb.command_toast.command.JsonArgument;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * @Project Command-Toast
 * @Author Hileb
 * @Date 2024/1/1 11:21
 **/
@Mod.EventBusSubscriber
public class SimpleToast {
    /**

     example command:
     /toast @a simple {"text":"Toast Title","color":"yellow","italic":true,"bold":true} {"text":"send more toasts!","strikethrough":true} minecraft:iron_sword{ench:[{id:1,lvl:2}]} 1


     */
    @SubscribeEvent
    public static void register(ToastType.RegisterEvent event){
        event.register(NAME,new Factory());
    }
    public static final String NAME="simple";
    public static class Factory extends ToastType.ServerToastFactory{

        public Factory() {
            super(NAME);
        }
        @Nonnull
        @Override
        public LiteralArgumentBuilder<CommandSource> register() {
            return Commands.literal(NAME)
                    .then(Commands.argument("title", JsonArgument.json())
                            .then(Commands.argument("text",JsonArgument.json())
                                    .then(Commands.argument("icon",ItemArgument.item())
                                            .executes((context)-> run(context,1))
                                            .then(Commands.argument("icon_item_count",IntegerArgumentType.integer(0))
                                                    .executes((context)-> run(context,IntegerArgumentType.getInteger(context,"icon_item_count")))
                                            )
                                    )
                            )
                    );
        }


        @Override
        public ITextComponent help() {
            //TODO: i18n
            return new StringTextComponent("simple <title> <text> <icon> [icon_item_count]");
        }

        public static int run(CommandContext<CommandSource> context,int count) throws CommandSyntaxException{
            try{
                Collection<ServerPlayerEntity> targets = EntityArgument.getPlayers(context, "targets");
                ItemStack icon = ItemArgument.getItem(context, "icon").createItemStack(count, false);
                ITextComponent title=ITextComponent.Serializer.fromJson(JsonArgument.getJson(context,"title"));
                ITextComponent text = ITextComponent.Serializer.fromJson(JsonArgument.getJson(context,"text"));
                SimpleServerToast serverToast = new SimpleServerToast(title, text, icon);
                for (ServerPlayerEntity serverPlayer : targets) {
                    post(serverPlayer, serverToast);
                }
                return targets.size();
            }catch (Exception e){
                CommandToastMod.LOGGER.error(e);
                return 0;
            }
        }
        @Override
        public ServerToast createToast() {
            return new SimpleServerToast();
        }
    }
    public static class SimpleServerToast extends ServerToast{
        public ItemStack icon;
        public ITextComponent title;
        public ITextComponent text;
        public SimpleServerToast(){
            super(NAME);
        }

        public SimpleServerToast(ITextComponent title,ITextComponent text,ItemStack icon) {
            super(NAME);
            this.icon=icon;
            this.title=title;
            this.text=text;
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public IToast get() {
            return new SimpleIconToast(title,text,icon);
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT compoundNBT=new CompoundNBT();
            compoundNBT.putString("title",ITextComponent.Serializer.toJson(title));
            compoundNBT.putString("text",ITextComponent.Serializer.toJson(text));
            compoundNBT.put("icon",icon.serializeNBT());
            return compoundNBT;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            title=ITextComponent.Serializer.fromJson(nbt.getString("title"));
            text=ITextComponent.Serializer.fromJson(nbt.getString("text"));
            icon=ItemStack.of(nbt.getCompound("icon"));
        }
    }
    @OnlyIn(Dist.CLIENT)
    public static class SimpleIconToast implements IToast {
        public final ItemStack icon;
        public final ITextComponent title;
        public final ITextComponent text;
        public SimpleIconToast(ITextComponent title,ITextComponent text,ItemStack icon){
            this.icon=icon;
            this.text=text;
            this.title=title;
        }
        @Override
        public IToast.Visibility render(MatrixStack matrixStack, ToastGui toastGui, long delta)
        {
            toastGui.getMinecraft().getTextureManager().bind(IToast.TEXTURE);
            RenderSystem.color3f(1.0F, 1.0F, 1.0F);
            toastGui.blit(matrixStack, 0, 0, 0, 0, this.width(), this.height());

            List<IReorderingProcessor> list = toastGui.getMinecraft().font.split(text, 125);
            int i = 16776960;

            if (list.size() == 1)
            {
                toastGui.getMinecraft().font.draw(matrixStack,title ,30, 7, i | -16777216);
                toastGui.getMinecraft().font.draw(matrixStack,text, 30, 18, -1);
            }
            else
            {
                int j = 1500;
                float f = 300.0F;

                if (delta < 1500L)
                {
                    int k = MathHelper.floor(MathHelper.clamp((float)(1500L - delta) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                    toastGui.getMinecraft().font.draw(matrixStack,title, 30, 11, i | k);
                }
                else
                {
                    int i1 = MathHelper.floor(MathHelper.clamp((float)(delta - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                    int l = this.height() / 2 - list.size() * 9 / 2;


                    final int FONT_HEIGHT=toastGui.getMinecraft().font.lineHeight;
                    for(IReorderingProcessor ireorderingprocessor : list) {
                        toastGui.getMinecraft().font.draw(matrixStack, ireorderingprocessor, 30.0F, (float)l, 16777215 | i1);
                        l += FONT_HEIGHT;
                    }
                }
            }
            toastGui.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(icon, 8, 8);
            return delta >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
        }
    }
}
