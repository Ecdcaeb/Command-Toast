package net.minecraft.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.authlib.GameProfile;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.JSONUtils;

public class FillPlayerHead extends LootFunction {
   private final LootContext.EntityTarget entityTarget;

   public FillPlayerHead(ILootCondition[] p_i51234_1_, LootContext.EntityTarget p_i51234_2_) {
      super(p_i51234_1_);
      this.entityTarget = p_i51234_2_;
   }

   public LootFunctionType getType() {
      return LootFunctionManager.FILL_PLAYER_HEAD;
   }

   public Set<LootParameter<?>> getReferencedContextParams() {
      return ImmutableSet.of(this.entityTarget.getParam());
   }

   public ItemStack run(ItemStack p_215859_1_, LootContext p_215859_2_) {
      if (p_215859_1_.getItem() == Items.PLAYER_HEAD) {
         Entity entity = p_215859_2_.getParamOrNull(this.entityTarget.getParam());
         if (entity instanceof PlayerEntity) {
            GameProfile gameprofile = ((PlayerEntity)entity).getGameProfile();
            p_215859_1_.getOrCreateTag().put("SkullOwner", NBTUtil.writeGameProfile(new CompoundNBT(), gameprofile));
         }
      }

      return p_215859_1_;
   }

   public static class Serializer extends LootFunction.Serializer<FillPlayerHead> {
      public void serialize(JsonObject p_230424_1_, FillPlayerHead p_230424_2_, JsonSerializationContext p_230424_3_) {
         super.serialize(p_230424_1_, p_230424_2_, p_230424_3_);
         p_230424_1_.add("entity", p_230424_3_.serialize(p_230424_2_.entityTarget));
      }

      public FillPlayerHead deserialize(JsonObject p_186530_1_, JsonDeserializationContext p_186530_2_, ILootCondition[] p_186530_3_) {
         LootContext.EntityTarget lootcontext$entitytarget = JSONUtils.getAsObject(p_186530_1_, "entity", p_186530_2_, LootContext.EntityTarget.class);
         return new FillPlayerHead(p_186530_3_, lootcontext$entitytarget);
      }
   }
}