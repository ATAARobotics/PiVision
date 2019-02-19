package ca.fourthreethreefour;

import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;



public class EasyTables {

    EasyTables(){

    }

    private ShuffleboardTab dashboardTab = Shuffleboard.getTab("Dashboard");
    private ShuffleboardTab dynamicSettingsTab = Shuffleboard.getTab("Dynamic Settings");
    private NetworkTableEntry DRIVE_ENTRY_SHUFFLE = dynamicSettingsTab.addPersistent("Drive Value", 0).getEntry();
    private NetworkTableEntry SPEED_ENTRY_SHUFFLE = dynamicSettingsTab.addPersistent("Drive Speed", 0).getEntry();
    private NetworkTableEntry VISION_ACTIVE_ENTRY_SHUFFLE = dynamicSettingsTab.addPersistent("Vision Active", false).getEntry();

    public void updateDirection(Double drive){
        DRIVE_ENTRY_SHUFFLE.setDouble(drive);
 
    }

    public void updateSpeed(Double speed){
        SPEED_ENTRY_SHUFFLE.setDouble(speed);
    }

    public boolean isVisionActive(){
        return(VISION_ACTIVE_ENTRY_SHUFFLE.getBoolean(false));
    }

    public void startDriverCamera(VideoSource camera){
        dashboardTab.add(camera);
    }
    
}