package in.ccl.model;

import java.util.Date;


public class MatchSchedule {
  private int matchId;
  private Date startTime;
  private Date endDate;
  private String status;
	
	public int getMatchId () {
		return matchId;
	}
	
	public void setMatchId (int matchId) {
		this.matchId = matchId;
	}
	
	public Date getStartTime () {
		return startTime;
	}
	
	public void setStartTime (Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndDate () {
		return endDate;
	}
	
	public void setEndDate (Date endDate) {
		this.endDate = endDate;
	}
	
	public String getStatus () {
		return status;
	}
	
	public void setStatus (String status) {
		this.status = status;
	}
  
	
}
