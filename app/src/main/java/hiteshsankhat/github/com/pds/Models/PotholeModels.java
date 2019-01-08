package hiteshsankhat.github.com.pds.Models;

import com.google.firebase.firestore.GeoPoint;

public class PotholeModels {
	private GeoPoint geoPoint;
	private String fileName;

	public PotholeModels() {
	}

	@Override
	public String toString() {
		return "PotholeModels{" +
				"geoPoint=" + geoPoint +
				", fileName='" + fileName + '\'' +
				'}';
	}

	public PotholeModels(GeoPoint geoPoint, String fileName) {
		this.geoPoint = geoPoint;
		this.fileName = fileName;
	}

	public GeoPoint getGeoPoint() {return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
