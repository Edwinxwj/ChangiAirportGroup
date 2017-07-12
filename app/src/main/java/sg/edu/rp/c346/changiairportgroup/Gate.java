package sg.edu.rp.c346.changiairportgroup;

import java.io.Serializable;

/**
 * Created by 15017363 on 21/6/2017.
 */

public class Gate {
    private String gateNumber;
    private String terminal;

    public Gate(){

    }

    public String getGateNumber() {
        return gateNumber;
    }

    public void setGateNumber(String gateNumber) {
        this.gateNumber = gateNumber;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public Gate(String gateNumber, String terminal) {
        this.gateNumber = gateNumber;
        this.terminal = terminal;
    }

    @Override
    public String toString() {
        return getGateNumber();
    }
}
