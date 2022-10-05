package com.omicron.coconut_mod;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.logging.LogUtils;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.lwjgl.system.CallbackI;
import org.slf4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("coconut_mod")
public class CoconutMod
{
    private static BlockBehaviour.Properties behaviour = BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().isViewBlocking((a, b, c) -> {
        return false;
    });
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String MODID = "coconut_mod";

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

    public static RegistryObject<CoconutItem> COCONUT = ITEMS.register("coconut", () -> new CoconutItem(new Item.Properties().stacksTo(10)));

    public static RegistryObject<EntityType<Coconut>> COCONUT_ENTITY = ENTITIES.register("coconut", () -> EntityType.Builder.<Coconut>of(Coconut::new, MobCategory.MISC)
        .build("coconut"));

    public static RegistryObject<Block> PALM_TREE1 = fromBlock(BLOCKS.register("palm_tree1", () -> new Block(behaviour)));

    public static RegistryObject<Block> PALM_TREE2 = fromBlock(BLOCKS.register("palm_tree2", () -> new Block(behaviour)));

    public static RegistryObject<Block> PALM_TREE3 = fromBlock(BLOCKS.register("palm_tree3", () -> new Block(behaviour)));

    public static RegistryObject<Block> PALM_TREE4 = fromBlock(BLOCKS.register("palm_tree4", () -> new Block(behaviour)));

    public static RegistryObject<Block> PALM_TREE5 = fromBlock(BLOCKS.register("palm_tree5", () -> new Block(behaviour)));

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab("coconut_mod") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(COCONUT.get());
        }
    };

    public static <B extends Block> RegistryObject<B> fromBlock(RegistryObject<B> block) {
        ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(ITEM_GROUP)));
        return block;
    }

    public CoconutMod()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CoconutConfig.spec, "coconut_mod_config.toml");
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        ENTITIES.register(bus);
        // Register ourselves for server and other game events we are interested in
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(CoconutMod::registerCommands);
   
    }

    public static void registerCommands(RegisterCommandsEvent event)
    {
        event.getDispatcher().register(
                Commands.literal("setmaxlife").requires((player) -> {
                    return player.hasPermission(2);
                }).then(Commands.argument("player", EntityArgument.player())
                .then(Commands.argument("maxlife", IntegerArgumentType.integer(0)).executes(
                        (context -> {
                            if(ServerLifecycleHooks.getCurrentServer() instanceof DedicatedServer server)
                            {
                                System.out.println("attribute " + EntityArgument.getPlayer(context, "player").getName().getString() + " minecraft:generic.max_health base set " + IntegerArgumentType.getInteger(context, "maxlife"));
                                server.runCommand("attribute " + EntityArgument.getPlayer(context, "player").getName().getString() + " minecraft:generic.max_health base set " + IntegerArgumentType.getInteger(context, "maxlife"));
                                EntityArgument.getPlayer(context, "player").setHealth(IntegerArgumentType.getInteger(context, "maxlife"));
                            }
                            return 1;
                        })
                )))
        );
    }


}
