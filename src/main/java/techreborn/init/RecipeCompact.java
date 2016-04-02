package techreborn.init;

import ic2.api.info.Info;
import ic2.api.item.IC2Items;
import ic2.core.ref.BlockName;
import ic2.core.ref.ItemName;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import techreborn.api.recipe.IRecipeCompact;
import techreborn.blocks.BlockMachineFrame;
import techreborn.compat.CompatManager;
import techreborn.items.ItemCells;
import techreborn.items.ItemIngots;
import techreborn.items.ItemParts;
import techreborn.items.ItemPlates;
import techreborn.parts.ItemStandaloneCables;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeCompact implements IRecipeCompact {

    HashMap<String, ItemStack> recipes = new HashMap<>();

    ArrayList<String> missingItems = new ArrayList<>();
    //OLD name, New Name
    HashMap<String, Ic2ItemValue> ic2RenameDictionary = new HashMap<>();

    boolean inited = false;

    public void init(){
        recipes.put("industrialDiamond", new ItemStack(Items.diamond));
        recipes.put("industrialTnt", new ItemStack(Blocks.tnt));
        recipes.put("copperIngot", ItemIngots.getIngotByName("copper"));
        recipes.put("tinIngot", ItemIngots.getIngotByName("tin"));
        recipes.put("bronzeIngot", ItemIngots.getIngotByName("bronze"));
        recipes.put("leadIngot", ItemIngots.getIngotByName("lead"));
        recipes.put("silverIngot", ItemIngots.getIngotByName("silver"));
        recipes.put("iridiumOre", ItemIngots.getIngotByName("Iridium"));
        recipes.put("plateiron", ItemPlates.getPlateByName("iron"));
        recipes.put("iridiumPlate", ItemPlates.getPlateByName("iridium"));
        recipes.put("cell", ItemCells.getCellByName("empty"));
        recipes.put("airCell", ItemCells.getCellByName("empty"));
        recipes.put("electronicCircuit", ItemParts.getPartByName("electronicCircuit"));
        recipes.put("advancedCircuit", ItemParts.getPartByName("advancedCircuit"));
        recipes.put("rubberWood", new ItemStack(ModBlocks.rubberLog));
        recipes.put("resin", ItemParts.getPartByName("rubberSap"));
        recipes.put("carbonPlate", ItemPlates.getPlateByName("carbon"));
        recipes.put("reBattery", new ItemStack(ModItems.reBattery));
        recipes.put("machine", BlockMachineFrame.getFrameByName("machine", 1));
        recipes.put("advancedMachine", BlockMachineFrame.getFrameByName("advancedMachine", 1));
        recipes.put("extractor", new ItemStack(ModBlocks.Extractor));
        recipes.put("generator", new ItemStack(ModBlocks.Generator));
        recipes.put("macerator", new ItemStack(ModBlocks.Grinder));
        recipes.put("diamondDrill", new ItemStack(ModItems.diamondDrill));
        recipes.put("miningDrill", new ItemStack(ModItems.ironDrill));
        recipes.put("solarPanel", new ItemStack(ModBlocks.solarPanel));
        recipes.put("waterCell", ItemCells.getCellByName("water"));
        recipes.put("lavaCell", ItemCells.getCellByName("lava"));
        recipes.put("pump", ItemParts.getPartByName("pump"));
        recipes.put("teleporter", ItemParts.getPartByName("teleporter"));
        recipes.put("advancedAlloy", ItemParts.getPartByName("advancedAlloy"));
        recipes.put("lvTransformer", new ItemStack(ModBlocks.lvt));
        recipes.put("mvTransformer", new ItemStack(ModBlocks.mvt));
        recipes.put("hvTransformer", new ItemStack(ModBlocks.hvt));
        recipes.put("windMill", new ItemStack(ModBlocks.windMill));
        recipes.put("energyCrystal", new ItemStack(ModItems.energyCrystal));
//        recipes.put("lapotronCrystal", new ItemStack(ModItems.lapotronCrystal));

        ic2RenameDictionary.put("plateiron", new Ic2ItemValue("plate", "iron"));
        inited = true;
    }

    public static ItemStack getCableByName(String name){
        return getCableByName(name, 1);
    }

    public static ItemStack getCableByName(String name, int count) {
        if(Info.isIc2Available()){
            return new ItemStack(ModItems.missingRecipe);
        } else {
            return ItemStandaloneCables.getCableByName(name, count);
        }
    }


    @Override
    public ItemStack getItem(String name) {
        if(!inited){
            init();
        }
        if(CompatManager.isIC2Loaded){
            Ic2ItemValue newVaule = null;
            if(ic2RenameDictionary.containsKey(name)){
                newVaule = ic2RenameDictionary.get(name);
            }
            if(newVaule == null){
                ItemStack stack = IC2Items.getItem(name);
                if(stack == null || stack.getItem() == null){
                    throw new InvalidParameterException("could not find " + name + " in the ic2 api");
                }
                return stack;
            } else {
                boolean requiresVariant = false;




                ItemStack stack = IC2Items.getItem(newVaule.item);
                if(!newVaule.variant.equals("null")){
                    stack = IC2Items.getItem(newVaule.item, newVaule.variant);
                }
                if(stack == null || stack.getItem() == null){
                    throw new InvalidParameterException("could not find " + name + " in the ic2 api");
                }
                return stack;
            }

        }
        if(!recipes.containsKey(name)){
            if(!missingItems.contains(name)){
                missingItems.add(name);
            }
            return new ItemStack(ModItems.missingRecipe);
        } else {
            return recipes.get(name);
        }
    }


    public void saveMissingItems(File mcDir) throws IOException {
        File missingItemsFile = new File(mcDir, "missingItems.txt");
        if(missingItemsFile.exists()){
            missingItemsFile.delete();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(missingItemsFile));
        for(String str : missingItems){
            writer.write(str);
            writer.newLine();
        }
        writer.close();
    }

    class Ic2ItemValue{
        String item;
        String variant = "null";

        public Ic2ItemValue(String item, String variant) {
            this.item = item;
            this.variant = variant;
        }

        public Ic2ItemValue(String item) {
            this.item = item;
        }
    }

}
