package ca.fourthreethreefour;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.cscore.VideoSource;


public class EasyTables {

    private NetworkTableInstance inst;

    EasyTables(NetworkTableInstance inst){
        this.inst = inst;
        NetworkTable table = inst.getTable("datatable");
        VISION_DRIVE_VALUE = table.getEntry("VISION_DRIVE_VALUE");
        VISION_ERROR_NOTARGET = table.getEntry("VISION_ERROR_NOTARGET");
    }

    private ShuffleboardTab dynamicSettingsTab = Shuffleboard.getTab("Dynamic Settings");
  
    private NetworkTableEntry VISION_ACTIVE_ENTRY_SHUFFLE = dynamicSettingsTab.addPersistent("Vision Active", false).getEntry();
    private NetworkTableEntry VISION_DRIVE_VALUE;
    private NetworkTableEntry VISION_ERROR_NOTARGET;

    public void updateDirection(Double drive){
        VISION_DRIVE_VALUE.setDouble(drive);
 
    }


    public boolean isVisionActive(){
        return(VISION_ACTIVE_ENTRY_SHUFFLE.getBoolean(false));
    }

    public void setNoTargetError(Boolean errorTrue){
        VISION_ERROR_NOTARGET.setBoolean(errorTrue);
    }
    
}