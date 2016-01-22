package com.cmcc.syw.ping;

import java.util.LinkedList;

/**
 * Created by sunyiwei on 16-1-21.
 */
public class Printer extends Thread {
    private LinkedList<Target> targets = new LinkedList<Target>();

    public Printer(){
        setName("Printer");
        setDaemon(true);
    }

    public void add(Target target){
        synchronized (targets){
            targets.add(target);
            targets.notify();
        }
    }

    @Override
    public void run() {
        while (true) {
            Target target = null;

            synchronized (targets) {
                while (targets.size() == 0) {
                    try {
                        targets.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                target = targets.removeFirst();
            }

            target.show();
        }
    }

    public LinkedList<Target> getTargets() {
        return targets;
    }

    public void setTargets(LinkedList<Target> targets) {
        this.targets = targets;
    }
}
