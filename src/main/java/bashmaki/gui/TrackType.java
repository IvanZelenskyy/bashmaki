package bashmaki.gui;

public enum TrackType {
	Normal("Обычный"), Hill("Гора"), Hollow("Яма");
	private final String displayName;
	private TrackType(final String name) {
		this.displayName = name;
	}
	
	@Override
	public String toString(){
		return this.displayName;
	}
}
