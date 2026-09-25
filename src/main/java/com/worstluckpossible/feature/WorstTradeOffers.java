package com.worstluckpossible.feature;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.DyeItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapDecorationType;
import net.minecraft.item.map.MapDecorationTypes;
import net.minecraft.item.map.MapState;
import net.minecraft.potion.Potions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.gen.structure.Structure;

public final class WorstTradeOffers {
	private WorstTradeOffers() {}

	public static void install() {
		set(VillagerProfession.ARMORER, 1, sell(Items.IRON_HELMET,5,1,12,1,.2F), sell(Items.IRON_BOOTS,4,1,12,1,.2F));
		set(VillagerProfession.ARMORER, 2, sell(Items.BELL,36,1,12,5,.2F), sell(Items.CHAINMAIL_BOOTS,1,1,12,5,.2F));
		set(VillagerProfession.ARMORER, 3, sell(Items.SHIELD,5,1,12,10,.2F), sell(Items.CHAINMAIL_HELMET,1,1,12,10,.2F));
		set(VillagerProfession.ARMORER, 4, enchanted(Items.DIAMOND_LEGGINGS,33,3,15,.2F,Enchantments.FIRE_PROTECTION), enchanted(Items.DIAMOND_BOOTS,27,3,15,.2F,Enchantments.FIRE_PROTECTION));
		set(VillagerProfession.ARMORER, 5, enchanted(Items.DIAMOND_HELMET,27,3,30,.2F,Enchantments.FIRE_PROTECTION), enchanted(Items.DIAMOND_CHESTPLATE,35,3,30,.2F,Enchantments.FIRE_PROTECTION));

		set(VillagerProfession.BUTCHER, 1, buy(Items.RABBIT,4,16,2), sell(Items.RABBIT_STEW,1,1,12,1,.05F));
		set(VillagerProfession.BUTCHER, 2, sell(Items.COOKED_PORKCHOP,1,5,16,5,.05F), sell(Items.COOKED_CHICKEN,1,8,16,5,.05F));
		set(VillagerProfession.CARTOGRAPHER, 3, buy(Items.COMPASS,1,12,20), new MapFactory(13, StructureTags.ON_OCEAN_EXPLORER_MAPS,"filled_map.monument",MapDecorationTypes.MONUMENT,12,10));
		set(VillagerProfession.CLERIC, 4, buy(Items.TURTLE_SCUTE,4,12,30), sell(Items.ENDER_PEARL,5,1,12,15,.05F));

		set(VillagerProfession.FARMER, 1, buy(Items.BEETROOT,15,16,2), sell(Items.BREAD,1,6,16,1,.05F));
		set(VillagerProfession.FARMER, 2, sell(Items.PUMPKIN_PIE,1,4,12,5,.05F), sell(Items.APPLE,1,4,16,5,.05F));
		set(VillagerProfession.FARMER, 4, stew(StatusEffects.WEAKNESS,140,15), stew(StatusEffects.POISON,280,15));

		set(VillagerProfession.FISHERMAN, 1, process(Items.COD,6,1,Items.COOKED_COD,6,16,1,.05F), sell(Items.COD_BUCKET,3,1,16,1,.05F));
		set(VillagerProfession.FISHERMAN, 2, process(Items.SALMON,6,1,Items.COOKED_SALMON,6,16,5,.05F), sell(Items.CAMPFIRE,2,1,12,5,.05F));

		set(VillagerProfession.FLETCHER, 1, sell(Items.ARROW,1,16,12,1,.05F), process(Blocks.GRAVEL,10,1,Items.FLINT,10,12,1,.05F));
		set(VillagerProfession.FLETCHER, 4, buy(Items.FEATHER,24,16,30), enchanted(Items.BOW,21,3,15,.05F,Enchantments.PUNCH));
		set(VillagerProfession.FLETCHER, 5, enchanted(Items.CROSSBOW,22,3,15,.05F,Enchantments.PIERCING), invisibilityArrows());

		set(VillagerProfession.LEATHERWORKER, 1, dyed(Items.LEATHER_LEGGINGS,3,12,1), dyed(Items.LEATHER_CHESTPLATE,7,12,1));
		set(VillagerProfession.LEATHERWORKER, 2, dyed(Items.LEATHER_HELMET,5,12,5), dyed(Items.LEATHER_BOOTS,4,12,5));

		set(VillagerProfession.LIBRARIAN, 1, enchantedBook(1), sell(Blocks.BOOKSHELF,9,1,12,1,.05F));
		set(VillagerProfession.LIBRARIAN, 2, enchantedBook(5), sell(Items.LANTERN,1,1,12,5,.05F));
		set(VillagerProfession.LIBRARIAN, 3, enchantedBook(10), sell(Items.GLASS,1,4,12,10,.05F));
		set(VillagerProfession.LIBRARIAN, 4, enchantedBook(15), sell(Items.COMPASS,4,1,12,15,.05F));

		set(VillagerProfession.TOOLSMITH, 1, sell(Items.STONE_HOE,1,1,12,1,.2F), sell(Items.STONE_SHOVEL,1,1,12,1,.2F));
		set(VillagerProfession.TOOLSMITH, 3, enchanted(Items.IRON_SHOVEL,21,3,10,.2F,Enchantments.EFFICIENCY), sell(Items.DIAMOND_HOE,4,1,3,10,.2F));
		set(VillagerProfession.TOOLSMITH, 4, enchanted(Items.DIAMOND_AXE,31,3,15,.2F,Enchantments.EFFICIENCY), enchanted(Items.DIAMOND_SHOVEL,24,3,15,.2F,Enchantments.EFFICIENCY));

		set(VillagerProfession.SHEPHERD, 1, buy(Blocks.BROWN_WOOL,18,16,2), sell(Items.SHEARS,2,1,12,1,.05F));
		set(VillagerProfession.SHEPHERD, 2, sell(Blocks.BLACK_CARPET,1,4,16,5,.05F), sell(Blocks.GRAY_CARPET,1,4,16,5,.05F));
		set(VillagerProfession.SHEPHERD, 3, sell(Blocks.BLACK_BED,3,1,12,10,.05F), sell(Blocks.GRAY_BED,3,1,12,10,.05F));
		set(VillagerProfession.SHEPHERD, 4, sell(Items.BLACK_BANNER,3,1,12,15,.05F), sell(Items.GRAY_BANNER,3,1,12,15,.05F));

		set(VillagerProfession.MASON, 3, sell(Blocks.POLISHED_ANDESITE,1,4,16,10,.05F), sell(Blocks.POLISHED_DIORITE,1,4,16,10,.05F));
		set(VillagerProfession.MASON, 4, sell(Blocks.BLACK_TERRACOTTA,1,1,12,15,.05F), sell(Blocks.GRAY_TERRACOTTA,1,1,12,15,.05F));

		set(VillagerProfession.WEAPONSMITH, 1, sell(Items.IRON_AXE,3,1,12,1,.2F), enchanted(Items.IRON_SWORD,21,3,1,.05F,Enchantments.BANE_OF_ARTHROPODS));
		set(VillagerProfession.WEAPONSMITH, 4, buy(Items.DIAMOND,1,12,30), enchanted(Items.DIAMOND_AXE,31,3,15,.2F,Enchantments.BANE_OF_ARTHROPODS));
		set(VillagerProfession.WEAPONSMITH, 5, enchanted(Items.DIAMOND_SWORD,27,3,30,.2F,Enchantments.BANE_OF_ARTHROPODS));
	}

