

public class Main {
    public static void main(String[] args) {
        Server client = new Server(); // {S: S}
        AuctionHouse auctionHouse1 = new AuctionHouse("auctionHouse1", client); // {AH: S, AH}
        AuctionHouse auctionHouse2 = new AuctionHouse("auctionHouse2", client); // {AH: S, AH}
        Auction auction1 = new Auction("auctionHouse1_auction1", 500); // {AH: S, AH, A}
        Auction auction2 = new Auction("auctionHouse2_auction2", 100); // {AH: S, AH, A}
        auctionHouse1.createAuction(auction1); // {AH: A, AH}
        auctionHouse2.createAuction(auction2); // {AH: A, AH}

        auctionHouse2.addTrustedAuctionHouse(auctionHouse1); // trust auctionHouse2 for Reputation System: {AH: AH}

        User user1 = new User("A", client); // {U: S, U}
        User user2 = new User("B", client); // {U: S, U}
        User user3 = new User("C", client); // {U: S, U}

        user1.registerToAuctionHouse(auctionHouse1); // {AH: S, U, AH}
        user2.registerToAuctionHouse(auctionHouse1); // {AH: S, U, AH}
        user3.registerToAuctionHouse(auctionHouse1); // {AH: S, U, AH}

        user1.subscribeToAuction(auctionHouse1, auction1); // {A: S, U, AH, A}
        user2.subscribeToAuction(auctionHouse1, auction1); // {A: S, U, AH, A}
        user3.subscribeToAuctionWithCommission(auctionHouse1, auction1, 750, 50); // {A: S, U, AH, A}

        auctionHouse1.startAuction(auction1); // {A: S, U, AH, A}
        user1.placeBid(auctionHouse1, auction1, 600); // {A: S, U, AH, A}
        user2.placeBid(auctionHouse1, auction1, 700); // {A: S, U, AH, A}
        user1.placeBid(auctionHouse1, auction1, 800); // {A: S, U, AH, A}
        user2.placeBid(auctionHouse1, auction1, 850); // {A: S, U, AH, A}
        auctionHouse1.endAuction(auction1); // {A: S, U, AH, A}

        user1.registerToAuctionHouse(auctionHouse2); // {AH: S, U, AH}
        user2.registerToAuctionHouse(auctionHouse2); // {AH: S, U, AH}

        user1.subscribeToAuction(auctionHouse2, auction2); // {A: S, U, AH, A}
        user2.subscribeToAuctionWithCommission(auctionHouse2, auction2, 600, 10); // {A: S, U, AH, A}

        auctionHouse2.startAuction(auction2); // {A: S, U, AH, A}
        user1.placeBid(auctionHouse2, auction2, 200); // {A: S, U, AH, A}
        user1.placeBid(auctionHouse2, auction2, 300); // {A: S, U, AH, A}
        user3.placeBid(auctionHouse2, auction2, 400); // {A: S, U, AH, A}
        auctionHouse2.endAuction(auction2); // {A: S, U, AH, A}
    }
}