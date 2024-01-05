package com.Hileb.command_toast.toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import java.util.Collection;
import java.util.Collections;

/**
 * @Project Command-Toast
 * @Author Hileb
 * @Date 2024/1/4 23:54
 **/
public class JsonArgument implements ArgumentType<JsonObject> {
    public static final Gson GSON=new GsonBuilder().create();
    public static final SimpleCommandExceptionType EXCEPTION_TYPE=new SimpleCommandExceptionType(new LiteralMessage("JsonSyntaxException"));
    @Override
    public JsonObject parse(StringReader reader) throws CommandSyntaxException {
        StringBuilder stringBuilder=new StringBuilder();
        int leftCount=0;
        int leftCount2=0;
        while (reader.canRead()){
            char each=reader.read();
            if (each=='{')leftCount++;
            if (each=='\"')leftCount2++;
            if (each==' '){
                if (leftCount2%2==0)continue;
            }
            if (each=='}'){
                if (leftCount==1){
                    stringBuilder.append(each);
                    break;
                }else leftCount--;
            }
            stringBuilder.append(each);
        }
        String rawInput=stringBuilder.toString();
        JsonObject json;
        try{
            json=GSON.fromJson(rawInput,JsonObject.class);
        }catch (JsonSyntaxException exception){
            throw EXCEPTION_TYPE.createWithContext(new StringReader(exception.toString()));
        }
        return json;
    }

    @Override
    public Collection<String> getExamples() {
        return Collections.singleton("{}");
    }
    public static JsonArgument json(){
        return new JsonArgument();
    }
    public static JsonObject getJson(CommandContext<?> context,String name){
        return context.getArgument(name,JsonObject.class);
    }
}
