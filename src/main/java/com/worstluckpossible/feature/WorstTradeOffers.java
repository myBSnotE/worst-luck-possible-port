package com.worstluckpossible.feature;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapDecorationTypes;
import net.minecraft.potion.Potions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;

/** Installs deterministic worst-case trade pools for normal villagers. */
public final class WorstTradeOffers {
	private WorstTradeOffers() {
	}

	public static void install() {
		// Armorer
		set(VillagerProfession.ARMORER, 1,
				sell(Items.IRON_HELMET, 5, 1, 12, 1, 0.2F),
				sell(Items.IRON_BOOTS, 4, 1, 12, 1, 0.2F));
		set(VillagerProfession.ARMORER, 2,
				sell(Items.BELL, 36, 1, 12, 5, 0.2F),
				sell(Items.CHAINMAIL_BOOTS, 1, 1, 12, 5, 0.2F));
		set(VillagerProfession.ARMORER, 3,
				sell(Items.SHIELD, 5, 1, 12, 10, 0.2F),
				sell(Items.CHAINMAIL_HELMET, 1, 1, 12, 10, 0.2F));
		set(VillagerProfession.ARMORER, 4,
				new FixedEnchantedItemFactory(Items.DIAMOND_LEGGINGS, 33, 3, 15, 0.2F, Enchantments.FIRE_PROTECTION),
				new FixedEnchantedItemFactory(Items.DIAMOND_BOOTS, 27, 3, 15, 0.2F, Enchantments.FIRE_PROTECTION));
		set(VillagerProfession.ARMORER, 5,
				new FixedEnchantedItemFactory(Items.DIAMOND_HELMET, 27, 3, 30, 0.2F, Enchantments.FIRE_PROTECTION),
				new FixedEnchantedItemFactory(Items.DIAMOND_CHESTPLATE, 35, 3, 30, 0.2F, Enchantments.FIRE_PROTECTION));

		// Butcher
		set(VillagerProfession.BUTCHER, 1,
				new TradeOffers.BuyItemFactory(Items.RABBIT, 4, 16, 2),
				new TradeOffers.SellItemFactory(Items.RABBIT_STEW, 1, 1, 1));
		set(VillagerProfession.BUTCHER, 2,
				new TradeOffers.SellItemFactory(Items.COOKED_PORKCHOP, 1, 5, 16, 5),
				new TradeOffers.SellItemFactory(Items.COOKED_CHICKEN, 1, 8, 16, 5));

		// Cartographer: compass purchase plus the ocean explorer map, never the trial-chambers map.
		set(VillagerProfession.CARTOGRAPHER, 3,
				new TradeOffers.BuyItemFactory(Items.COMPASS, 1, 12, 20),
				new TradeOffers.SellMapFactory(13, StructureTags.ON_OCEAN_EXPLORER_MAPS,
						"filled_map.monument", MapDecorationTypes.MONUMENT, 12, 10));

		// Cleric: remove the glass-bottle sale at expert level.
		set(VillagerProfession.CLERIC, 4,
				new TradeOffers.BuyItemFactory(Items.TURTLE_SCUTE, 4, 12, 30),
				new TradeOffers.SellItemFactory(Items.ENDER_PEARL, 5, 1, 15));

		// Farmer
		set(VillagerProfession.FARMER, 1,
				new TradeOffers.BuyItemFactory(Items.BEETROOT, 15, 16, 2),
				new TradeOffers.SellItemFactory(Items.BREAD, 1, 6, 16, 1));
		set(VillagerProfession.FARMER, 2,
				new TradeOffers.SellItemFactory(Items.PUMPKIN_PIE, 1, 4, 5),
				new TradeOffers.SellItemFactory(Items.APPLE, 1, 4, 16, 5));
		set(VillagerProfession.FARMER, 4,
				new TradeOffers.SellSuspiciousStewFactory(StatusEffects.WEAKNESS, 140, 15),
				new TradeOffers.SellSuspiciousStewFactory(StatusEffects.POISON, 280, 15));

		// Fisherman
		set(VillagerProfession.FISHERMAN, 1,
				new TradeOffers.ProcessItemFactory(Items.COD, 6, 1, Items.COOKED_COD, 6, 16, 1, 0.05F),
				new TradeOffers.SellItemFactory(Items.COD_BUCKET, 3, 1, 16, 1));
		set(VillagerProfession.FISHERMAN, 2,
				new TradeOffers.ProcessItemFactory(Items.SALMON, 6, 1, Items.COOKED_SALMON, 6, 16, 5, 0.05F),
				new TradeOffers.SellItemFactory(Items.CAMPFIRE, 2, 1, 5));

		// Fletcher
		set(VillagerProfession.FLETCHER, 1,
				new TradeOffers.SellItemFactory(Items.ARROW, 1, 16, 1),
				new TradeOffers.ProcessItemFactory(Blocks.GRAVEL, 10, 1, Items.FLINT, 10, 12, 1, 0.05F));
		set(VillagerProfession.FLETCHER, 4,
				new TradeOffers.BuyItemFactory(Items.FEATHER, 24, 16, 30),
				new FixedEnchantedItemFactory(Items.BOW, 21, 3, 15, 0.05F, Enchantments.PUNCH));
		set(VillagerProfession.FLETCHER, 5,
				new FixedEnchantedItemFactory(Items.CROSSBOW, 22, 3, 15, 0.05F, Enchantments.PIERCING),
				new FixedInvisibilityArrowFactory());

		// Leatherworker
		set(VillagerProfession.LEATHERWORKER, 1,
				new TradeOffers.SellDyedArmorFactory(Items.LEATHER_LEGGINGS, 3),
				new TradeOffers.SellDyedArmorFactory(Items.LEATHER_CHESTPLATE, 7));
		set(VillagerProfession.LEATHERWORKER, 2,
				new TradeOffers.SellDyedArmorFactory(Items.LEATHER_HELMET, 5, 12, 5),
				new TradeOffers.SellDyedArmorFactory(Items.LEATHER_BOOTS, 4, 12, 5));

		// Librarian: Curse of Vanishing I at its maximum vanilla price (38 emeralds).
		set(VillagerProfession.LIBRARIAN, 1,
				new FixedEnchantedBookFactory(1),
				new TradeOffers.SellItemFactory(Blocks.BOOKSHELF, 9, 1, 12, 1));
		set(VillagerProfession.LIBRARIAN, 2,
				new FixedEnchantedBookFactory(5),
				new TradeOffers.SellItemFactory(Items.LANTERN, 1, 1, 5));
		set(VillagerProfession.LIBRARIAN, 3,
				new FixedEnchantedBookFactory(10),
				new TradeOffers.SellItemFactory(Items.GLASS, 1, 4, 10));
		set(VillagerProfession.LIBRARIAN, 4,
				new FixedEnchantedBookFactory(15),
				new TradeOffers.SellItemFactory(Items.COMPASS, 4, 1, 15));

		// Toolsmith
		set(VillagerProfession.TOOLSMITH, 1,
				sell(Items.STONE_HOE, 1, 1, 12, 1, 0.2F),
				sell(Items.STONE_SHOVEL, 1, 1, 12, 1, 0.2F));
		set(VillagerProfession.TOOLSMITH, 3,
				new FixedEnchantedItemFactory(Items.IRON_SHOVEL, 21, 3, 10, 0.2F, Enchantments.EFFICIENCY),
				sell(Items.DIAMOND_HOE, 4, 1, 3, 10, 0.2F));
		set(VillagerProfession.TOOLSMITH, 4,
				new FixedEnchantedItemFactory(Items.DIAMOND_AXE, 31, 3, 15, 0.2F, Enchantments.EFFICIENCY),
				new FixedEnchantedItemFactory(Items.DIAMOND_SHOVEL, 24, 3, 15, 0.2F, Enchantments.EFFICIENCY));

		// Shepherd
		set(VillagerProfession.SHEPHERD, 1,
				new TradeOffers.BuyItemFactory(Blocks.BROWN_WOOL, 18, 16, 2),
				new TradeOffers.SellItemFactory(Items.SHEARS, 2, 1, 1));
		set(VillagerProfession.SHEPHERD, 2,
				new TradeOffers.SellItemFactory(Blocks.BLACK_CARPET, 1, 4, 16, 5),
				new TradeOffers.SellItemFactory(Blocks.GRAY_CARPET, 1, 4, 16, 5));
		set(VillagerProfession.SHEPHERD, 3,
				new TradeOffers.SellItemFactory(Blocks.BLACK_BED, 3, 1, 12, 10),
				new TradeOffers.SellItemFactory(Blocks.GRAY_BED, 3, 1, 12, 10));
		set(VillagerProfession.SHEPHERD, 4,
				new TradeOffers.SellItemFactory(Items.BLACK_BANNER, 3, 1, 12, 15),
				new TradeOffers.SellItemFactory(Items.GRAY_BANNER, 3, 1, 12, 15));

		// Mason
		set(VillagerProfession.MASON, 3,
				new TradeOffers.SellItemFactory(Blocks.POLISHED_ANDESITE, 1, 4, 16, 10),
				new TradeOffers.SellItemFactory(Blocks.POLISHED_DIORITE, 1, 4, 16, 10));
		set(VillagerProfession.MASON, 4,
				new TradeOffers.SellItemFactory(Blocks.BLACK_TERRACOTTA, 1, 1, 12, 15),
				new TradeOffers.SellItemFactory(Blocks.GRAY_TERRACOTTA, 1, 1, 12, 15));

		// Weaponsmith
		set(VillagerProfession.WEAPONSMITH, 1,
				sell(Items.IRON_AXE, 3, 1, 12, 1, 0.2F),
				new FixedEnchantedItemFactory(Items.IRON_SWORD, 21, 3, 1, 0.05F, Enchantments.BANE_OF_ARTHROPODS));
		set(VillagerProfession.WEAPONSMITH, 4,
				new TradeOffers.BuyItemFactory(Items.DIAMOND, 1, 12, 30),
				new FixedEnchantedItemFactory(Items.DIAMOND_AXE, 31, 3, 15, 0.2F, Enchantments.BANE_OF_ARTHROPODS));
		set(VillagerProfession.WEAPONSMITH, 5,
				new FixedEnchantedItemFactory(Items.DIAMOND_SWORD, 27, 3, 30, 0.2F, Enchantments.BANE_OF_ARTHROPODS));
	}

