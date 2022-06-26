package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Team> listAllTeams(){
		String sql = "SELECT * FROM Teams";
		List<Team> result = new ArrayList<Team>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
				result.add(team);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID " 
				+ " order by m.MatchID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Action> getVertici(Match m){
		String sql = "select a.`PlayerID`,a.MatchID,a.TeamID,a.Starts,a.Goals,a.TimePlayed,a.RedCards,a.YellowCards,a.TotalSuccessfulPassesAll,TotalUnsuccessfulPassesAll,a.Assists,a.TotalFoulsConceded,a.Offsides "
				+ "from Matches m,Actions a "
				+ "where m.`MatchID`=a.`MatchID` and m.`MatchID`= ?";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, m.getMatchID());
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("TotalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Adiacenza> getArchi(Match m){
		String sql ="select a.`PlayerID`as id1,a2.`PlayerID` as id2,(((a.`TotalSuccessfulPassesAll`+a.`Assists`)/a.`TimePlayed`)-((a2.`TotalSuccessfulPassesAll`+a2.`Assists`)/a2.`TimePlayed`))as peso, "
				+ "a.MatchID,a.TeamID,a.Starts,a.Goals,a.TimePlayed,a.RedCards,a.YellowCards,a.TotalSuccessfulPassesAll,a.TotalUnsuccessfulPassesAll,a.Assists,a.TotalFoulsConceded,a.Offsides, "
				+ "a2.MatchID,a2.TeamID,a2.Starts,a2.Goals,a2.TimePlayed,a2.RedCards,a2.YellowCards,a2.TotalSuccessfulPassesAll,a2.`TotalUnsuccessfulPassesAll`,a2.Assists,a2.TotalFoulsConceded,a2.Offsides "
				
				+ "from Matches m,Actions a,Actions a2 "
				+ "where m.`MatchID`=a.`MatchID` and m.`MatchID`=? and a2.`MatchID`=a.`MatchID`and  a.TeamID>a2.`TeamID` "
				+ "group by a.`PlayerID`,a2.`PlayerID`,"
				+ "a.MatchID,a.TeamID,a.Starts,a.Goals,a.TimePlayed,a.RedCards,a.YellowCards,a.TotalSuccessfulPassesAll,a.TotalUnsuccessfulPassesAll,a.Assists,a.TotalFoulsConceded,a.Offsides,  "
				+ "a2.MatchID,a2.TeamID,a2.Starts,a2.Goals,a2.TimePlayed,a2.RedCards,a2.YellowCards,a2.TotalSuccessfulPassesAll,a.TotalUnsuccessfulPassesAll,a2.Assists,a2.TotalFoulsConceded,a2.Offsides ";
		
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, m.getMatchID());
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Action a1 = new Action(res.getInt("id1"),res.getInt("a.MatchID"),res.getInt("a.TeamID"),res.getInt("a.Starts"),res.getInt("a.Goals"),
						res.getInt("a.TimePlayed"),res.getInt("a.RedCards"),res.getInt("a.YellowCards"),res.getInt("a.TotalSuccessfulPassesAll"),res.getInt("a.TotalUnsuccessfulPassesAll"),
						res.getInt("a.Assists"),res.getInt("a.TotalFoulsConceded"),res.getInt("a.Offsides"));
				Action a2 = new Action(res.getInt("id2"),res.getInt("a2.MatchID"),res.getInt("a2.TeamID"),res.getInt("a2.Starts"),res.getInt("a2.Goals"),
						res.getInt("a2.TimePlayed"),res.getInt("a2.RedCards"),res.getInt("a2.YellowCards"),res.getInt("a2.TotalSuccessfulPassesAll"),res.getInt("a2.TotalUnsuccessfulPassesAll"),
						res.getInt("a2.Assists"),res.getInt("a2.TotalFoulsConceded"),res.getInt("a2.Offsides"));
				
				result.add(new Adiacenza(a1,a2,res.getDouble("peso")));
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
