package telran.bc.dto;

import java.io.Serializable;

public class MoveData implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public long userId;
    public String number;

    public MoveData(String number, long userId) {
        this.userId = userId;
        this.number = number;
    }
}
