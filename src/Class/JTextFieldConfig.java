package Class;

import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class JTextFieldConfig extends PlainDocument {
    private int limit;
    private boolean upper;

    public JTextFieldConfig(int limit, boolean upper) {
        super();
        this.limit = limit;
        this.upper = upper;
    }

    public void insertString(int offset, String  str, AttributeSet attr ) throws BadLocationException {
        if (str == null) return;

        if ((getLength() + str.length()) <= limit) {
            if (upper) {
                super.insertString(offset, str.toUpperCase(), attr);
            } else {
                super.insertString(offset, str, attr);
            }
        }
    }

}