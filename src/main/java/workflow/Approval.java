package workflow;

public class Approval {

    Long approverId;
    String approverName;
    Status status;

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }
}
