
import java.util.UUID;

public class Commission {
    private UUID customerId;
    private double maxBidAmount;
    private double increment;

    // {A: U, S, A}
    public Commission(UUID customerId, double maxBidAmount, double increment) {
        this.customerId = customerId;
        this.maxBidAmount = maxBidAmount;
        this.increment = increment;
    }

    // {A: U, S, A}
    public double getMaxBidAmount() {
        return maxBidAmount;
    }

    // {A: U, S, A}
    public double getIncrement() {
        return increment;
    }

    // {A: U, S, A}
    public UUID getCustomerId() {
        return customerId;
    }
}