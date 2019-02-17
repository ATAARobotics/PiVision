package ca.fourthreethreefour;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PIDSetup extends PIDSubsystem {
    

        public PIDSetup(){
            super("PIDSetup", 0.0, 0.0, 0.0);
            setAbsoluteTolerance(0.00);
            getPIDController().setContinuous(false);
        }
    protected double returnPIDInput() {
        return 0;
    }

    protected void usePIDOutput(double arg0) {

    }

    protected void initDefaultCommand() {
    }

}