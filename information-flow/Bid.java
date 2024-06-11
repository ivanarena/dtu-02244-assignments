

import java.util.UUID;

public class Bid {
    private UUID customerId;
    private double amount;

    // {A: U, S, A}
    public Bid(UUID customerId, double amount) {
        this.customerId = customerId;
        this.amount = amount;
    }

    // {A: U, S, A}
    public UUID getCustomerId() {
        return customerId;
    }

    // {A: U, S, A}
    public double getBidAmount() {
        return amount;
    }
}
