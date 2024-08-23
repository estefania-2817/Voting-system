package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

import data_structures.SinglyLinkedList;
import interfaces.List;

public class Election {

		private String candidates_filename; 
		private String ballot_filename;    
		private String winner = " ";
		private int winnerCount = 0;
		private List<Candidate> candidateList = new SinglyLinkedList<>();
		private List<Ballot> ballotList = new SinglyLinkedList<>();
		private List<String> candidatesEliminated = new SinglyLinkedList<>();

		private int round = 1;

	
	/* Constructor that implements the election logic using the files candidates.csv
	and ballots.csv as input. (Default constructor) */	
		
	
	/**
	 * Creates a new Election object reading the excel sheets that are located in the inputFiles folder 
	 * inside the project and creates a candidate list and a ballot list.
	 */
	public Election() {
		this.candidates_filename = "inputFiles/candidates.csv";
		this.ballot_filename = "inputFiles/ballots.csv";
		
		//Just making the list of candidates, later make them into an array
        try (BufferedReader br = new BufferedReader(new FileReader(candidates_filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("/"); 
                
                String fullLine;
                for(int i = 0; i < values.length; i++) {
                	fullLine = values[i];
                	candidateList.add(new Candidate(fullLine));
                } 
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
		
		//Just making the list of ballots, later make them into an array
        try (BufferedReader br = new BufferedReader(new FileReader(ballot_filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(" "); // Split the line by comma
                
                String fullLine;
                for(int i = 0; i < values.length; i++) {
                	fullLine = values[i];
                	ballotList.add(new Ballot(fullLine,candidateList));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
                
	}
	
	/* Constructor that receives the name of the candidate and ballot files and applies
	the election logic. Note: The files should be found in the input folder. */
	
	
	/**
	 * Creates a new Election object reading the excel sheets in the parameter and 
	 * creates a candidate list and a ballot list.
	 * @param candidatesFilename
	 * @param ballotFilename
	 */
	public Election(String candidatesFilename, String ballotFilename) {
		this.candidates_filename = "inputFiles/"+candidatesFilename;
		this.ballot_filename = "inputFiles/"+ballotFilename;
		
		//Just making the list of candidates, later make them into an array
        try (BufferedReader br = new BufferedReader(new FileReader(candidates_filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("/"); 
                
                String fullLine;
                for(int i = 0; i < values.length; i++) {
                	fullLine = values[i];
                	candidateList.add(new Candidate(fullLine));
                } 
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
		
		//Just making the list of ballots, later make them into an array
        try (BufferedReader br = new BufferedReader(new FileReader(ballot_filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(" "); // Split the line by comma
                
                String fullLine;
                for(int i = 0; i < values.length; i++) {
                	fullLine = values[i];
                	ballotList.add(new Ballot(fullLine,candidateList));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        
	}
	
	
	/**
	 *  Counts the votes in all the ballots in the ballot list, if a candidate has more than 50% of
	 *  the ballot votes then the win, if not it looks for who has the lowest count and eliminates them 
	 *  by setting their isActive to false. If two candidates have the least amount of votes, then they
	 *  are eliminated by their least amount of second rank.
	 *  Loops until a winner is found.
	 */
	public void countVotes() {
		
		while(winner == " ") {
			
			int indexOfCandidate = -1;
			int indexOfCandidate2 = -1;
			int winningNumber = this.getTotalValidBallots()/2;
			int minVotes = this.getTotalValidBallots();
			int minVotes2 = this.getTotalValidBallots();
			int minVotesCurrentCandidate = -1;
			int minVotesCurrentCandidate2 = -1;
			
			for(int i = 0; i < candidateList.size(); i++) {
				if(candidateList.get(i).isActive() == true) {
					minVotesCurrentCandidate2 = -1;
					int maxVotesCurrent = 0;
					int minVotesCurrent = 0;
					for(Ballot value : ballotList) {
						if(value.getBallotType() == 0) {
							if(value.getRankByCandidate(candidateList.get(i).getId()) == 1) {
								maxVotesCurrent++;
								minVotesCurrent++;
							}
						}
					}

					//winner found before elimination round
					if(maxVotesCurrent > winningNumber) {
						winner = candidateList.get(i).getName();
						winnerCount = maxVotesCurrent;
						break;
					}
					
					if(minVotes != this.getTotalValidBallots() && minVotesCurrent == minVotes) { 
						minVotes2 = minVotesCurrent;    
						minVotesCurrentCandidate2 = candidateList.get(i).getId();
						indexOfCandidate2 = i;
						//System.out.println("hi2");
						break;
					}
					
					if(minVotesCurrent < minVotes) {  
						minVotes = minVotesCurrent;
						minVotesCurrentCandidate = candidateList.get(i).getId();
						indexOfCandidate = i;
					}

				}
			}
						
		// only one person has least amount of votes	
			if(minVotesCurrentCandidate2 == -1 && indexOfCandidate != -1 && winner == " ") {
				
				candidateList.get(indexOfCandidate).setActive(false);
				String eliminatedCandName = candidateList.get(indexOfCandidate).getName();
				String minVotesString = Integer.toString(minVotes);
				candidatesEliminated.add(eliminatedCandName+"-"+minVotesString);
				
				for(Ballot value : ballotList) {
					if(value.getBallotType() == 0) {
						if(value.getRankByCandidate(minVotesCurrentCandidate) == 1) {
							value.eliminate(minVotesCurrentCandidate);
						}
						for(int i = 0; i < candidateList.size(); i++) {
							int tempID = candidateList.get(i).getId();
							boolean tempActive = candidateList.get(i).isActive();
							if(tempActive == false && value.getCandidateByRank(1) == tempID) {
								value.eliminate(tempID);
							}
						}
					}
				}
				
			//two people tied for least
			} else {
				int maxVoteRank2Cand = 0;
				int maxVoteRank2Cand2 = 0;
				//find who has least amount of rank 2 to eliminate
				for(Ballot value : ballotList) {
					if(value.getBallotType() == 0) {
						if(value.getRankByCandidate(minVotesCurrentCandidate) == 2) {
							maxVoteRank2Cand++;
						}
						if(value.getRankByCandidate(minVotesCurrentCandidate2) == 2) {
							maxVoteRank2Cand2++;
	
						}
					}
				}
				//if second person has least amount of 2 rank elimination
				if(maxVoteRank2Cand2 > maxVoteRank2Cand && winner == " ") {
					
					candidateList.get(indexOfCandidate).setActive(false);
					String eliminatedCandName = candidateList.get(indexOfCandidate).getName();
					String minVotesString = Integer.toString(minVotes);
					candidatesEliminated.add(eliminatedCandName+"-"+minVotesString);
					
					for(Ballot value : ballotList) {
						if(value.getBallotType() == 0) {
							if(value.getRankByCandidate(minVotesCurrentCandidate) == 1) {
								value.eliminate(minVotesCurrentCandidate);
							}
							for(int i = 0; i < candidateList.size(); i++) {
								int tempID = candidateList.get(i).getId();
								boolean tempActive = candidateList.get(i).isActive();
								if(tempActive == false && value.getCandidateByRank(1) == tempID) {
									value.eliminate(tempID);
								}
							}
						}
					}
					
				//if first person has least amount of 2 rank elimination
				} else if(maxVoteRank2Cand2 < maxVoteRank2Cand && winner == " "){
					
					candidateList.get(indexOfCandidate2).setActive(false);
					String eliminatedCandName = candidateList.get(indexOfCandidate2).getName();
					String minVotesString = Integer.toString(minVotes2);
					candidatesEliminated.add(eliminatedCandName+"-"+minVotesString);
					
					for(Ballot value : ballotList) {
						if(value.getBallotType() == 0) {
							if(value.getRankByCandidate(minVotesCurrentCandidate2) == 1) {
								value.eliminate(minVotesCurrentCandidate2);
							}
							for(int i = 0; i < candidateList.size(); i++) {
								int tempID = candidateList.get(i).getId();
								boolean tempActive = candidateList.get(i).isActive();
								if(tempActive == false && value.getCandidateByRank(1) == tempID) {
									value.eliminate(tempID);
								}
							}
						}
					}
				}
			}
		} //End of while loop
	}
		
	/**
	 * Calls the countVotes method and creates the output file.
	 * @return winner
	 */
	public String getWinner() {
		countVotes();
		String winnerCountString = Integer.toString(winnerCount);
		String winnerFileName = winner.replace(" ", "_") + winnerCountString;
		try {
		    BufferedWriter writer = new BufferedWriter(new FileWriter("outputFiles/"+winnerFileName));
		    writer.write("Number of ballots: " + this.getTotalBallots());
		    writer.newLine();
		    writer.write("Number of blank ballots: " + this.getTotalBlankBallots());
		    writer.newLine();
		    writer.write("Number of invalid ballots: " + this.getTotalInvalidBallots());
		    writer.newLine();
		    for(int i = 0; i < candidatesEliminated.size(); i++) {
		    	String fullLine = candidatesEliminated.get(i);
		    	int indexOfHyphen = fullLine.indexOf('-');
		    	String candName = fullLine.substring(0, indexOfHyphen);
		    	String candVotes = fullLine.substring(indexOfHyphen+1, fullLine.length());
		    	writer.write("Round " + (i+1) + ": " + candName + " was eliminated with " + candVotes + " #1's" );
			    writer.newLine();
		    }
		    writer.write("Winner: " + winner + " wins with " + winnerCount +" #1's");

		    writer.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return winner;
	}
	
	
	/**
	 * Returns the total size of the ballots submitted by getting the ballot list size
	 * @return totalBallots
	 */
	public int getTotalBallots() {
		int totalBallots = ballotList.size();

		return totalBallots;
	}
	
	/**
	 * Returns the total amount of invalid ballots by iterating over the ballot list
	 * an counting the ones that are invalid
	 * @return totalInvalidBallots
	 */
	public int getTotalInvalidBallots() {
		int totalInvalidBallots = 0;
		for(int i = 0; i < ballotList.size(); i++) {
			if(ballotList.get(i).getBallotType() == 2) {
				totalInvalidBallots++;
			}
		}
		return totalInvalidBallots;
	}
	
	/**
	 * 	Returns the total amount of blank ballots by iterating over the ballot list
	 * an counting the ones that are blank
	 * @return totalBlankBallots
	 */
	public int getTotalBlankBallots() {
		int totalBlankBallots = 0;
		for(int i = 0; i < ballotList.size(); i++) {
			if(ballotList.get(i).getBallotType() == 1) {
				totalBlankBallots++;
			}
		}
		return totalBlankBallots;
	}
	
	/**
	 * 	Returns the total amount of valid ballots  by iterating over the ballot list
	 * an counting the ones that are valid
	 * @return totalValidBallots
	 */
	public int getTotalValidBallots() {
		int totalValidBallots = 0;
		for(int i = 0; i < ballotList.size(); i++) {
			if(ballotList.get(i).getBallotType() == 0) {
				totalValidBallots++;
			}
		}
		return totalValidBallots;
	}
	
	/**
	 * Returns the list of names for the eliminated candidates with the numbers of 1s 
	 * they had and are in the order of their elimination
	 * @return candidatesEleminated
	 */
	public List<String> getEliminatedCandidates() {
		return candidatesEliminated;
	}
	
	/**
	* Prints all the general information about the election as well as a
	* table with the vote distribution.
	* Meant for helping in the debugging process.
	*/
	public void printBallotDistribution() {
		 System.out.println("Total ballots:" + getTotalBallots());
		 System.out.println("Total blank ballots:" + getTotalBlankBallots());
		 System.out.println("Total invalid ballots:" + getTotalInvalidBallots());
		 System.out.println("Total valid ballots:" + getTotalValidBallots());
		 System.out.println(getEliminatedCandidates());
		 
		 System.out.println();
		 System.out.print("Ballot Numer: ");
		 System.out.print("\t");
		 for(Ballot value : ballotList) {
			 System.out.print("\t" + value.getBallotNum());
		 }
		 System.out.println();
		 System.out.println();
		 for(Candidate value : candidateList) {
			 System.out.print(value.getName());
			 System.out.print("\t");
			 for(Ballot value2 : ballotList) {
				 System.out.print("\t" + value2.getRankByCandidate(value.getId()));
			 }
			 System.out.println();
			 System.out.println();
		}
	}
	
	/**
	 * Returns a lambda expression that makes a string with the name of the candidate and the
	 * round they were eliminated
	 * @return lambda expression
	 */
	public Function<Candidate, String> candidateDetails() {
        return (c) ->
            c.getName() + " was eliminated in round " + round;
    }
	
	/**
	 * Uses the lambda function in the parameter to print the name and round in which a 
	 * candidate was eliminated, if they were not eliminated it returns -1
	 * @param function
	 */
	public void printCandidates(Function<Candidate, String> function) {
		int size = candidateList.size();
		int counter = 0;
		while (counter < size) {
	        for (int j = 0; j < candidateList.size();) {
	    		boolean found = false;
	        	for(int i = 0; i < candidatesEliminated.size(); i++) {
	        		String eliminatedCandidate = candidatesEliminated.get(i).substring(0, candidatesEliminated.get(i).length()-2);
	        		String candidateName = candidateList.get(j).getName();
	        		if(candidateName.equals(eliminatedCandidate)) {
	        			round = i + 1;
	        			counter++;
	        			String output = function.apply(candidateList.get(j));
	    	            System.out.println(output);
	    	            found = true;
	    	            i = 0;
	    	            j++;
	    	            break;
	        		}
	        	}
	        	if(found == false) {
	        		round = -1;
        			String output = function.apply(candidateList.get(j));
    	            System.out.println(output);
        			counter++;
        			j++;
	        	}
	        }
		}
    }
	
	public static void main(String[] args) {
		
	    Election election = new Election();
	    election.printBallotDistribution();
	    System.out.println("Winner: "+ election.getWinner());
	    election.printBallotDistribution();
	    for(String value : election.getEliminatedCandidates()) {
	    	System.out.println(value);
	    }
	    
	    System.out.println();
	    
	    Election election2 = new Election("candidates.csv","ballots2.csv");
	    election2.printBallotDistribution();
	    System.out.println("Winner: "+ election2.getWinner());
	    election2.printBallotDistribution();
	    for(String value : election2.getEliminatedCandidates()) {
	    	System.out.println(value);
	    }
	    
	    System.out.println();
	    System.out.println("BONUS METHOD 1");
	    
	    System.out.println();
        election.printCandidates(election.candidateDetails());
        
	    System.out.println();
        election2.printCandidates(election2.candidateDetails());


	}
}
