package ak.loans;

public class LoanRequest {
    private String requestId;
    private String accountNumber;
    private double loanAmount;
    private String loanReason;
    private String status;

    public LoanRequest(String requestId, String accountNumber, double loanAmount, String loanReason, String status) {
        this.requestId = requestId;
        this.accountNumber = accountNumber;
        this.loanAmount = loanAmount;
        this.loanReason = loanReason;
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public String getLoanReason() {
        return loanReason;
    }

    public String getStatus() {
        return status;
    }
}