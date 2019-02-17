package ca.fourthreethreefour;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.PIDController;


public class VisionAlignment{

    //Declare vision Variables
    private static final int IMG_WIDTH = 320;
    private static final int IMG_HEIGHT = 240;
    private static final int FOV = 60;
    
    private List<Rect> rectList = new LinkedList<Rect>();
    private final Object imgLock = new Object();
    private Rect[] visionTarget = new Rect[2];

    CvSink cvSink = CameraServer.getInstance().getVideo();
        
        //TODO Remove once feedback is not required
    CvSource outputStream = CameraServer.getInstance().putVideo("Image Analysis", IMG_WIDTH, IMG_HEIGHT);

    public VisionAlignment(){
    }

    //TODO Test if necessary to add line to process code
    

    //Declare Variables for process function
    Rect placeHolder = new Rect(0,0,1,1);
    int largestIndex = 0;
    int secondIndex = 0;

    public Rect[] process(MyPipeline pipeline, Mat source){

        cvSink.grabFrame(source);

        visionTarget[0] = placeHolder;
        visionTarget[1] = placeHolder;

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

            } else if(rectList.size() == 2){
                visionTarget[0] = rectList.get(0);
                visionTarget[1] = rectList.get(1);

            } else if(rectList.size() > 2){
                //Detect and set two largest rectangles to variable
                for (int i = 1; i < rectList.size();i++){
                    //If the current rectangle is larger than our largest
                    if(rectList.get(largestIndex).area()<rectList.get(i).area()){
                        visionTarget[1] = visionTarget[0];
                        secondIndex = largestIndex;
                        visionTarget[0] = rectList.get(i);
                        largestIndex = i;
                    }
                    //If the current rectangle is larger thanm the second largest
                    else if(rectList.get(secondIndex).area()<rectList.get(i).area()){
                        visionTarget[1] = rectList.get(i);
                        secondIndex = i;
                    }
                }

            }


            rectList.clear();
            
        } else {
            System.out.println("No Contours Detected");
        }

        synchronized(imgLock){
        }
        
        return(visionTarget);
    }

    public void updateVideo(Rect[] visionTargets, Mat source){
        //Draws Rectangle
        Imgproc.rectangle(source, new Point(visionTarget[0].x, visionTarget[0].y), new Point(visionTarget[0].x + visionTarget[0].width, visionTarget[0].y + visionTarget[0].height), new Scalar(0,0,255), 2);
        Imgproc.rectangle(source, new Point(visionTarget[1].x, visionTarget[1].y), new Point(visionTarget[1].x + visionTarget[1].width, visionTarget[1].y + visionTarget[1].height), new Scalar(0,0,255), 2);
        
        //Send Frame
        outputStream.putFrame(source);

    }

    public double alignValues(Rect[] visionTargets){
        double centerX;
        double centerX2;
        double finalCenterX;
        double turn = 0;
        double focalLength;
        double angleToTarget;
        synchronized (imgLock) {
            //Calculates CenterX 
            centerX = visionTarget[0].x + (visionTarget[0].width / 2); 
            centerX2 = visionTarget[1].x + (visionTarget[1].width / 2); 
            finalCenterX = (centerX + centerX2) / 2;
            //Calculates focalLength of camera
            focalLength = IMG_WIDTH/(2*Math.tan(FOV/2));

        }
      
        if(visionTarget[0].x != visionTarget[1].x){
            //TODO add proper turning calcs 

            //Calculates angle to the target
            angleToTarget = Math.atan((finalCenterX - 159.5) / focalLength);
            System.out.print(angleToTarget);
            
            
            /*TODO: Use encoder values to turn with the angle
             We can use encoder.getSelectedSensorPosition? and divide by 3 000 000? to get cm
            Then we can tell the encoder to move x cm depending on the angle
            (This will require a lot of testing and trial/error)
            The ideal is making angleToTarget equal to 0 
            (Note: Above calculations are not tested and I may have misinterpreted how to do them)
            */
        }

        return(turn);
    }
}