package hu.laborreg.server.student;

import static org.junit.Assert.*;

import org.junit.Test;

public class StudentTest
{
	private Student s1;
	private Student s2;
	
	@Test
	public void basicAttributesTest()
	{
		s1 = new Student("abcdef".toUpperCase());
		s2 = new Student("a123bcd".toLowerCase());
		
		assertEquals("abcdef".toUpperCase(),s1.getNeptunCode());
		assertEquals("a123bcd".toUpperCase(),s2.getNeptunCode());
	}
}