	private static TradeOffers.Factory sell(Item item, int price, int count, int maxUses, int experience, float multiplier) {
		return new TradeOffers.SellItemFactory(new ItemStack(item), price, count, maxUses, experience, multiplier);
	}

	private static void set(RegistryKey<VillagerProfession> profession, int level, TradeOffers.Factory... factories) {
		setInMap(TradeOffers.PROFESSION_TO_LEVELED_TRADE, profession, level, factories);
		setInMap(TradeOffers.REBALANCED_PROFESSION_TO_LEVELED_TRADE, profession, level, factories);
	}

	private static void setInMap(
			Map<RegistryKey<VillagerProfession>, Int2ObjectMap<TradeOffers.Factory[]>> map,
			RegistryKey<VillagerProfession> profession,
			int level,
			TradeOffers.Factory[] factories
	) {
		Int2ObjectMap<TradeOffers.Factory[]> levels = map.get(profession);
		if (levels != null) {
			levels.put(level, factories);
		}
	}

	private record FixedEnchantedItemFactory(
			Item item,
			int price,
			int maxUses,
			int experience,
			float multiplier,
			RegistryKey<Enchantment> enchantmentKey
	) implements TradeOffers.Factory {
		@Override
		public TradeOffer create(ServerWorld world, Entity entity, Random random) {
			ItemStack result = new ItemStack(item);
			RegistryEntry<Enchantment> enchantment = world.getRegistryManager()
					.getOrThrow(RegistryKeys.ENCHANTMENT)
					.getOrThrow(enchantmentKey);
			result.addEnchantment(enchantment, 1);
			return new TradeOffer(new TradedItem(Items.EMERALD, price), result, maxUses, experience, multiplier);
		}
	}

	private record FixedEnchantedBookFactory(int experience) implements TradeOffers.Factory {
		@Override
		public TradeOffer create(ServerWorld world, Entity entity, Random random) {
			RegistryEntry<Enchantment> enchantment = world.getRegistryManager()
					.getOrThrow(RegistryKeys.ENCHANTMENT)
					.getOrThrow(Enchantments.VANISHING_CURSE);
			ItemStack result = EnchantmentHelper.getEnchantedBookWith(new EnchantmentLevelEntry(enchantment, 1));
			return new TradeOffer(
					new TradedItem(Items.EMERALD, 38),
					Optional.of(new TradedItem(Items.BOOK)),
					result,
					12,
					experience,
					0.2F
			);
		}
	}

	private static final class FixedInvisibilityArrowFactory implements TradeOffers.Factory {
		@Override
		public TradeOffer create(ServerWorld world, Entity entity, Random random) {
			ItemStack result = new ItemStack(Items.TIPPED_ARROW, 5);
			result.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Potions.LONG_INVISIBILITY));
			return new TradeOffer(
					new TradedItem(Items.EMERALD, 2),
					Optional.of(new TradedItem(Items.ARROW, 5)),
					result,
					12,
					30,
					0.05F
			);
		}
	}
}
