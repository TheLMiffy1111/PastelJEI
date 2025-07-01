package thelm.pasteljei;

import java.util.stream.Stream;

import earth.terrarium.pastel.PastelCommon;
import earth.terrarium.pastel.blocks.idols.FirestarterIdolBlock;
import earth.terrarium.pastel.blocks.idols.FreezingIdolBlock;
import earth.terrarium.pastel.data_loaders.NaturesStaffConversionDataLoader;
import earth.terrarium.pastel.inventories.BlackHoleChestScreen;
import earth.terrarium.pastel.inventories.CinderhearthScreen;
import earth.terrarium.pastel.inventories.CinderhearthScreenHandler;
import earth.terrarium.pastel.inventories.CraftingTabletScreen;
import earth.terrarium.pastel.inventories.CraftingTabletScreenHandler;
import earth.terrarium.pastel.inventories.FilteringScreen;
import earth.terrarium.pastel.inventories.PastelScreenHandlerTypes;
import earth.terrarium.pastel.inventories.PedestalScreen;
import earth.terrarium.pastel.inventories.PedestalScreenHandler;
import earth.terrarium.pastel.inventories.PotionWorkshopScreen;
import earth.terrarium.pastel.inventories.PotionWorkshopScreenHandler;
import earth.terrarium.pastel.inventories.QuickNavigationGridScreen;
import earth.terrarium.pastel.recipe.InkConvertingRecipe;
import earth.terrarium.pastel.recipe.anvil_crushing.AnvilCrushingRecipe;
import earth.terrarium.pastel.recipe.cinderhearth.CinderhearthRecipe;
import earth.terrarium.pastel.recipe.crystallarieum.CrystallarieumRecipe;
import earth.terrarium.pastel.recipe.enchanter.EnchanterCraftingRecipe;
import earth.terrarium.pastel.recipe.enchanter.EnchantmentUpgradeRecipe;
import earth.terrarium.pastel.recipe.fluid_converting.DragonrotConvertingRecipe;
import earth.terrarium.pastel.recipe.fluid_converting.HumusConvertingRecipe;
import earth.terrarium.pastel.recipe.fluid_converting.LiquidCrystalConvertingRecipe;
import earth.terrarium.pastel.recipe.fluid_converting.MidnightSolutionConvertingRecipe;
import earth.terrarium.pastel.recipe.fusion_shrine.FusionShrineRecipe;
import earth.terrarium.pastel.recipe.pedestal.PedestalRecipe;
import earth.terrarium.pastel.recipe.pedestal.PedestalRecipeTier;
import earth.terrarium.pastel.recipe.potion_workshop.PotionWorkshopBrewingRecipe;
import earth.terrarium.pastel.recipe.potion_workshop.PotionWorkshopCraftingRecipe;
import earth.terrarium.pastel.recipe.potion_workshop.PotionWorkshopReactingRecipe;
import earth.terrarium.pastel.recipe.primordial_fire_burning.PrimordialFireBurningRecipe;
import earth.terrarium.pastel.recipe.spirit_instiller.SpiritInstillerRecipe;
import earth.terrarium.pastel.recipe.titration_barrel.ITitrationBarrelRecipe;
import earth.terrarium.pastel.registries.PastelAdvancements;
import earth.terrarium.pastel.registries.PastelBlocks;
import earth.terrarium.pastel.registries.PastelItems;
import earth.terrarium.pastel.registries.PastelRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Blocks;
import thelm.pasteljei.gui.handler.CraftingTabletRecipeClickAreaHandler;
import thelm.pasteljei.gui.handler.OverlayHidingExtraAreaHandler;
import thelm.pasteljei.gui.handler.PedestalRecipeClickAreaHandler;
import thelm.pasteljei.gui.handler.ShadowSlotGhostIngredientHandler;
import thelm.pasteljei.recipe.BlockConversionRecipe;
import thelm.pasteljei.recipe.BlockConversionWithChanceRecipe;
import thelm.pasteljei.recipe.category.AnvilCrushingRecipeCategory;
import thelm.pasteljei.recipe.category.BlockConversionRecipeCategory;
import thelm.pasteljei.recipe.category.BlockConversionWithChanceRecipeCategory;
import thelm.pasteljei.recipe.category.CinderhearthRecipeCategory;
import thelm.pasteljei.recipe.category.CrystallarieumRecipeCategory;
import thelm.pasteljei.recipe.category.DescriptiveGatedRecipeCategory;
import thelm.pasteljei.recipe.category.EnchanterRecipeCategory;
import thelm.pasteljei.recipe.category.EnchantmentUpgradeRecipeCategory;
import thelm.pasteljei.recipe.category.FluidConvertingRecipeCategory;
import thelm.pasteljei.recipe.category.FusionShrineRecipeCategory;
import thelm.pasteljei.recipe.category.InkConvertingRecipeCategory;
import thelm.pasteljei.recipe.category.PedestalRecipeCategory;
import thelm.pasteljei.recipe.category.PotionWorkshopRecipeCategory;
import thelm.pasteljei.recipe.category.PrimordialFireBurningRecipeCategory;
import thelm.pasteljei.recipe.category.SpiritInstillerRecipeCategory;
import thelm.pasteljei.recipe.category.TitrationBarrelRecipeCategory;
import thelm.pasteljei.recipe.transfer.CraftingTabletRecipeTransferHandler;
import thelm.pasteljei.recipe.transfer.GatedRecipeTransferInfo;
import thelm.pasteljei.recipe.transfer.PedestalRecipeTransferInfo;

