package hiteshsankhat.github.com.pds;

import android.app.Application;

import hiteshsankhat.github.com.pds.Models.User;

public class UserClient extends Application {
	private User user = null;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
