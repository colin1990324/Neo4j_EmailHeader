import java.util.ArrayList;
import java.util.List;

public class Email {

	int fromId;
	List<Integer> toIds;
	String time;
	String content;

	public Email() {
		toIds = new ArrayList<Integer>();
	}
}
