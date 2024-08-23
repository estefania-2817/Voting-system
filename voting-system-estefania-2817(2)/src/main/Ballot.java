package main;

import data_structures.ArrayList;
import data_structures.SinglyLinkedList;
import interfaces.List;

public class Ballot {

		private String line;
		private List<Candidate> candidates;
		private List<Integer> candWithRank = new SinglyLinkedList<>();
			
	/**
	 * Creates a ballot based on the line in the parameter, and receives the list of 
	 * candidates for the ballot in this election
	 * @param line
	 * @param candidates
	 */
	public Ballot(String line, List<Candidate> candidates) {
		this.line = line;
		this.candidates = candidates;
		
		for(int i = 0; i < line.length()-1; i++) {
			for(int j = i+1; j < line.length(); j++) {
				if(line.charAt(i) == ',' && line.charAt(j) == ':') {
					//is a candidate
					String candidateString = line.substring(i+1, j);
					int candidateNum = Integer.parseInt(candidateString);
					candWithRank.add(candidateNum);
					i = j;
					j = i+1;
				}
				if(line.charAt(i) == ':' && line.charAt(j) == ',') {
					//is a rank
					String rankString = line.substring(i+1, j);
					int rankNum = Integer.parseInt(rankString);
					candWithRank.add(rankNum);
					i = j;
					j = i+1;
				}
				if(line.charAt(i) == ':' && j == line.length()-1) {
					//is a rank
					String rankString = line.substring(i+1, line.length());
					int rankNum = Integer.parseInt(rankString);
					candWithRank.add(rankNum);
					i = j;
					j = i+1;
				}
			}
		}
	}
		
	/**
	 * Returns the ballot number from the line of the parameter in the constructor
	 * @return ballotNum
	 */
	public int getBallotNum() {
        String ballot;
        int ballotNum;
        int indexOfComa = 0;
        for(int j = 0; j < line.length(); j++) {
        	indexOfComa = line.length();
        	if(line.charAt(j) == ',') {
        		indexOfComa = j;
        		break;
        	}
        } 
        ballot = line.substring(0, indexOfComa);
        ballotNum = Integer.parseInt(ballot);
		return ballotNum;
	}
	
	/**
	 * Returns the rank for the candidate in the parameter, if no rank is available it returns -1
	 * @param candidateID
	 * @return rank
	 */
	public int getRankByCandidate(int candidateID) {
		int rank = -1;
		for(int i = 0; i < candWithRank.size()-1; i+=2) {
			if(candWithRank.get(i) == candidateID) {
				rank = candWithRank.get(i+1);
				return rank;
			}
		}
		return rank;                              
	}
	
	/**
	 * Returns the candidate with that rank, if no candidate is available it returns -1
	 * @param rank
	 * @return iD
	 */
	public int getCandidateByRank(int rank) {
		int iD = -1;
		for(int i = 1; i < candWithRank.size(); i+=2) {
			if(candWithRank.get(i) == rank) {
				iD = candWithRank.get(i-1);
				return iD;
			}
		}
		return iD; 
	}
	
	/**
	 * Eliminates the candidate with the given id and returns true, if the given candidate is 
	 * not found it returns false
	 * @param candidateId
	 * @return boolean value
	 */
	public boolean eliminate(int candidateId) {
		int candidateToDelete = -1;
		int newRank = -1;
		for(int i = 0; i < candWithRank.size()-1; i+=2) {
			if(candWithRank.get(i) == candidateId) {
				candidateToDelete = i;
				newRank = candWithRank.get(i+1);
				break;
			}
		}
		
	    if (candidateToDelete == -1) {
	        return false; 
	    }
		
	    candWithRank.remove(candidateToDelete);
	    candWithRank.remove(candidateToDelete);
	    
		for(int i = candidateToDelete+1; i < candWithRank.size(); i+=2) {
			candWithRank.set(i, newRank);
			newRank++;
		}
				
		return true;
	}
	
