
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Server {
    private HashMap<String, Customer> customers = new HashMap<>(); // username -> customer
    private HashMap<Auction, ArrayList<Commission>> commissionBidders = new HashMap<>();
    private HashMap<Auction, ArrayList<UUID>> auctionsParticipants = new HashMap<>(); // auction -> customerId

    // {U: S, U}
    public void registerUserToAuctionHouse(User user, AuctionHouse auctionHouse) {
        // Check if the user is already in the customers list
        if (checkAuctionHousePermissions(user)) {
            Customer customer = customers.get(user.getName());
            Integer reputationStatus = auctionHouse.getCustomerReputationFromTrustedAuctionHouses(customer.getId());

            auctionHouse.registerCustomer(customer.getId(), reputationStatus);
        } else {
            Customer newCustomer = new Customer(user);
            auctionHouse.registerCustomer(newCustomer.getId(), 0); // first time customer, lower level of "status" for
                                                                   // the reputation system
            customers.put(user.getName(), newCustomer);
        }
    }

    // {AH: U, AH}
    public void subscribeUserToAuction(User user, AuctionHouse auctionHouse, Auction auction) {
        if (!auctionsParticipants.containsKey(auction)) {
            auctionsParticipants.put(auction, new ArrayList<>());
        }

        if (checkAuctionHousePermissions(user)) {
            Customer customer = customers.get(user.getName());
            auctionsParticipants.get(auction).add(customer.getId());
            auctionHouse.subscribeCustomerToAuction(customer.getId(), auction);
        } else {
            System.out.println("User " + user.getName() + " doesn't have auction permissions.");
        }
    }

    // {AH: U, AH}
    public void subscribeUserToAuctionHouseWithCommission(User user, AuctionHouse auctionHouse, Auction auction,
            double maxBidAmount, double increment) {
        if (!commissionBidders.containsKey(auction)) {
            commissionBidders.put(auction, new ArrayList<>());
        }
        if (!auctionsParticipants.containsKey(auction)) {
            auctionsParticipants.put(auction, new ArrayList<>());
        }

        if (checkAuctionHousePermissions(user)) {
            Customer customer = customers.get(user.getName());

            Integer reputationStatus = auctionHouse.getCustomerReputationFromTrustedAuctionHouses(customer.getId());
            if (reputationStatus == 1) {
                maxBidAmount = 500;
                System.err.println("!!! User " + user.getName()
                        + " has low reputation. Max bid amount set to default 500 Kr.");
            }

            Commission commission = new Commission(customer.getId(), maxBidAmount, increment);
            commissionBidders.get(auction).add(commission);
            auctionsParticipants.get(auction).add(customer.getId());
            auctionHouse.subscribeCustomerToAuction(customer.getId(), auction);

        } else {
            System.out.println("User " + user.getName() + " doesn't have auction permissions.");
        }
    }

	// {AH: S, A, AH}
    public boolean receiveBid(AuctionHouse auctionHouse, Auction auction, User user, double bidAmount) {
        if (customers.containsKey(user.getName())) {
            Customer customer = customers.get(user.getName());
            if (!auctionHouse.checkPermissions(auction, customer.getId())) {
                System.out.println("User " + user.getName() + " does not have permissions to bid on this auction.");
                return false;
            }

            Boolean bidIsPlaced = auctionHouse.placeBid(auction, customer.getId(), bidAmount);
            if (bidIsPlaced) {
                System.out.println("Auction " + auction.getId() + ": " + user.getName() + " bids " + bidAmount
                        + " Kr");
            } else {
                System.out.println("Auction " + auction.getId() + ": " + user.getName() + " cannot bid " + bidAmount
                        + " Kr");
            }

            placeCommissionBids(auctionHouse, auction);
            return true;
        } else {
            System.out.println("User " + user.getName() + " is not registered to the auction house.");
            return false;
        }
    }

	// {S: S, U}
    public void notifyParticipants(Auction auction, UUID winnerCustomerId) {
        for (UUID participant : auctionsParticipants.get(auction)) {
            Customer customer = findCustomerById(participant);
            if (participant == winnerCustomerId) {
                System.out.println("sold to "  + customer.getUsername() + " for " + auction.getWinningBid().getBidAmount() + " Kr");
            }
        }
    }

	// {AH: S, AH, A}
    public void placeCommissionBids(AuctionHouse auctionHouse, Auction auction) {
        if (!commissionBidders.containsKey(auction)) {
            return;
        }

        List<Commission> commissions = new ArrayList<>(commissionBidders.get(auction));

        for (Commission commission : commissions) {
            double incrementedBid = auction.getWinningBid().getBidAmount() + commission.getIncrement();

            Customer commissionCustomer = findCustomerById(commission.getCustomerId());
            if (commissionCustomer != null) {
                if (commission.getMaxBidAmount() >= incrementedBid) {
                    double bidAmount = auction.hasPriorityOverWinningBid(commissionCustomer.getId())
                            ? auction.getWinningBid().getBidAmount()
                            : incrementedBid;

                    auctionHouse.placeBid(auction, commissionCustomer.getId(), bidAmount);

                    System.out.println("Auction " + auction.getId() + ": auction house bids for "
                            + commissionCustomer.getUsername() + ": "
                            + bidAmount + " Kr");
                } else {
                    commissionBidders.get(auction).remove(commission);
                }
            }
        }
    }

	// {S: S, U, AH}
    public void updateCustomerReputation(UUID customerId, AuctionHouse auctionHouse) {
        Integer currentStatus = auctionHouse.getCustomerStatus(customerId);
        auctionHouse.updateCustomerStatus(customerId, currentStatus + 1);
        System.out.println("Auction House " + auctionHouse.getName() + ": Customer "
                + findCustomerById(customerId).getUsername() + " reputation updated -> " + (currentStatus + 1));
    }

	// {S: S, U}
    public Customer findCustomerById(UUID customerId) {
        for (Customer customer : customers.values()) {
            if (customer.getId().equals(customerId)) {
                return customer;
            }
        }
        return null; // return null if no customer with the given id is found
    }

	// {S: S}
    public boolean checkAuctionHousePermissions(User user) {
        return customers.containsKey(user.getName());
    }
}