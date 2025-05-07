package cads.roboticArm.real;

import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;
import org.cads.vs.roboticArm.hal.real.CaDSRoboticArmReal;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;



public class TestCaDSRoboticArmRealWithControls {

    private static ICaDSRoboticArm real;
    private static final int STEP_SIZE = 1;

    private static int leftRight = 50;
    private static int upDown = 50;
    private static int backForth = 50;
    private static int openClose = 50;

    private static void updatePositions() {
        real.setLeftRightPercentageTo(leftRight);
        real.setUpDownPercentageTo(upDown);
        real.setBackForthPercentageTo(backForth);
        real.setOpenClosePercentageTo(openClose);
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(100, value));
    }

    public static void main(String[] args) {
        try {
            real = new CaDSRoboticArmReal("172.16.1.63", 50055);

            boolean setupOk = real.init();
            real.waitUntilInitIsFinished();

            if (!setupOk) {
                System.out.println("Setup failed");
                return;
            }

            Terminal terminal = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            System.out.println("Setup OK. Control the robotic arm using keys:");
            System.out.println("a/s -> Left/Right");
            System.out.println("d/f -> Up/Down");
            System.out.println("j/k -> Back/Forth");
            System.out.println("l/ö -> Open/Close");
            System.out.println("Press 'q' to quit");

            updatePositions();

            while (true) {
            	int key = terminal.reader().read();

                switch (key) {
                    case 'a':
                        leftRight = clamp(leftRight + STEP_SIZE);
                        break;
                    case 's':
                        leftRight = clamp(leftRight - STEP_SIZE);
                        break;
                    case 'd':
                        upDown = clamp(upDown + STEP_SIZE);
                        break;
                    case 'f':
                        upDown = clamp(upDown - STEP_SIZE);
                        break;
                    case 'j':
                        backForth = clamp(backForth + STEP_SIZE);
                        break;
                    case 'k':
                        backForth = clamp(backForth - STEP_SIZE);
                        break;
                    case 'l':
                        openClose = clamp(openClose + STEP_SIZE);
                        break;
                    case 'ö':
                    case 'o':
                        openClose = clamp(openClose - STEP_SIZE);
                        break;
                    case 'q':
                        System.out.println("Exiting.");
                        return;
                    default:
                        break;
                }
                
                updatePositions();

                System.out.printf("L/R: %d, U/D: %d, B/F: %d, O/C: %d\n", leftRight, upDown, backForth, openClose);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