	/**
	 * Returns an integer that indicates if the ballot is: 0 – valid, 1 – blank or 2 - invalid
	 * @return type
	 */
	public int getBallotType() {
		
		int type = 0;

		//checks if ballot is blank, no info after ballot number
		boolean commaFound = false;
		for(int i = 0; i < this.line.length(); i++) {
			if(line.charAt(i) == ',') {
				commaFound = true;
				break;
			}
		}
		if(!commaFound) {
			type = 1;
			return type;
		}

		//checks repeated candidates, comparing even indexes 
		for(int i = 0; i < candWithRank.size()-2; i+=2) {
			for(int j = i+2; j < candWithRank.size(); j+=2) {
				if(candWithRank.get(i) == candWithRank.get(j)) {
					type = 2;
					break;
				}
			}
		}
		
		//checks repeated rankings, comparing odd indexes 
		for(int i = 1; i < candWithRank.size()-2; i+=2) {
			for(int j = i+2; j < candWithRank.size(); j+=2) {
				if(candWithRank.get(i) == candWithRank.get(j)) {
					type = 2;
					break;
				}
			}
		}
						
		//checking skipped rank
		int size = candWithRank.size() / 2;
		List<Integer> ranks = new ArrayList<>(size); //put size
		for(int i = 1; i < candWithRank.size(); i+=2) {
			ranks.add(candWithRank.get(i));
		}
		//find minimum rank then find next value till counter equals size
		int numberOfRanks = ranks.size()-1; //substract bc the number of comparisons is one less
		int counter = 0;
		int minRank = ranks.get(0);
		for(int i = 1; i < ranks.size(); i++) {
			if(ranks.get(i) < minRank) {
				minRank = ranks.get(i);
			}
		}
		int comparativeValue = minRank;
		for(int i = 0; i < ranks.size(); i++) {
			if(ranks.get(i) == comparativeValue + 1) {
				comparativeValue = ranks.get(i);
				counter++;
				i = 0;
			}
		}
		if(numberOfRanks != counter) {
			type = 2;
		}
		
		return type;
	}
	
//	Inside the actual Ballot class, create a constructor such that you can pass the line of text from the
//	input file and then the constructor would take care of storing the information accordingly. You may
//	create as many additional methods as you deem necessary and/or helpful.
	
