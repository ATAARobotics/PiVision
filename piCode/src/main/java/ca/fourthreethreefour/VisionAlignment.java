package ca.fourthreethreefour;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSource;
import edu.wpi.first.cameraserver.CameraServer;


public class VisionAlignment{
    
    //Declare vision Variables
    private static final int IMG_WIDTH = 320;
    private static final int IMG_HEIGHT = 240;
    private static final int FOV = 60;
    
    private final Object imgLock = new Object();
    
    //TODO Remove once feedback is not required
    private static CvSource outputStream = CameraServer.getInstance().putVideo("Image Analysis", IMG_WIDTH, IMG_HEIGHT);
    
    public RotatedRect[] findTargets(MyPipeline pipeline, Mat source){
        pipeline.process(source);
        RotatedRect[] visionTarget = {new RotatedRect(), new RotatedRect()};
        
        if (pipeline.filterContoursOutput().size() < 2) {
            return visionTarget;
        }
        
        //TODO test best way of determining target validity
        // Determines the two largest rectangles puts them in visionTarget
        for (int i = 0; i < pipeline.filterContoursOutput().size(); i++) {
            
            MatOfPoint mop = pipeline.filterContoursOutput().get(i);
            MatOfPoint2f mop2f = new MatOfPoint2f();
            
            mop2f.fromArray(mop.toArray());
            // Creates temporary object
            RotatedRect currentRectangle = Imgproc.minAreaRect(mop2f);
            
            // If the current rectangle is larger than our largest
            if (currentRectangle.size.area() > visionTarget[0].size.area()) {
                
                // Changes largest target to second largest
                visionTarget[1] = visionTarget[0];
                
                // Changes largest target to current target
                visionTarget[0] = currentRectangle;
            }
            
            // If the current rectangle is larger thanm the second largest
            else if (currentRectangle.size.area() > visionTarget[1].size.area()) {
                
                // Changes second largest target to current rectangles
                visionTarget[1] = currentRectangle;
            }
        }
        
        // Synchronizes data to prevent acess to variables
        synchronized (imgLock) {
            
        }
        return visionTarget;
    }
    
    public void updateVideo(RotatedRect[] visionTargets, Mat source){
        //Draws Rectangle
        Point[] boxPoints = new Point[4];
        visionTargets[0].points(boxPoints);
        for (int j = 0; j < 4; j++) {
            Imgproc.line(source, boxPoints[j], boxPoints[(j + 1) % 4], new Scalar(0,0,255), 3);
        }
        visionTargets[1].points(boxPoints);
        for (int j = 0; j < 4; j++) {
            Imgproc.line(source, boxPoints[j], boxPoints[(j + 1) % 4], new Scalar(0,0,255), 3);
        }
        
        //Send Frame
        outputStream.putFrame(source);
        
    }
    
    public double alignValues(RotatedRect[] visionTargets){
        double centerX;
        double centerX2;
        double finalCenterX;
        double turn = 0;
        double focalLength;
        double angleToTarget;
        synchronized (imgLock) {
            //Calculates CenterX 
            centerX = visionTargets[0].center.x;
            centerX2 = visionTargets[1].center.x;
            finalCenterX = (centerX + centerX2) / 2;
            //Calculates focalLength of camera
            focalLength = IMG_WIDTH/(2*Math.tan(FOV/2));
            
        }
        
        if(visionTargets.length == 2){
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