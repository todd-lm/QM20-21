/*
About this File:
This is the main driver control code. Mostly uses functions in the simpleBotConstants file.
 */
package org.firstinspires.ftc.teamcode.simpleBotCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import static org.firstinspires.ftc.teamcode.simpleBotCode.Constants.DRIVE_STICK_THRESHOLD;


@TeleOp(name = "!QM TeleOP", group = "Sensor")
public class simpleBotTeleOp extends LinearOpMode {

    private final HardwareSimpleBot rb = new HardwareSimpleBot();
    // Declare OpMode members.
    private final ElapsedTime runtime = new ElapsedTime();

    //Setup variables:
    private boolean flywheelOn = false;
    private final boolean shooterOut = false;

//    RevBlinkinLedDriver blinkinLedDriver;
//    RevBlinkinLedDriver.BlinkinPattern pattern;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initializing");
        telemetry.update();
        rb.init(hardwareMap, this);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
//        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            drive();
            shooter();
            flywheel();


            //  Show the elapsed game time and wheel power.
//            telemetry.addData("Status", "Run Time: " + runtime.toString());
//            telemetry.update();

            /* CONTROLS:
             * Driver: (Start + A)
             * Left Stick - Movement
             * Right Stick - Rotation
             *
             * Gunner: (Start + B)
             *
             * */
        }
    }


    private void drive() {
        //Init variables
        double leadleftPower;
        double leadrightPower;
        double rearRightPower;
        double rearLeftPower;

        double leftY = gamepad1.left_stick_y;
        double leftX = gamepad1.left_stick_x;
        double rightX = gamepad1.right_stick_x;

//        pattern = RevBlinkinLedDriver.BlinkinPattern.ORANGE;
//        blinkinLedDriver.setPattern(pattern);

        if (rightX < -DRIVE_STICK_THRESHOLD || rightX > DRIVE_STICK_THRESHOLD || leftY < -DRIVE_STICK_THRESHOLD || leftY > DRIVE_STICK_THRESHOLD || leftX < -DRIVE_STICK_THRESHOLD || leftX > DRIVE_STICK_THRESHOLD) {
            //Get stick values and apply modifiers:
            double drive = -gamepad1.left_stick_y * 1.10;
            double turn = gamepad1.right_stick_x * 1.25;
            double strafe = gamepad1.left_stick_x;

            //Calculate each individual motor speed using the stick values:
            //range.clip calculates a value between min and max, change those values to reduce overall speed
            leadleftPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
            leadrightPower = Range.clip(drive - turn - strafe, -1.0, 1.0);
            rearLeftPower = Range.clip(drive + turn - strafe, -1.0, 1.0);
            rearRightPower = Range.clip(drive - turn + strafe, -1.0, 1.0);

            rb.drive(-leadrightPower, -leadleftPower, -rearRightPower, -rearLeftPower); //Uses each of the motor values calculated above

            telemetry.addData("Front-right motor", "%5.2f", leadrightPower);
            telemetry.addData("Back-right motor", "%5.2f", rearRightPower);
            telemetry.addData("Front-left motor", "%5.2f", leadleftPower);
            telemetry.addData("Back-left motor", "%5.2f", rearLeftPower);
            telemetry.update();
        } else {
            rb.driveStop(); //Stop robot if no stick value (delete this if u want to drift lol)
        }

    }


    //TODO: Setup teleop for 2 driver control w/ gamepad2
    private void shooter() throws InterruptedException {
        if (gamepad1.right_bumper && flywheelOn) { //TODO: Figure out why trigger gamepad1.right_trigger > .5 isnt working
            telemetry.addData(">", "Shooter Out!");
            telemetry.update();

            rb.moveShooter(true); //Shoot
            Thread.sleep(250); //Wait a tiny bit before going back
            rb.moveShooter(false);
            Thread.sleep(1500); //Wait for flywheel to get back to 100 percent speed
        } else if (gamepad1.right_bumper && flywheelOn == false) {
            telemetry.addData("WARNING:", "flywheel is not running");
            telemetry.update();

        }

    }

    private void flywheel() throws InterruptedException {
        boolean leftb = gamepad1.left_bumper;

        if (leftb && flywheelOn == false) {
            telemetry.addData(">", "Flywheel should be running");
            telemetry.update();
            rb.runFlywheel(true);
            flywheelOn = true;
            Thread.sleep(500);
        } else if (leftb && flywheelOn) {
            telemetry.addData(">", "Flywheel should NOT be running");
            telemetry.update();
            rb.runFlywheel(false);
            flywheelOn = false;
            Thread.sleep(500);
        }


    }


    //Old Functions: Keeping for reference

//    private void intake(){
//        boolean rightb = gamepad2.right_bumper;
//        boolean leftb = gamepad2.left_bumper;
//
//        if (rightb && gamepad2.right_stick_button) {
//            rb.intakeIn(-1);
//        } else if (leftb) {
//            rb.intakeIn();
//        }
//        else if (rightb) {
//            rb.intakeOut();
//        }
//        else{
//            rb.intakeStop();
//        }
//
//    }
//
//    private void capstone(){
//        boolean y = gamepad2.y;
//        boolean x = gamepad2.x;
//
//        if(x){
//            rb.capservo.setPosition(simpleBotConstants.CAP_DOWN);
//        }else if (y){
//            rb.capservo.setPosition(simpleBotConstants.CAP_UP);
//
//        }
//
//    }
//
//    private void platform(){
//        boolean dpadUp = gamepad2.dpad_up;
//        boolean dpadDown = gamepad2.dpad_down;
////        if(rightTrigger>.15){
////            rb.setPlatformUp(false);
////        }else if(leftTrigger>.15){
////            rb.setPlatformUp(true);
////        }
//
//        if(dpadUp){
//            rb.setPlatformUp(true);
//        }else if(dpadDown){
//            rb.setPlatformUp(false);
//        }
//    }
//
//    private void tapeMeasure(){
////        double leftx = gamepad2.left_stick_y;
////
////        if(leftx > .1){
////            rb.tapeIn();
////        } else if(leftx < -.1){
////            rb.tapeOut();
////        }else{
////            rb.tapeStop();
////        }
//    }
//
//    private void blockGrabber(){
//
//        double rightTrigger = gamepad2.right_trigger;
//
//        if (gamepad2.a){
//            rb.blockDown();
//        } else {
//            rb.blockUp();
//        }
//     }

}