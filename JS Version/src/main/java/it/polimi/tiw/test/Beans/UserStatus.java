package it.polimi.tiw.test.Beans;

public enum UserStatus {
	TEACHER(0), STUDENT(1) ;

	private final int i;
	
	UserStatus(int i) {
		this.i=i;
	}
	
	public UserStatus getStatusFromInt() {
		switch (i) {
		case 0:
			return UserStatus.TEACHER;
		case 1:
			return UserStatus.STUDENT;
		}
		return null;
	}
	
	public static UserStatus getStatusFromInt(int value) {
		switch (value) {
		case 0:
			return UserStatus.TEACHER;
		case 1:
			return UserStatus.STUDENT;
		}
		return null;
	}
	

}
