package in.ccl.livescore.service;

import in.ccl.database.JSONPullParser;
import in.ccl.score.Batting;
import in.ccl.score.Bowler;
import in.ccl.score.Innings;
import in.ccl.score.LiveScore;
import in.ccl.score.MatchesResponse;
import in.ccl.score.ScoreBoard;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LiveScoreParser {

	public static String parseCurrentScore (InputStream inputStream) {
		String result = JSONPullParser.readStream(inputStream);
		System.out.println("JSON RESULT " + result);
		if (result != null) {
			try {
				JSONObject object = new JSONObject(result);
				if (object.has("score")) {
					return object.getString("score");
				}
			}
			catch (JSONException e) {
				System.out.println("JSON OBJECT EXCEPTIN");
				return null;
			}
		}
		return null;
	}

	public static String parseCurrentMatchSchedule (InputStream inputStream) {
		String result = JSONPullParser.readStream(inputStream);
		String matchSchedule = null;
		if (result != null) {
			try {
				JSONObject object = new JSONObject(result);
				if (object.has("schedule")) {
					org.json.JSONArray jsonArray = object.getJSONArray("schedule");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject innerObject = jsonArray.getJSONObject(i);
						if (innerObject.has("status") && !(innerObject.getString("status").equals("over"))) {
							matchSchedule = innerObject.getString("start_date");
							break;
						}
					}
				}
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return matchSchedule;
	}

	public static ArrayList <MatchesResponse> parseMatches (InputStream inputStream) {
		ArrayList <MatchesResponse> matchesResponseList = null;
		String result = JSONPullParser.readStream(inputStream);
		JSONObject responseObject = null;
		try {
			responseObject = new JSONObject(result);
			if (responseObject.has("data")) {
				org.json.JSONArray matchesList = responseObject.getJSONArray("data");
				matchesResponseList = new ArrayList <MatchesResponse>();
				for (int i = 0; i < matchesList.length(); i++) {
					MatchesResponse matchesResponse = new MatchesResponse();
					JSONObject matchId = matchesList.getJSONObject(i);
					if (matchId.has("id")) {
						matchesResponse.setId(matchId.getInt("id"));
					}
					if (matchId.has("name")) {
						matchesResponse.setMathesName(matchId.getString("name"));
					}
					matchesResponseList.add(matchesResponse);
				}
			}
		}
		catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return matchesResponseList;
	}

	public static LiveScore parseLiveScore (InputStream inputStream) {
		String result = JSONPullParser.readStream(inputStream);
		LiveScore liveScore = null;

		if (result != null) {
			JSONObject dataObject;
			try {
				dataObject = new JSONObject(result);
				liveScore = new LiveScore();
				if (dataObject.has("data")) {
					JSONObject object = dataObject.getJSONObject("data");

					if (object.has("teams")) {
						JSONObject teamObject = object.getJSONObject("teams");

						if (teamObject.has("team1")) {
							liveScore.setTeam1(teamObject.getString("team1"));
						}
						if (teamObject.has("team2")) {
							liveScore.setTeam2(teamObject.getString("team2"));
						}
					}

					if (object.has("target")) {
						JSONObject targetObject = object.getJSONObject("target");
						if (targetObject.has("score")) {
							liveScore.setTarget_score(targetObject.getInt("score"));
						}
						if (targetObject.has("wickets")) {
							liveScore.setTarget_wickets(targetObject.getInt("wickets"));
						}
						if (targetObject.has("overs")) {
							liveScore.setTarget_overs(targetObject.getDouble("overs"));
						}
						if (targetObject.has("score")) {
							liveScore.setNeed_score(object.getString("score"));
						}
					}
					if (object.has("currentscore")) {
						JSONObject currentScoreObject = object.getJSONObject("currentscore");
						if (currentScoreObject.has("score")) {
							liveScore.setCurrent_score_score(currentScoreObject.getInt("score"));
						}
						if (currentScoreObject.has("wickets")) {
							liveScore.setCurrent_score_wickets(currentScoreObject.getInt("wickets"));
						}
						if (currentScoreObject.has("overs")) {
							liveScore.setCurrent_score_overs(currentScoreObject.getDouble("overs"));
						}
					}

					if (object.has("batsman1")) {

						JSONObject strikerObject = object.getJSONObject("batsman1");
						if (strikerObject.has("name")) {
							liveScore.setStriker_name(strikerObject.getString("name"));
						}
						if (strikerObject.has("score")) {

							liveScore.setStriker_score(strikerObject.getInt("score"));
						}
						if (strikerObject.has("balls")) {

							liveScore.setStriker_balls(strikerObject.getInt("balls"));
						}
						if (strikerObject.has("strikerrate")) {

							liveScore.setStriker_strikerate(strikerObject.getDouble("strikerrate"));
						}
					}
					if (object.has("batsman2")) {

						JSONObject nonStrikerObject = object.getJSONObject("batsman2");
						if (nonStrikerObject.has("name")) {
							liveScore.setNonstriker_name(nonStrikerObject.getString("name"));
						}
						if (nonStrikerObject.has("score")) {
							liveScore.setNonstriker_score(nonStrikerObject.getInt("score"));
						}
						if (nonStrikerObject.has("balls")) {

							liveScore.setNonstriker_balls(nonStrikerObject.getInt("balls"));
						}
						if (nonStrikerObject.has("strikerate")) {

							liveScore.setNonstriker_strikerate(nonStrikerObject.getDouble("strikerate"));
						}

					}
					if (object.has("bowler1")) {

						JSONObject currentBowlerObject = object.getJSONObject("bowler1");
						if (currentBowlerObject.has("name")) {
							liveScore.setCurrent_bowler_name(currentBowlerObject.getString("name"));
						}
						if (currentBowlerObject.has("overs")) {

							liveScore.setCurrent_bowler_overs(currentBowlerObject.getDouble("overs"));
						}
						if (currentBowlerObject.has("madins")) {

							liveScore.setCurrent_bowler_madiens(currentBowlerObject.getInt("madins"));
						}
						if (currentBowlerObject.has("runs")) {

							liveScore.setCurrent_bowler_runs(currentBowlerObject.getInt("runs"));
						}
						if (currentBowlerObject.has("wickets")) {

							liveScore.setCurrent_bowler_wickets(currentBowlerObject.getInt("wickets"));
						}
					}
					if (object.has("bowler2")) {

						JSONObject previousBowlerObject = object.getJSONObject("bowler2");

						if (previousBowlerObject.has("name")) {

							liveScore.setPrevious_bowler_name(previousBowlerObject.getString("name"));
						}
						if (previousBowlerObject.has("overs")) {

							liveScore.setPrevious_bowler_overs(previousBowlerObject.getDouble("overs"));
						}
						if (previousBowlerObject.has("madins")) {

							liveScore.setPrevious_bowler_madiens(previousBowlerObject.getInt("madins"));
						}
						if (previousBowlerObject.has("runs")) {

							liveScore.setPrevious_bowler_runs(previousBowlerObject.getInt("runs"));
						}
						if (previousBowlerObject.has("wickets")) {

							liveScore.setPrevious_bowler_wickets(previousBowlerObject.getInt("wickets"));
						}
					}
				}
				return liveScore;
			}
			catch (org.json.JSONException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public static ScoreBoard parseScoreBoard (InputStream inputStream) {
		
		String result = JSONPullParser.readStream(inputStream);
      ScoreBoard scoreBoardItems = null;
  
      if(result != null){
      	JSONObject dataObject ;
      	try {
					dataObject = new JSONObject(result);
					scoreBoardItems = new ScoreBoard();
   			if(dataObject.has("data")){
   				JSONObject object = dataObject.getJSONObject("data");
						if(object.has("innings1")){
							String firstInn = object.getString("innings1");
							JSONObject firstInningsObject = new JSONObject(firstInn.trim());
							Innings firstInningsItems = parseInningsData(firstInningsObject);
							scoreBoardItems.setFirstInningsList(firstInningsItems);
						}else{
							System.out.println("phani no firstInn");
						}
						if(object.has("innings2")){
							String secondInn = object.getString("innings2");
							JSONObject secondInningsObject = new JSONObject(secondInn.trim());
							Innings secondInningsItems = parseInningsData(secondInningsObject);
							scoreBoardItems.setSecondInningsList(secondInningsItems);
						}else{
							System.out.println("phani no secondInn");
						}
					}else{
						System.out.println("phani no data");
					}
   			return scoreBoardItems;
					
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
      }
		 
		return null;
	}

	private static Innings parseInningsData (JSONObject inningsObject) {
		Innings innings = new Innings();
		try {
			if (!inningsObject.isNull("bowling_team")) {
				if (inningsObject.has("bowling_team")) {
					innings.setBowling_team(inningsObject.getString("bowling_team"));
				}
			}

			if (!inningsObject.isNull("batting_team")) {
				if (inningsObject.has("batting_team")) {
					innings.setBatting_team(inningsObject.getString("batting_team"));
				}
			}

			if (!inningsObject.isNull("total")) {
				if (inningsObject.has("total")) {
					JSONObject totalObject = inningsObject.getJSONObject("total");
					if (totalObject.has("runs")) {
						innings.setRuns(totalObject.getInt("runs"));
					}
					if (totalObject.has("overs")) {
						innings.setOvers(totalObject.getDouble("overs"));
					}
					if (totalObject.has("wickets")) {
						innings.setWickets(totalObject.getInt("wickets"));
					}

				}
			}
			System.out.println("data :"+inningsObject.toString());

			if (!inningsObject.isNull("extras")) {
				if (inningsObject.has("extras")) {
					JSONObject extraObject = inningsObject.getJSONObject("extras");
					if (extraObject.has("legbyes")) {
						innings.setLegbyes(extraObject.getInt("legbyes"));
					}
					if (extraObject.has("wides")) {
						innings.setWides(extraObject.getInt("legbyes"));
					}
					if (extraObject.has("byes")) {
						innings.setByes(extraObject.getInt("byes"));
					}
					if (extraObject.has("noballs")) {
						innings.setNoballs(extraObject.getInt("noballs"));
					}

				}

			}
			if (!inningsObject.isNull("batting")) {
				if (inningsObject.has("batting")) {
					JSONArray battingArrary = inningsObject.getJSONArray("batting");
					ArrayList <Batting> battingList = new ArrayList <Batting>();
					for (int i = 0; i < battingArrary.length(); i++) {
						JSONObject battJsonObject = new JSONObject();
						battJsonObject = battingArrary.getJSONObject(i);
						Batting batting = new Batting();
						if (battJsonObject.has("name")) {
							batting.setName(battJsonObject.getString("name"));
							System.out.println("phani batsman "+battJsonObject.getString("name"));
						}
						if (battJsonObject.has("balls")) {
							batting.setBalls(battJsonObject.getInt("balls"));
						}
						if (battJsonObject.has("score")) {
							batting.setScore(battJsonObject.getInt("score"));
						}
						if (battJsonObject.has("fours")) {
							batting.setFours(battJsonObject.getInt("fours"));
						}
						if (battJsonObject.has("sixes")) {
							batting.setSixes(battJsonObject.getInt("sixes"));
						}
						if (battJsonObject.has("caught")) {
							batting.setCaught(battJsonObject.getString("caught"));
						}
						if (battJsonObject.has("bowled")) {
							batting.setBowled(battJsonObject.getString("bowled"));
						}
						if (battJsonObject.has("hitwicket")) {
							batting.setHitwicket(battJsonObject.getString("hitwicket"));
						}
						if (battJsonObject.has("retiredhurt")) {
							batting.setRetiredhurt(battJsonObject.getString("retiredhurt"));
						}
						if (battJsonObject.has("runout")) {
							batting.setRunout(battJsonObject.getString("runout"));
						}
						if (battJsonObject.has("notout")) {
							batting.setNotout(battJsonObject.getString("notout"));
						}
						if (battJsonObject.has("stumped")) {
							batting.setStumped(battJsonObject.getString("stumped"));
						}
						if (battJsonObject.has("candb")) {
							batting.setCandb(battJsonObject.getString("candb"));
						}
						if (battJsonObject.has("handledtheball")) {
							batting.setHandledtheball(battJsonObject.getString("handledtheball"));
						}
						if (battJsonObject.has("didnotbat")) {
							batting.setDidnotbat(battJsonObject.getString("didnotbat"));
						}

						battingList.add(batting);
					}
					innings.setBatting_info(battingList);
					System.out.println("phaniBattlistSize"+battingList.size());
				}else{
					System.out.println("phani no batting");
				}

			}else{
				System.out.println("phani battingnull");
			}
			if (!inningsObject.isNull("bowling")) {
				if (inningsObject.has("bowling")) {
					JSONArray bowlerArrary = inningsObject.getJSONArray("bowling");
					ArrayList <Bowler> bowerList = new ArrayList <Bowler>();
					for (int i = 0; i < bowlerArrary.length(); i++) {
						JSONObject bowlerJsonObject = new JSONObject();
						bowlerJsonObject = bowlerArrary.getJSONObject(i);
						Bowler bowler = new Bowler();
						if (bowlerJsonObject.has("name")) {
							bowler.setBowlerName(bowlerJsonObject.getString("name"));
						}
						if (bowlerJsonObject.has("madiens")) {
							bowler.setMadiens(bowlerJsonObject.getInt("madiens"));
						}
						if (bowlerJsonObject.has("runs")) {
							bowler.setBowlerRuns(bowlerJsonObject.getInt("runs"));
						}
						if (bowlerJsonObject.has("wickets")) {
							bowler.setBowlerWickets(bowlerJsonObject.getInt("wickets"));
						}
						if (bowlerJsonObject.has("name")) {
							bowler.setBowlerName(bowlerJsonObject.getString("name"));
						}
						if (bowlerJsonObject.has("overs")) {
							bowler.setBowlerOvers(bowlerJsonObject.getDouble("overs"));
						}

						bowerList.add(bowler);
					}
					innings.setBowler_info(bowerList);
					System.out.println("phaniBowllistSize"+bowerList.size());
				}
			}else{
				System.out.println("phani no bowl info");
			}

		}
		catch (JSONException e) {
			// e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return innings;
	}

}