@JeiPlugin
public class PastelJEI implements IModPlugin {

	public static final ResourceLocation UID = ResourceLocation.parse("pasteljei:pastel");

	public static IJeiHelpers jeiHelpers;
	public static IJeiRuntime jeiRuntime;

	//public static final List<RecipeType<?>> RECIPE_TYPES = new ArrayList<>();
	public static final RecipeType<RecipeHolder<PedestalRecipe>> PEDESTAL_BASIC = RecipeType.createRecipeHolderType(PastelCommon.locate("pedestal_basic"));
	public static final RecipeType<RecipeHolder<PedestalRecipe>> PEDESTAL_SIMPLE = RecipeType.createRecipeHolderType(PastelCommon.locate("pedestal_simple"));
	public static final RecipeType<RecipeHolder<PedestalRecipe>> PEDESTAL_ADVANCED = RecipeType.createRecipeHolderType(PastelCommon.locate("pedestal_advanced"));
	public static final RecipeType<RecipeHolder<PedestalRecipe>> PEDESTAL_COMPLEX = RecipeType.createRecipeHolderType(PastelCommon.locate("pedestal_complex"));
	public static final RecipeType<RecipeHolder<AnvilCrushingRecipe>> ANVIL_CRUSHING = RecipeType.createRecipeHolderType(PastelCommon.locate("anvil_crushing"));
	public static final RecipeType<RecipeHolder<FusionShrineRecipe>> FUSION_SHRINE = RecipeType.createRecipeHolderType(PastelCommon.locate("fusion_shrine"));
	public static final RecipeType<RecipeHolder<EnchanterCraftingRecipe>> ENCHANTER = RecipeType.createRecipeHolderType(PastelCommon.locate("enchanter"));
	public static final RecipeType<RecipeHolder<EnchantmentUpgradeRecipe>> ENCHANTMENT_UPGRADE = RecipeType.createRecipeHolderType(PastelCommon.locate("enchantment_upgrade"));
	public static final RecipeType<RecipeHolder<PotionWorkshopBrewingRecipe>> POTION_WORKSHOP_BREWING = RecipeType.createRecipeHolderType(PastelCommon.locate("potion_workshop_brewing"));
	public static final RecipeType<RecipeHolder<PotionWorkshopCraftingRecipe>> POTION_WORKSHOP_CRAFTING = RecipeType.createRecipeHolderType(PastelCommon.locate("potion_workshop_crafting"));
	public static final RecipeType<RecipeHolder<PotionWorkshopReactingRecipe>> POTION_WORKSHOP_REACTING = RecipeType.createRecipeHolderType(PastelCommon.locate("potion_workshop_reacting"));
	public static final RecipeType<RecipeHolder<HumusConvertingRecipe>> HUMUS_CONVERTING = RecipeType.createRecipeHolderType(PastelCommon.locate("humus_converting"));
	public static final RecipeType<RecipeHolder<LiquidCrystalConvertingRecipe>> LIQUID_CRYSTAL_CONVERTING = RecipeType.createRecipeHolderType(PastelCommon.locate("liquid_crystal_converting"));
	public static final RecipeType<RecipeHolder<MidnightSolutionConvertingRecipe>> MIDNIGHT_SOLUTION_CONVERTING = RecipeType.createRecipeHolderType(PastelCommon.locate("midnight_solution_converting"));
	public static final RecipeType<RecipeHolder<DragonrotConvertingRecipe>> DRAGONROT_CONVERTING = RecipeType.createRecipeHolderType(PastelCommon.locate("dragonrot_converting"));
	public static final RecipeType<RecipeHolder<SpiritInstillerRecipe>> SPIRIT_INSTILLER = RecipeType.createRecipeHolderType(PastelCommon.locate("spirit_instiller"));
	public static final RecipeType<RecipeHolder<InkConvertingRecipe>> INK_CONVERTING = RecipeType.createRecipeHolderType(PastelCommon.locate("ink_converting"));
	public static final RecipeType<RecipeHolder<CrystallarieumRecipe>> CRYSTALLARIEUM = RecipeType.createRecipeHolderType(PastelCommon.locate("crystallarieum"));
	public static final RecipeType<RecipeHolder<CinderhearthRecipe>> CINDERHEARTH = RecipeType.createRecipeHolderType(PastelCommon.locate("cinderhearth"));
	public static final RecipeType<RecipeHolder<ITitrationBarrelRecipe>> TITRATION_BARREL = RecipeType.createRecipeHolderType(PastelCommon.locate("titration_barrel"));
	public static final RecipeType<RecipeHolder<PrimordialFireBurningRecipe>> PRIMORDIAL_FIRE_BURNING = RecipeType.createRecipeHolderType(PastelCommon.locate("primordial_fire_burning"));

