package uk.ac.sheffield.com1003.assignment;

import uk.ac.sheffield.com1003.assignment.codeprovided.*;
import java.util.*;

/**
 * QueryParser class extends AbstractQueryParser for parsing queries from tokens.
 */
public class QueryParser extends AbstractQueryParser {

    @Override
    public List<Query> readQueries(List<String> queryTokens) throws IllegalArgumentException {
        List<Query> queries = new ArrayList<>();
        League league = null;
        int index = 0;

        while (index < queryTokens.size()) {
            String token = queryTokens.get(index);

            if (token.equalsIgnoreCase("select")) {
                league = League.valueOf(queryTokens.get(index + 1).toUpperCase());
                index += 2;
            } else if (token.equalsIgnoreCase("or")) {
                League league1 = League.valueOf(queryTokens.get(index + 1).toUpperCase());
                if (league != league1) {
                    league = League.ALL;
                }
                index += 2;
            } else if (token.equalsIgnoreCase("where")) {
                index++; // Skip the "where" token
                List<SubQuery> subQueryList = new ArrayList<>();

                while (index < queryTokens.size() && !queryTokens.get(index).equalsIgnoreCase("or") && !queryTokens.get(index).equalsIgnoreCase("select")) {
                    if (league == null) {
                        throw new IllegalArgumentException("League type not specified.");
                    }

                    String subProperty = queryTokens.get(index);
                    PlayerProperty property = PlayerProperty.fromName(subProperty);

                    if (property == null) {
                        throw new IllegalArgumentException("Invalid property: " + subProperty);
                    }

                    String subOperator = queryTokens.get(index + 1);
                    if (!SubQuery.isValidOperator(subOperator)) {
                        throw new IllegalArgumentException("Invalid operator: " + subOperator);
                    }

                    String subValueStr = queryTokens.get(index + 2);
                    double subValue;
                    try {
                        subValue = Double.parseDouble(subValueStr);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid value in subquery: " + subValueStr);
                    }

                    SubQuery subQuery = new SubQuery(property, subOperator, subValue);
                    subQueryList.add(subQuery);
                    index += 3; // Advance the index by 3 as we've handled 3 tokens

                    if (index < queryTokens.size() && queryTokens.get(index).equalsIgnoreCase("and")) {
                        index++; // If next token is "and", skip it
                    }
                }

                Query query = new Query(new ArrayList<>(subQueryList), league);
                queries.add(query);
            } else {
                index++;
            }
        }

        return queries;
    }
}
