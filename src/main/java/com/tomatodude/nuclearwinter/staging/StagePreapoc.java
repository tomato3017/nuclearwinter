package com.tomatodude.nuclearwinter.staging;

import net.minecraft.world.World;

public class StagePreapoc extends StageBase{

    public StagePreapoc(int dimID, long worldTickStart) {
        super("Preapocalypse",dimID, worldTickStart);
        this.setMaxRadiation(512)
            .setChunkProcessorActive(true);
    }

    @Override
    public boolean canDoNextStage(World worldIn) {
//       if(!worldIn.isRemote && StageController.hasStageWorldSettings(worldIn)){
//           IStageWorldSettings stageSettings = StageController.getStageWorldSettings(worldIn);
//
//           if(stageSettings.isActive()){
//               return true;
//           }
//       }

       return false;
    }

}
