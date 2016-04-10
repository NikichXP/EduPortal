package eduportal.utils;

import java.io.Serializable;

public final class Text implements Serializable {

	private static final long serialVersionUID = 1223327408984552609L;
	private String value;

	/**
	 * This constructor exists for frameworks (e.g. Google Web Toolkit) that
	 * require it for serialization purposes. It should not be called
	 * explicitly.
	 */
	@SuppressWarnings("unused")
	private Text() {
	}

	public Text(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		if (value == null) {
			return -1;
		}
		return value.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Text) {
			Text key = (Text) object;
			if (value == null) {
				return key.value == null;
			}
			return value.equals(key.value);
		}
		return false;
	}

	@Override
	public String toString() {
		if (value == null) {
			return "<Text: null>";
		}
		String text = value;
		if (text.length() > 70) {
			text = text.substring(0, 70) + "...";
		}
		return "<Text: " + text + ">";
	}
}
