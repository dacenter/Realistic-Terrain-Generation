package teamrtg.rtg.api.tools.deco;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.terraingen.TerrainGen;
import teamrtg.rtg.api.tools.feature.WorldGenGrass;
import teamrtg.rtg.api.world.RTGWorld;
import teamrtg.rtg.api.world.biome.deco.DecoBase;
import teamrtg.rtg.api.world.gen.RealisticBiomeGenerator;

/**
 * 
 * @author WhichOnesPink
 *
 */
public class DecoGrass extends DecoBase
{
    
	protected static final int MAX_LOOPS = 10;
	
	public float strengthFactor;
	public int minY;
	public int maxY;
	public int loops;
	public int chance;
	public int notEqualsZerochance;
	private IBlockState block;
	private int meta;
	public IBlockState[] randomGrassBlocks;
	public byte[] randomGrassMetas;
	protected boolean useRandomGrass;
    private WorldGenGrass grassGenerator;
	
	public DecoGrass()
	{
		super();
		
		/**
		 * Default values.
		 * These can be overridden when configuring the Deco object in the realistic biome.
		 */
		this.minY = 1; // No height limit by default.
		this.maxY = 255; // No height limit by default.
		this.strengthFactor = 0f; // Not sure why it was done like this, but... the higher the value, the more there will be.
		this.loops = 1;
		this.chance = 1;
		this.notEqualsZerochance = 1;
		this.block = Blocks.TALLGRASS.getDefaultState();
		this.meta = 1;
		this.randomGrassBlocks = new IBlockState[]{};
		this.randomGrassMetas = new byte[]{};
		this.useRandomGrass = (this.randomGrassBlocks.length > 0 && this.randomGrassBlocks.length == this.randomGrassMetas.length);
		
		this.addDecoTypes(DecoType.GRASS);
        grassGenerator = new WorldGenGrass(block, meta);
	}

    public DecoGrass(int meta) {
        this();
        this.meta = meta;
        grassGenerator = new WorldGenGrass.SingleType(block,meta);
    }

    public DecoGrass(IBlockState block) {
        this();
        this.block = block;
        grassGenerator = new WorldGenGrass.SingleType(block,meta);
    }

    public DecoGrass(IBlockState[] randomBlocks, byte[] randomMetas) {
        this();
        if (randomBlocks.length != randomMetas.length) {
            throw new RuntimeException("Mismatch in block and metadata arrays for DecoGrass");
        }
		this.randomGrassBlocks = randomBlocks;
		this.randomGrassMetas = randomMetas;
        grassGenerator = new WorldGenGrass.RandomType(randomGrassBlocks,randomGrassMetas);
    }
	@Override
	public void generate(RTGWorld rtgWorld, Random rand, int chunkX, int chunkY, float strength, float river, RealisticBiomeGenerator realisticBiomeGenerator, boolean hasPlacedVillageBlocks)
	{
		if (this.allowed) {
			
			if (TerrainGen.decorate(rtgWorld.world, rand, new BlockPos(chunkX, 0, chunkY), GRASS)) {
	            
				this.loops = (this.strengthFactor > 0f) ? (int)(this.strengthFactor * strength) : this.loops;
				this.loops = (this.loops > this.MAX_LOOPS) ? this.MAX_LOOPS : this.loops;
	            for (int i = 0; i < this.loops*64; i++)
	            {
	                int intX = chunkX + rand.nextInt(16) ;// + 8;
	                int intY = this.minY + (rand.nextInt(this.maxY - this.minY) + 1);
	                int intZ = chunkY + rand.nextInt(16) ;// + 8;

    				//Do we want to choose a random grass?
    				if (this.useRandomGrass) {
    					
    					//this.block = this.randomGrassBlocks[rand.nextInt(this.randomGrassBlocks.length)];
    					//this.meta = this.randomGrassMetas[rand.nextInt(this.randomGrassMetas.length)];
    				}
    				
	                if (this.notEqualsZerochance > 1) {
	                	
		                if (intY >= this.minY && intY <= this.maxY && rand.nextInt(this.notEqualsZerochance) != 0) {
		                	grassGenerator.generate(rtgWorld.world, rand, new BlockPos(intX, intY, intZ));
		                }
	                }
	                else {
	                	
		                if (intY >= this.minY && intY <= this.maxY && rand.nextInt(this.chance) == 0) {
		                	grassGenerator.generate(rtgWorld.world, rand, new BlockPos(intX, intY, intZ));
		                }
	                }
	            }
	        }
		}
	}
}
