package it.polimi.tiw.test.Beans;

public enum EvalStatus {
	NOT_INSERTED(0), INSERTED(1), PUBLISHED(2), REFUSED(3), VERBALIZED(4);

	private final int i;
	EvalStatus(int i) {
		this.i=i;
	}
	
	public EvalStatus getEvalStatusFromInt() {
		switch (i) {
		case 0:
			return EvalStatus.NOT_INSERTED;
		case 1:
			return EvalStatus.INSERTED;
		case 2:
			return EvalStatus.PUBLISHED;
		case 3:
			return EvalStatus.REFUSED;
		case 4:
			return EvalStatus.VERBALIZED;
		}
		return null;
	}
	

	public static EvalStatus getEvalStatusFromInt(int value) {
		switch (value) {
		case 0:
			return EvalStatus.NOT_INSERTED;
		case 1:
			return EvalStatus.INSERTED;
		case 2:
			return EvalStatus.PUBLISHED;
		case 3:
			return EvalStatus.REFUSED;
		case 4:
			return EvalStatus.VERBALIZED;
		}
		return null;
	}
		
	public int getValue() {
		switch(this) {
		case NOT_INSERTED:
			return 0;
		case INSERTED:
			return 1;
		case PUBLISHED:
			return 2;
		case REFUSED:
			return 3;
		case VERBALIZED:
			return 4;
		}
		return -1;
	}
	
	
}