	public static void main(String[] args) {

		List<Candidate> candidates = new ArrayList<Candidate>(10);
		candidates.add(new Candidate("1,Ron Stoppable"));
		candidates.add(new Candidate("2,Kim Possible"));
		candidates.add(new Candidate("3,Rufus Stoppable"));
		candidates.add(new Candidate("4,Ben Tenison"));
		candidates.add(new Candidate("5,Timmy Turner"));
		candidates.add(new Candidate("6,Danny Fenton"));
		candidates.add(new Candidate("7,Tommy Pickles"));
		candidates.add(new Candidate("8,P Lankton"));
		candidates.add(new Candidate("9,Jimmy Neutron"));
		candidates.add(new Candidate("10,Luz Noceda"));
		Ballot ballot = new Ballot("2,7:1,5:2,1:3,9:4,4:5,2:6,8:7,6:8,10:9,3:10", candidates);
		System.out.println("Ballot Number: " + ballot.getBallotNum());
		System.out.println("Ballot Type: " + ballot.getBallotType()); //0
		System.out.println("CandidateByRank: " + ballot.getCandidateByRank(2)); // 5 
		System.out.println("CandidateByRank: " + ballot.getCandidateByRank(9)); // 10
		System.out.println("RankByCandidate: " + ballot.getRankByCandidate(7)); // 1
		System.out.println("RankByCandidate: " + ballot.getRankByCandidate(2)); // 6

		
		System.out.println();
		
		List<Candidate> candidates2 = new ArrayList<Candidate>(10);
		candidates2.add(new Candidate("1,Ron Stoppable"));
		candidates2.add(new Candidate("2,Kim Possible"));
		candidates2.add(new Candidate("3,Rufus Stoppable"));
		candidates2.add(new Candidate("4,Ben Tenison"));
		candidates2.add(new Candidate("5,Timmy Turner"));
		candidates2.add(new Candidate("6,Danny Fenton"));
		candidates2.add(new Candidate("7,Tommy Pickles"));
		candidates2.add(new Candidate("8,P Lankton"));
		candidates2.add(new Candidate("9,Jimmy Neutron"));
		candidates2.add(new Candidate("10,Luz Noceda"));
		Ballot ballot2 = new Ballot("2158,7:1,5:2,1:3,9:4,4:5,2:6,8:7,6:8,10:9,3:10", candidates);
		System.out.println("Ballot Number: " + ballot2.getBallotNum());
		System.out.println("Ballot type: " + ballot2.getBallotType()); // 0  done
		System.out.println("CandidateByRank: " + ballot2.getCandidateByRank(10)); // 3
		System.out.println("CandidateByRank: " + ballot2.getCandidateByRank(6)); // 2
		System.out.println("RankByCandidate: " + ballot2.getRankByCandidate(10)); // 9 
		System.out.println("RankByCandidate: " + ballot2.getRankByCandidate(1)); // 3

		System.out.println();
		
		List<Candidate> candidates3 = new ArrayList<Candidate>(10);
		candidates3.add(new Candidate("1,Ron Stoppable"));
		candidates3.add(new Candidate("2,Kim Possible"));
		candidates3.add(new Candidate("3,Rufus Stoppable"));
		candidates3.add(new Candidate("4,Ben Tenison"));
		candidates3.add(new Candidate("5,Timmy Turner"));
		candidates3.add(new Candidate("6,Danny Fenton"));
		candidates3.add(new Candidate("7,Tommy Pickles"));
		candidates3.add(new Candidate("8,P Lankton"));
		candidates3.add(new Candidate("9,Jimmy Neutron"));
		candidates3.add(new Candidate("10,Luz Noceda"));
		Ballot ballot3 = new Ballot("489,9:1,7:2,10:3,5:4", candidates2);
		System.out.println("Ballot Number: " + ballot3.getBallotNum());
		System.out.println("CandidateByRank: " + ballot3.getRankByCandidate(4)); //-1
		System.out.println("Ballot type: " + ballot3.getBallotType()); // 0  done
		System.out.println("CandidateByRank: " + ballot3.getCandidateByRank(3)); // 10
		System.out.println("CandidateByRank: " + ballot3.getCandidateByRank(6)); // -1
		System.out.println("RankByCandidate: " + ballot3.getRankByCandidate(7)); // 2
		System.out.println("RankByCandidate: " + ballot3.getRankByCandidate(3)); // -1

		System.out.println();
		
		List<Candidate> candidates4 = new ArrayList<Candidate>(10);
		candidates4.add(new Candidate("1,Ron Stoppable"));
		candidates4.add(new Candidate("2,Kim Possible"));
		candidates4.add(new Candidate("3,Rufus Stoppable"));
		candidates4.add(new Candidate("4,Ben Tenison"));
		candidates4.add(new Candidate("5,Timmy Turner"));
		candidates4.add(new Candidate("6,Danny Fenton"));
		candidates4.add(new Candidate("7,Tommy Pickles"));
		candidates4.add(new Candidate("8,P Lankton"));
		candidates4.add(new Candidate("9,Jimmy Neutron"));
		candidates4.add(new Candidate("10,Luz Noceda"));
		Ballot ballot4 = new Ballot("582,7:1,5:2,1:3,9:4,4:5,2:9", candidates);
		System.out.println("Ballot Number: " + ballot4.getBallotNum());
		System.out.println("Ballot type: " + ballot4.getBallotType()); // 2  done
		
		System.out.println();
		
		List<Candidate> candidates5 = new ArrayList<Candidate>(10);
		candidates5.add(new Candidate("1,Ron Stoppable"));
		candidates5.add(new Candidate("2,Kim Possible"));
		candidates5.add(new Candidate("3,Rufus Stoppable"));
		candidates5.add(new Candidate("4,Ben Tenison"));
		candidates5.add(new Candidate("5,Timmy Turner"));
		candidates5.add(new Candidate("6,Danny Fenton"));
		candidates5.add(new Candidate("7,Tommy Pickles"));
		candidates5.add(new Candidate("8,P Lankton"));
		candidates5.add(new Candidate("9,Jimmy Neutron"));
		candidates5.add(new Candidate("10,Luz Noceda"));
		Ballot ballot5 = new Ballot("1256,7:1,5:2,1:3,9:4,4:5,7:6", candidates);
		System.out.println("Ballot Number: " + ballot5.getBallotNum());
		System.out.println("Ballot type: " + ballot5.getBallotType()); // 2 done
		
		System.out.println();
		
		List<Candidate> candidates6 = new ArrayList<Candidate>(10);
		candidates6.add(new Candidate("1,Ron Stoppable"));
		candidates6.add(new Candidate("2,Kim Possible"));
		candidates6.add(new Candidate("3,Rufus Stoppable"));
		candidates6.add(new Candidate("4,Ben Tenison"));
		candidates6.add(new Candidate("5,Timmy Turner"));
		candidates6.add(new Candidate("6,Danny Fenton"));
		candidates6.add(new Candidate("7,Tommy Pickles"));
		candidates6.add(new Candidate("8,P Lankton"));
		candidates6.add(new Candidate("9,Jimmy Neutron"));
		candidates6.add(new Candidate("10,Luz Noceda"));
		Ballot ballot6 = new Ballot("1256,7:1,5:2,1:3,9:1,4:5,2:6", candidates);
		System.out.println("Ballot Number: " + ballot6.getBallotNum());
		System.out.println("Ballot type: " + ballot6.getBallotType()); // 2 done
		
		System.out.println();
		
		List<Candidate> candidates7 = new ArrayList<Candidate>(10);
		candidates7.add(new Candidate("1,Ron Stoppable"));
		candidates7.add(new Candidate("2,Kim Possible"));
		candidates7.add(new Candidate("3,Rufus Stoppable"));
		candidates7.add(new Candidate("4,Ben Tenison"));
		candidates7.add(new Candidate("5,Timmy Turner"));
		candidates7.add(new Candidate("6,Danny Fenton"));
		candidates7.add(new Candidate("7,Tommy Pickles"));
		candidates7.add(new Candidate("8,P Lankton"));
		candidates7.add(new Candidate("9,Jimmy Neutron"));
		candidates7.add(new Candidate("10,Luz Noceda"));
		Ballot ballot7 = new Ballot("1256", candidates);
		System.out.println("Ballot Number: " + ballot7.getBallotNum());
		System.out.println("Ballot type: " + ballot7.getBallotType()); // 1 done
		
		System.out.println();
		
		List<Candidate> candidates8 = new ArrayList<Candidate>(10);
		candidates8.add(new Candidate("1,Ron Stoppable"));
		candidates8.add(new Candidate("2,Kim Possible"));
		candidates8.add(new Candidate("3,Rufus Stoppable"));
		candidates8.add(new Candidate("4,Ben Tenison"));
		candidates8.add(new Candidate("5,Timmy Turner"));
		candidates8.add(new Candidate("6,Danny Fenton"));
		candidates8.add(new Candidate("7,Tommy Pickles"));
		candidates8.add(new Candidate("8,P Lankton"));
		candidates8.add(new Candidate("9,Jimmy Neutron"));
		candidates8.add(new Candidate("10,Luz Noceda"));
		Ballot ballot8 = new Ballot("126,7:1,5:2,1:3,9:4,6:5,2:6", candidates);
//		ballot8.eliminate(6);
//		System.out.println("Ballot Number: " + ballot8.getBallotNum());
//		System.out.println("RankByCandidate: " + ballot8.getRankByCandidate(6)); //-1
//		System.out.println("CandidateByRank: " + ballot8.getCandidateByRank(5)); //2
		ballot8.eliminate(1);
		System.out.println("Ballot Number: " + ballot8.getBallotNum());
		System.out.println("RankByCandidate: " + ballot8.getRankByCandidate(6)); //4
		System.out.println("CandidateByRank: " + ballot8.getCandidateByRank(5)); //2
		
//		List<Candidate> candidates9 = new ArrayList<Candidate>(10);
//		candidates9.add(new Candidate("1,Ron Stoppable"));
//		candidates9.add(new Candidate("2,Kim Possible"));
//		candidates9.add(new Candidate("3,Rufus Stoppable"));
//		candidates9.add(new Candidate("4,Ben Tenison"));
//		candidates9.add(new Candidate("5,Timmy Turner"));
//		candidates9.add(new Candidate("6,Danny Fenton"));
//		candidates9.add(new Candidate("7,Tommy Pickles"));
//		candidates9.add(new Candidate("8,P Lankton"));
//		candidates9.add(new Candidate("9,Jimmy Neutron"));
//		candidates9.add(new Candidate("10,Luz Noceda"));
//		Ballot ballot9 = new Ballot("126,7:6,5:4,1:7,9:3,6:8,2:5,3:1,4:2", candidates);
//		ballot9.eliminate(2);
//		System.out.println("CandidateByRank: " + ballot9.getCandidateByRank(5));

	}
}
