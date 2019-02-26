package ca.fourthreethreefour;

import org.opencv.core.RotatedRect;

public class VisionAlignment {
    
    //Declare vision Variables
    private static final int IMG_WIDTH = 320;
    //private static final int IMG_HEIGHT = 240;
    private static final int FOV = 60;
    private static final double FOCAL_LENGTH = IMG_WIDTH/(2*Math.tan(FOV/2));
    
    public VisionAlignment(){

    }
    
    //Declare Variables for process function
    RotatedRect placeHolder = new RotatedRect();
    
    public RotatedRect[] process(MyPipeline pipeline){
        RotatedRect[] visionTarget = new RotatedRect[2];
        
        if(pipeline.filterContoursOutput().size() < 2){
            visionTarget = new RotatedRect[] {new RotatedRect(), new RotatedRect()};
        } 
        else {
            //TODO test detection algorithm
        }
        
        return(visionTarget);
    }
    
    //Determine motor movements from location of vision targets
    public double alignValues(RotatedRect[] target){
        double turn = 0;
        double angleToTarget;
        double finalCenterX;
        if(target.length > 1){
            finalCenterX = (target[0].center.x + target[1].center.x) / 2;
            
            //TODO add proper turning calcs 
            //Calculates angle to the target
            angleToTarget = Math.atan((finalCenterX - 159.5) / FOCAL_LENGTH);
            System.out.print(angleToTarget);
            
            return(turn);
        }else{
            System.out.print("Unexpected target size, size is <2");
            return(turn);
        }
    }
}