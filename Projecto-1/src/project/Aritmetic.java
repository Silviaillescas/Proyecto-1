package project;

public class Arithmetic implements IAritmetic{

	@Override
	public float sum(float unit1, float unit2) {
		return unit1+unit2;
	}

	@Override
	public float substract(float unit1, float unit2) {
		return unit1-unit2;
	}

	@Override
	public float multiply(float unit1, float unit2) {
		return unit1*unit2;
	}

	@Override
	public float divide(float unit1, float unit2) {
		return unit1/unit2;
	}
}