	public static final RecipeType<BlockConversionRecipe> NATURES_STAFF = new RecipeType<>(PastelCommon.locate("natures_staff"), BlockConversionRecipe.class);
	public static final RecipeType<BlockConversionWithChanceRecipe> HEATING = new RecipeType<>(PastelCommon.locate("heating"), BlockConversionWithChanceRecipe.class);
	public static final RecipeType<BlockConversionWithChanceRecipe> FREEZING = new RecipeType<>(PastelCommon.locate("freezing"), BlockConversionWithChanceRecipe.class);

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		jeiHelpers = registration.getJeiHelpers();

		registration.addRecipeCategories(new PedestalRecipeCategory(PedestalRecipeTier.BASIC));
		registration.addRecipeCategories(new PedestalRecipeCategory(PedestalRecipeTier.SIMPLE));
		registration.addRecipeCategories(new PedestalRecipeCategory(PedestalRecipeTier.ADVANCED));
		registration.addRecipeCategories(new PedestalRecipeCategory(PedestalRecipeTier.COMPLEX));
		registration.addRecipeCategories(new AnvilCrushingRecipeCategory());
		registration.addRecipeCategories(new FusionShrineRecipeCategory());
		registration.addRecipeCategories(new EnchanterRecipeCategory());
		registration.addRecipeCategories(new EnchantmentUpgradeRecipeCategory());
		registration.addRecipeCategories(new PotionWorkshopRecipeCategory<>(POTION_WORKSHOP_BREWING, Component.translatable("container.pastel.rei.potion_workshop_brewing.title")));
		registration.addRecipeCategories(new PotionWorkshopRecipeCategory<>(POTION_WORKSHOP_CRAFTING, Component.translatable("container.pastel.rei.potion_workshop_crafting.title")));
		registration.addRecipeCategories(new DescriptiveGatedRecipeCategory<>(POTION_WORKSHOP_REACTING, Component.translatable("container.pastel.rei.potion_workshop_reacting.title")));
		registration.addRecipeCategories(new FluidConvertingRecipeCategory<>(HUMUS_CONVERTING, Component.translatable("container.pastel.rei.humus_converting.title")));
		registration.addRecipeCategories(new FluidConvertingRecipeCategory<>(LIQUID_CRYSTAL_CONVERTING, Component.translatable("container.pastel.rei.liquid_crystal_converting.title")));
		registration.addRecipeCategories(new FluidConvertingRecipeCategory<>(MIDNIGHT_SOLUTION_CONVERTING, Component.translatable("container.pastel.rei.midnight_solution_converting.title")));
		registration.addRecipeCategories(new FluidConvertingRecipeCategory<>(DRAGONROT_CONVERTING, Component.translatable("container.pastel.rei.dragonrot_converting.title")));
		registration.addRecipeCategories(new SpiritInstillerRecipeCategory());
		registration.addRecipeCategories(new InkConvertingRecipeCategory());
		registration.addRecipeCategories(new CrystallarieumRecipeCategory());
		registration.addRecipeCategories(new CinderhearthRecipeCategory());
		registration.addRecipeCategories(new TitrationBarrelRecipeCategory());
		registration.addRecipeCategories(new PrimordialFireBurningRecipeCategory());

