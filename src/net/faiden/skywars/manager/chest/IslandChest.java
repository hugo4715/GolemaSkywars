package net.faiden.skywars.manager.chest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.golema.database.support.builder.items.ItemBuilder;
import net.golema.database.support.utils.GolemaLogger;

public class IslandChest {

	private static final int FOOD_AMOUNT = 20;
	private static final int BLOCKS_AMOUNT = 60;
	private static final int TOOLS_AMOUNT = 20;

	private static List<RefuelChest> c;

	private static List<ItemStack> t1Armor;
	private static List<ItemStack> t1Swords;

	private static List<ItemStack> t2Armor;
	private static List<ItemStack> t2Swords;

	private static List<ItemStack> t3Armor;
	private static List<ItemStack> t3Swords;

	private static Map<ItemStack, Integer> food;
	private static List<ItemStack> potions;
	private static Map<ItemStack, Integer> blocks;
	private static HashMap<ItemStack, Integer> t2Blocks;
	private static Map<ItemStack, Integer> tools;

	private static Random rand = new Random();

	private static int power = 0;

	public static void refuel(boolean insane) {
		loadStuff(insane);

		c = RefuelChest.get();

		if (c == null || c.size() < 1) {
			GolemaLogger.log("There is no island chest to refuel!");
			return;
		}
		GolemaLogger.log("Starting refuel... (" + c.size() + " total chests to check)");

		if (power == 0) {
			refuelStartingIsland();
		}

		refuelMiddle(power, power > 0);

		unloadStuff();
		power++;
		GolemaLogger.log("Finished refuel!");

	}

	private static void refuelStartingIsland() {
		Map<Integer, List<RefuelChest>> chests = Maps.newHashMap();

		c.forEach(ch -> {
			if (ch.getId() != -1) {
				if (chests.get(ch.getId()) == null) {
					chests.put(ch.getId(), Lists.newArrayList());
				}
				List<RefuelChest> l = chests.get(ch.getId());
				l.add(ch);
				chests.put(ch.getId(), l);
			}
		});

		GolemaLogger.log("Found " + chests.size() + " island to refuel");
		GolemaLogger.log("Loaded all stuff");

		for (int i : chests.keySet()) {
			List<RefuelChest> f = chests.get(i);
			GolemaLogger.log(" island " + i + " contains " + f.size() + " hests");

			refuelIsland(f);
			GolemaLogger.log("Refuelled island " + i);
		}
	}

	private static void refuelMiddle(int power, boolean includeIslands) {
		rand = new Random();
		List<ItemStack> swords;
		List<ItemStack> armor;
		if (power <= 1) {
			swords = t2Swords;
			armor = t2Armor;
		} else if (power == 2) {
			swords = t3Swords;
			armor = t3Armor;
		} else {
			swords = t3Swords;
			armor = t3Armor;
		}

		c.forEach(chest -> {

			List<ItemStack> items = Lists.newArrayList();
			if ((includeIslands || chest.getId() == -1)
					&& chest.getLocation().getBlock().getType().equals(Material.CHEST)) {

				for (int i = 0; i < 5; i++) {
					double chance = rand.nextDouble();

					if (chance < 0.5) {
						items.add(swords.get(rand.nextInt(swords.size())));
					} else {
						items.add(armor.get(rand.nextInt(armor.size())));
					}

					chance = rand.nextDouble();
					if (chance < 0.3) {
						items.add(potions.get(rand.nextInt(potions.size())));
					} else if (chance < 0.5) {
						items.add(blocks.keySet().toArray(new ItemStack[blocks.size()])[rand.nextInt(blocks.size())]);
					} else if (chance < 0.7) {
						items.add(food.keySet().toArray(new ItemStack[food.size()])[rand.nextInt(food.size())]);
					} else {
						items.add(tools.keySet().toArray(new ItemStack[tools.size()])[rand.nextInt(tools.size())]);
					}
					
					if(power > 1 && chance > 0.8) {
						items.add(t2Blocks.keySet().toArray(new ItemStack[t2Blocks.size()])[rand.nextInt(t2Blocks.size())]);
					}
					
					if(chance < 0.001) {
						items.add(new ItemBuilder().type(Material.COOKIE).name("�1I�2'�3m�4 �5a�6 �7c�8o�9o�ak�bi�de").build());
					}
				}

				putInChest(items, ((Chest) chest.getLocation().getBlock().getState()).getBlockInventory());
			}
		});
	}

