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
		s1 = new Student("abcdef","Bela");
		s2 = new Student("a123bcd","Geza");
		
		assertEquals("abcdef",s1.getNeptunCode());
		assertEquals("Bela",s1.getName());
		assertEquals("a123bcd",s2.getNeptunCode());
		assertEquals("Geza",s2.getName());
	}
}
