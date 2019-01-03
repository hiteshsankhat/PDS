package hiteshsankhat.github.com.pds.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

	private String email;
	private String user_id;

	private String name;

	public User(String email, String user_id, String name) {
		this.email = email;
		this.user_id = user_id;
		this.name = name;
	}

	public User() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User{" +
				"email='" + email + '\'' +
				", user_id='" + user_id + '\'' +
				", name='" + name + '\'' +
				'}';
	}

	public static Creator<User> getCREATOR() {
		return CREATOR;
	}

	protected User(Parcel in) {
		email = in.readString();
		user_id = in.readString();
		name = in.readString();
	}


	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(email);
		parcel.writeString(user_id);
		parcel.writeString(name);
	}
}