	private static void refuelIsland(List<RefuelChest> f) {
		GolemaLogger.log("Refuelling new island with " + f.size() + " chests");

		/*
		 * CHOOSE LOOT
		 */
		LinkedList<ItemStack> items = Lists.newLinkedList();

		/*
		 * Protection
		 */
		{
			ItemStack choosed = t1Armor.get(rand.nextInt(t1Armor.size()));
			for (int i = 0; i < 4; i++) {
				while (containsArmorPiece(items, choosed)) {
					choosed = t1Armor.get(rand.nextInt(t1Armor.size()));
				}
				items.add(choosed);
				GolemaLogger
						.log("Choosed tier 1 armor " + choosed.getType().toString().toLowerCase().replace("_", " "));
			}
		}

		/*
		 * Swords
		 */
		{
			ItemStack s = t1Swords.get(rand.nextInt(t1Swords.size()));
			items.add(s);
			GolemaLogger.log("Choosed sword " + s.getType().toString().toLowerCase().replace("_", " "));
		}

		/*
		 * Food
		 */
		{
			int amount = 0;
			while (amount < FOOD_AMOUNT) {
				ItemStack s = (ItemStack) food.keySet().toArray()[rand.nextInt(food.size())];
				amount += food.get(s);
				items.add(s);
			}
		}

		/*
		 * Potions
		 */
		{
			items.add(potions.get(rand.nextInt(potions.size())));
		}

		/*
		 * Blocks
		 */
		{
			int amount = 0;

			while (amount < BLOCKS_AMOUNT) {
				ItemStack s = (ItemStack) blocks.keySet().toArray()[rand.nextInt(blocks.size())];
				amount += blocks.get(s);
				items.add(s);
			}
		}

		/*
		 * tools
		 */
		{
			int amount = 0;

			while (amount < TOOLS_AMOUNT) {
				ItemStack s = (ItemStack) tools.keySet().toArray()[rand.nextInt(tools.size())];
				amount += tools.get(s);
				items.add(s);
			}
		}

		GolemaLogger.log("Splitting stuff between " + f.size() + " chests...");

		/*
		 * SPLIT LOOT BETWEEN CHEST
		 */
		List<List<ItemStack>> islands = Lists.newArrayList();

		// create lists
		for (int i = 0; i < f.size(); i++)
			islands.add(Lists.newArrayList());

		GolemaLogger.log("Created " + islands.size() + "item lists");

		while (!items.isEmpty()) {
			for (List<ItemStack> c : islands) {
				if (items.isEmpty())
					break;
				c.add(items.pop());
			}
		}

		GolemaLogger.log("Randomizing slots....");
		/*
		 * RANDOMIZATION OF SLOTS
		 */
		for (int i = 0; i < f.size(); i++) {
			List<ItemStack> c = islands.get(i);
			RefuelChest r = f.get(i);
			r.reset();

			Chest chest = (Chest) r.getLocation().getBlock().getState();
			Inventory inv = chest.getBlockInventory();
			putInChest(c, inv);
		}

		GolemaLogger.log("Finsished refuel of this island.");
	}

	private static boolean containsArmorPiece(LinkedList<ItemStack> items, ItemStack choosed) {
		String check = "";
		if (choosed.getType().toString().contains("HELMET"))
			check = "HELMET";
		if (choosed.getType().toString().contains("CHESTPLATE"))
			check = "CHESTPLATE";
		if (choosed.getType().toString().contains("LEGGINGS"))
			check = "LEGGINGS";
		if (choosed.getType().toString().contains("BOOTS"))
			check = "BOOTS";
		AtomicBoolean b = new AtomicBoolean(false);
		final String c = check;

		items.forEach(i -> {
			if (i.getType().toString().contains(c))
				b.set(true);
		});

		return b.get();
	}

