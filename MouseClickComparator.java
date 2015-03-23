import java.util.Comparator;


public class MouseClickComparator implements Comparator<MouseClick>{
	
	
	
	@Override
	public int compare(MouseClick mc1, MouseClick mc2) {
		
		if (mc1.getGroup() < mc2.getGroup()) return -1;
		if (mc1.getGroup() == mc2.getGroup()) return 0;
		
		return 1;
	}

}
