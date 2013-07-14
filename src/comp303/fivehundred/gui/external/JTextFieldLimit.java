package comp303.fivehundred.gui.external;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Code by balmark http://www.daniweb.com/software-development/java/threads/263964/length-of-a-jtextfield
 * 
 * Creates a document with a limited number of chars.
 *
 */
@SuppressWarnings("serial")
public class JTextFieldLimit extends PlainDocument
{
	private int limit;

	public JTextFieldLimit(int limit)
	{
		super();
		this.limit = limit;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
	{
		if (str == null)
		{
			return;
		}
		if ((getLength() + str.length()) <= limit)
		{
			super.insertString(offset, str, attr);
		}
	}
}