	private static void putInChest(List<ItemStack> c, Inventory inv) {
		GolemaLogger.log("Randomzing " + c.size() + " items in slots");

		LinkedList<ItemStack> s = new LinkedList<>(c);
		while (!s.isEmpty() && inv.firstEmpty() >= 0) {
			int slot = rand.nextInt(inv.getSize());
			if (inv.getItem(slot) == null)
				inv.setItem(slot, s.pop());
		}
	}

	private static void unloadStuff() {
		t1Armor = null;
		t1Swords = null;

		t2Armor = null;
		t2Swords = null;

		t3Armor = null;
		t3Swords = null;

		blocks = null;
	}

	private static void loadStuff(boolean insane) {
		
		if(insane) {
			loadInsaneStuff();
		}else {
			loadNormalStuff();
		}
	}

	private static void loadNormalStuff() {
		t1Armor = Lists.newArrayList();
		{
			t1Armor.add(new ItemBuilder().type(Material.LEATHER_HELMET).build());
			t1Armor.add(new ItemBuilder().type(Material.LEATHER_CHESTPLATE).build());
			t1Armor.add(new ItemBuilder().type(Material.LEATHER_LEGGINGS).build());
			t1Armor.add(new ItemBuilder().type(Material.LEATHER_BOOTS).build());
			
			t1Armor.add(new ItemBuilder().type(Material.CHAINMAIL_HELMET).build());
			t1Armor.add(new ItemBuilder().type(Material.CHAINMAIL_CHESTPLATE).build());
			t1Armor.add(new ItemBuilder().type(Material.CHAINMAIL_LEGGINGS).build());
			t1Armor.add(new ItemBuilder().type(Material.CHAINMAIL_BOOTS).build());
			
			t1Armor.add(new ItemBuilder().type(Material.IRON_HELMET).build());
			t1Armor.add(new ItemBuilder().type(Material.IRON_CHESTPLATE).build());
			t1Armor.add(new ItemBuilder().type(Material.IRON_LEGGINGS).build());
			t1Armor.add(new ItemBuilder().type(Material.IRON_BOOTS).build());
		}
		
		t1Swords = Lists.newArrayList();
		{
			t1Swords.add(new ItemBuilder().type(Material.STONE_SWORD).build());
			t1Swords.add(new ItemBuilder().type(Material.IRON_SWORD).build());
		}

		t2Armor = Lists.newArrayList();
		{
			t2Armor.add(new ItemBuilder().type(Material.CHAINMAIL_HELMET).build());
			t2Armor.add(new ItemBuilder().type(Material.CHAINMAIL_CHESTPLATE).build());
			t2Armor.add(new ItemBuilder().type(Material.CHAINMAIL_LEGGINGS).build());
			t2Armor.add(new ItemBuilder().type(Material.CHAINMAIL_BOOTS).build());
			
			t2Armor.add(new ItemBuilder().type(Material.IRON_HELMET).build());
			t2Armor.add(new ItemBuilder().type(Material.IRON_CHESTPLATE).build());
			t2Armor.add(new ItemBuilder().type(Material.IRON_LEGGINGS).build());
			t2Armor.add(new ItemBuilder().type(Material.IRON_BOOTS).build());
			
			t2Armor.add(new ItemBuilder().type(Material.CHAINMAIL_CHESTPLATE).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_PROJECTILE, 3).build());
			t2Armor.add(new ItemBuilder().type(Material.CHAINMAIL_LEGGINGS).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build());
		}

		t2Swords = Lists.newArrayList();
		{
			t2Swords.add(new ItemBuilder().type(Material.FLINT_AND_STEEL).build());
			t2Swords.add(new ItemBuilder().type(Material.STONE_SWORD).enchantments(Maps.newHashMap()).enchant(Enchantment.DAMAGE_ALL, 1).build());
			t2Swords.add(new ItemBuilder().type(Material.IRON_SWORD).build());
			t2Swords.add(new ItemBuilder().type(Material.IRON_SWORD).enchantments(Maps.newHashMap()).enchant(Enchantment.FIRE_ASPECT, 1).build());
			t2Swords.add(new ItemBuilder().type(Material.BOW).enchantments(Maps.newHashMap()).enchant(Enchantment.ARROW_DAMAGE, 2).build());
			t2Swords.add(new ItemBuilder().type(Material.BOW).enchantments(Maps.newHashMap()).enchant(Enchantment.ARROW_DAMAGE, 1).build());
			t2Swords.add(new ItemBuilder().type(Material.GOLDEN_APPLE).amount(3).build());
			t2Swords.add(new ItemBuilder().type(Material.ENDER_PEARL).amount(1).build());
		}

