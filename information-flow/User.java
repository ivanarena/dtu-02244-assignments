

public class User {
    private String name;
    private Server client;

    // {U: S, U}
    public User(String name, Server client) {
        this.name = name;
        this.client = client;
    }

    // {S: U, S}
    public boolean placeBid(AuctionHouse auctionHouse, Auction auction, double bidAmount) {
        return client.receiveBid(auctionHouse, auction, this, bidAmount);
    }

    // {S: U, S}
    public void registerToAuctionHouse(AuctionHouse auctionHouse) {
        client.registerUserToAuctionHouse(this, auctionHouse);
    }

    // {S: U, S}
    public void subscribeToAuction(AuctionHouse auctionHouse, Auction auction) {
        client.subscribeUserToAuction(this, auctionHouse, auction);
    }

    // {S: U, S}
    public void subscribeToAuctionWithCommission(AuctionHouse auctionHouse, Auction auction, double maxBidAmount,
            double increment) {
        client.subscribeUserToAuctionHouseWithCommission(this, auctionHouse, auction, maxBidAmount, increment);
    }

    // {U: S, U}
    public String getName() {
        return name;
    }
}