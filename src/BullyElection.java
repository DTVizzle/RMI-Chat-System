
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author AdrianF
 */
public class BullyElection {

    public final int TOTAL_PROCESSES;
    public final int REQUESTER_ID;
    private int leader;
    private final int[] STATUS, PRIORITY;

    public BullyElection(int requesterID, int totalProcesses) {
        this.TOTAL_PROCESSES = totalProcesses;

        Random rand = new Random();

        this.REQUESTER_ID = requesterID + 1;

        this.STATUS = new int[totalProcesses];
        this.PRIORITY = new int[totalProcesses];

        for (int i = 0; i < this.TOTAL_PROCESSES; i++) {
            STATUS[i] = rand.nextInt(2) + 1;
            PRIORITY[i] = rand.nextInt(2) + 1;
        }

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

    public int getElected() {
        return (this.leader-1);
    }
}
