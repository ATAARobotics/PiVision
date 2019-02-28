package ca.fourthreethreefour;

import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.first.cameraserver.CameraServer;


public class VisionAlignment {

    //Declare vision Variables
    private static final int IMG_WIDTH = 320;
    private static final int IMG_HEIGHT = 240;
    private static final int FOV = 60;
    private static final double FOCAL_LENGTH = IMG_WIDTH/(2*Math.tan(Math.toRadians(FOV/2)));
    
    private List<Rect> rectList = new LinkedList<Rect>();
    private final Object imgLock = new Object();
    private Rect[] visionTarget = new Rect[2];

    private EasyTables easyTables;


        
    CvSink cvSink = CameraServer.getInstance().getVideo();
        

    public VisionAlignment(EasyTables easyTables){
        this.easyTables = easyTables;
    }

    //Declare Variables for process function
    Rect placeHolder = new Rect(0,0,1,1);
    int largestIndex = 0;
    int secondIndex = 0;

    public Rect[] process(MyPipeline pipeline){

        visionTarget[0] = placeHolder;
        visionTarget[1] = placeHolder;

        largestIndex = 0;
        secondIndex = 0;

        if(!pipeline.filterContoursOutput().isEmpty()){

            for(int i = 0; i < pipeline.filterContoursOutput().size(); i++){
                Rect rx = Imgproc.boundingRect(pipeline.filterContoursOutput().get(i));

                //Prints Location of Rectangle
                System.out.println("Object " + i + ": " + rx.toString());

                //Adds rectangle to List to store size and position values
                rectList.add(rx);
            }

            //Sets up Indices For rectangle index holding

            //Calculate largest rectangles if needed
            if(rectList.size() == 1){
                visionTarget[0] = rectList.get(0);
                visionTarget[1] = visionTarget[0];
                easyTables.setNoTargetError(true);

            } else if(rectList.size() == 2){
                visionTarget[0] = rectList.get(0);
                visionTarget[1] = rectList.get(1);
                easyTables.setNoTargetError(false);

            } else if(rectList.size() > 2){
                easyTables.setNoTargetError(false);
                //Detect and set two largest rectangles to variable
                for (int i = 1; i < rectList.size();i++){
                    //If the current rectangle is larger than our largest
                    if(rectList.get(largestIndex).area()<rectList.get(i).area()){
                        visionTarget[1] = visionTarget[0];
                        secondIndex = largestIndex;
                        visionTarget[0] = rectList.get(i);
                        largestIndex = i;
                    }
                    //If the current rectangle is larger than the second largest
                    else if(rectList.get(secondIndex).area()<rectList.get(i).area()){
                        visionTarget[1] = rectList.get(i);
                        secondIndex = i;
                    }
                }

            }


            rectList.clear();
            
        } else {
            System.out.println("No Contours Detected");
            easyTables.setNoTargetError(true);
        }

        synchronized(imgLock){
        }
        
        return(visionTarget);
    }

    //Determine motor movements from location of vision targets
    public double alignValues(Rect[] visionTargets){
        double angleToTarget = 0;
        double centerX;
        double centerX2;
        double finalCenterX;
        if(visionTargets.length == 2){
            centerX = visionTarget[0].x + (visionTarget[0].width / 2); 
            centerX2 = visionTarget[1].x + (visionTarget[1].width / 2); 
            finalCenterX = (centerX + centerX2) / 2;
            
            //TODO add proper turning calcs 
            //Calculates angle to the target
            angleToTarget = Math.toDegrees(Math.atan((finalCenterX - 159.5) / FOCAL_LENGTH));
            System.out.print(angleToTarget);
            
            
        }
        
        return(angleToTarget);
    }
}