	private static TradeOffers.Factory buy(ItemConvertible item,int count,int maxUses,int xp) {
		return (world,entity,random) -> new TradeOffer(new TradedItem(item,count),new ItemStack(Items.EMERALD),maxUses,xp,.05F);
	}
	private static TradeOffers.Factory sell(ItemConvertible item,int price,int count,int maxUses,int xp,float multiplier) {
		return (world,entity,random) -> new TradeOffer(new TradedItem(Items.EMERALD,price),new ItemStack(item,count),maxUses,xp,multiplier);
	}
	private static TradeOffers.Factory process(ItemConvertible input,int inputCount,int price,ItemConvertible output,int outputCount,int maxUses,int xp,float multiplier) {
		return (world,entity,random) -> new TradeOffer(new TradedItem(Items.EMERALD,price),Optional.of(new TradedItem(input,inputCount)),new ItemStack(output,outputCount),maxUses,xp,multiplier);
	}
	private static TradeOffers.Factory dyed(Item item,int price,int maxUses,int xp) {
		return (world,entity,random) -> {
			ItemStack result=DyedColorComponent.setColor(new ItemStack(item),List.of(DyeItem.byColor(DyeColor.BLACK)));
			return new TradeOffer(new TradedItem(Items.EMERALD,price),result,maxUses,xp,.2F);
		};
	}
	private static TradeOffers.Factory enchanted(Item item,int price,int maxUses,int xp,float multiplier,RegistryKey<Enchantment> key) {
		return (world,entity,random) -> {
			ItemStack result=new ItemStack(item);
			result.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(key),1);
			return new TradeOffer(new TradedItem(Items.EMERALD,price),result,maxUses,xp,multiplier);
		};
	}
	private static TradeOffers.Factory enchantedBook(int xp) {
		return (world,entity,random) -> {
			RegistryEntry<Enchantment> e=world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.VANISHING_CURSE);
			ItemStack result=EnchantmentHelper.getEnchantedBookWith(new EnchantmentLevelEntry(e,1));
			return new TradeOffer(new TradedItem(Items.EMERALD,38),Optional.of(new TradedItem(Items.BOOK)),result,12,xp,.2F);
		};
	}
	private static TradeOffers.Factory stew(RegistryEntry<StatusEffect> effect,int duration,int xp) {
		return (world,entity,random) -> {
			ItemStack result=new ItemStack(Items.SUSPICIOUS_STEW);
			result.set(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS,new SuspiciousStewEffectsComponent(List.of(new SuspiciousStewEffectsComponent.StewEffect(effect,duration))));
			return new TradeOffer(new TradedItem(Items.EMERALD),result,12,xp,.05F);
		};
	}
	private static TradeOffers.Factory invisibilityArrows() {
		return (world,entity,random) -> {
			ItemStack result=new ItemStack(Items.TIPPED_ARROW,5);
			result.set(DataComponentTypes.POTION_CONTENTS,new PotionContentsComponent(Potions.LONG_INVISIBILITY));
			return new TradeOffer(new TradedItem(Items.EMERALD,2),Optional.of(new TradedItem(Items.ARROW,5)),result,12,30,.05F);
		};
	}

	private static void set(RegistryKey<VillagerProfession> profession,int level,TradeOffers.Factory... factories) {
		setIn(TradeOffers.PROFESSION_TO_LEVELED_TRADE,profession,level,factories);
		setIn(TradeOffers.REBALANCED_PROFESSION_TO_LEVELED_TRADE,profession,level,factories);
	}
	private static void setIn(Map<RegistryKey<VillagerProfession>,Int2ObjectMap<TradeOffers.Factory[]>> map,RegistryKey<VillagerProfession> profession,int level,TradeOffers.Factory[] factories) {
		Int2ObjectMap<TradeOffers.Factory[]> levels=map.get(profession);
		if(levels!=null) levels.put(level,factories);
	}

	private record MapFactory(int price,net.minecraft.registry.tag.TagKey<Structure> structure,String nameKey,RegistryEntry<MapDecorationType> decoration,int maxUses,int xp) implements TradeOffers.Factory {
		@Override public TradeOffer create(ServerWorld world,Entity entity,Random random) {
			BlockPos target=world.locateStructure(structure,entity.getBlockPos(),100,true);
			if(target==null) return null;
			ItemStack result=FilledMapItem.createMap(world,target.getX(),target.getZ(),(byte)2,true,true);
			FilledMapItem.fillExplorationMap(world,result);
			MapState.addDecorationsNbt(result,target,"+",decoration);
			result.set(DataComponentTypes.ITEM_NAME,Text.translatable(nameKey));
			return new TradeOffer(new TradedItem(Items.EMERALD,price),Optional.of(new TradedItem(Items.COMPASS)),result,maxUses,xp,.2F);
		}
	}
}
