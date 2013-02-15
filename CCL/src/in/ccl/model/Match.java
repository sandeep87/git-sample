package in.ccl.model;

public class Match {

	private String hostingTeam;

	private String opponentTeam;

	private String time;

	private int matchId;

	public int getMatchId () {
		return matchId;
	}

	public void setMatchId (int matchId) {
		this.matchId = matchId;
	}

	public String getHostingTeam () {
		return hostingTeam;
	}

	public void setHostingTeam (String hostingTeam) {
		this.hostingTeam = hostingTeam;
	}

	public String getOpponentTeam () {
		return opponentTeam;
	}

	public void setOpponentTeam (String opponentTeam) {
		this.opponentTeam = opponentTeam;
	}

	public String getTime () {
		return time;
	}

	public void setTime (String time) {
		this.time = time;
	}

}
