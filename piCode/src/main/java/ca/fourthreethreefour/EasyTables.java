package ca.fourthreethreefour;


import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;




public class EasyTables {

    NetworkTableInstance inst;

    EasyTables(NetworkTableInstance inst){
        this.inst = inst;
    }


    ShuffleboardTab dynamicSettingsTab = Shuffleboard.getTab("Dynamic Settings");
    NetworkTableEntry VISION_ACTIVE_ENTRY_SHUFFLE = dynamicSettingsTab.addPersistent("Vision Active", false).getEntry();
    NetworkTableEntry VISION_DRIVE_VALUE;

    public void init(){
        NetworkTable table = inst.getTable("datatable");
        VISION_DRIVE_VALUE = table.getEntry("VISION_DRIVE_VALUE");
    }

    public void updateDirection(Double drive){
        VISION_DRIVE_VALUE.setDouble(drive);
 
    }

    public boolean isVisionActive(){
        return(VISION_ACTIVE_ENTRY_SHUFFLE.getBoolean(false));
    }
    
}