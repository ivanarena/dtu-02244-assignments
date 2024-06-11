
import java.util.HashMap;
import java.util.ArrayList;
import java.util.UUID;

public class AuctionHouse {
    private String name;
    private HashMap<UUID, Integer> customers = new HashMap<>(); // customer ID -> status (reputation system)
    private ArrayList<Auction> auctions = new ArrayList<>();
    private ArrayList<AuctionHouse> trustedAuctionHouses = new ArrayList<>();
    private Server client;

    // {AH: S, AH}
    AuctionHouse(String name, Server client) {
        this.name = name;
        this.client = client;
    }

    // {AH: A, AH}
    public void createAuction(Auction auction) {
        auctions.add(auction);
    }

    // {AH: AH}
    public String getName() {
        return name;
    }

    // {AH: S, AH}
    public void registerCustomer(UUID customerId, Integer status) {
        customers.put(customerId, status);
    }

    // {AH: S, AH}
    public void subscribeCustomerToAuction(UUID customerId, Auction auction) {
        if (!auctions.contains(auction)) {
            System.out.println("Auction not found in the auction house " + this.name);
            return;
        }
        auction.subscribeCustomer(customerId);
    }

    // {AH: AH}
    public HashMap<UUID, Integer> getCustomers() {
        return customers;
    }

    // {AH: AH}
    public Integer getCustomerStatus(UUID customerId) {
        return customers.get(customerId);
    }

    // {AH: AH}
    public Integer updateCustomerStatus(UUID customerId, Integer newStatus) {
        return customers.put(customerId, newStatus);
    }

    // {AH: AH}
    public Integer getCustomerReputationFromTrustedAuctionHouses(UUID customerId) {
        Integer maxStatus = 0;
        // get the highest status from the trusted auction houses (reputation)
        for (AuctionHouse auctionHouse : trustedAuctionHouses) {
            Integer currentStatus = auctionHouse.getCustomerStatus(customerId);
            if (currentStatus > maxStatus) {
                maxStatus = currentStatus;
            }
        }

        return maxStatus;
    }

    // {AH: AH}
    public void addTrustedAuctionHouse(AuctionHouse auctionHouse) {
        trustedAuctionHouses.add(auctionHouse);
    }

    // {AH: A, AH}
    public boolean checkPermissions(Auction auction, UUID customerId) {
        return customers.containsKey(customerId) & auction.getParticipantsIDs().contains(customerId);
    }

    // {A: AH, A}
    public Boolean placeBid(Auction auction, UUID customerId, double bidAmount) {
        if (bidAmount > 500 & customers.get(customerId) == 1) {
            System.err.println("!!! User " + customerId + " has low reputation. Max bid amount set to default $500.");
            return false;
        }
        return auction.receiveBid(customerId, bidAmount);
    }

    // {AH: S, AH, A}
    public void startAuction(Auction auction) {
        client.placeCommissionBids(this, auction);
    }

    // {AH: S, AH, A}
    public UUID endAuction(Auction auction) {
        Bid winner = auction.endAuction();
        System.out.println("Auction " + auction.getId() + ": going once ... going twice ...");
        client.notifyParticipants(auction, winner.getCustomerId());
        System.out.println("---------------------------------");
        client.updateCustomerReputation(winner.getCustomerId(), this);
        return winner.getCustomerId();
    }
}