		t3Armor = Lists.newArrayList();
		{
			
			t3Armor.add(new ItemBuilder().type(Material.CHAINMAIL_HELMET).build());
			t3Armor.add(new ItemBuilder().type(Material.CHAINMAIL_CHESTPLATE).build());
			t3Armor.add(new ItemBuilder().type(Material.CHAINMAIL_LEGGINGS).build());
			t3Armor.add(new ItemBuilder().type(Material.CHAINMAIL_BOOTS).build());
			
			t3Armor.add(new ItemBuilder().type(Material.IRON_HELMET).build());
			t3Armor.add(new ItemBuilder().type(Material.IRON_CHESTPLATE).build());
			t3Armor.add(new ItemBuilder().type(Material.IRON_LEGGINGS).build());
			t3Armor.add(new ItemBuilder().type(Material.IRON_BOOTS).build());
			
			
			t3Armor.add(new ItemBuilder().type(Material.DIAMOND_HELMET).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
			t3Armor.add(new ItemBuilder().type(Material.CHAINMAIL_CHESTPLATE).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_PROJECTILE, 3).build());
			t3Armor.add(new ItemBuilder().type(Material.CHAINMAIL_LEGGINGS).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build());
			t3Armor.add(new ItemBuilder().type(Material.DIAMOND_BOOTS).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_FIRE, 4).build());


			t3Armor.add(new ItemBuilder().type(Material.ENDER_PEARL).amount(1).build());
			t3Armor.add(new ItemBuilder().type(Material.ENDER_PEARL).amount(3).build());

