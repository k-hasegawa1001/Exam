package bean;

import java.io.Serializable;

public class User implements Serializable {
	private boolean isAuthenticated;

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}
}
