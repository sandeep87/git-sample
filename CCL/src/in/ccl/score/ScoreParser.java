/*package in.ccl.score;

import in.ccl.ui.ScoreBoardActivity;
import in.ccl.util.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

public class ScoreParser {

	public void ParseJsonScoreBoard (Context ctx) {
		boolean isSecondInnings = false;
		StringBuilder builder = new StringBuilder();
		File file = new File(Environment.getExternalStorageDirectory() + "/score_board");
		if (file.exists()) {

			try {
				FileInputStream inputStream = new FileInputStream(file);
				if (inputStream != null) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					if (reader != null) {
						String line;
						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}
					}
				}
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();

			}
		}
		try {
			ArrayList <Innings> firstInnings = new ArrayList <Innings>();
			ArrayList <Innings> secondInnings = new ArrayList <Innings>();
			// System.out.println("" + builder.toString().trim());
			JSONObject mainJsonObject = new JSONObject(builder.toString().trim());

			String firstInn = mainJsonObject.getString("innings1");
			// System.out.println("firstInn" + firstInn);

			JSONObject firstInningsObject = new JSONObject(firstInn.trim());

			Innings firstInningsItems = parseInningsData(firstInningsObject);

			firstInnings.add(firstInningsItems);
			if (mainJsonObject.has("innings2")) {
				isSecondInnings = true;

				String secondInn = mainJsonObject.getString("innings2");

				JSONObject secondInningsObject = new JSONObject("innings2");

				Innings secondInningsItems = parseInningsData(secondInningsObject);

				secondInnings.add(secondInningsItems);
			}
			else {
				isSecondInnings = false;
			}
			if (isSecondInnings) {
				if (firstInnings != null && firstInnings.size() > 0 && secondInnings != null && secondInnings.size() > 0) {
					Intent scoreBoardIntent = new Intent(ctx, ScoreBoardActivity.class);
					scoreBoardIntent.putParcelableArrayListExtra(Constants.FIRST_INNINGS_KEY, firstInnings);
					scoreBoardIntent.putParcelableArrayListExtra(Constants.SECOND_INNINGS_KEY, secondInnings);
					scoreBoardIntent.putExtra("secondInnsStatus", isSecondInnings);
					ctx.startActivity(scoreBoardIntent);
				}

			}
			else {
				if (firstInnings != null && firstInnings.size() > 0) {
					Intent scoreBoardIntent = new Intent(ctx, ScoreBoardActivity.class);
					scoreBoardIntent.putParcelableArrayListExtra(Constants.FIRST_INNINGS_KEY, firstInnings);
					scoreBoardIntent.putParcelableArrayListExtra(Constants.SECOND_INNINGS_KEY, secondInnings);
					scoreBoardIntent.putExtra(Constants.SECOND_INNINGS_STATUS, isSecondInnings);
					ctx.startActivity(scoreBoardIntent);
				}

			}

		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Innings parseInningsData (JSONObject inningsObject) {
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
				}

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
				}
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

	public LiveScore parseLiveScore () {
		LiveScore liveScore = null;
		StringBuilder builder = new StringBuilder();
		File file = new File(Environment.getExternalStorageDirectory() + "/livescore.txt");
		if (file.exists()) {

			try {
				FileInputStream inputStream = new FileInputStream(file);
				if (inputStream != null) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					if (reader != null) {
						String line;
						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}
					}
				}
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();

			}
		}

		JSONObject object;
		try {
			object = new JSONObject(builder.toString());
			liveScore = new LiveScore();
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
			if (object.has("current_score")) {
				JSONObject currentScoreObject = object.getJSONObject("current_score");
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

			if (object.has("bastman1")) {

				JSONObject strikerObject = object.getJSONObject("bastman1");
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
			if (object.has("bastman2")) {

				JSONObject nonStrikerObject = object.getJSONObject("bastman2");
				if (nonStrikerObject.has("name")) {
					liveScore.setNonstriker_name(nonStrikerObject.getString("name"));
				}
				if (nonStrikerObject.has("score")) {
					liveScore.setNonstriker_score(nonStrikerObject.getInt("score"));
				}
				if (nonStrikerObject.has("balls")) {

					liveScore.setNonstriker_balls(nonStrikerObject.getInt("balls"));
				}
				if (nonStrikerObject.has("strikerrate")) {

					liveScore.setNonstriker_strikerate(nonStrikerObject.getDouble("strikerrate"));
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
				if (currentBowlerObject.has("madiens")) {

					liveScore.setCurrent_bowler_madiens(currentBowlerObject.getInt("madiens"));
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
				if (previousBowlerObject.has("madiens")) {

					liveScore.setPrevious_bowler_madiens(previousBowlerObject.getInt("madiens"));
				}
				if (previousBowlerObject.has("runs")) {

					liveScore.setPrevious_bowler_runs(previousBowlerObject.getInt("runs"));
				}
				if (previousBowlerObject.has("wickets")) {

					liveScore.setPrevious_bowler_wickets(previousBowlerObject.getInt("wickets"));
				}
			}
		}
		catch (org.json.JSONException e) {
			e.printStackTrace();
		}
		return liveScore;

	}

	public ArrayList <MatchesResponse> liveMatchesRequest () {
		StringBuilder matchresponsebuilder = new StringBuilder();
		File file = new File(Environment.getExternalStorageDirectory() + "/matchrequest");
		if (file.exists()) {

			try {
				FileInputStream inputStream = new FileInputStream(file);
				if (inputStream != null) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					if (reader != null) {
						String line;
						while ((line = reader.readLine()) != null) {
							matchresponsebuilder.append(line);
						}
					}
					// parseLiveScore(builder);
				}
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();

			}
		}

		ArrayList <MatchesResponse> matchesResponseList = null;

		JSONObject responseObject = null;
		try {
			responseObject = new JSONObject(matchresponsebuilder.toString());
			if (responseObject.has("data")) {
				JSONArray matchesList = responseObject.getJSONArray("data");
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

}
*/