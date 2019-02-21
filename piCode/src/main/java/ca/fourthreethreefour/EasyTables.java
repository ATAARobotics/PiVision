package ca.fourthreethreefour;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;


public class EasyTables {

    EasyTables(){

    }

    //NetworkTableEntry DRIVE_ENTRY;

    ShuffleboardTab dynamicSettingsTab = Shuffleboard.getTab("Dynamic Settings");
    NetworkTableEntry DRIVE_ENTRY_SHUFFLE = dynamicSettingsTab.addPersistent("Drive Value", 0).getEntry();

    public void updateDirection(NetworkTableInstance ntinst, Double drive){
        //NetworkTable table = ntinst.getTable("datatable");
        DRIVE_ENTRY_SHUFFLE.setDouble(drive);
    }
    
}