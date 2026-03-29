package ui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;
import java.util.regex.Pattern;

/**
 * UI-layer input restrictions for employee forms.
 * Keeps the domain/persistence layer responsible for final validation via EmployeeValidationUtil.
 * [INHERITANCE] Inner {@link RegexOnlyDocumentFilter} extends {@link DocumentFilter} to restrict input.
 * [ENCAPSULATION] Non-instantiable facade; applies filters via static methods.
 */
public final class InputFilters {

    private InputFilters() {}

    private static final Pattern DIGITS_ONLY = Pattern.compile("^[0-9]*$");
    private static final Pattern DIGITS_AND_HYPHEN = Pattern.compile("^[0-9-]*$");
    // Letters/spaces + common punctuation; intentionally blocks digits.
    private static final Pattern CHARACTERS_ONLY = Pattern.compile("^[A-Za-z .&'\\-]*$");

    public static void setDigitsOnly(JTextField field) {
        applyRegexFilter(field, DIGITS_ONLY);
    }

    public static void setDigitsAndHyphenOnly(JTextField field) {
        applyRegexFilter(field, DIGITS_AND_HYPHEN);
    }

    public static void setCharactersOnlyNoDigits(JTextField field) {
        applyRegexFilter(field, CHARACTERS_ONLY);
    }

    private static void applyRegexFilter(JTextField field, Pattern allowedPattern) {
        if (field == null || allowedPattern == null) return;
        Document doc = field.getDocument();
        if (!(doc instanceof AbstractDocument)) return;

        AbstractDocument abstractDoc = (AbstractDocument) doc;
        abstractDoc.setDocumentFilter(new RegexOnlyDocumentFilter(allowedPattern));
    }

    private static final class RegexOnlyDocumentFilter extends DocumentFilter {
        private final Pattern allowedPattern;

        private RegexOnlyDocumentFilter(Pattern allowedPattern) {
            this.allowedPattern = allowedPattern;
        }

        /** [INHERITANCE] Overrides DocumentFilter.insertString; delegates to {@link #replace} for validation. */
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string == null) return;
            replace(fb, offset, 0, string, attr);
        }

        /** [INHERITANCE] Overrides DocumentFilter.replace to allow only text matching the regex pattern. */
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            // Allow deletions.
            if (text == null) {
                super.replace(fb, offset, length, null, attrs);
                return;
            }

            Document doc = fb.getDocument();
            String current = doc.getText(0, doc.getLength());
            String candidate =
                    current.substring(0, offset) +
                    text +
                    current.substring(offset + length);

            if (allowedPattern.matcher(candidate).matches()) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}

