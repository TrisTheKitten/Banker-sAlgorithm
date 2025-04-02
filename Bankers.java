import java.io.*;
import java.util.*;

public class Bankers {
    static int n;
    static int m;
    static int[] available;
    static int[][] max;
    static int[][] allocation;
    static int[][] need;

    // Function to read input from a file
    public static void readInput(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
        String[] tokens = line.trim().split("\\s+");
        n = Integer.parseInt(tokens[0]);
        m = Integer.parseInt(tokens[1]);
        available = new int[m];
        line = br.readLine();
        tokens = line.trim().split("\\s+");
        for (int j = 0; j < m; j++) {
            available[j] = Integer.parseInt(tokens[j]);
        }
        max = new int[n][m];
        for (int i = 0; i < n; i++) {
            line = br.readLine();
            tokens = line.trim().split("\\s+");
            for (int j = 0; j < m; j++) {
                max[i][j] = Integer.parseInt(tokens[j]);
            }
        }
        allocation = new int[n][m];
        for (int i = 0; i < n; i++) {
            line = br.readLine();
            tokens = line.trim().split("\\s+");
            for (int j = 0; j < m; j++) {
                allocation[i][j] = Integer.parseInt(tokens[j]);
            }
        }
        br.close();
    }

    // Function to calculate the Need matrix
    public static void calculateNeed() {
        need = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
    }

    // Function to print the current state of the system
    public static void printState() {
        System.out.println("Available Resources: " + Arrays.toString(available));
        System.out.println("\nMax Matrix:");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + i + ": " + Arrays.toString(max[i]));
        }
        System.out.println("\nAllocation Matrix:");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + i + ": " + Arrays.toString(allocation[i]));
        }
        System.out.println("\nNeed Matrix:");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + i + ": " + Arrays.toString(need[i]));
        }
        System.out.println();
    }

    // Function to execute the Banker's Algorithm
    public static void Execute() {
        boolean[] finished = new boolean[n];
        ArrayList<Integer> safeSequence = new ArrayList<>();
        boolean progress;

        do {
            progress = false;
            for (int i = 0; i < n; i++) {
                if (!finished[i] && canSatisfy(i)) {
                    System.out.println("Process P" + i + " can finish. Executing process P" + i + "...");
                    for (int j = 0; j < m; j++) {
                        available[j] += allocation[i][j];
                        allocation[i][j] = 0;
                        need[i][j] = 0;
                    }
                    finished[i] = true;
                    safeSequence.add(i);
                    progress = true;
                    printState();
                    break;
                }
            }
        } while (progress);

        boolean allFinished = true;
        for (int i = 0; i < n; i++) {
            if (!finished[i]) {
                allFinished = false;
                break;
            }
        }

        if (allFinished) {
            System.out.println("All processes finished execution. System is in a safe state.");
            System.out.print("Final Safe Sequence: ");
            for (int i = 0; i < safeSequence.size(); i++) {
                System.out.print("P" + safeSequence.get(i));
                if (i != safeSequence.size() - 1)
                    System.out.print(" -> ");
            }
            System.out.println();
        } else {
            System.out.println("Deadlock detected! The following processes could not complete:");
            for (int i = 0; i < n; i++) {
                if (!finished[i])
                    System.out.print("P" + i + " ");
            }
            System.out.println();
            System.out.print("Safe Sequence until deadlock: ");
            for (int i = 0; i < safeSequence.size(); i++) {
                System.out.print("P" + safeSequence.get(i));
                if (i != safeSequence.size() - 1)
                    System.out.print(" -> ");
            }
            System.out.println();
        }
    }

    // Function to check if the system can satisfy the resource request of a process
    public static boolean canSatisfy(int i) {
        for (int j = 0; j < m; j++) {
            if (need[i][j] > available[j])
                return false;
        }
        return true;
    }

    // Main method
    public static void main(String[] args) throws IOException {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter file name: ");
        String filename = s.nextLine();
        readInput(filename);
        calculateNeed();
        printState();
        Execute();
        s.close();
    }
}
