package com.Hileb.command_toast.command;

import com.Hileb.command_toast.CTMConfig;
import com.Hileb.command_toast.network.NetworkHandler;
import com.Hileb.command_toast.network.S2CToastPack;
import com.Hileb.command_toast.toast.ServerToast;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;
import net.minecraftforge.server.command.TextComponentHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Project CommandToast
 * @Author Hileb
 * @Date 2023/12/30 20:05
 **/
public class CommandToast extends CommandTreeBase {
    public CommandToast(){
        ToastType.onRegister();
        for(Map.Entry<String, ToastType.ServerToastFactory> entry: ToastType.REGISTRY.entrySet()){
            super.addSubcommand(new Child(entry.getKey(),entry.getValue()));
        }
        super.addSubcommand(new CommandTreeHelp(this));
    }
    @Override
    public String getName() {
        return "toast";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "<target> <child command> ... call [/toast help] for more information";
    }
    @Override
    public int getRequiredPermissionLevel() {
        return CTMConfig.general.commandLevel;
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP playerMP;
        if (args.length < 1){
            throw new WrongUsageException("no usable target");
        }
        else {
            playerMP = getPlayer(server, sender, args[0]);
        }
        if (args.length < 2)
        {
            String subCommandsString = getAvailableSubCommandsString(server, sender);
            sender.sendMessage(TextComponentHelper.createComponentTranslation(sender, "commands.tree_base.available_subcommands", subCommandsString));
        }
        else
        {
            ICommand cmd = getSubCommand(args[1]);

            if(cmd == null)
            {
                String subCommandsString = getAvailableSubCommandsString(server, sender);
                throw new CommandException("commands.tree_base.invalid_cmd.list_subcommands", args[1], subCommandsString);
            }
            else if(!cmd.checkPermission(server, sender))
            {
                throw new CommandException("commands.generic.permission");
            }
            else
            {
                if (cmd instanceof Child){
                    ((Child)cmd).execute(playerMP,server,sender,shiftArgs(args));
                }else cmd.execute(server, sender, shiftArgs(args));
            }
        }
    }
    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        if (index > 1 && args.length > 2)
        {
            ICommand cmd = getSubCommand(args[1]);
            if (cmd != null)
            {
                return cmd.isUsernameIndex(shiftArgs(args), index - 2);
            }
        }
        return index == 1;
    }
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        if(args.length == 2)
        {
            List<String> keys = new ArrayList<>();

            for (ICommand c : getSubCommands())
            {
                if(c.checkPermission(server, sender))
                {
                    keys.add(c.getName());
                }
            }

            keys.sort(null);
            return getListOfStringsMatchingLastWord(args, keys);
        }

        ICommand cmd = getSubCommand(args[1]);

        if(cmd != null)
        {
            return cmd.getTabCompletions(server, sender, shiftArgs(args), pos);
        }

        return super.getTabCompletions(server, sender, args, pos);
    }

    //copy the private method
    private String getAvailableSubCommandsString(MinecraftServer server, ICommandSender sender)
    {
        Collection<String> availableCommands = new ArrayList<>();
        for (ICommand command : getSubCommands())
        {
            if (command.checkPermission(server, sender))
            {
                availableCommands.add(command.getName());
            }
        }
        return CommandBase.joinNiceStringFromCollection(availableCommands);
    }
    private static String[] shiftArgs(@Nullable String[] s)
    {
        if(s == null || s.length == 0)
        {
            return new String[0];
        }

        String[] s1 = new String[s.length - 2];
        System.arraycopy(s, 2, s1, 0, s1.length);
        return s1;
    }

    /**
     * @Project CommandToast
     * @Author Hileb
     * @Date 2023/12/30 20:14
     **/
    public static class Child extends CommandBase {
        public final ToastType.ServerToastFactory factory;
        public final String name;
        public Child(String name, ToastType.ServerToastFactory factory){
            this.factory=factory;
            this.name=name;
        }
        @Override
        public int getRequiredPermissionLevel() {
            return CTMConfig.general.commandLevel;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return factory.getUsage(sender);
        }

        @Deprecated
        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {}
        public ServerToast execute(EntityPlayerMP mp, MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
            ServerToast serverToast =factory.product(args);
            S2CToastPack pack=new S2CToastPack(serverToast);
            NetworkHandler.CHANNEL.sendTo(pack,mp);
            return serverToast;
        }

        @Override
        public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
            return factory.getTabCompletions(server,sender,args,targetPos);
        }
    }
}
