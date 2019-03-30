package edu.wpi.first.smartdashboard;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import edu.wpi.first.smartdashboard.gui.DashboardFrame;
import edu.wpi.first.smartdashboard.properties.IntegerProperty;
import edu.wpi.first.smartdashboard.robot.Robot;

/**
 * SmartDashboard logic
 *
 * @author Joe Grinstead
 * @author pmalmsten
 */
public class SmartDashboard {

  /**
   * Variable used in the {@link SmartDashboard#inCompetition() inCompetition()}
   * method
   */
  private static boolean inCompetition = false;

  /**
   * Returns whether or not this is in "competition" mode. Competition mode should
   * be used on the netbook provided for teams to use the dashboard. If the
   * SmartDashboard is in competition mode, then it automatically sizes itself to
   * be the standard dashboard size and to remove the frame around it. It can be
   * set to be in competition if "competition" is one of the words passed in
   * through the command line.
   *
   * @return whether or not this is in "competition" mode
   */
  public static boolean inCompetition() {
    return inCompetition;
  }

  public static DashboardFrame frame;

  static File lockFile;
  static FileChannel lockChannel;
  static FileLock singleInstanceLock;

  /**
   * Starts the program
   *
   * @param args the standard arguments. If "competition" is one of them, then the
   *             SmartDashboard will be in competition mode
   * @see SmartDashboard#inCompetition() inCompetition()
   */
  public static void main(final String[] args) {
    // Parse all arguments to see if multiple instances are allowed
    boolean allowMultiple = false;
    for(String s : args) {
      if(s.equals("-m") || s.equals("--allow-multiple")) {
        allowMultiple = true;
      }
    }
    // If multiple instances are not allowed, do a single instance check using sockets
    if(!allowMultiple) {
      try {
        // Try creating the lock file and aquire a channel
        lockFile = new File(System.getProperty("user.home") + File.separator + ".arctosdashboard.lock");
        lockChannel = new RandomAccessFile(lockFile, "rw").getChannel();
        try {
          // Attempt to lock it
          singleInstanceLock = lockChannel.tryLock();
        }
        catch(OverlappingFileLockException e) {
          // If it failed there's another instance running already
          singleInstanceLock = null;
        }
        
        if(singleInstanceLock == null) {
          // Lock failed - another instance is running
          // Show error dialog and exit
          SwingUtilities.invokeAndWait(() -> {
            try {
              UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e1) {
              e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Another instance of ArctosDashboard is already running!", "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(0);
          });
        }
        
        // Add a shutdown hook so the socket can be cleaned up when the program exits
        Runtime.getRuntime().addShutdownHook(new Thread() {
          @Override
          public void run() {
            try {
              // Release lock
              if(singleInstanceLock != null) {
               singleInstanceLock.release();
              }
              // Close channel
              if(lockChannel != null) {
                lockChannel.close();
              }
              // Delete file
              if(lockFile != null) {
                lockFile.delete();
              }
            }
            catch(IOException e) {
              e.printStackTrace();
              System.exit(1);
            }
          }
        });
      }
      catch(IOException | InvocationTargetException | InterruptedException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(2);
    }

    // Present a loading bar (it will only show up if this is going slowly)
    final ProgressMonitor monitor = new ProgressMonitor(null, "Loading SmartDashboard", "Initializing internal code...",
        0, 1000);

    // Search the filesystem for extensions (49%)
    // FileSniffer.findExtensions(monitor, 0, 490);
    // Extensions disabled until classloading can be fixed, see
    // https://github.com/wpilibsuite/SmartDashboard/issues/107

    // Parse arguments
    ArgParser argParser = new ArgParser(args, true, true, new String[] { "ip" });
    inCompetition = argParser.hasFlag("competition");

    // Initialize GUI
    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          frame = new DashboardFrame(inCompetition);
        }
      });
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(2);
    }

    if (argParser.hasValue("ip")) {
      monitor.setProgress(650);
      monitor.setNote("Connecting to robot at: " + argParser.getValue("ip"));
      Robot.setHost(argParser.getValue("ip"));
      System.out.println("IP: " + argParser.getValue("ip"));
    } else {
      monitor.setProgress(600);
      monitor.setNote("Getting Team Number");
      IntegerProperty teamProp = frame.getPrefs().team;
      int teamNumber = teamProp.getValue();

      teamNumberLoop: while (teamNumber <= 0) {
        try {
          String input = JOptionPane.showInputDialog("Input Team Number");
          if (input == null) {
            teamNumber = 0;
            break teamNumberLoop;
          }
          teamNumber = Integer.parseInt(input);
        } catch (Exception e) {
          // TODO
        }
      }

      monitor.setProgress(650);
      monitor.setNote("Connecting to robot of team: " + teamNumber);
      teamProp.setValue(teamNumber);
      Robot.setTeam(teamNumber);
    }

    try {
      SwingUtilities.invokeAndWait(new Runnable() {

        public void run() {
          try {
            frame.pack();
            frame.setVisible(true);

            monitor.setProgress(750);
            monitor.setNote("Loading From Save");

            // Load
            File file = new File(frame.getPrefs().saveFile.getValue());
            if (file.exists()) {
              frame.load(file.getPath());
            }

            monitor.setProgress(1000);

          } catch (Exception e) {
            e.printStackTrace();

            System.exit(1);
          }
        }
      });
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(2);
    }
  }
}