		registration.addRecipeCategories(new BlockConversionRecipeCategory(NATURES_STAFF, Component.translatable("item.pastel.natures_staff"), PastelAdvancements.UNLOCK_NATURES_STAFF));
		registration.addRecipeCategories(new BlockConversionWithChanceRecipeCategory(HEATING, Component.translatable("container.pastel.rei.heating.title"), PastelAdvancements.UNLOCK_IDOLS));
		registration.addRecipeCategories(new BlockConversionWithChanceRecipeCategory(FREEZING, Component.translatable("container.pastel.rei.freezing.title"), PastelAdvancements.UNLOCK_IDOLS));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
		registration.addRecipes(PEDESTAL_BASIC, recipeManager.getAllRecipesFor(PastelRecipeTypes.PEDESTAL).stream().
				filter(r -> r.value().getTier() == PedestalRecipeTier.BASIC).toList());
		registration.addRecipes(PEDESTAL_SIMPLE, recipeManager.getAllRecipesFor(PastelRecipeTypes.PEDESTAL).stream().
				filter(r -> r.value().getTier() == PedestalRecipeTier.SIMPLE).toList());
		registration.addRecipes(PEDESTAL_ADVANCED, recipeManager.getAllRecipesFor(PastelRecipeTypes.PEDESTAL).stream().
				filter(r -> r.value().getTier() == PedestalRecipeTier.ADVANCED).toList());
		registration.addRecipes(PEDESTAL_COMPLEX, recipeManager.getAllRecipesFor(PastelRecipeTypes.PEDESTAL).stream().
				filter(r -> r.value().getTier() == PedestalRecipeTier.COMPLEX).toList());
		registration.addRecipes(ANVIL_CRUSHING, recipeManager.getAllRecipesFor(PastelRecipeTypes.ANVIL_CRUSHING));
		registration.addRecipes(FUSION_SHRINE, recipeManager.getAllRecipesFor(PastelRecipeTypes.FUSION_SHRINE));
		registration.addRecipes(ENCHANTER, recipeManager.getAllRecipesFor(PastelRecipeTypes.ENCHANTER));
		registration.addRecipes(ENCHANTMENT_UPGRADE, recipeManager.getAllRecipesFor(PastelRecipeTypes.ENCHANTMENT_UPGRADE));
		registration.addRecipes(POTION_WORKSHOP_BREWING, recipeManager.getAllRecipesFor(PastelRecipeTypes.POTION_WORKSHOP_BREWING));
		registration.addRecipes(POTION_WORKSHOP_CRAFTING, recipeManager.getAllRecipesFor(PastelRecipeTypes.POTION_WORKSHOP_CRAFTING));
		registration.addRecipes(POTION_WORKSHOP_REACTING, recipeManager.getAllRecipesFor(PastelRecipeTypes.POTION_WORKSHOP_REACTING));
		registration.addRecipes(HUMUS_CONVERTING, recipeManager.getAllRecipesFor(PastelRecipeTypes.HUMUS_CONVERTING));
		registration.addRecipes(LIQUID_CRYSTAL_CONVERTING, recipeManager.getAllRecipesFor(PastelRecipeTypes.LIQUID_CRYSTAL_CONVERTING));
		registration.addRecipes(MIDNIGHT_SOLUTION_CONVERTING, recipeManager.getAllRecipesFor(PastelRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING));
		registration.addRecipes(DRAGONROT_CONVERTING, recipeManager.getAllRecipesFor(PastelRecipeTypes.DRAGONROT_CONVERTING));
		registration.addRecipes(SPIRIT_INSTILLER, recipeManager.getAllRecipesFor(PastelRecipeTypes.SPIRIT_INSTILLING));
		registration.addRecipes(INK_CONVERTING, recipeManager.getAllRecipesFor(PastelRecipeTypes.INK_CONVERTING));
		registration.addRecipes(CRYSTALLARIEUM, recipeManager.getAllRecipesFor(PastelRecipeTypes.CRYSTALLARIEUM));
		registration.addRecipes(CINDERHEARTH, recipeManager.getAllRecipesFor(PastelRecipeTypes.CINDERHEARTH));
		registration.addRecipes(TITRATION_BARREL, recipeManager.getAllRecipesFor(PastelRecipeTypes.TITRATION_BARREL));
		registration.addRecipes(PRIMORDIAL_FIRE_BURNING, recipeManager.getAllRecipesFor(PastelRecipeTypes.PRIMORDIAL_FIRE_BURNING));