			t3Armor.add(new ItemBuilder().type(Material.COMPASS).build());
		}

		t3Swords = Lists.newArrayList();
		{
			t3Swords.add(new ItemBuilder().type(Material.STONE_SWORD).enchantments(Maps.newHashMap()).enchant(Enchantment.DAMAGE_ALL, 1).build());
			t3Swords.add(new ItemBuilder().type(Material.IRON_SWORD).build());
			t3Swords.add(new ItemBuilder().type(Material.IRON_SWORD).enchantments(Maps.newHashMap()).enchant(Enchantment.FIRE_ASPECT, 1).build());
			
			t3Swords.add(new ItemBuilder().type(Material.ENDER_PEARL).amount(3).build());
			t3Swords.add(new ItemBuilder().type(Material.ENDER_PEARL).amount(5).build());
			t3Swords.add(new ItemBuilder().type(Material.COMPASS).build());
			
			//t3Swords.add(new Potion(PotionType.SPEED, Tier.ONE, false, false).toItemStack(1));
		}

		food = Maps.newHashMap();
		{
			food.put(new ItemBuilder().type(Material.COOKED_BEEF).amount(4).build(), 4);
			food.put(new ItemBuilder().type(Material.COOKED_BEEF).amount(12).build(), 12);

		}

		potions = Lists.newArrayList();
		{
			potions.add(new ItemBuilder().type(Material.GOLDEN_APPLE).build());

			//potions.add(new Potion(PotionType.FIRE_RESISTANCE, Tier.TWO, false).toItemStack(1));
			//potions.add(new Potion(PotionType.REGEN, Tier.ONE, true).toItemStack(1));
			//potions.add(new Potion(PotionType.SPEED, Tier.ONE, true, false).toItemStack(1));
			//potions.add(new Potion(PotionType.POISON, Tier.ONE, true, false).toItemStack(1));
		}

		blocks = Maps.newHashMap();
		{
			blocks.put(new ItemBuilder().type(Material.STONE).amount(8).build(), 10);
			blocks.put(new ItemBuilder().type(Material.STONE).amount(16).build(), 16);
			blocks.put(new ItemBuilder().type(Material.STONE).amount(32).build(), 16);
			blocks.put(new ItemBuilder().type(Material.WOOD).amount(8).build(), 10);
			blocks.put(new ItemBuilder().type(Material.WOOD).amount(16).build(), 16);
			blocks.put(new ItemBuilder().type(Material.WOOD).amount(32).build(), 16);
		}
		
		t2Blocks = Maps.newHashMap();
		{
			t2Blocks.put(new ItemBuilder().type(Material.STONE).amount(32).build(), 16);
			t2Blocks.put(new ItemBuilder().type(Material.STONE).amount(64).build(), 16);
			t2Blocks.put(new ItemBuilder().type(Material.WOOD).amount(32).build(), 16);
			t2Blocks.put(new ItemBuilder().type(Material.WOOD).amount(64).build(), 16);
			t2Blocks.put(new ItemBuilder().type(Material.LOG).amount(16).build(), 16);
			t2Blocks.put(new ItemBuilder().type(Material.LOG).amount(32).build(), 16);

			t2Blocks.put(new ItemBuilder().type(Material.TNT).amount(2).build(), 8);
			t2Blocks.put(new ItemBuilder().type(Material.TNT).amount(4).build(), 10);
		}

		tools = Maps.newHashMap();
		{
			tools.put(new ItemBuilder().type(Material.LAPIS_BLOCK).amount(1).build(), 2);
			tools.put(new ItemBuilder().type(Material.EXP_BOTTLE).amount(16).build(), 2);
			tools.put(new ItemBuilder().type(Material.EXP_BOTTLE).amount(8).build(), 3);
			
			
			
			tools.put(new ItemBuilder().type(Material.SNOW_BALL).amount(8).build(), 2);
			tools.put(new ItemBuilder().type(Material.SNOW_BALL).amount(16).build(), 5);

			tools.put(new ItemBuilder().type(Material.EGG).amount(8).build(), 2);
			tools.put(new ItemBuilder().type(Material.EGG).amount(16).build(), 5);

			tools.put(new ItemBuilder().type(Material.IRON_AXE).build(), 5);
			tools.put(new ItemBuilder().type(Material.IRON_AXE).enchantments(Maps.newHashMap()).enchant(Enchantment.DIG_SPEED, 1).build(), 5);
			tools.put(new ItemBuilder().type(Material.IRON_SPADE).build(), 5);
			tools.put(new ItemBuilder().type(Material.IRON_SPADE).enchantments(Maps.newHashMap()).enchant(Enchantment.DIG_SPEED, 1).build(), 5);
			tools.put(new ItemBuilder().type(Material.IRON_PICKAXE).build(), 5);
			tools.put(new ItemBuilder().type(Material.IRON_PICKAXE).enchantments(Maps.newHashMap()).enchant(Enchantment.DIG_SPEED, 1).build(), 5);

			tools.put(new ItemBuilder().type(Material.BOW).build(), 5);
			tools.put(new ItemBuilder().type(Material.BOW).enchantments(Maps.newHashMap()).enchant(Enchantment.ARROW_DAMAGE, 1).build(), 5);
			tools.put(new ItemBuilder().type(Material.ARROW).amount(8).build(), 3);
			tools.put(new ItemBuilder().type(Material.ARROW).amount(16).build(), 3);
			tools.put(new ItemBuilder().type(Material.ARROW).amount(24).build(), 3);

			tools.put(new ItemBuilder().type(Material.FLINT_AND_STEEL).build(), 3);
			tools.put(new ItemBuilder().type(Material.FISHING_ROD).build(), 3);
			tools.put(new ItemBuilder().type(Material.GOLDEN_APPLE).build(), 3);

			tools.put(new ItemBuilder().type(Material.ENCHANTMENT_TABLE).build(), 5);

			tools.put(new ItemBuilder().type(Material.WATER_BUCKET).build(), 5);
			tools.put(new ItemBuilder().type(Material.WATER_BUCKET).build(), 5);
			tools.put(new ItemBuilder().type(Material.LAVA_BUCKET).build(), 5);
		}
	}

	private static void loadInsaneStuff() {
		t1Armor = Lists.newArrayList();
		{
			t1Armor.add(new ItemBuilder().type(Material.DIAMOND_HELMET).build());
			t1Armor.add(new ItemBuilder().type(Material.DIAMOND_CHESTPLATE).build());
			t1Armor.add(new ItemBuilder().type(Material.DIAMOND_LEGGINGS).build());
			t1Armor.add(new ItemBuilder().type(Material.DIAMOND_BOOTS).build());
			
			t1Armor.add(new ItemBuilder().type(Material.IRON_HELMET).build());
			t1Armor.add(new ItemBuilder().type(Material.IRON_CHESTPLATE).build());
			t1Armor.add(new ItemBuilder().type(Material.IRON_LEGGINGS).build());
			t1Armor.add(new ItemBuilder().type(Material.IRON_BOOTS).build());
		}
		
		t1Swords = Lists.newArrayList();
		{
			t1Swords.add(new ItemBuilder().type(Material.STONE_SWORD).enchantments(Maps.newHashMap()).enchant(Enchantment.DAMAGE_ALL, 1).build());
			t1Swords.add(new ItemBuilder().type(Material.DIAMOND_SWORD).build());
			t1Swords.add(new ItemBuilder().type(Material.DIAMOND_SWORD).enchantments(Maps.newHashMap()).enchant(Enchantment.DAMAGE_ALL, 1).build());
		}

		t2Armor = Lists.newArrayList();
		{
			t2Armor.add(new ItemBuilder().type(Material.DIAMOND_HELMET).build());
			t2Armor.add(new ItemBuilder().type(Material.DIAMOND_HELMET).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build());
			t2Armor.add(new ItemBuilder().type(Material.DIAMOND_CHESTPLATE).build());
			t2Armor.add(new ItemBuilder().type(Material.DIAMOND_CHESTPLATE).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build());
			t2Armor.add(new ItemBuilder().type(Material.DIAMOND_CHESTPLATE).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_PROJECTILE, 4).build());
			t2Armor.add(new ItemBuilder().type(Material.DIAMOND_BOOTS).build());
			t2Armor.add(new ItemBuilder().type(Material.DIAMOND_BOOTS).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.PROTECTION_FALL, 2).build());
			
			t2Armor.add(new ItemBuilder().type(Material.IRON_HELMET).build());
			t2Armor.add(new ItemBuilder().type(Material.IRON_CHESTPLATE).build());
			t2Armor.add(new ItemBuilder().type(Material.IRON_LEGGINGS).build());
			t2Armor.add(new ItemBuilder().type(Material.IRON_BOOTS).build());
			
			
		}

		t2Swords = Lists.newArrayList();
		{
			t2Swords.add(new ItemBuilder().type(Material.FLINT_AND_STEEL).build());
			t2Swords.add(new ItemBuilder().type(Material.STONE_SWORD).enchantments(Maps.newHashMap()).enchant(Enchantment.DAMAGE_ALL, 1).build());
			t2Swords.add(new ItemBuilder().type(Material.DIAMOND_SWORD).build());
			t2Swords.add(new ItemBuilder().type(Material.DIAMOND_SWORD).enchantments(Maps.newHashMap()).enchant(Enchantment.DAMAGE_ALL, 1).build());
			t2Swords.add(new ItemBuilder().type(Material.BOW).enchantments(Maps.newHashMap()).enchant(Enchantment.ARROW_DAMAGE, 1).build());
			t2Swords.add(new ItemBuilder().type(Material.BOW).enchantments(Maps.newHashMap()).enchant(Enchantment.ARROW_DAMAGE, 5).build());
			if(Math.random() < 0.25)t2Swords.add(new ItemBuilder().type(Material.BOW).enchantments(Maps.newHashMap()).enchant(Enchantment.ARROW_KNOCKBACK, 2).build());
			t2Swords.add(new ItemBuilder().type(Material.GOLDEN_APPLE).amount(1).build());
			t2Swords.add(new ItemBuilder().type(Material.GOLDEN_APPLE).amount(3).build());
			t2Swords.add(new ItemBuilder().type(Material.GOLDEN_APPLE).amount(5).build());
			
			//t3Swords.add(new Potion(PotionType.INSTANT_HEAL, Tier.ONE, true, false).toItemStack(1));
		}

		t3Armor = Lists.newArrayList();
		{
			
			t3Armor.add(new ItemBuilder().type(Material.DIAMOND_HELMET).build());
			t3Armor.add(new ItemBuilder().type(Material.DIAMOND_HELMET).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build());
			t3Armor.add(new ItemBuilder().type(Material.DIAMOND_CHESTPLATE).build());
			t3Armor.add(new ItemBuilder().type(Material.DIAMOND_CHESTPLATE).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build());
			t3Armor.add(new ItemBuilder().type(Material.DIAMOND_CHESTPLATE).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_PROJECTILE, 4).build());
			t3Armor.add(new ItemBuilder().type(Material.DIAMOND_BOOTS).build());
			t3Armor.add(new ItemBuilder().type(Material.DIAMOND_BOOTS).enchantments(Maps.newHashMap()).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).enchant(Enchantment.PROTECTION_FALL, 2).build());

			t3Armor.add(new ItemBuilder().type(Material.ENDER_PEARL).amount(3).build());
			t3Armor.add(new ItemBuilder().type(Material.ENDER_PEARL).amount(5).build());

			t3Armor.add(new ItemBuilder().type(Material.COMPASS).build());
		}

		t3Swords = Lists.newArrayList();
		{
			t3Swords.add(new ItemBuilder().type(Material.FLINT_AND_STEEL).build());
			t3Swords.add(new ItemBuilder().type(Material.DIAMOND_SWORD).build());
			t3Swords.add(new ItemBuilder().type(Material.DIAMOND_SWORD).enchantments(Maps.newHashMap()).enchant(Enchantment.DAMAGE_ALL, 1).build());
			t3Swords.add(new ItemBuilder().type(Material.DIAMOND_SWORD).enchantments(Maps.newHashMap()).enchant(Enchantment.DAMAGE_ALL, 1).enchant(Enchantment.FIRE_ASPECT, 2).build());
			t3Swords.add(new ItemBuilder().type(Material.BOW).enchantments(Maps.newHashMap()).enchant(Enchantment.ARROW_DAMAGE, 1).build());
			t3Swords.add(new ItemBuilder().type(Material.BOW).enchantments(Maps.newHashMap()).enchant(Enchantment.ARROW_DAMAGE, 5).build());
			if(Math.random() < 0.25)t3Swords.add(new ItemBuilder().type(Material.BOW).enchantments(Maps.newHashMap()).enchant(Enchantment.ARROW_KNOCKBACK, 2).build());
			t3Swords.add(new ItemBuilder().type(Material.GOLDEN_APPLE).amount(1).build());
			t3Swords.add(new ItemBuilder().type(Material.GOLDEN_APPLE).amount(3).build());
			t3Swords.add(new ItemBuilder().type(Material.GOLDEN_APPLE).amount(5).build());
			
			//t3Swords.add(new Potion(PotionType.SPEED, Tier.ONE, false, false).toItemStack(1));
		}

		food = Maps.newHashMap();
		{
			food.put(new ItemBuilder().type(Material.COOKED_BEEF).amount(4).build(), 4);
			food.put(new ItemBuilder().type(Material.COOKED_BEEF).amount(12).build(), 12);

		}

		potions = Lists.newArrayList();
		{
			potions.add(new ItemBuilder().type(Material.GOLDEN_APPLE).build());
			potions.add(new ItemBuilder().type(Material.GOLDEN_APPLE).amount(2).build());

			//potions.add(new Potion(PotionType.FIRE_RESISTANCE, Tier.TWO, false).toItemStack(1));
			//potions.add(new Potion(PotionType.REGEN, Tier.ONE, true).toItemStack(1));
			//potions.add(new Potion(PotionType.SPEED, Tier.ONE, true, false).toItemStack(1));
			//potions.add(new Potion(PotionType.POISON, Tier.ONE, true, false).toItemStack(1));
		}

		blocks = Maps.newHashMap();
		{
			blocks.put(new ItemBuilder().type(Material.STONE).amount(8).build(), 10);
			blocks.put(new ItemBuilder().type(Material.STONE).amount(16).build(), 16);
			blocks.put(new ItemBuilder().type(Material.STONE).amount(32).build(), 16);
			blocks.put(new ItemBuilder().type(Material.WOOD).amount(8).build(), 10);
			blocks.put(new ItemBuilder().type(Material.WOOD).amount(16).build(), 16);
			blocks.put(new ItemBuilder().type(Material.WOOD).amount(32).build(), 16);
		}
		
		t2Blocks = Maps.newHashMap();
		{
			t2Blocks.put(new ItemBuilder().type(Material.STONE).amount(32).build(), 16);
			t2Blocks.put(new ItemBuilder().type(Material.STONE).amount(64).build(), 16);
			t2Blocks.put(new ItemBuilder().type(Material.WOOD).amount(32).build(), 16);
			t2Blocks.put(new ItemBuilder().type(Material.WOOD).amount(64).build(), 16);
			t2Blocks.put(new ItemBuilder().type(Material.LOG).amount(16).build(), 16);
			t2Blocks.put(new ItemBuilder().type(Material.LOG).amount(32).build(), 16);

			t2Blocks.put(new ItemBuilder().type(Material.TNT).amount(2).build(), 8);
			t2Blocks.put(new ItemBuilder().type(Material.TNT).amount(4).build(), 10);
			
			t2Blocks.put(new ItemBuilder().type(Material.DIAMOND_PICKAXE).enchantments(Maps.newHashMap()).enchant(Enchantment.DIG_SPEED, 3).build(), 16);
			t2Blocks.put(new ItemBuilder().type(Material.DIAMOND_AXE).enchantments(Maps.newHashMap()).enchant(Enchantment.DIG_SPEED, 2).build(), 16);
		}

		tools = Maps.newHashMap();
		{
			tools.put(new ItemBuilder().type(Material.LAPIS_BLOCK).amount(1).build(), 2);
			tools.put(new ItemBuilder().type(Material.EXP_BOTTLE).amount(16).build(), 2);
			tools.put(new ItemBuilder().type(Material.EXP_BOTTLE).amount(8).build(), 3);
			
			
			
			tools.put(new ItemBuilder().type(Material.SNOW_BALL).amount(8).build(), 2);
			tools.put(new ItemBuilder().type(Material.SNOW_BALL).amount(16).build(), 5);

			tools.put(new ItemBuilder().type(Material.EGG).amount(8).build(), 2);
			tools.put(new ItemBuilder().type(Material.EGG).amount(16).build(), 5);

			tools.put(new ItemBuilder().type(Material.IRON_AXE).build(), 5);
			tools.put(new ItemBuilder().type(Material.IRON_AXE).enchantments(Maps.newHashMap()).enchant(Enchantment.DIG_SPEED, 1).build(), 5);
			tools.put(new ItemBuilder().type(Material.IRON_SPADE).build(), 5);
			tools.put(new ItemBuilder().type(Material.IRON_SPADE).enchantments(Maps.newHashMap()).enchant(Enchantment.DIG_SPEED, 1).build(), 5);
			tools.put(new ItemBuilder().type(Material.IRON_PICKAXE).build(), 5);
			tools.put(new ItemBuilder().type(Material.IRON_PICKAXE).enchantments(Maps.newHashMap()).enchant(Enchantment.DIG_SPEED, 1).build(), 5);

			tools.put(new ItemBuilder().type(Material.BOW).build(), 5);
			tools.put(new ItemBuilder().type(Material.BOW).enchantments(Maps.newHashMap()).enchant(Enchantment.ARROW_DAMAGE, 1).build(), 5);
			tools.put(new ItemBuilder().type(Material.ARROW).amount(8).build(), 3);
			tools.put(new ItemBuilder().type(Material.ARROW).amount(16).build(), 3);
			tools.put(new ItemBuilder().type(Material.ARROW).amount(24).build(), 3);

			tools.put(new ItemBuilder().type(Material.FLINT_AND_STEEL).build(), 3);
			tools.put(new ItemBuilder().type(Material.FISHING_ROD).build(), 3);
			tools.put(new ItemBuilder().type(Material.FISHING_ROD).build(), 3);
			tools.put(new ItemBuilder().type(Material.FISHING_ROD).build(), 3);
			tools.put(new ItemBuilder().type(Material.GOLDEN_APPLE).build(), 3);

			tools.put(new ItemBuilder().type(Material.ENCHANTMENT_TABLE).build(), 5);

			tools.put(new ItemBuilder().type(Material.WATER_BUCKET).build(), 5);
			tools.put(new ItemBuilder().type(Material.WATER_BUCKET).build(), 5);
			tools.put(new ItemBuilder().type(Material.LAVA_BUCKET).build(), 5);

		}
	}
}