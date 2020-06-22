
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * This class is used to hold a bully leader election
 * @author AdrianF
 */
public class BullyElection {

    public final int TOTAL_PROCESSES;
    public final int REQUESTER_ID;
    private int leader;
    private final int[] STATUS, PRIORITY;

    //constructor takes in the ID of the requesting process and the total number of processes in the system
    public BullyElection(int requesterID, int totalProcesses) {
        this.TOTAL_PROCESSES = totalProcesses;

        Random rand = new Random();
        
        //make sure requester ID is not zero
        this.REQUESTER_ID = requesterID + 1;

        this.STATUS = new int[totalProcesses];
        this.PRIORITY = new int[totalProcesses];

        //assign random statuses and priorities to the processes
        for (int i = 0; i < this.TOTAL_PROCESSES; i++) {
            STATUS[i] = rand.nextInt(2) + 1;
            PRIORITY[i] = rand.nextInt(2) + 1;
        }
        
        //start recursive election method
        elect(this.REQUESTER_ID);
    }

    public void elect(int starter) {
        starter = starter - 1;
        this.leader = starter + 1;
        for (int i = 0; i < this.TOTAL_PROCESSES; i++) {
            if (this.PRIORITY[starter] < this.PRIORITY[i]) {
                if (this.STATUS[i] == 1) {
                    elect(i + 1);
                }
            }
        }
    }

    //get the leader 
    public int getElected() {
        return (this.leader-1);
    }
}