		registration.addRecipes(NATURES_STAFF,
				NaturesStaffConversionDataLoader.CONVERSIONS.entrySet().stream().
				map(entry -> new BlockConversionRecipe(entry.getKey(), entry.getValue())).
				filter(BlockConversionRecipe::isViewable).toList());
		registration.addRecipes(HEATING,
				FirestarterIdolBlock.BURNING_MAP.entrySet().stream().
				map(entry -> new BlockConversionWithChanceRecipe(entry.getKey(), entry.getValue().getA(), entry.getValue().getB())).
				filter(BlockConversionWithChanceRecipe::isViewable).toList());
		registration.addRecipes(FREEZING,
				Stream.concat(
						FreezingIdolBlock.FREEZING_STATE_MAP.entrySet().stream().
						map(entry -> new BlockConversionWithChanceRecipe(entry.getKey(), entry.getValue().getA(), entry.getValue().getB())),
						FreezingIdolBlock.FREEZING_MAP.entrySet().stream().
						map(entry -> new BlockConversionWithChanceRecipe(entry.getKey(), entry.getValue().getA(), entry.getValue().getB()))).
				filter(BlockConversionWithChanceRecipe::isViewable).toList());
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		IRecipeTransferHandlerHelper transferHelper = registration.getTransferHelper();
		registration.addRecipeTransferHandler(new PedestalRecipeTransferInfo(PedestalRecipeTier.BASIC));
		registration.addRecipeTransferHandler(new PedestalRecipeTransferInfo(PedestalRecipeTier.SIMPLE));
		registration.addRecipeTransferHandler(new PedestalRecipeTransferInfo(PedestalRecipeTier.ADVANCED));
		registration.addRecipeTransferHandler(new PedestalRecipeTransferInfo(PedestalRecipeTier.COMPLEX));
		registration.addRecipeTransferHandler(PedestalScreenHandler.class, PastelScreenHandlerTypes.PEDESTAL, RecipeTypes.CRAFTING, 0, 9, 16, 36);
		registration.addRecipeTransferHandler(new CraftingTabletRecipeTransferHandler(PedestalRecipeTier.BASIC, transferHelper), PEDESTAL_BASIC);
		registration.addRecipeTransferHandler(new CraftingTabletRecipeTransferHandler(PedestalRecipeTier.SIMPLE, transferHelper), PEDESTAL_SIMPLE);
		registration.addRecipeTransferHandler(new CraftingTabletRecipeTransferHandler(PedestalRecipeTier.ADVANCED, transferHelper), PEDESTAL_ADVANCED);
		registration.addRecipeTransferHandler(new CraftingTabletRecipeTransferHandler(PedestalRecipeTier.COMPLEX, transferHelper), PEDESTAL_COMPLEX);
		registration.addRecipeTransferHandler(CraftingTabletScreenHandler.class, PastelScreenHandlerTypes.CRAFTING_TABLET, RecipeTypes.CRAFTING, 0, 9, 15, 36);
		registration.addRecipeTransferHandler(new GatedRecipeTransferInfo<>(PotionWorkshopScreenHandler.class, PastelScreenHandlerTypes.POTION_WORKSHOP, POTION_WORKSHOP_BREWING, 0, 9, 21, 36));
		registration.addRecipeTransferHandler(new GatedRecipeTransferInfo<>(PotionWorkshopScreenHandler.class, PastelScreenHandlerTypes.POTION_WORKSHOP, POTION_WORKSHOP_CRAFTING, 0, 9, 21, 36));
		registration.addRecipeTransferHandler(new GatedRecipeTransferInfo<>(CinderhearthScreenHandler.class, PastelScreenHandlerTypes.CINDERHEARTH, CINDERHEARTH, 2, 1, 11, 36));
		registration.addRecipeTransferHandler(CinderhearthScreenHandler.class, PastelScreenHandlerTypes.CINDERHEARTH, RecipeTypes.BLASTING, 2, 1, 11, 36);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(PastelBlocks.FABRICATION_CHEST, RecipeTypes.CRAFTING);

