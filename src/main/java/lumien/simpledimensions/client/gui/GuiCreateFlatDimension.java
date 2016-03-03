package lumien.simpledimensions.client.gui;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateFlatWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCreateFlatDimension extends GuiScreen
{
    private final GuiCreateDimension createDimensionGui;
    private FlatGeneratorInfo theFlatGeneratorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
    /** The title given to the flat world currently in creation */
    private String flatWorldTitle;
    private String field_146394_i;
    private String field_146391_r;
    private GuiCreateFlatDimension.Details createFlatWorldListSlotGui;
    private GuiButton field_146389_t;
    private GuiButton field_146388_u;
    private GuiButton field_146386_v;
    private static final String __OBFID = "CL_00000687";

    public GuiCreateFlatDimension(GuiCreateDimension p_i1029_1_, String p_i1029_2_)
    {
        this.createDimensionGui = p_i1029_1_;
        this.func_146383_a(p_i1029_2_);
    }

    public String func_146384_e()
    {
        return this.theFlatGeneratorInfo.toString();
    }

    public void func_146383_a(String p_146383_1_)
    {
        this.theFlatGeneratorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(p_146383_1_);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
	public void initGui()
    {
        this.buttonList.clear();
        this.flatWorldTitle = I18n.format("createWorld.customize.flat.title", new Object[0]);
        this.field_146394_i = I18n.format("createWorld.customize.flat.tile", new Object[0]);
        this.field_146391_r = I18n.format("createWorld.customize.flat.height", new Object[0]);
        this.createFlatWorldListSlotGui = new GuiCreateFlatDimension.Details();
        this.buttonList.add(this.field_146389_t = new GuiButton(2, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("createWorld.customize.flat.addLayer", new Object[0]) + " (NYI)"));
        this.buttonList.add(this.field_146388_u = new GuiButton(3, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("createWorld.customize.flat.editLayer", new Object[0]) + " (NYI)"));
        this.buttonList.add(this.field_146386_v = new GuiButton(4, this.width / 2 - 155, this.height - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(5, this.width / 2 + 5, this.height - 52, 150, 20, I18n.format("createWorld.customize.presets", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.field_146389_t.visible = this.field_146388_u.visible = false;
        this.theFlatGeneratorInfo.func_82645_d();
        this.func_146375_g();
    }

    /**
     * Handles mouse input.
     */
    @Override
	public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.createFlatWorldListSlotGui.handleMouseInput();
    }

    @Override
	protected void actionPerformed(GuiButton button) throws IOException
    {
        int i = this.theFlatGeneratorInfo.getFlatLayers().size() - this.createFlatWorldListSlotGui.field_148228_k - 1;

        if (button.id == 1)
        {
            this.mc.displayGuiScreen(this.createDimensionGui);
        }
        else if (button.id == 0)
        {
            this.createDimensionGui.chunkProviderSettingsJson = this.func_146384_e();
            this.mc.displayGuiScreen(this.createDimensionGui);
        }
        else if (button.id == 5)
        {
            this.mc.displayGuiScreen(new GuiFlatDimensionPresets(this));
        }
        else if (button.id == 4 && this.func_146382_i())
        {
            this.theFlatGeneratorInfo.getFlatLayers().remove(i);
            this.createFlatWorldListSlotGui.field_148228_k = Math.min(this.createFlatWorldListSlotGui.field_148228_k, this.theFlatGeneratorInfo.getFlatLayers().size() - 1);
        }

        this.theFlatGeneratorInfo.func_82645_d();
        this.func_146375_g();
    }

    public void func_146375_g()
    {
        boolean flag = this.func_146382_i();
        this.field_146386_v.enabled = flag;
        this.field_146388_u.enabled = flag;
        this.field_146388_u.enabled = false;
        this.field_146389_t.enabled = false;
    }

    private boolean func_146382_i()
    {
        return this.createFlatWorldListSlotGui.field_148228_k > -1 && this.createFlatWorldListSlotGui.field_148228_k < this.theFlatGeneratorInfo.getFlatLayers().size();
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.createFlatWorldListSlotGui.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.flatWorldTitle, this.width / 2, 8, 16777215);
        int k = this.width / 2 - 92 - 16;
        this.drawString(this.fontRendererObj, this.field_146394_i, k, 32, 16777215);
        this.drawString(this.fontRendererObj, this.field_146391_r, k + 2 + 213 - this.fontRendererObj.getStringWidth(this.field_146391_r), 32, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @SideOnly(Side.CLIENT)
    class Details extends GuiSlot
    {
        public int field_148228_k = -1;
        private static final String __OBFID = "CL_00000688";

        public Details()
        {
            super(GuiCreateFlatDimension.this.mc, GuiCreateFlatDimension.this.width, GuiCreateFlatDimension.this.height, 43, GuiCreateFlatDimension.this.height - 60, 24);
        }

        private void func_148225_a(int p_148225_1_, int p_148225_2_, ItemStack p_148225_3_)
        {
            this.func_148226_e(p_148225_1_ + 1, p_148225_2_ + 1);
            GlStateManager.enableRescaleNormal();

            if (p_148225_3_ != null && p_148225_3_.getItem() != null)
            {
                RenderHelper.enableGUIStandardItemLighting();
                GuiCreateFlatDimension.this.itemRender.renderItemIntoGUI(p_148225_3_, p_148225_1_ + 2, p_148225_2_ + 2);
                RenderHelper.disableStandardItemLighting();
            }

            GlStateManager.disableRescaleNormal();
        }

        private void func_148226_e(int p_148226_1_, int p_148226_2_)
        {
            this.func_148224_c(p_148226_1_, p_148226_2_, 0, 0);
        }

        private void func_148224_c(int p_148224_1_, int p_148224_2_, int p_148224_3_, int p_148224_4_)
        {
        	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(Gui.statIcons);
            float f = 0.0078125F;
            float f1 = 0.0078125F;
            int i = 18;
            int j = 18;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos((double)(p_148224_1_ + 0), (double)(p_148224_2_ + 18), (double)GuiCreateFlatDimension.this.zLevel).tex((double)((float)(p_148224_3_ + 0) * 0.0078125F), (double)((float)(p_148224_4_ + 18) * 0.0078125F)).endVertex();
            worldrenderer.pos((double)(p_148224_1_ + 18), (double)(p_148224_2_ + 18), (double)GuiCreateFlatDimension.this.zLevel).tex((double)((float)(p_148224_3_ + 18) * 0.0078125F), (double)((float)(p_148224_4_ + 18) * 0.0078125F)).endVertex();
            worldrenderer.pos((double)(p_148224_1_ + 18), (double)(p_148224_2_ + 0), (double)GuiCreateFlatDimension.this.zLevel).tex((double)((float)(p_148224_3_ + 18) * 0.0078125F), (double)((float)(p_148224_4_ + 0) * 0.0078125F)).endVertex();
            worldrenderer.pos((double)(p_148224_1_ + 0), (double)(p_148224_2_ + 0), (double)GuiCreateFlatDimension.this.zLevel).tex((double)((float)(p_148224_3_ + 0) * 0.0078125F), (double)((float)(p_148224_4_ + 0) * 0.0078125F)).endVertex();
            tessellator.draw();
        }

        @Override
		protected int getSize()
        {
            return GuiCreateFlatDimension.this.theFlatGeneratorInfo.getFlatLayers().size();
        }

        /**
         * The element in the slot that was clicked, boolean for whether it was double clicked or not
         */
        @Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
        {
            this.field_148228_k = slotIndex;
            GuiCreateFlatDimension.this.func_146375_g();
        }

        /**
         * Returns true if the element passed in is currently selected
         */
        @Override
		protected boolean isSelected(int slotIndex)
        {
            return slotIndex == this.field_148228_k;
        }

        @Override
		protected void drawBackground() {}

        @Override
		protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_)
        {
            FlatLayerInfo flatlayerinfo = (FlatLayerInfo)GuiCreateFlatDimension.this.theFlatGeneratorInfo.getFlatLayers().get(GuiCreateFlatDimension.this.theFlatGeneratorInfo.getFlatLayers().size() - entryID - 1);
            IBlockState iblockstate = flatlayerinfo.getLayerMaterial();
            Block block = iblockstate.getBlock();
            Item item = Item.getItemFromBlock(block);
            ItemStack itemstack = block != Blocks.air && item != null ? new ItemStack(item, 1, block.getMetaFromState(iblockstate)) : null;
            String s = itemstack == null ? "Air" : item.getItemStackDisplayName(itemstack);

            if (item == null)
            {
                if (block != Blocks.water && block != Blocks.flowing_water)
                {
                    if (block == Blocks.lava || block == Blocks.flowing_lava)
                    {
                        item = Items.lava_bucket;
                    }
                }
                else
                {
                    item = Items.water_bucket;
                }

                if (item != null)
                {
                    itemstack = new ItemStack(item, 1, block.getMetaFromState(iblockstate));
                    s = block.getLocalizedName();
                }
            }

            this.func_148225_a(p_180791_2_, p_180791_3_, itemstack);
            GuiCreateFlatDimension.this.fontRendererObj.drawString(s, p_180791_2_ + 18 + 5, p_180791_3_ + 3, 16777215);
            String s1;

            if (entryID == 0)
            {
                s1 = I18n.format("createWorld.customize.flat.layer.top", new Object[] {Integer.valueOf(flatlayerinfo.getLayerCount())});
            }
            else if (entryID == GuiCreateFlatDimension.this.theFlatGeneratorInfo.getFlatLayers().size() - 1)
            {
                s1 = I18n.format("createWorld.customize.flat.layer.bottom", new Object[] {Integer.valueOf(flatlayerinfo.getLayerCount())});
            }
            else
            {
                s1 = I18n.format("createWorld.customize.flat.layer", new Object[] {Integer.valueOf(flatlayerinfo.getLayerCount())});
            }

            GuiCreateFlatDimension.this.fontRendererObj.drawString(s1, p_180791_2_ + 2 + 213 - GuiCreateFlatDimension.this.fontRendererObj.getStringWidth(s1), p_180791_3_ + 3, 16777215);
        }

        @Override
		protected int getScrollBarX()
        {
            return this.width - 70;
        }
    }
}