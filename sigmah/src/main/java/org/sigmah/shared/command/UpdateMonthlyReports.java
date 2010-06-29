package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

import java.io.Serializable;
import java.util.ArrayList;
/*
 * @author Alex Bertram
 */

public class UpdateMonthlyReports implements Command<VoidResult> {


    public  static class Change implements Serializable {
        public Month month;
        public int indicatorId;
        public Double value;

        public Change() {

        }

        public Change(int indicatorId, Month month, Double value) {
            this.indicatorId = indicatorId;
            this.month = month;
            this.value = value;
        }


    }

    private int siteId;
    private ArrayList<Change> changes;


    public UpdateMonthlyReports() {
    }

    public UpdateMonthlyReports(int siteId, ArrayList<Change> changes) {
        this.siteId = siteId;
        this.changes = changes;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public ArrayList<Change> getChanges() {
        return changes;
    }

    public void setChanges(ArrayList<Change> changes) {
        this.changes = changes;
    }
}