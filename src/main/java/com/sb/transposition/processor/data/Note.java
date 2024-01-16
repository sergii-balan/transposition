package com.sb.transposition.processor.data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Note implements Comparable<Note> {
	private static final byte D_B12 = 12;
	private static final byte D_B10 = 10;
	
	@Min(-3) @Max(5)
	private int octave;
	@Min(1) @Max(12)
	private int number;
	
	public static Note from(byte[] note) {
		return new Note(note[0], note[1]);
	}
	
	public Note transpose(int shift) {
		
		int b12 = to12(octave, number) + to12(shift);
		int[] note = toNote(b12);
		
		this.octave = note[0];
		this.number = note[1];
		return this;
	}
	
	public byte[] asByteArray() {
		return new byte[] {(byte) octave, (byte) number};
	}
	
	public int[] asIntArray() {
		return new int[] {octave, number};
	}
	
	public boolean isGreaterThan(Note note) {
		return this.compareTo(note) > 0;
	}
	
	public boolean isLessThan(Note note) {
		return this.compareTo(note) < 0;
	}
	
	@Override
	public int compareTo(Note note) {
		if (Objects.isNull(note)) {
			return 1;
		}
		
		if (this == note) {
			return 0;
		}
		
		if (this.octave > note.getOctave()) {
			return 1;
		}
		
		if (this.octave < note.getOctave()) {
			return -1;
		}
		
		if (this.number > note.getNumber()) {
			return 1;
		}
		
		if (this.number < note.getNumber()) {
			return -1;
		}
		
		return 0;
	}
	
	private static int to12(int octave, int note) {
		if (octave >= 0) {
			return octave * D_B12 + note;
		}
		
		int b = (octave + 1) * D_B12;
		int r = note - D_B12 - 1;
		return b + r;
	}
	
	private static int to12(int shift) {
		return -D_B10 < shift && shift < D_B10 ? shift : shift / D_B12 * D_B10 + shift % D_B12;
	}
	
	private static int[] toNote(int b12) {
		if (b12 >= 0) {
			return fromPositive(b12);
		}
		
		return fromNegative(b12);
	}
	
	private static int[] fromPositive(int b12) {
		int h = b12 / D_B12;
		int l = b12 % D_B12;
		
		if (l == 0) {
			h -= 1;
			l = 12;
		}
		
		return new int[] {h, l};
	}
	
	private static int[] fromNegative(int b12) {
		return new int[] {b12 / D_B12 - 1, b12 % D_B12 + D_B12 + 1};
	}

}
