package in.ccl.score;

public class LiveScore {
	
	//team details variables
	private String team1;
	
	private  String team2;
	
	//first inning score details variables;
	private int target_score;
	
	private int target_wickets;
	
	private double target_overs;
	
	//need score
	private String need_score;
	
	//second inning scoe details variables
	
	private int current_score_score;
	
	private int current_score_wickets;
	
	private double current_score_overs;
	
	//first batsman details variables
	private String striker_name;
	
	private int striker_score;
	
	private int striker_balls;
	
	private double striker_strikerate;
	
	//second batsman details variables
	private String nonstriker_name;
	
	private int nonstriker_score;
	
	private int nonstriker_balls;
	
	private double nonstriker_strikerate;
	
	//first bowler details
	private  String current_bowler_name;
	
	private double current_bowler_overs;
	
	private int current_bowler_madiens;
	
	private int current_bowler_runs;
	
	private int current_bowler_wickets;
	
	//second bowler details
	private String previous_bowler_name;
	
	private double previous_bowler_overs;
	
    private int previous_bowler_madiens;
	
	private int previous_bowler_runs;
	
	private int previous_bowler_wickets;

	public String getTeam1() {
		return team1;
	}

	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	public String getTeam2() {
		return team2;
	}

	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	public int getTarget_score() {
		return target_score;
	}

	public void setTarget_score(int target_score) {
		this.target_score = target_score;
	}

	public int getTarget_wickets() {
		return target_wickets;
	}

	public void setTarget_wickets(int target_wickets) {
		this.target_wickets = target_wickets;
	}

	public double getTarget_overs() {
		return target_overs;
	}

	public void setTarget_overs(double target_overs) {
		this.target_overs = target_overs;
	}

	public String getNeed_score() {
		return need_score;
	}

	public void setNeed_score(String need_score) {
		this.need_score = need_score;
	}

	public int getCurrent_score_score() {
		return current_score_score;
	}

	public void setCurrent_score_score(int current_score_score) {
		this.current_score_score = current_score_score;
	}

	public int getCurrent_score_wickets() {
		return current_score_wickets;
	}

	public void setCurrent_score_wickets(int current_score_wickets) {
		this.current_score_wickets = current_score_wickets;
	}

	public double getCurrent_score_overs() {
		return current_score_overs;
	}

	public void setCurrent_score_overs(double current_score_overs) {
		this.current_score_overs = current_score_overs;
	}

	public String getStriker_name() {
		return striker_name;
	}

	public void setStriker_name(String striker_name) {
		this.striker_name = striker_name;
	}

	public int getStriker_score() {
		return striker_score;
	}

	public void setStriker_score(int striker_score) {
		this.striker_score = striker_score;
	}

	public int getStriker_balls() {
		return striker_balls;
	}

	public void setStriker_balls(int striker_balls) {
		this.striker_balls = striker_balls;
	}

	public double getStriker_strikerate() {
		return striker_strikerate;
	}

	public void setStriker_strikerate(double striker_strikerate) {
		this.striker_strikerate = striker_strikerate;
	}

	public String getNonstriker_name() {
		return nonstriker_name;
	}

	public void setNonstriker_name(String nonstriker_name) {
		this.nonstriker_name = nonstriker_name;
	}

	public int getNonstriker_score() {
		return nonstriker_score;
	}

	public void setNonstriker_score(int nonstriker_score) {
		this.nonstriker_score = nonstriker_score;
	}

	public int getNonstriker_balls() {
		return nonstriker_balls;
	}

	public void setNonstriker_balls(int nonstriker_balls) {
		this.nonstriker_balls = nonstriker_balls;
	}

	public double getNonstriker_strikerate() {
		return nonstriker_strikerate;
	}

	public void setNonstriker_strikerate(double nonstriker_strikerate) {
		this.nonstriker_strikerate = nonstriker_strikerate;
	}

	public String getCurrent_bowler_name() {
		return current_bowler_name;
	}

	public void setCurrent_bowler_name(String current_bowler_name) {
		this.current_bowler_name = current_bowler_name;
	}

	public double getCurrent_bowler_overs() {
		return current_bowler_overs;
	}

	public void setCurrent_bowler_overs(double current_bowler_overs) {
		this.current_bowler_overs = current_bowler_overs;
	}

	public int getCurrent_bowler_madiens() {
		return current_bowler_madiens;
	}

	public void setCurrent_bowler_madiens(int current_bowler_madiens) {
		this.current_bowler_madiens = current_bowler_madiens;
	}

	public int getCurrent_bowler_runs() {
		return current_bowler_runs;
	}

	public void setCurrent_bowler_runs(int current_bowler_runs) {
		this.current_bowler_runs = current_bowler_runs;
	}

	public int getCurrent_bowler_wickets() {
		return current_bowler_wickets;
	}

	public void setCurrent_bowler_wickets(int current_bowler_wickets) {
		this.current_bowler_wickets = current_bowler_wickets;
	}

	public String getPrevious_bowler_name() {
		return previous_bowler_name;
	}

	public void setPrevious_bowler_name(String previous_bowler_name) {
		this.previous_bowler_name = previous_bowler_name;
	}

	public double getPrevious_bowler_overs() {
		return previous_bowler_overs;
	}

	public void setPrevious_bowler_overs(double previous_bowler_overs) {
		this.previous_bowler_overs = previous_bowler_overs;
	}

	public int getPrevious_bowler_madiens() {
		return previous_bowler_madiens;
	}

	public void setPrevious_bowler_madiens(int previous_bowler_madiens) {
		this.previous_bowler_madiens = previous_bowler_madiens;
	}

	public int getPrevious_bowler_runs() {
		return previous_bowler_runs;
	}

	public void setPrevious_bowler_runs(int previous_bowler_runs) {
		this.previous_bowler_runs = previous_bowler_runs;
	}

	public int getPrevious_bowler_wickets() {
		return previous_bowler_wickets;
	}

	public void setPrevious_bowler_wickets(int previous_bowler_wickets) {
		this.previous_bowler_wickets = previous_bowler_wickets;
	}
	
	

}
