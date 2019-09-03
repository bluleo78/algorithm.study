import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Main2 {
	static int N;
	static Line[] lines;
	static List<LineGroup> lineGroups;
	static int maxGroupLineCnt = 0;

	static class Line {
		int x1;
		int y1;
		int x2;
		int y2;

		LineGroup group;

		public Line(int x12, int y12, int x22, int y22) {
			x1 = x12;
			y1 = y12;
			x2 = x22;
			y2 = y22;
		}

		public boolean isConnectedWith(Line line) {
			int ccw1 = ccw(x1, y1, x2, y2, line.x1, line.y1) * ccw(x1, y1, x2, y2, line.x2, line.y2);
			int ccw2 = ccw(line.x1, line.y1, line.x2, line.y2, x1, y1)
					* ccw(line.x1, line.y1, line.x2, line.y2, x2, y2);
			if (ccw1 == 0 && ccw2 == 0) {
				return isOverlapped(x1, y1, x2, y2, line.x1, line.y1, line.x2, line.y2);
			}

			return ccw1 <= 0 && ccw2 <= 0;
//			if (x1 - x2 != 0) {
//				double a0 = 1.0 * (y1 - y2) / (x1 - x2);
//				double b0 = 1.0 * (x1 * y2 - x2 * y1) / (x1 - x2);
//
//				if (line.x1 - line.x2 != 0) {
//					double a1 = 1.0 * (line.y1 - line.y2) / (line.x1 - line.x2);
//					double b1 = 1.0 * (line.x1 * line.y2 - line.x2 * line.y1) / (line.x1 - line.x2);
//
//					if (a0 - a1 != 0) {
//						double x = 1.0 * (b1 - b0) / (a0 - a1);
//						double y = 1.0 * (a0 * b1 - b0 * a1) / (a0 - a1);
//
//						return x >= Math.min(x1, x2) && x <= Math.max(x1, x2) && y >= Math.min(y1, y2)
//								&& y <= Math.max(y1, y2) && x >= Math.min(line.x1, line.x2)
//								&& x <= Math.max(line.x1, line.x2) && y >= Math.min(line.y1, line.y2)
//								&& y <= Math.max(line.y1, line.y2);
//					} else {
//						if (b0 == b1) {
//							return Math.min(x1, x2) <= Math.max(line.x1, line.x2)
//									&& Math.max(x1, x2) >= Math.min(line.x1, line.x2);
//						} else {
//							return false;
//						}
//					}
//				} else {
//					double x = line.x1;
//					double y = a0 * x + b0;
//
//					return x >= Math.min(x1, x2) && x <= Math.max(x1, x2) && y >= Math.min(y1, y2)
//							&& y <= Math.max(y1, y2) && x >= Math.min(line.x1, line.x2)
//							&& x <= Math.max(line.x1, line.x2) && y >= Math.min(line.y1, line.y2)
//							&& y <= Math.max(line.y1, line.y2);
//				}
//			} else {
//				double x = x1;
//				if (line.x1 - line.x2 != 0) {
//					double a1 = 1.0 * (line.y1 - line.y2) / (line.x1 - line.x2);
//					double b1 = 1.0 * (line.x1 * line.y2 - line.x2 * line.y1) / (line.x1 - line.x2);
//
//					double y = a1 * x + b1;
//
//					return x >= Math.min(x1, x2) && x <= Math.max(x1, x2) && y >= Math.min(y1, y2)
//							&& y <= Math.max(y1, y2) && x >= Math.min(line.x1, line.x2)
//							&& x <= Math.max(line.x1, line.x2) && y >= Math.min(line.y1, line.y2)
//							&& y <= Math.max(line.y1, line.y2);
//				} else {
//					if (x == line.x1) {
//						return Math.min(y1, y2) <= Math.max(line.y1, line.y2)
//								&& Math.max(y1, y2) >= Math.min(line.y1, line.y2);
//					} else {
//						return false;
//					}
//				}
//			}
		}

		private boolean isOverlapped(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
			if (Math.max(x1, x2) < Math.min(x3, x4))
				return false;
			if (Math.min(x1, x2) > Math.max(x3, x4))
				return false;
			if (Math.max(y1, y2) < Math.min(y3, y4))
				return false;
			if (Math.min(y1, y2) > Math.max(y3, y4))
				return false;
			return true;
		}

		private int ccw(int x1, int y1, int x2, int y2, int x3, int y3) {
			int tmp = x1 * y2 + x2 * y3 + x3 * y1;
			tmp -= (y1 * x2 + y2 * x3 + y3 * x1);

			if (tmp < 0)
				return -1;
			if (tmp > 0)
				return 1;
			return 0;
		}
	}

	static class LineGroup {
		List<Line> lines = new ArrayList<Line>();

		public LineGroup(Line line) {
			line.group = this;
			lines.add(line);
		}

		public void mergeGroup(LineGroup group) {
			for (Line line : group.lines) {
				line.group = this;
			}
			lines.addAll(group.lines);
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		N = Integer.parseInt(br.readLine());

		// Process
		lines = new Line[N];
		for (int i = 0; i < N; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int x1 = Integer.parseInt(st.nextToken());
			int y1 = Integer.parseInt(st.nextToken());
			int x2 = Integer.parseInt(st.nextToken());
			int y2 = Integer.parseInt(st.nextToken());
			lines[i] = new Line(x1, y1, x2, y2);
		}

		solve();
		System.out.format("%d\n", lineGroups.size());
		System.out.format("%d\n", lineGroups.get(0).lines.size());
	}

	private static void solve() {
		lineGroups = new ArrayList<LineGroup>();
		for (int i = 0; i < N; i++) {
			lines[i].group = new LineGroup(lines[i]);
			lineGroups.add(lines[i].group);
		}
		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				if (lines[i].group != lines[j].group && lines[i].isConnectedWith(lines[j])) {
					lineGroups.remove(lines[j].group);
					lines[i].group.mergeGroup(lines[j].group);
				}
			}
		}

		lineGroups.sort(new Comparator<LineGroup>() {

			@Override
			public int compare(LineGroup o1, LineGroup o2) {
				return o2.lines.size() - o1.lines.size();
			}
		});
	}

}