		registration.addRecipeCatalyst(PastelBlocks.PEDESTAL_BASIC_TOPAZ, PEDESTAL_BASIC);
		registration.addRecipeCatalyst(PastelBlocks.PEDESTAL_BASIC_AMETHYST, PEDESTAL_BASIC);
		registration.addRecipeCatalyst(PastelBlocks.PEDESTAL_BASIC_CITRINE, PEDESTAL_BASIC);
		registration.addRecipeCatalyst(PastelBlocks.PEDESTAL_ALL_BASIC, PEDESTAL_BASIC, PEDESTAL_SIMPLE);
		registration.addRecipeCatalyst(PastelBlocks.PEDESTAL_ONYX, PEDESTAL_BASIC, PEDESTAL_SIMPLE, PEDESTAL_ADVANCED);
		registration.addRecipeCatalyst(PastelBlocks.PEDESTAL_MOONSTONE, PEDESTAL_BASIC, PEDESTAL_SIMPLE, PEDESTAL_ADVANCED, PEDESTAL_COMPLEX);
		if(PastelCommon.CONFIG.canPedestalCraftVanillaRecipes()) {
			registration.addRecipeCatalyst(PastelBlocks.PEDESTAL_BASIC_TOPAZ, RecipeTypes.CRAFTING);
			registration.addRecipeCatalyst(PastelBlocks.PEDESTAL_BASIC_AMETHYST, RecipeTypes.CRAFTING);
			registration.addRecipeCatalyst(PastelBlocks.PEDESTAL_BASIC_CITRINE, RecipeTypes.CRAFTING);
			registration.addRecipeCatalyst(PastelBlocks.PEDESTAL_ALL_BASIC, RecipeTypes.CRAFTING);
			registration.addRecipeCatalyst(PastelBlocks.PEDESTAL_ONYX, RecipeTypes.CRAFTING);
			registration.addRecipeCatalyst(PastelBlocks.PEDESTAL_MOONSTONE, RecipeTypes.CRAFTING);
		}
		registration.addRecipeCatalyst(PastelItems.CRAFTING_TABLET, RecipeTypes.CRAFTING);
		registration.addRecipeCatalyst(Blocks.ANVIL, ANVIL_CRUSHING);
		registration.addRecipeCatalyst(PastelBlocks.BEDROCK_ANVIL, ANVIL_CRUSHING);
		registration.addRecipeCatalyst(PastelBlocks.STRATINE_FLOATBLOCK, ANVIL_CRUSHING);
		registration.addRecipeCatalyst(PastelBlocks.PALTAERIA_FLOATBLOCK, ANVIL_CRUSHING);
		registration.addRecipeCatalyst(PastelBlocks.FUSION_SHRINE_BASALT, FUSION_SHRINE);
		registration.addRecipeCatalyst(PastelBlocks.FUSION_SHRINE_CALCITE, FUSION_SHRINE);
		registration.addRecipeCatalyst(PastelBlocks.ENCHANTER, ENCHANTER, ENCHANTMENT_UPGRADE);
		registration.addRecipeCatalyst(PastelBlocks.POTION_WORKSHOP, POTION_WORKSHOP_BREWING, POTION_WORKSHOP_CRAFTING, POTION_WORKSHOP_REACTING);
		registration.addRecipeCatalyst(PastelItems.HUMUS_BUCKET, HUMUS_CONVERTING);
		registration.addRecipeCatalyst(PastelItems.LIQUID_CRYSTAL_BUCKET, LIQUID_CRYSTAL_CONVERTING);
		registration.addRecipeCatalyst(PastelItems.MIDNIGHT_SOLUTION_BUCKET, MIDNIGHT_SOLUTION_CONVERTING);
		registration.addRecipeCatalyst(PastelItems.DRAGONROT_BUCKET, DRAGONROT_CONVERTING);
		registration.addRecipeCatalyst(PastelBlocks.SPIRIT_INSTILLER, SPIRIT_INSTILLER);
		registration.addRecipeCatalyst(PastelBlocks.COLOR_PICKER, INK_CONVERTING);
		registration.addRecipeCatalyst(PastelBlocks.CRYSTALLARIEUM, CRYSTALLARIEUM);
		registration.addRecipeCatalyst(PastelBlocks.CINDERHEARTH, CINDERHEARTH, RecipeTypes.BLASTING);
		registration.addRecipeCatalyst(PastelBlocks.TITRATION_BARREL, TITRATION_BARREL);
		registration.addRecipeCatalyst(PastelItems.DOOMBLOOM_SEED, PRIMORDIAL_FIRE_BURNING);
		registration.addRecipeCatalyst(PastelItems.PRIMORDIAL_LIGHTER, PRIMORDIAL_FIRE_BURNING);
		registration.addRecipeCatalyst(PastelBlocks.INCANDESCENT_AMALGAM, PRIMORDIAL_FIRE_BURNING);
		registration.addRecipeCatalyst(PastelItems.PIPE_BOMB, PRIMORDIAL_FIRE_BURNING);

