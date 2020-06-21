
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

    public final int totalProcesses;
    public final int requesterID;
    private int leader;
    private final int[] status, priority;

    public BullyElection(int requesterID, int totalProcesses) {
        this.totalProcesses = totalProcesses;

        Random rand = new Random();

        this.requesterID = requesterID + 1;

        this.status = new int[totalProcesses];
        this.priority = new int[totalProcesses];

        for (int i = 0; i < this.totalProcesses; i++) {
            status[i] = rand.nextInt(2) + 1;
            priority[i] = rand.nextInt(2) + 1;
        }

        elect(this.requesterID);
    }

    public void elect(int starter) {
        starter = starter - 1;
        this.leader = starter + 1;
        for (int i = 0; i < this.totalProcesses; i++) {
            if (this.priority[starter] < this.priority[i]) {
                if (this.status[i] == 1) {
                    elect(i + 1);
                }
            }
        }
    }

    public int getElected() {
        return (this.leader-1);
    }
}
