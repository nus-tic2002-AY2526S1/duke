package checker;

import exceptions.FindException;

public class FindChecker {
    public static void checkKeyword(String keyword) throws FindException {
        if (keyword.isEmpty()){
            throw new FindException.MissingKeywordException();
        }
    }
}
