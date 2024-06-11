
import java.util.ArrayList;
import java.util.UUID;

public class Auction {
    private String id;
    private double startingPrice;
    private Boolean active;
    private ArrayList<UUID> participants = new ArrayList<>(); // customer IDs
    private ArrayList<Bid> bids = new ArrayList<>();

    // {A: A}
    public Auction(String id, double startingPrice) {
        this.id = id;
        this.startingPrice = startingPrice;
        this.active = true;
    }

    // {A: A}
    public ArrayList<UUID> getParticipantsIDs() {
        return participants;
    }

    // {A: A}
    public ArrayList<Bid> getBids() {
        return bids;
    }

    // {A: A}
    public String getId() {
        return id;
    }

    // {A: A}
    public double getStartingPrice() {
        return startingPrice;
    }

    // {A: A}
    public Bid getWinningBid() {
        if (bids.size() > 0) {
            return bids.get(bids.size() - 1); // get last bid
        }
        return new Bid(null, this.startingPrice);
    }

    // {A: U, A}
    public void subscribeCustomer(UUID customerId) {
        participants.add(customerId);
    }

    // {A: A}
    public Boolean receiveBid(UUID customerId, double bidAmount) {
        if (!active) {
            return false; // not valid bid. The auction is not active
        }
        if (bidAmount <= this.getWinningBid().getBidAmount() || bidAmount <= startingPrice) {
            return false; // not valid bid. The bid amount should be higher than the last bid and higher
                          // than the starting price
        }
        if (!participants.contains(customerId)) {
            return false; // not valid bid. The customer is not subscribed to the auction
        }
        Bid bid = new Bid(customerId, bidAmount);
        bids.add(bid);
        return true;
    }

    // {A: A}
    public Bid endAuction() {
        this.active = false;
        return this.getWinningBid();
    }

    // {A: A}
    public boolean hasPriorityOverWinningBid(UUID bidderId) {
        // check if the bidder has joined the participants before the winning bidder
        if (participants.indexOf(bidderId) <= participants.indexOf(this.getWinningBid().getCustomerId())) {
            return true;
        } else {
            return false;
        }
    }
}