		registration.addRecipeCatalyst(PastelItems.NATURES_STAFF, NATURES_STAFF);
		registration.addRecipeCatalyst(PastelBlocks.BLAZE_IDOL, HEATING);
		registration.addRecipeCatalyst(PastelBlocks.POLAR_BEAR_IDOL, FREEZING);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGuiContainerHandler(PedestalScreen.class, new PedestalRecipeClickAreaHandler());
		registration.addGuiContainerHandler(CraftingTabletScreen.class, new CraftingTabletRecipeClickAreaHandler());
		registration.addRecipeClickArea(PotionWorkshopScreen.class, 28, 41, 12, 42, POTION_WORKSHOP_BREWING, POTION_WORKSHOP_CRAFTING, POTION_WORKSHOP_REACTING);
		registration.addRecipeClickArea(CinderhearthScreen.class, 35, 31, 22, 16, CINDERHEARTH, RecipeTypes.BLASTING);

		registration.addGuiContainerHandler(QuickNavigationGridScreen.class, new OverlayHidingExtraAreaHandler<>());

		registration.addGhostIngredientHandler(BlackHoleChestScreen.class, new ShadowSlotGhostIngredientHandler<>());
		registration.addGhostIngredientHandler(FilteringScreen.class, new ShadowSlotGhostIngredientHandler<>());
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		PastelJEI.jeiRuntime = jeiRuntime;
	}
}
