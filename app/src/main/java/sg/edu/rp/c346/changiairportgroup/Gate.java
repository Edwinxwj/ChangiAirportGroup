package sg.edu.rp.c346.changiairportgroup;

import java.io.Serializable;

/**
 * Created by 15017363 on 21/6/2017.
 */

public class Gate {
    private String gate;
    private String terminal;

    public Gate(){

    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public Gate(String gate, String terminal) {
        this.gate = gate;
        this.terminal = terminal;
    }

    @Override
    public String toString() {
        return getGate();
    }
}
