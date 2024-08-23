package main;

public class Candidate {
	/* Creates a Candidate from the line. The line will have the format
	ID#,candidate_name . */
		private String line;
		private int candidateId;
		private boolean isActive;
		private String iD;
		private String name;
		
	/**
	 *  Creates a new candidate object from the line in the excel sheet
	 *  It sets the default value of isActive variable to true
	 * @param line
	 */
	public Candidate(String line) {
		this.line = line;
		this.isActive = true;
	}
		
	/**
	 * Returns the candidate ID from the line of the parameter in the constructor
	 * @return candidateId
	 */
	public int getId() {
		int indexOfComma = 0;
    	for(int j = 0; j < line.length(); j++) {
        	if (line.charAt(j) == ',') {
        		indexOfComma = j;
        	}
    	}
        iD = line.substring(0, indexOfComma);
        candidateId = Integer.parseInt(iD);
		return candidateId;
	}
	
	/**
	 * Returns the value of the isActive variable from the constructor
	 * @return isActive
	 */
	public boolean isActive() {
		return this.isActive;
	}
		
	/**
	 * Receives the boolean value which we want to set the isActive variable to
	 * @param value
	 */
	public void setActive(boolean value) {
		this.isActive = value;
	}
		
	/**
	 * Returns the name of the candidate from the line in the parameter in the constructor
	 * @return name
	 */
	public String getName() {
		int indexOfComma = 0;
    	for(int j = 0; j < line.length(); j++) {
        	if (line.charAt(j) == ',') {
        		indexOfComma = j;
        	}
    	}
		name = line.substring(indexOfComma+1);
		return name;
	}
	public static void main(String[] args) {
		Candidate candidate = new Candidate("12,Ron Stoppable");
		System.out.println(candidate.getId());
		System.out.println(candidate.getName());
		System.out.println(candidate.isActive);
		candidate.setActive(false);
		System.out.println(candidate.isActive);

	}
}