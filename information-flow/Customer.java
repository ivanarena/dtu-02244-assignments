
import java.util.UUID;

public class Customer {
	private String username;
	private UUID id;

	// {S: S, U}
	public Customer(User user) {
		this.username = user.getName();
		this.id = generateId();
	}

	// {S: S}
	private UUID generateId() {
		return UUID.randomUUID();
	}

	// {S: S}
	public Boolean isUser(User user) {
		return this.username.equals(user.getName());
	}

	// {U: S, U}
	public UUID getId() {
		return id;
	}

	// {U: S, U}
	public String getUsername() {
		return username;
	}
}